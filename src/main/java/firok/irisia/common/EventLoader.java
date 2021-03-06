package firok.irisia.common;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import baubles.api.BaublesApi;
import firok.irisia.DamageSources;
import firok.irisia.Irisia;
import firok.irisia.Keys;
import firok.irisia.ability.*;
import firok.irisia.item.*;
import firok.irisia.potion.Potions;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.items.wands.ItemWandCasting;

import static firok.irisia.item.EquipmentSets.EffectArmorSet.*;
import static firok.irisia.item.EquipmentSets.*;


@SuppressWarnings("static-method")
public class EventLoader
{
    public EventLoader()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

	@SubscribeEvent
	public void playerLoad(PlayerEvent.LoadFromFile event) {
		EntityPlayer p = event.entityPlayer;
		File f=getPlayerFile("irisia", event.playerDirectory, p.getCommandSenderName());
		DataManager.loadPlayerData(p.getCommandSenderName(),f);
	}
	@SubscribeEvent
	public void playerSave(PlayerEvent.SaveToFile event) {
		EntityPlayer p = event.entityPlayer;
		File f=getPlayerFile("irisia",event.playerDirectory,p.getCommandSenderName());
		DataManager.savePlayerData(p.getCommandSenderName(),f);
	}
	// copied from tc4
	public static File getPlayerFile(String suffix, File playerDirectory, String playername) {
		if ("dat".equals(suffix)) throw new IllegalArgumentException("The suffix 'dat' is reserved");
		else return new File(playerDirectory, playername + "." + suffix);
	}

	@SubscribeEvent
	public void entitySpawns(EntityJoinWorldEvent event)
	{
		if(event.isCanceled()) return;
		if (!event.world.isRemote)
		{
			Entity en=event.entity;
			if(en instanceof EntityPlayer)
			{
				if(Irisia.IN_DEV)
				{
					Irisia.tellPlayerKey(Keys.InfoWarnInDev,(EntityPlayer)en);
				}
			}
		}
	}

    @SubscribeEvent
    public void onLivingDead(net.minecraftforge.event.entity.living.LivingDeathEvent event)
    {
	    if(event.isCanceled()) return;
	    //SoulCrystal
	    EntityLivingBase enlb=event.entityLiving;
	    World world=enlb.worldObj;
	    if(world.isRemote || enlb instanceof EntityPlayer)
	    	return;
	    Random rand=world.rand;

	    List<EntityPlayer> playersAround=world.getEntitiesWithinAABBExcludingEntity(
	    		enlb,
			    AxisAlignedBB.getBoundingBox(enlb.posX-10,enlb.posY-5,enlb.posZ-10,
					    enlb.posX+10,enlb.posY+5,enlb.posZ+10),
			    EntitySelectors.SelectPlayerAlive
	    ); // 寻找周围的玩家
	    for(EntityPlayer playerAround:playersAround)
	    {
	    	int sizeInv=playerAround.inventory.getSizeInventory();

		    boolean hasRegenVis=false;
	    	for(int i=0;i<sizeInv;i++) // 遍历物品栏
		    {
		    	ItemStack stack=playerAround.inventory.getStackInSlot(i);
		    	if(stack==null) continue;
		    	Item item=stack.getItem();
			    int regen;

		    	if(item instanceof ItemWandCasting) // 为周围持有怨灵法杖的玩家恢复魔力
			    {
			    	if(hasRegenVis) continue;

			    	ItemWandCasting wandCasting=(ItemWandCasting)stack.getItem();
			    	WandRod rod=wandCasting.getRod(stack);
				    if(rod==Wands.SpectreSet.wandRod)
				    {
				    	// 找到法杖 开始恢复魔力

				    	if(rand.nextFloat()<0.25&&(regen=rand.nextInt(6))>0)
						    wandCasting.addVis(stack,Aspect.ORDER,regen,true);
					    if(rand.nextFloat()<0.25&&(regen=rand.nextInt(6))>0)
						    wandCasting.addVis(stack,Aspect.ENTROPY,regen,true);
					    if(rand.nextFloat()<0.25&&(regen=rand.nextInt(6))>0)
						    wandCasting.addVis(stack,Aspect.FIRE,regen,true);
					    if(rand.nextFloat()<0.25&&(regen=rand.nextInt(6))>0)
						    wandCasting.addVis(stack,Aspect.WATER,regen,true);
					    if(rand.nextFloat()<0.25&&(regen=rand.nextInt(6))>0)
						    wandCasting.addVis(stack,Aspect.EARTH,regen,true);
					    if(rand.nextFloat()<0.25&&(regen=rand.nextInt(6))>0)
						    wandCasting.addVis(stack,Aspect.AIR,regen,true);
				    }

				    hasRegenVis=true;
			    }
			    else if(EquipmentSets.SpectreSet.inSetWeapon(item)
					    ||EquipmentSets.SpectreSet.inSetArmor(item)
					    ||EquipmentSets.SpectreSet.inSetTool(item)) // 为怨灵套装提供耐久恢复
			    {
			    	if((regen=rand.nextInt(35))>0)
			    	CauseRepairItem.To(stack,playerAround,regen);
			    }
		    }
	    }

	    PotionEffect midas=enlb.getActivePotionEffect(Potions.Midas);
	    int factor=midas==null?0:midas.getAmplifier()+1; // 如果目标有迈达斯buff 掉钱更多

	    if(rand.nextFloat()<0.01+enlb.getMaxHealth()/400) // 每10滴血上限+2.5%掉落几率
	    {
	    	enlb.entityDropItem(new ItemStack(RawMaterials.SoulCrystal),0);
	    }
	    enlb.entityDropItem(new ItemStack(RawMaterials.CoinCopper,(int)Math.ceil((1+factor*0.4)*enlb.getMaxHealth()/4)),0);

	    LootManager.dropLoot(enlb); // 调用掉落物管理器的接口 来掉落物品
    }

