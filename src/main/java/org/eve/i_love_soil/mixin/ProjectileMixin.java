package org.eve.i_love_soil.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.eve.i_love_soil.capabilities.ILSCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class ProjectileMixin {

    //@Shadow
    //public void addDeltaMovement(Vec3 pAddend) {}

    @Shadow
    private Level level;

    @Inject(
            method = "tick",
            at = @At(value = "HEAD")
    )
    private void mixin(CallbackInfo ci){
        Entity entity = ((Entity) (Object) this);
        if (!(entity instanceof Projectile)) return;
        level.getCapability(ILSCapabilities.WIND_CAPABILITY).ifPresent(data -> {
            Vec3 vec = data.getWindAt(entity.blockPosition());
            entity.addDeltaMovement(vec.multiply(0.08, 0, 0.08));
        });
    }
}
