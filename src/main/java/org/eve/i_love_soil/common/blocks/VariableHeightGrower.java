package org.eve.i_love_soil.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

public class VariableHeightGrower extends AbstractTreeGrower {

    ResourceKey<ConfiguredFeature<?, ?>> resourceKey;
    int minHeight;
    int maxHeight;
    int xySpacing;

    public VariableHeightGrower(ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureResourceKey, int minHeight, int maxHeight, int xySpacing){
        this.resourceKey = configuredFeatureResourceKey;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.xySpacing = xySpacing;
    }

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pHasFlowers) {
        //return TreeFeatures.OAK;
        return resourceKey;
    }

    public boolean canSeeSun(Level world, BlockPos blockPos){
        BlockPos blockPos1 = blockPos;
        for (int i = blockPos.getY(); i < world.getMaxBuildHeight(); i++) {
            if (!world.getBlockState(blockPos1).propagatesSkylightDown(world, blockPos1) && !world.getBlockState(blockPos).isAir()) {
                return false;
            }
            blockPos1 = blockPos1.offset(0, 1, 0);
        }
        return true;
    }


    // todo i might add some randomness to this and/or make it return early if there is like more than 70% sunlight
    @Override
    public boolean growTree(ServerLevel pLevel, ChunkGenerator pGenerator, BlockPos pPos, BlockState pState, RandomSource pRandom) {
        //return super.growTree(pLevel, pGenerator, pPos, pState, pRandom);
        ResourceKey<ConfiguredFeature<?, ?>> resourcekey = this.getConfiguredFeature(pRandom, this.hasFlowers(pLevel, pPos));
        if (resourcekey == null) {
            return false;
        } else {
            Holder<ConfiguredFeature<?, ?>> holder = pLevel.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder(resourcekey).orElse((Holder.Reference<ConfiguredFeature<?, ?>>)null);
            var event = ForgeEventFactory.blockGrowFeature(pLevel, pRandom, pPos, holder);
            holder = event.getFeature();
            if (event.getResult() == Event.Result.DENY) return false;
            if (holder == null) {
                return false;
            } else {
                ConfiguredFeature<?, ?> configuredfeature = holder.value();
                var config = configuredfeature.config();

                Map<Integer, Integer> layerCanSeeLightNumber = new HashMap<>();
                for (int y = minHeight; y < maxHeight; y++) {
                    layerCanSeeLightNumber.put(y, 0);

                    for (int x = -xySpacing; x < xySpacing; x++) {
                        for (int z = -xySpacing; z < xySpacing; z++) {
                            var newpos = pPos.offset(x, y, z);

                            if (canSeeSun(pLevel, newpos)){
                                int old = layerCanSeeLightNumber.get(y);
                                layerCanSeeLightNumber.replace(y, old + 1);
                            }

                        }
                    }
                }

                Optional<Integer> y = layerCanSeeLightNumber.entrySet().stream().max(Comparator.comparingInt(e -> e.getValue())).map(e -> e.getKey());
                int yheight;
                yheight = y.orElse(5);


                // base, rand A, rand B
                var trunk = new StraightTrunkPlacer(yheight, 2, 0);
                Field[] fields = config.getClass().getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].getName().equals("trunkPlacer")){
                        fields[i].setAccessible(true);
                        try {
                            fields[i].set(config, trunk);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                BlockState blockstate = pLevel.getFluidState(pPos).createLegacyBlock();
                pLevel.setBlock(pPos, blockstate, 4);
                if (configuredfeature.place(pLevel, pGenerator, pRandom, pPos)) {
                    if (pLevel.getBlockState(pPos) == blockstate) {
                        pLevel.sendBlockUpdated(pPos, pState, blockstate, 2);
                    }

                    return true;
                } else {
                    pLevel.setBlock(pPos, pState, 4);
                    return false;
                }
            }
        }
    }

    // @Override?
    private boolean hasFlowers(LevelAccessor pLevel, BlockPos pPos) {
        for(BlockPos blockpos : BlockPos.MutableBlockPos.betweenClosed(pPos.below().north(2).west(2), pPos.above().south(2).east(2))) {
            if (pLevel.getBlockState(blockpos).is(BlockTags.FLOWERS)) {
                return true;
            }
        }

        return false;
    }
}
