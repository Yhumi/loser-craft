// package yhumi.losercraft.mixin;

// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// import net.minecraft.world.InteractionResult;
// import net.minecraft.world.item.FireworkRocketItem;

// @Mixin(FireworkRocketItem.class)
// public class FireworkRocketItemMixin {
//     @Inject(
//         method = "use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
//         at = @At(
//             value = "INVOKE",
//             target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;",
//             shift = At.Shift.BEFORE
//         ),
//         cancellable = true
//     )
//     private void cancelElytraFlightRocketBoost(CallbackInfoReturnable<InteractionResult> infoReturnable) {
//         //infoReturnable.setReturnValue(InteractionResult.FAIL);
//     }
// }
