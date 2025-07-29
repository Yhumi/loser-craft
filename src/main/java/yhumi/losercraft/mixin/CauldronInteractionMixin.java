package yhumi.losercraft.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import yhumi.losercraft.LosercraftDataCompontents;
import yhumi.losercraft.block.LosercraftBlocks;
import yhumi.losercraft.block.custom.LosercraftExperienceCauldron;

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
                    player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.USE_CAULDRON);
                    level.setBlockAndUpdate(blockPos, LosercraftBlocks.EXPERIENCE_CAULDRON.defaultBlockState().setValue(LosercraftExperienceCauldron.EXPERIENCE_HELD, experienceToFill));
                    level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }
        });

    }
}
