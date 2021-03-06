package thelm.jaopca.proxy;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.FMLCommonHandler;
import thelm.jaopca.api.IObjectWithProperty;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.model.ModelFluidTextured;

public class ClientProxy extends CommonProxy {

	public static final CreativeTabs JAOPCA_TAB = new CreativeTabs("jaopca") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Items.GLOWSTONE_DUST);
		}

		@Override
		public boolean hasSearchBar() {
			return true;
		}
	}.setBackgroundImageName("item_search.png");

	public static final IItemColor JAOPCA_ITEM_COLOR = new IItemColor() {
		@Override
		public int getColorFromItemstack(ItemStack stack, int tintIndex) {
			if(tintIndex == 0) {
				if(stack.getItem() instanceof IObjectWithProperty) {
					return ((IObjectWithProperty)stack.getItem()).getOreEntry().getColor();
				}
			}
			return 0xFFFFFF;
		}
	};

	public static final IBlockColor JAOPCA_BLOCK_COLOR = new IBlockColor() {
		@Override
		public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
			if(tintIndex == 0) {
				if(state.getBlock() instanceof IObjectWithProperty) {
					return ((IObjectWithProperty)state.getBlock()).getOreEntry().getColor();
				}
			}
			return 0xFFFFFF;
		}
	};

	static {
		ModelLoaderRegistry.registerLoader(ModelFluidTextured.FluidTexturedLoader.INSTANCE);
	}

	@Override
	public void handleBlockRegister(Block block, ItemBlock itemblock) {
		super.handleBlockRegister(block, itemblock);
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			block.setCreativeTab(JAOPCA_TAB);
			((IObjectWithProperty)block).registerModels();
			((IObjectWithProperty)itemblock).registerModels();
		}
	}

	@Override
	public void handleItemRegister(Item item) {
		super.handleItemRegister(item);
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			item.setCreativeTab(JAOPCA_TAB);
			((IObjectWithProperty)item).registerModels();
		}
	}

	@Override
	public void initItemColors() {
		super.initItemColors();
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			ItemColors itemcolors = Minecraft.getMinecraft().getItemColors();
			for(Item item : JAOPCAApi.ITEMS_TABLE.values()) {
				itemcolors.registerItemColorHandler(JAOPCA_ITEM_COLOR, item);
			}
		}
	}

	@Override
	public void initBlockColors() {
		super.initBlockColors();
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			BlockColors blockcolors = Minecraft.getMinecraft().getBlockColors();
			ItemColors itemcolors = Minecraft.getMinecraft().getItemColors();
			for(Block block : JAOPCAApi.BLOCKS_TABLE.values()) {
				blockcolors.registerBlockColorHandler(JAOPCA_BLOCK_COLOR, block);
				itemcolors.registerItemColorHandler(JAOPCA_ITEM_COLOR, block);
			}
		}
	}

	@Override
	public void initFluidColors() {
		super.initFluidColors();
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			BlockColors blockcolors = Minecraft.getMinecraft().getBlockColors();
			ItemColors itemcolors = Minecraft.getMinecraft().getItemColors();
			for(Fluid fluid : JAOPCAApi.FLUIDS_TABLE.values()) {
				if(fluid.getBlock() != null) {
					Block block = fluid.getBlock();
					blockcolors.registerBlockColorHandler(JAOPCA_BLOCK_COLOR, block);
					itemcolors.registerItemColorHandler(JAOPCA_ITEM_COLOR, block);
				}
			}
		}
	}
}
