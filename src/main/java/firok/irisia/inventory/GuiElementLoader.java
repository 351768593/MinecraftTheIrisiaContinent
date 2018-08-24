package firok.irisia.inventory;

import firok.irisia.Irisia;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiElementLoader implements IGuiHandler {
	// public static final int GUI_CRAFTING_BENCH=1; // 合成台
	// public static final int GUI_RECIPE_BENCH=2; // 卷轴制作台
	public static final int GUI_BERRY_MIXER=10;
	
	public GuiElementLoader()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Irisia.instance, this);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(ID)
		{
			case GUI_BERRY_MIXER:
				return new BerryMixerGui.ServerSide(world.getTileEntity(x,y,z));

			default:
				return null;
		}
	}
	
	@Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
	    switch(ID)
	    {
		    case GUI_BERRY_MIXER:
			    return new BerryMixerGui.ClientSide(new BerryMixerGui.ServerSide(world.getTileEntity(x,y,z)));

		    default:
			    return null;
	    }
    }
}
