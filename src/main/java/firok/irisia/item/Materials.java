package firok.irisia.item;

import firok.irisia.Irisia;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public class Materials
{
	/*CLOTH(5, new int[]{1, 3, 2, 1}, 15),
        CHAIN(15, new int[]{2, 5, 4, 1}, 12),
        IRON(15, new int[]{2, 6, 5, 2}, 9),
        GOLD(7, new int[]{2, 5, 3, 1}, 25),
        DIAMOND(33, new int[]{3, 8, 6, 3}, 10);*/
	public static final ItemArmor.ArmorMaterial BoneArmor =
			EnumHelper.addArmorMaterial(Irisia.MODID+":BONE",
					8, new int[]{ 2, 6, 4, 2 }, 15);

	public static final ItemArmor.ArmorMaterial SpectreArmor =
			EnumHelper.addArmorMaterial(Irisia.MODID+":SPECTRE",
					8, new int[]{ 2, 6, 4, 2 }, 25);

	public static final ItemArmor.ArmorMaterial MithrilArmor =
			EnumHelper.addArmorMaterial(Irisia.MODID+":MITHRIL",
					10, new int[]{ 2, 6, 4, 2 }, 25);

	public static final ItemArmor.ArmorMaterial AdamantiumArmor =
			EnumHelper.addArmorMaterial(Irisia.MODID+":ADAMANTIUM",
					50, new int[]{ 2, 6, 4, 2 }, 8);

	public static final ItemArmor.ArmorMaterial FlumetalArmor =
			EnumHelper.addArmorMaterial(Irisia.MODID+":FLUMETAL",
					18, new int[]{ 2, 6, 4, 2 }, 20);

	public static final ItemArmor.ArmorMaterial DwartSteelArmor =
			EnumHelper.addArmorMaterial(Irisia.MODID+":DWARTSTELL",
					8, new int[]{ 2, 6, 4, 2 }, 15);

	public static final ItemArmor.ArmorMaterial LifeWoodArmor =
			EnumHelper.addArmorMaterial(Irisia.MODID+":LIFEWOOD",
					8, new int[]{ 2, 6, 4, 2 }, 15);

	public static final ItemArmor.ArmorMaterial MogigaArmor =
			EnumHelper.addArmorMaterial(Irisia.MODID+":MOGIGA",
					8, new int[]{ 2, 6, 4, 2 }, 15);

	public static final ItemArmor.ArmorMaterial SolitaArmor =
			EnumHelper.addArmorMaterial(Irisia.MODID+":SOLITA",
					8, new int[]{ 2, 6, 4, 2 }, 15);

	public static final Item.ToolMaterial BoneTool =
			EnumHelper.addToolMaterial(Irisia.MODID+":BONE",
					3, 16, 16.0F, 0.0F, 10);

	public static final Item.ToolMaterial SpectreTool =
			EnumHelper.addToolMaterial(Irisia.MODID+":SPECTRE",
					3, 16, 16.0F, 0.0F, 10);

	public static final Item.ToolMaterial MithrilTool =
			EnumHelper.addToolMaterial(Irisia.MODID+":MITHRIL",
					3, 16, 16.0F, 0.0F, 10);

	public static final Item.ToolMaterial AdamantiumTool =
			EnumHelper.addToolMaterial(Irisia.MODID+":ADAMANTIUM",
					3, 16, 16.0F, 0.0F, 10);

	public static final Item.ToolMaterial FlumetalTool =
			EnumHelper.addToolMaterial(Irisia.MODID+":FLUMETAL",
					3, 16, 16.0F, 0.0F, 10);

	public static final Item.ToolMaterial DwartSteelTool =
			EnumHelper.addToolMaterial(Irisia.MODID+":DWARTSTEEL",
					3, 16, 16.0F, 0.0F, 10);

	public static final Item.ToolMaterial LifeWoodTool =
			EnumHelper.addToolMaterial(Irisia.MODID+":LIFEWOOD",
					3, 16, 16.0F, 0.0F, 10);

	public static final Item.ToolMaterial MogigaTool =
			EnumHelper.addToolMaterial(Irisia.MODID+":MOGIGA",
					3, 16, 16.0F, 0.0F, 10);

	public static final Item.ToolMaterial SolitaTool =
			EnumHelper.addToolMaterial(Irisia.MODID+":SOLITA",
					3, 16, 16.0F, 0.0F, 10);


}