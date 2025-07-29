package yhumi.losercraft.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import yhumi.losercraft.Losercraft;
import yhumi.losercraft.LosercraftDataCompontents;
import yhumi.losercraft.LosercraftExperienceOrbDuck;

@Mixin(BottleItem.class)
public class BottleItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true) 
    public void useOnExperienceOrb(CallbackInfoReturnable<InteractionResult> ci, @Local LocalRef<Level> levelRef, @Local LocalRef<Player> playerRef, @Local LocalRef<InteractionHand> interactionHandRef) {
        Level level = levelRef.get();
        Player player = playerRef.get();
        InteractionHand interactionHand = interactionHandRef.get();

        List<ExperienceOrb> orbs = level.getEntitiesOfClass(
            ExperienceOrb.class, 
            player.getBoundingBox().inflate(2.0),
            orb -> orb.isAlive());

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (!orbs.isEmpty()) {
            ExperienceOrb orb = (ExperienceOrb) orbs.get(0);
            int experienceToBeAdded = Math.min(Losercraft.MAX_EXPERIENCE_IN_BOTTLE, orb.getValue());

            Losercraft.LOGGER.info("Orb value: {}", orb.getValue());
            Losercraft.LOGGER.info("Experience To Be Added: {}", experienceToBeAdded);

            if (orb.getValue() > experienceToBeAdded) {
                ((LosercraftExperienceOrbDuck)orb).updateValue(orb.getValue() - experienceToBeAdded);
            }
            else {
                orb.discard();
            }

            ItemStack itemStack2 = this.fillExperienceBottleFromStack(itemStack, player, Math.min(Losercraft.MAX_EXPERIENCE_IN_BOTTLE, experienceToBeAdded));
            Losercraft.LOGGER.info("Experience Bottle Value: {}", itemStack2.getOrDefault(LosercraftDataCompontents.EXP_HELD, 0));
            ci.setReturnValue(InteractionResult.SUCCESS.heldItemTransformedTo(itemStack2));
            return;
        }
    }

    @Unique
    protected ItemStack fillExperienceBottleFromStack(ItemStack itemStack, Player player, int newStackValue) {
		//player.awardStat(Stats.ITEM_USED.get(this));
        ItemStack itemStackTemp = new ItemStack(Items.EXPERIENCE_BOTTLE);
        itemStackTemp.set(LosercraftDataCompontents.EXP_HELD, newStackValue);

		ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStackTemp);
        return itemStack3;
	}
}
