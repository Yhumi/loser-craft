package yhumi.losercraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.ExperienceOrb;
import yhumi.losercraft.LosercraftExperienceOrbDuck;

@Mixin(ExperienceOrb.class)
public class ExperienceOrbMixin implements LosercraftExperienceOrbDuck {
    @Shadow private void setValue(int i) {};

    @Inject(method = "followNearbyPlayer", at = @At("HEAD"), cancellable = true)
    private void disablePlayerFollowing(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true) 
    private void disablePlayerTouch(CallbackInfo ci) {
        ci.cancel();
    }

    @Unique
    public void updateValue(int i) {
        this.setValue(i);
    }
}
