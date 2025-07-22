package yhumi.losercraft.mixin;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.BedBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class BedBlockMixin {
    @Inject(at = @At("HEAD"), method = "useWithoutItem", cancellable = true)
    private void cancelBedInteraction(CallbackInfoReturnable<InteractionResult> infoReturnable) {
        infoReturnable.setReturnValue(InteractionResult.FAIL);
    }
}