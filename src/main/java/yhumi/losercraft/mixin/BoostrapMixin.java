package yhumi.losercraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.Bootstrap;
import yhumi.losercraft.block.custom.LosercraftExperienceCauldronInteraction;

@Mixin(Bootstrap.class)
public class BoostrapMixin {

    @Inject(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/cauldron/CauldronInteraction;bootStrap()V", shift = At.Shift.AFTER))
    private static void initialize0(CallbackInfo ci) {
        LosercraftExperienceCauldronInteraction.bootStrap();
    }

}