    public static final int intervalEffectArmorTick=80; // 每隔多少tick系统执行一次套装效果
    @SubscribeEvent
    public void onPlayerTick_effectArmorSet(LivingEvent.LivingUpdateEvent event)
    {
	    if(event.isCanceled()) return;
	    if (!event.entity.worldObj.isRemote && event.entity.ticksExisted%intervalEffectArmorTick==0 && event.entity instanceof EntityPlayer) {
		    EntityPlayer player = (EntityPlayer)event.entity;
		    EffectArmorSet set=getCurrentEquipmentedEffectArmorSet(player);
		    if(set!=null && getEnableEffect(player.inventory.armorInventory[0]))
		    	set.performEffect(player.inventory.armorInventory,player);
	    }
    }

    @SubscribeEvent // 玩家攻击别的生物发生的事件
    // entity & entityLiving & entityPlayer 攻击其它生物的玩家
    // target 被攻击的生物entity
	public void onPlayerAttackOthers(net.minecraftforge.event.entity.player.AttackEntityEvent event)
    {
//	    System.out.println("AttackEntityEvent\nentity:"+event.entity.toString()
//			    +"\nentityLiving:"+event.entityLiving.toString()
//	            +"\nentityPlayer:"+event.entityPlayer.toString()
//	            +"\ntarget:"+event.target.toString());
	    if(event.isCanceled()) return;
	    if(!(event.target instanceof EntityLivingBase))
	    	return;
	    if(event.entity.worldObj.isRemote)
	    	return;

	    EntityPlayer player=event.entityPlayer;
	    IInventory baubles=BaublesApi.getBaubles(player);
	    if(baubles!=null)
	    {
	    	for(int i=0;i<baubles.getSizeInventory();i++)
		    {
		    	ItemStack stackInSlot=baubles.getStackInSlot(i);
		    	if(stackInSlot!=null&&stackInSlot.getItem()==EquipmentUniqueBaubles.MidasRelic) // 迈达斯判定
			    {
			    	((EntityLivingBase) event.target).addPotionEffect(new PotionEffect(Potions.Midas.id,200,1));
			    	break;
			    }
		    }
	    }
	    // Collection effects=enlb.getActivePotionEffects();

	    // 黩武debuff解除判定
	    PotionEffect militaristic=player.getActivePotionEffect(Potions.Militaristic);
	    if(militaristic!=null)
	    {
	    	if(player.worldObj.rand.nextFloat()<0.08) // 每次攻击只有8%几率消除黩武debuff
		    {
		    	player.removePotionEffect(Potions.Militaristic.id);
		    	player.worldObj.playSoundAtEntity(player,Keys.SoundGulp,1,1); // todo 这里以后换成别的声音
		    }
	    }

	    // 法力增幅buff结算
	    PotionEffect amplificative=player.getActivePotionEffect(Potions.MagicAmplificative);
	    if(amplificative!=null)
	    {
		    CauseDamage.toLiving((EntityLivingBase) event.target,DamageSources.MagicAmplificativeDamage,
				    (1+amplificative.getAmplifier())*4,true);
	    }

	    if(event.target instanceof EntityLivingBase) // potion leaderly // 领袖buff结算
	    {
		    List players=player.worldObj.getEntitiesWithinAABBExcludingEntity(player,
				    AxisAlignedBB.getBoundingBox(player.posX-5,player.posY-3,player.posZ-5,
						    player.posX+5,player.posY+3,player.posZ+5),
				    EntitySelectors.SelectPlayerAlive);
		    int levelLeaderly=0;
		    for(Object obj:players)
		    {
			    EntityPlayer playerNearby=(EntityPlayer)obj;
			    PotionEffect leaderly=playerNearby.getActivePotionEffect(Potions.Leaderly);
			    if(leaderly!=null)
			    {
				    levelLeaderly+=1+leaderly.getAmplifier();
			    }
		    }
		    firok.irisia.ability.CauseDamage.toLiving(
		    		(EntityLivingBase)event.target,
				    DamageSources.StoneDamage,3*levelLeaderly,
				    true);
	    }
    }

