package yhumi.losercraft.block.custom;

import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import yhumi.losercraft.Losercraft;
import yhumi.losercraft.LosercraftDataCompontents;
import yhumi.losercraft.block.custom.entity.LosercraftExperienceCauldronBlockEntity;

public interface LosercraftExperienceCauldronInteraction extends CauldronInteraction {
    Map<String, CauldronInteraction.InteractionMap> INTERACTIONS = new Object2ObjectArrayMap<>();
    CauldronInteraction.InteractionMap EXPERIENCE_CAULDRON_BEHAVIOR = LosercraftExperienceCauldronInteraction.createMap("experience");

    InteractionResult interact(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack);

    static CauldronInteraction.InteractionMap createMap(String string) {
        Object2ObjectOpenHashMap<Item, CauldronInteraction> object2ObjectOpenHashMap = new Object2ObjectOpenHashMap<>();
		object2ObjectOpenHashMap.defaultReturnValue((blockState, level, blockPos, player, interactionHand, itemStack) -> InteractionResult.TRY_WITH_EMPTY_HAND);
		CauldronInteraction.InteractionMap interactionMap = new CauldronInteraction.InteractionMap(string, object2ObjectOpenHashMap);
		INTERACTIONS.put(string, interactionMap);
		return interactionMap;
    }

    /*TODO: Implement interactions with items:
    Empty bottle - Take experience from the cauldron.
    In all cases we should take/add as much as possible to the item/cauldron being filled.*/
    public static void bootStrap() { 
        Map<Item, CauldronInteraction> map = EXPERIENCE_CAULDRON_BEHAVIOR.map();

        map.put(Items.EXPERIENCE_BOTTLE, (CauldronInteraction)(blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (!(level.getBlockEntity(blockPos) instanceof LosercraftExperienceCauldronBlockEntity losercraftExperienceCauldronBlockEntity)) {
                return InteractionResult.FAIL;
            }

            if (losercraftExperienceCauldronBlockEntity.getExperienceHeld() >= Losercraft.MAX_EXPERIENCE_IN_CAULDRON) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }

            if (player.isCrouching()) {
                //TODO: Take from cauldron when player is crouching
            } else {
                if (!level.isClientSide) {
                    int experienceLeftInBottle = losercraftExperienceCauldronBlockEntity.addExperienceToCauldronFromExperienceBottle(level, blockPos, itemStack);
                    losercraftExperienceCauldronBlockEntity.updateFillLevelFromExperienceHeld(level, blockState, blockPos);
                    ItemStack newStack = alterExperienceBottleFromStack(itemStack, player, experienceLeftInBottle);

                    return InteractionResult.SUCCESS.heldItemTransformedTo(newStack);
                }
                
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.FAIL;
        });
    }

    static ItemStack alterExperienceBottleFromStack(ItemStack itemStack, Player player, int newStackValue) {
		//player.awardStat(Stats.ITEM_USED.get(this));
        ItemStack itemStackTemp;

        if (newStackValue > 0) {
            itemStackTemp = new ItemStack(Items.EXPERIENCE_BOTTLE);
            itemStackTemp.set(LosercraftDataCompontents.EXP_HELD, newStackValue);
        }
        else { 
            itemStackTemp = new ItemStack(Items.GLASS_BOTTLE); 
        }

		ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStackTemp);
        return itemStack3;
	}
}