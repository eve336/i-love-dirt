package org.eve.i_love_soil.common.data;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import org.eve.i_love_soil.api.registries.ILSRegistries;
import org.eve.i_love_soil.common.blocks.LeafLitter;
import org.eve.i_love_soil.common.blocks.LeafLitterBlock;

public class ILSBlocks {

    public static final BlockEntry<SaplingBlock> testTree = ILSRegistries.ILSREGISTRATE
            .block("test_tree", properties -> new SaplingBlock(new VariableHeightGrower(TreeFeatures.OAK, 5, 15, 5), properties.noCollission()))
            .initialProperties(() -> Blocks.OAK_SAPLING)
            .lang("Test Sapling")
            .addLayer(() -> RenderType::cutoutMipped)
            .tag(BlockTags.SAPLINGS)
            .blockstate(ILSModels::createCrossBlockState)
            .item()
            .build().register();

    public static final BlockEntry<LeafLitterBlock> leafLitter = ILSRegistries.ILSREGISTRATE
            .block("leaf_litter", properties -> new LeafLitterBlock(
                    LeafLitter.DecomposingState.LEAVES,
                    properties
                            .randomTicks()
                            .forceSolidOff()
                            .isViewBlocking((one, two, three) -> one.getValue(LeafLitterBlock.LAYERS) >= 8)
                            .instabreak()
            ))
            .blockstate((ctx, prov) -> {})
            .lang("Leaf litter")
            .item()
            .build().register();

    public static final BlockEntry<LeafLitterBlock> partially_decomposed_leaves = ILSRegistries.ILSREGISTRATE
            .block("partially_decomposed_leaves", properties -> new LeafLitterBlock(
                    LeafLitter.DecomposingState.PARTIALLY_DECOMPOSED,
                    properties
                            .randomTicks()
                            .forceSolidOff()
                            .isViewBlocking((one, two, three) -> one.getValue(LeafLitterBlock.LAYERS) >= 8)
                            .instabreak()
            ))
            .blockstate((ctx, prov) -> {})
            .lang("Partially Decomposed Leaves")
            .item()
            .build().register();

    public static final BlockEntry<LeafLitterBlock> humus = ILSRegistries.ILSREGISTRATE
            .block("humus", properties -> new LeafLitterBlock(
                    LeafLitter.DecomposingState.HUMUS,
                    properties
                            .randomTicks()
                            .forceSolidOff()
                            .isViewBlocking((one, two, three) -> one.getValue(LeafLitterBlock.LAYERS) >= 8)
                            .instabreak()
            ))
            .blockstate((ctx, prov) -> {})
            .lang("Humus")
            .item()
            .build().register();

    public static void init(){}
}
