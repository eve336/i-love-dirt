package org.eve.i_love_soil.common.data.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import org.eve.i_love_soil.ILSCapabilities;

public class pHStripItem extends Item {
    public pHStripItem(Properties pProperties) {
        super(pProperties);
    }

//    @Override
//    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
//        //return super.use(pLevel, pPlayer, pUsedHand);
//        if (pUsedHand == InteractionHand.OFF_HAND){
//            LevelChunk chunk = pLevel.getChunkAt(
//        }
//    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        //return super.useOn(pContext);
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        LevelChunk chunk = level.getChunkAt(pos);
        if (level.getBlockState(pos) == Blocks.DIRT.defaultBlockState()) {
            chunk.getCapability(ILSCapabilities.SOIL_CHUNK_DATA_CAPABILITY).ifPresent(data -> {
                System.out.println(data.getpH());
            });
        }
        return super.useOn(pContext);
    }
}