    @SubscribeEvent // 有生物攻击其它生物发生的事件
    // entity & entityLiving 被打的entity
    // ammount 攻击力
    // damageType mob/player
	public void onLivingAttackEvent(net.minecraftforge.event.entity.living.LivingAttackEvent event)
    {
	    if(event.isCanceled()) return;
//	    System.out.println("LivingAttackEvent\nentity:"+event.entity.toString()
//			    +"\nentityLiving:"+event.entityLiving.toString()
//			    +"\nammount:"+event.ammount
//			    +"\ndamage::"+toString(event.source));
    }

    @SubscribeEvent // 有生物受伤发生的事件
    // entity & entityLiving 受伤的entity
    // ammount 伤害数值
    // damageType mob/player
	public void onLivingHurtEvent(net.minecraftforge.event.entity.living.LivingHurtEvent event)
    {
	    if(event.isCanceled()) return;
//	    System.out.println("LivingHurtEvent\nentity:"+event.entity.toString()
//			    +"\nentityLiving:"+event.entityLiving.toString()
//			    +"\nammount:"+event.ammount
//			    +"\ndamage::"+toString(event.source));
	    EntityLivingBase enlb=event.entityLiving;

	    if(enlb.worldObj.isRemote)
	    	return;

	    float amount=event.ammount;

	    float rateMissPhy=0; // 闪避几率
	    float rateMissMag=0;
	    float maxHp=enlb.getMaxHealth();
	    float nowHp=enlb.getHealth();
	    boolean isFireDamage=event.source.isFireDamage();
	    boolean isMagicalDamage=event.source.isMagicDamage();
	    // 如果是玩家 先判断身上的装备 提供一些装备效果
	    if(enlb instanceof EntityPlayer)
	    {
	    	EntityPlayer player=(EntityPlayer)enlb;
		    IInventory inv=BaublesApi.getBaubles(player);
		    for(int i=0;i<inv.getSizeInventory();i++)
		    {
			    ItemStack stackInSlot=inv.getStackInSlot(i);
			    if(stackInSlot==null) continue;

			    Item item=stackInSlot.getItem();
			    if(item== WainItems.PhecdaTheEcho && nowHp-amount<maxHp*0.1)
			    {
				    // 找到Echo护身符 检查是不是开启 另外是不是在等cd
				    NBTTagCompound nbt=stackInSlot.hasTagCompound()?stackInSlot.getTagCompound():new NBTTagCompound();
				    boolean isOn=nbt.hasKey("isOn")?nbt.getBoolean("isOn"):true;
				    int cd=nbt.hasKey("cd")?nbt.getInteger("cd"):0;

				    if(isOn&&cd<=0)
				    { // 一切就绪 给一个buff
					    enlb.worldObj.playSoundAtEntity(enlb,Keys.SoundDoor,1,1);
					    enlb.addPotionEffect(new PotionEffect(Potions.Echo.id,200,0));
					    cd=6;
				    }

				    nbt.setBoolean("isOn",isOn);
				    nbt.setInteger("cd",cd);
				    stackInSlot.setTagCompound(nbt);
			    }
			    else if(item==EquipmentUniqueBaubles.SylphBelt)
			    {
			    	rateMissPhy+=0.25f;
			    }
			    else if(isFireDamage && item==EquipmentUniqueBaubles.FrostyStone)
			    {
			    	amount=0;
			    }
		    }

		    // 凤凰套装判定
		    if(isFireDamage
				    && EquipmentSets.EffectArmorSet.getCurrentEquipmentedEffectArmorSet(player)==EquipmentSets.PhoneixSet)
		    {
			    performPhoenixDamageTransform(player,amount);
		    	amount=0; // 取消伤害
			    event.setCanceled(true);
			    return;
		    }

		    // 绿晶剑判定
		    ItemStack stackHeld;
		    if((stackHeld=player.getHeldItem())!=null && stackHeld.getItem()==Weapons.GreenCrystalSword)
		    {
		    	event.ammount*=isMagicalDamage?0.7:0.85;
		    }
	    }

	    // 先判断有没有风行和忍者效果 有的话先执行这个
	    PotionEffect ninjia=enlb.getActivePotionEffect(Potions.Ninjia);
	    PotionEffect windranger=enlb.getActivePotionEffect(Potions.WindRanger);
	    if(ninjia!=null)
	    {
	    	rateMissPhy+=ninjia.getAmplifier()*0.2+0.2;
	    }
	    if(windranger!=null)
	    {
	    	rateMissPhy+=1;
	    }
	    boolean isMagic=event.source.isMagicDamage();
	    if(isMagic && enlb.worldObj.rand.nextFloat()<rateMissMag)
	    {
		    event.setCanceled(true); // todo 以后加上音效
	    }
	    else if(!isMagic && enlb.worldObj.rand.nextFloat()<rateMissPhy)
	    {
		    event.setCanceled(true); // todo 以后加上音效
	    }

	    // 进行一些小效果的伤害计算
	    Collection effects=enlb.getActivePotionEffects();
	    for(Object obj:effects)
	    {
		    PotionEffect effect=(PotionEffect)obj;
		    if(effect.getPotionID()==Potions.MagicResistance.id) // 魔法抗性 // 减伤
		    {
		    	amount*= 1 - 0.2*(effect.getAmplifier()+1);
		    }
		    else if(effect.getPotionID()==Potions.Ethereal.id) // 虚无 // 增伤
		    {
		    	amount*=event.source.isMagicDamage()?1.5:0;
		    }
	    }

	    // 最后判断阈化效果 这个效果判定最强
	    PotionEffect thresholded=enlb.getActivePotionEffect(Potions.Thresholded);
	    if(thresholded!=null)
	    {
		    float maxDamage=16-thresholded.getAmplifier()*4;
		    if(maxDamage<0)maxDamage=0;
		    if(amount>maxDamage)amount=maxDamage;
	    }

	    if(amount<0) amount=0;
	    event.ammount=amount;
	    if(amount==0) event.setCanceled(true);

	    if(amount>0)
	    {
		    PotionEffect healing=enlb.getActivePotionEffect(Potions.Healing);
		    if(healing!=null)
		    {
			    enlb.removePotionEffect(Potions.Healing.id);
		    }
	    }

	    // echo
	    PotionEffect echo=enlb.getActivePotionEffect(Potions.Echo);
	    if(echo!=null && echo.getDuration()>0)
	    {
	    	event.setCanceled(true);
	    	enlb.worldObj.playSoundAtEntity(enlb, Keys.SoundGulp,1,1);
	    	enlb.heal(event.ammount);
	    }
    }


