package org.eve.i_love_soil.common.blocks;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.eve.i_love_soil.common.data.blocks.ILSBlocks;

import java.util.Optional;
import java.util.function.Supplier;

public interface LeafLitter extends ChangeOverTimeLeaves<LeafLitter.DecomposingState> {
    Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
            .put(ILSBlocks.leafLitter.get(), ILSBlocks.partially_decomposed_leaves.get()).put(ILSBlocks.partially_decomposed_leaves.get(), ILSBlocks.humus.get())
            .build());

    static Optional<Block> getNext(Block pBlock) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(pBlock));
    }

    default Optional<BlockState> getNext(BlockState pState) {
        var state = getNext(pState.getBlock()).map((block) -> block.withPropertiesOf(pState));
        if (pState.getBlock() instanceof LeafLitterBlock){
            int layers = pState.getValue(LeafLitterBlock.LAYERS);
            if (state.isPresent()) {
                state = Optional.of(state.get().setValue(LeafLitterBlock.LAYERS, layers));
            }
        }
        return state;
    }

    enum DecomposingState {
        LEAVES,
        PARTIALLY_DECOMPOSED,
        HUMUS
    }
}
