package yhumi.losercraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class LivingEntitiyMixin {
    @Inject(method = "take(Lnet/minecraft/world/entity/Entity;I)V", at = @At("HEAD"), cancellable = true)
    private void takeOverrideForExperience(CallbackInfo ci, @Local LocalRef<Entity> entity) {
        if (entity.get() instanceof ExperienceOrb) {
            ci.cancel();
        }
    }
}
