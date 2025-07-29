package yhumi.losercraft.block.custom;

import org.apache.commons.lang3.math.Fraction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction.InteractionMap;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import yhumi.losercraft.Losercraft;
import yhumi.losercraft.LosercraftDataCompontents;

public class LosercraftExperienceCauldron extends LayeredCauldronBlock {
    public static final IntegerProperty EXPERIENCE_HELD = IntegerProperty.create("experience_held", 0, Losercraft.MAX_EXPERIENCE_IN_CAULDRON);

    public LosercraftExperienceCauldron(Properties properties, Precipitation precipitation, InteractionMap interactionMap) {
        super(precipitation, interactionMap, properties);
        this.registerDefaultState(this.defaultBlockState()
            .setValue(EXPERIENCE_HELD, 0)
        );
    }

    @Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(EXPERIENCE_HELD, LEVEL);
	}
    
    /**
     * Helper method to add experience from an Experience Bottle into a Cauldron.
     * 
     * @param level
     * @param cauldronBlockState
     * @param pos
     * @param interactingItem
     * @return Amount of experience left in the bottle.
     */
    public static int addExperienceToCauldronFromExperienceBottle(Level level, BlockState cauldronBlockState, BlockPos pos, ItemStack interactingItem) {
        int originalCauldronAmount = cauldronBlockState.getValueOrElse(EXPERIENCE_HELD, 0);
        int bottleAmount = interactingItem.getOrDefault(LosercraftDataCompontents.EXP_HELD, 0);

        int newCauldronAmount = Math.min(originalCauldronAmount + bottleAmount, Losercraft.MAX_EXPERIENCE_IN_CAULDRON);
        int amountTakenFromBottle = Math.min(newCauldronAmount - originalCauldronAmount, bottleAmount);

        level.setBlockAndUpdate(pos, cauldronBlockState.setValue(EXPERIENCE_HELD, newCauldronAmount));
        return bottleAmount - amountTakenFromBottle;
    }

    //TODO: Take experience from cauldron logic for nearby enchanting tables.
    public static void takeExperienceFromCauldron(Level level, BlockState cauldronBlockState, BlockPos pos) {

    }

    public static void fillBottleFromCauldron(Level level, BlockState cauldronBlockState, BlockPos pos, ItemStack interactingItem) {

    }

    public static void updateFillLevelFromExperienceHeld(Level level, BlockState cauldronBlockState, BlockPos pos) {
        int fillLevel = Math.min(Mth.mulAndTruncate(Fraction.getFraction(cauldronBlockState.getValue(EXPERIENCE_HELD), Losercraft.MAX_EXPERIENCE_IN_CAULDRON), MAX_FILL_LEVEL), MAX_FILL_LEVEL);
        level.setBlockAndUpdate(pos, cauldronBlockState.setValue(LEVEL, fillLevel));
    }
}