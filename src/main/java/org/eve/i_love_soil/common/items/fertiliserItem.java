package org.eve.i_love_soil.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import org.eve.i_love_soil.capabilities.ILSCapabilities;

public class fertiliserItem extends Item {
    public fertiliserItem(Properties pProperties) {
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
        if (!level.isClientSide()) {
            BlockPos pos = pContext.getClickedPos();
            LevelChunk chunk = level.getChunkAt(pos);
            if (level.getBlockState(pos).getBlock() == Blocks.DIRT) {
                chunk.getCapability(ILSCapabilities.SOIL_CHUNK_DATA_CAPABILITY).ifPresent(data -> {
                    // just for testing rn
//                    System.out.println("ph: " + data.getpH());
//                    System.out.println("water: " + data.getWater());
//                    System.out.println("nutrients: " + data.getNutrients());
//                    System.out.println(" ");
                    data.addNutrients(50);
                    chunk.setUnsaved(true);
                });
            }
        }
        return super.useOn(pContext);
    }
}
