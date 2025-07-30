package yhumi.losercraft.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import yhumi.losercraft.Losercraft;
import yhumi.losercraft.LosercraftDataCompontents;
import yhumi.losercraft.block.LosercraftBlocks;
import yhumi.losercraft.block.custom.LosercraftExperienceCauldronInteraction;
import yhumi.losercraft.block.custom.entity.LosercraftExperienceCauldronBlockEntity;

@Mixin(CauldronInteraction.class)
public interface CauldronInteractionMixin {
    @Shadow Map<String, CauldronInteraction.InteractionMap> INTERACTIONS = new Object2ObjectArrayMap<>();
    @Shadow CauldronInteraction.InteractionMap EMPTY = CauldronInteraction.newInteractionMap("empty");

    @Inject(
        method = "bootStrap",
        at = @At("TAIL")
    )
    private static void bootStrapExperienceCauldronInteraction(CallbackInfo callbackInfo) {
        //Add intraction to empty cauldron
        Map<Item, CauldronInteraction> map = EMPTY.map();
        map.put(Items.EXPERIENCE_BOTTLE, (CauldronInteraction)(blockState, level, blockPos, player, interactionHand, itemStack) -> {
            int experienceToFill = itemStack.getOrDefault(LosercraftDataCompontents.EXP_HELD, 0);

            if (experienceToFill > 0) {
                if (!level.isClientSide) {
                    player.awardStat(Stats.USE_CAULDRON);
                    level.setBlockAndUpdate(blockPos, LosercraftBlocks.EXPERIENCE_CAULDRON.defaultBlockState());
                    
                    if (!(level.getBlockEntity(blockPos) instanceof LosercraftExperienceCauldronBlockEntity losercraftExperienceCauldronBlockEntity)) {
                        //Catastrophicly bad.
                        level.setBlockAndUpdate(blockPos, Blocks.CAULDRON.defaultBlockState());
                        return InteractionResult.FAIL;
                    }

                    if (losercraftExperienceCauldronBlockEntity.getExperienceHeld() >= Losercraft.MAX_EXPERIENCE_IN_CAULDRON) {
                        return InteractionResult.TRY_WITH_EMPTY_HAND;
                    }

                    int experienceLeftInBottle = losercraftExperienceCauldronBlockEntity.addExperienceToCauldronFromExperienceBottle(level, blockPos, itemStack);
                    losercraftExperienceCauldronBlockEntity.updateFillLevelFromExperienceHeld(level, LosercraftBlocks.EXPERIENCE_CAULDRON.defaultBlockState(), blockPos);
                    ItemStack newStack = LosercraftExperienceCauldronInteraction.alterExperienceBottleFromStack(itemStack, player, experienceLeftInBottle);

                    level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);

                    return InteractionResult.SUCCESS.heldItemTransformedTo(newStack);
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }
        });

    }
}
