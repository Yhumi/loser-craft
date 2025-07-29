package yhumi.losercraft.mixin;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import yhumi.losercraft.Losercraft;
import yhumi.losercraft.LosercraftDataCompontents;
import yhumi.losercraft.LosercraftExperienceBottleTooltip;
import yhumi.losercraft.LosercraftExperienceOrbDuck;

@Mixin(ExperienceBottleItem.class)
public class ExperienceBottleItemMixin extends Item {
    private static final int FULL_BAR_COLOR = ARGB.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);
	private static final int BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.44F, 0.53F, 1.0F);

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

            Losercraft.LOGGER.info("Orb value: {}", orb.getValue());
            Losercraft.LOGGER.info("Experience To Be Added: {}", experienceToBeAdded);

            if (orb.getValue() > experienceToBeAdded) {
                ((LosercraftExperienceOrbDuck)orb).updateValue(orb.getValue() - experienceToBeAdded);
            }
            else {
                orb.discard();
            }

            itemStack.set(LosercraftDataCompontents.EXP_HELD, Math.min(MAX_EXPERIENCE_IN_BOTTLE, experienceAmountHeld + experienceToBeAdded));

            Losercraft.LOGGER.info("Experience Bottle Value: {}", itemStack.getOrDefault(LosercraftDataCompontents.EXP_HELD, 0));
            ci.setReturnValue(InteractionResult.SUCCESS);
            return;
        }

        ci.setReturnValue(InteractionResult.FAIL);
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
		int experienceHeld = itemStack.getOrDefault(LosercraftDataCompontents.EXP_HELD, 0);
		return MAX_EXPERIENCE_IN_BOTTLE - experienceHeld > 0;
	}

    @Override
	public int getBarWidth(ItemStack itemStack) {
		int experienceHeld = itemStack.getOrDefault(LosercraftDataCompontents.EXP_HELD, 0);
		return Math.min(Mth.mulAndTruncate(Fraction.getFraction(experienceHeld, MAX_EXPERIENCE_IN_BOTTLE), 13), 13);
	}

	@Override
	public int getBarColor(ItemStack itemStack) {
		int experienceHeld = itemStack.getOrDefault(LosercraftDataCompontents.EXP_HELD, 0);
		return Fraction.getFraction(experienceHeld, MAX_EXPERIENCE_IN_BOTTLE).compareTo(Fraction.ONE) >= 0 ? FULL_BAR_COLOR : BAR_COLOR;
	}

    @Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
		TooltipDisplay tooltipDisplay = itemStack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
		return !tooltipDisplay.shows(LosercraftDataCompontents.EXP_HELD)
			? Optional.empty()
			: Optional.ofNullable(itemStack.getOrDefault(LosercraftDataCompontents.EXP_HELD, 0)).map(LosercraftExperienceBottleTooltip::new);
	}
}
