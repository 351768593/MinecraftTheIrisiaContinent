package firok.irisia.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import firok.irisia.common.EntitySelectors;
import firok.irisia.entity.Throwables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class ConsumableWeapons
{
	public final static Item ThrowableWeapons;
	static
	{
		ThrowableWeapons=new Item()
		{
			public String getUnlocalizedName(ItemStack itemStack)
			{
				int i = MathHelper.clamp_int(itemStack.getItemDamage(), 0, 15);
				StringBuffer ret= new StringBuffer(super.getUnlocalizedName()).append('.');
				switch (i)
				{
					case 0:
						ret.append("none");
						break;
					case 1:
						ret.append("smallStone");
						break;
					case 2:
						ret.append("middleStone");
						break;
					case 3:
						ret.append("bigStone");
						break;
					case 4:
						ret.append("hugeStone");
						break;
					case 5:
						ret.append("magicalBall");
					default:
						ret.append("others");
						break;
				}

				return ret.toString();
			}
			public boolean getHasSubtypes()
			{
				return true;
			}
			public int getMaxDamage()
			{
				return 0;
			}
			public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
			{
				if(world.isRemote)
					return itemStack;
				boolean hasUsed=false;
				if(player.isSneaking())
				{
					int damage=itemStack.getItemDamage()+1;
					itemStack.setItemDamage(damage<=6?damage:0);
					player.addChatComponentMessage(new ChatComponentText("damage="+damage));
				}
				else
				{
					int damage=itemStack.getItemDamage();
					// player.addChatComponentMessage(new ChatComponentText("damage="+itemStack.getItemDamage()));

					world.playSoundAtEntity(player, "random.bow",
							0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
					if(damage>=1&&damage<=4)
					{
						hasUsed=true;
						world.spawnEntityInWorld(new Throwables.EntityStone(world,player,damage));
					}
					if(damage==5)
					{
						hasUsed=true;
						world.spawnEntityInWorld(new Throwables.EntityMagicalDirtBall(world,player,5));
					}
				}
				if(hasUsed && !player.capabilities.isCreativeMode)
					itemStack.stackSize--;

				return itemStack;
			}
			@Override
			@SideOnly(Side.CLIENT)
			public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean p_77624_4_)
			{
				int damage=itemStack.getItemDamage();
				info.add(new StringBuffer("damage : ").append(damage).toString());
			}

		};

	}
	public final static Item SmokeBomb;
	static
	{
		SmokeBomb=new Item()
		{
			{
				this.setNoRepair();
				this.setMaxDamage(0);
				this.setMaxStackSize(32);
			}
			@Override
			public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
			{
				if(world.isRemote)
					return itemStack;

				if(!player.capabilities.isCreativeMode)
					itemStack.stackSize--;

				List entities=world.getEntitiesWithinAABBExcludingEntity(player,
						AxisAlignedBB.getBoundingBox(player.posX-5,player.posY-5,player.posZ-5,
								player.posX+5,player.posY+5,player.posZ+5),
						EntitySelectors.SelectEntityMonstersAlive);
				for(Object obj:entities)
				{
					EntityLivingBase enlb=(EntityLivingBase)obj;
					enlb.addPotionEffect(new PotionEffect(Potion.confusion.id,100,0));
				}
				player.addPotionEffect(new PotionEffect(Potion.invisibility.id,100,0));
				// todo 以后在这播放一个音效
				return itemStack;
			}
		};
	}
}
