package firok.irisia.common;

import firok.irisia.item.RawMaterials;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class TcContent
{
	static void init()
	{
		// thaumcraft.common.config.ConfigResearch
		initResearchCategories();
		initResearchItems();
		initItemAspects();
	}

	private static final String cata ="IRISIA";
	private static final AspectList noneed=new AspectList();
	private static final ResearchPage[] nopage=new ResearchPage[0];
	private static int col(int i){return 0+i;}
	private static int row(int i){return 0+i;}


	private static void initResearchCategories()
	{
		ResearchCategories.registerCategory(cata,
				new ResourceLocation("thaumcraft", "textures/gui/gui_researchbackeldritch.png"),
				new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png"));
	}
	private static void initResearchItems()
	{
		// col 减少是往左 增加是往右
		// row 减少是往上 增加是往下
		(new ResearchItem("DES_irisia", cata, noneed, col(0), row(0), 0,
			new ItemStack(Items.diamond)))
				.setAutoUnlock()
				.setPages(nopage)
				.registerResearchItem();
		(new ResearchItem("DES_human", cata, noneed, col(0), row(-3), 0,
				new ItemStack(RawMaterials.AdamantiumIngot)))
				.setPages(nopage)
				.setParents("DES_irisia")
				.setAutoUnlock().registerResearchItem();
		(new ResearchItem("DES_elf", cata, noneed, col(-2), row(3), 0,
				new ItemStack(RawMaterials.AdamantiumIngot)))
				.setPages(nopage)
				.setParents("DES_irisia")
				.setAutoUnlock().registerResearchItem();
		(new ResearchItem("DES_dwart_gnome", cata, noneed, col(2), row(3), 0,
				new ItemStack(RawMaterials.AdamantiumIngot)))
				.setPages(nopage)
				.setParents("DES_irisia")
				.setAutoUnlock().registerResearchItem();
		(new ResearchItem("DES_central_teleport", cata, noneed, col(-2), row(-1), 0,
				new ItemStack(RawMaterials.AdamantiumIngot)))
				.setParents("DES_irisia")
				.setPages(nopage)
				.setAutoUnlock().registerResearchItem();
		(new ResearchItem("ITEM_teleport_stone", cata, noneed, col(-4), row(-1), 0,
				new ItemStack(RawMaterials.AdamantiumIngot)))
				.setParents("DES_central_teleport")
				.setPages(nopage)
				.setAutoUnlock().registerResearchItem();
	}

	private static void initItemAspects()
	{
		ThaumcraftApi.registerObjectTag(new ItemStack(RawMaterials.IcyGel),new AspectList().add(Aspect.LIFE,2));
		ThaumcraftApi.registerObjectTag(new ItemStack(RawMaterials.SoulCrystal),new AspectList().add(Aspect.SOUL,3));
		ThaumcraftApi.registerObjectTag(new ItemStack(RawMaterials.LostPage),new AspectList().add(Aspect.MIND,10));
		ThaumcraftApi.registerObjectTag(new ItemStack(RawMaterials.StormBall),new AspectList().add(Aspect.METAL,6).add(Aspect.GREED,2));
		ThaumcraftApi.registerObjectTag(new ItemStack(RawMaterials.ChargedStormBall),new AspectList().add(Aspect.METAL,6).add(Aspect.GREED,2).add(Aspect.WEATHER,16));
	}
//	private static void initR()
//	{
//
//
//		(new ResearchItem("ROD_wood", "THAUMATURGY")).setAutoUnlock().registerResearchItem();
//		(new ResearchItem("ROD_greatwood", "THAUMATURGY",
//				(new AspectList())
//						.add(Aspect.TOOL, 3)
//						.add(Aspect.TREE, 6)
//						.add(Aspect.MAGIC, 3),
//				-5, 2, 1,
//				new ItemStack(ConfigItems.itemWandRod, 1, 0)))
//				.setPages(new ResearchPage[]
//						{
//								new ResearchPage("tc.research_page.ROD_greatwood.1"),
//								new ResearchPage((IArcaneRecipe)recipes.get("WandRodGreatwood"))})
//				.setParents(new String[]{"BASICTHAUMATURGY"}).registerResearchItem();
//
//		(new ResearchItem("GOGGLES", "ARTIFICE",
//				(new AspectList()).add(Aspect.SENSES, 3)
//						.add(Aspect.AURA, 3)
//						.add(Aspect.MAGIC, 3),
//				4, 1, 1,
//				new ItemStack(ConfigItems.itemGoggles)))
//				.setPages(new ResearchPage[]
//						{
//								new ResearchPage("tc.research_page.GOGGLES.1"),
//								new ResearchPage((IArcaneRecipe)recipes
//										.get("Goggles"))})
//				.setParents(new String[]{"THAUMOMETER"}).setConcealed().registerResearchItem();
//		(new ResearchItem("ARCANEEAR", "ARTIFICE", (new AspectList()).add(Aspect.SENSES, 3).add(Aspect.ENERGY, 3).add(Aspect.AIR, 3), 6, 0, 1, new ItemStack(ConfigBlocks.blockWoodenDevice, 1, 1))).setPages(new ResearchPage[]{new ResearchPage("tc.research_page.ARCANEEAR.1"), new ResearchPage((IArcaneRecipe)recipes.get("ArcaneEar"))}).setParents(new String[]{"GOGGLES"}).setConcealed().registerResearchItem();
//		(new ResearchItem("SINSTONE", "ARTIFICE", (new AspectList()).add(Aspect.SENSES, 3).add(Aspect.DARKNESS, 3).add(Aspect.ELDRITCH, 3).add(Aspect.AURA, 3), 6, 2, 1, new ItemStack(ConfigItems.itemCompassStone, 1, 1))).setPages(new ResearchPage[]{new ResearchPage("tc.research_page.SINSTONE.1"), new ResearchPage((InfusionRecipe)recipes.get("SinStone"))}).setParents(new String[]{"GOGGLES"}).setConcealed().registerResearchItem();
//
//		(new ResearchItem("LIQUIDDEATH", "ALCHEMY",
//				(new AspectList())
//						.add(Aspect.DEATH, 3)
//						.add(Aspect.POISON, 3)
//						.add(Aspect.ENTROPY, 1)
//						.add(Aspect.WATER, 1), -7, 3, 2,
//				new ItemStack(ConfigItems.itemBucketDeath)))
//				.setPages(new ResearchPage[]{
//						new ResearchPage("tc.research_page.LIQUIDDEATH.1"),
//						new ResearchPage((CrucibleRecipe)recipes.get("LiquidDeath"))})
//				.setHidden()
//				.setAspectTriggers(new Aspect[]{Aspect.DEATH, Aspect.POISON})
//				.setParents(new String[]{"ENTROPICPROCESSING"})
//				.registerResearchItem();
//
//	}



}
