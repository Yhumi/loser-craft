package yhumi.losercraft.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import yhumi.losercraft.Losercraft;
import yhumi.losercraft.LosercraftDataCompontents;
import yhumi.losercraft.LosercraftExperienceOrbDuck;

@Mixin(ExperienceBottleItem.class)
public class ExperienceBottleItemMixin extends Item {
    private final int MAX_EXPERIENCE_IN_BOTTLE = 465;

    public ExperienceBottleItemMixin(Item.Properties properties) {
        super(properties);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(CallbackInfoReturnable<InteractionResult> ci, @Local LocalRef<Level> levelRef, @Local LocalRef<Player> playerRef, @Local LocalRef<InteractionHand> interactionHandRef) {
        Level level = levelRef.get();
        Player player = playerRef.get();
        InteractionHand interactionHand = interactionHandRef.get();

        List<ExperienceOrb> orbs = level.getEntitiesOfClass(
            ExperienceOrb.class, 
            player.getBoundingBox().inflate(2.0),
            orb -> orb.isAlive());

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (!orbs.isEmpty()) {
            int experienceAmountHeld = itemStack.getOrDefault(LosercraftDataCompontents.EXP_HELD, 0);
            if (experienceAmountHeld >= MAX_EXPERIENCE_IN_BOTTLE) {
                ci.setReturnValue(InteractionResult.FAIL);
                return;
            }

            ExperienceOrb orb = (ExperienceOrb) orbs.get(0);
            int experienceToBeAdded = Math.min(MAX_EXPERIENCE_IN_BOTTLE - experienceAmountHeld, orb.getValue());

            if (orb.getValue() > experienceToBeAdded) {
                ((LosercraftExperienceOrbDuck)orb).updateValue(orb.getValue() - experienceToBeAdded);
            }
            else {
                orb.discard();
            }

            itemStack.set(LosercraftDataCompontents.EXP_HELD, Math.min(MAX_EXPERIENCE_IN_BOTTLE, experienceAmountHeld + experienceToBeAdded));
            Losercraft.LOGGER.info("Experience Bottle Value: {}", itemStack.getOrDefault(LosercraftDataCompontents.EXP_HELD, 0));
        }

        ci.setReturnValue(InteractionResult.FAIL);
    }

    // @Inject(at = @At("HEAD"), method = "<init>")
    // private void ExperienceBottle_Init(CallbackInfo info, @Local LocalRef<Item.Properties> propertiesRef) {
    //     Item.Properties properties = propertiesRef.get();
    //     properties.stacksTo(1);
    // }
}