	@SubscribeEvent // 生物被闪电击中发生的事件
	public void onLivingStuckedByLightning(EntityStruckByLightningEvent event)
	{
		if(event.isCanceled()) return;
		Entity entity=event.entity;
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;

			EquipmentSets.EffectArmorSet set= EquipmentSets.EffectArmorSet.getCurrentEquipmentedEffectArmorSet(player);
			if(set!=null)
			{
				if(set==EquipmentSets.StormSet) // 风暴套
				{
					player.attackEntityFrom(DamageSources.LightningDamege,2.5f); // todo low 可能会改伤害类型
					event.setCanceled(true);
				}
			}
		}
	}

    public static String toString(DamageSource damage)
    {
    	StringBuffer ret=new StringBuffer();
	    ret.append("\ndamageType: ");ret.append(damage.damageType);
    	ret.append("\nisMagicDamage: ");ret.append(damage.isMagicDamage());
	    ret.append("\nisDamageAbsolute: ");ret.append(damage.isDamageAbsolute());
	    ret.append("\nisDifficultyScaled: ");ret.append(damage.isDifficultyScaled());
	    ret.append("\nisProjectile: ");ret.append(damage.isProjectile());
	    ret.append("\nisFireDamage: ");ret.append(damage.isFireDamage());
	    ret.append("\nisUnblockable: ");ret.append(damage.isUnblockable());
    	return ret.toString();
    }

    // 星象相关计算

}