package yhumi.losercraft.block.custom.entity;

import org.apache.commons.lang3.math.Fraction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import yhumi.losercraft.Losercraft;
import yhumi.losercraft.LosercraftDataCompontents;
import yhumi.losercraft.block.LosercraftBlocks;
import yhumi.losercraft.block.custom.LosercraftExperienceCauldron;

public class LosercraftExperienceCauldronBlockEntity extends BlockEntity {
    int experienceHeld = 0;

    public LosercraftExperienceCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(LosercraftBlockEntities.EXPERIENCE_CAULDRON_BLOCK, pos, state);
	}

    @Override
    public void loadAdditional(ValueInput input) {
        this.experienceHeld = input.getIntOr("experience_held", 0);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        output.putInt("experience_held", experienceHeld);
    }

    @Override
    public void applyImplicitComponents(DataComponentGetter dataComponentGetter) {
        super.applyImplicitComponents(dataComponentGetter);
        this.experienceHeld = dataComponentGetter.getOrDefault(LosercraftDataCompontents.EXP_HELD, 0);
    }

    @Override
    public void collectImplicitComponents(DataComponentMap.Builder builder) {
        builder.set(LosercraftDataCompontents.EXP_HELD, experienceHeld);
        super.collectImplicitComponents(builder);
    }

    public int getExperienceHeld() {
        return experienceHeld;
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
    public int addExperienceToCauldronFromExperienceBottle(Level level, BlockPos pos, ItemStack interactingItem) {
        int originalCauldronAmount = experienceHeld;
        int bottleAmount = interactingItem.getOrDefault(LosercraftDataCompontents.EXP_HELD, 0);

        int newCauldronAmount = Math.min(originalCauldronAmount + bottleAmount, Losercraft.MAX_EXPERIENCE_IN_CAULDRON);
        int amountTakenFromBottle = Math.min(newCauldronAmount - originalCauldronAmount, bottleAmount);

        experienceHeld = newCauldronAmount;
        this.setChanged();

        return bottleAmount - amountTakenFromBottle;
    }

    //TODO: Take experience from cauldron logic for nearby enchanting tables.
    public void takeExperienceFromCauldron(Level level, BlockState cauldronBlockState, BlockPos pos) {

    }

    /**
     * Helper method to remove experience from the Cauldron and add it into a bottle.
     * This should work agnostically of Glass/Experience bottle.
     * 
     * @param level
     * @param cauldronBlockState
     * @param pos
     * @param interactingItem
     * @return The amount of experience to set the bottle to. -1 for no change.
     */
    public int fillBottleFromCauldron(Level level, BlockState cauldronBlockState, BlockPos pos, ItemStack interactingItem) {
        int bottleAmount = interactingItem.getOrDefault(LosercraftDataCompontents.EXP_HELD, 0);
        int remainingSpaceInBottle = Losercraft.MAX_EXPERIENCE_IN_BOTTLE - bottleAmount;

        if (remainingSpaceInBottle <= 0) {
            return -1; 
        }

        int amountToAdd = Math.min(remainingSpaceInBottle, experienceHeld);

        experienceHeld -= amountToAdd;
        this.setChanged();

        if (experienceHeld <= 0) {
            BlockState blockState2 = Blocks.CAULDRON.defaultBlockState();
            level.setBlockAndUpdate(pos, blockState2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockState2));
        } else {
            updateFillLevelFromExperienceHeld(level, cauldronBlockState, pos);
        }

        return bottleAmount + amountToAdd;
    }

    public void updateFillLevelFromExperienceHeld(Level level, BlockState cauldronBlockState, BlockPos pos) {
        int fillLevel = Math.min(1 + Mth.mulAndTruncate(Fraction.getFraction(experienceHeld, Losercraft.MAX_EXPERIENCE_IN_CAULDRON), LosercraftExperienceCauldron.MAX_FILL_LEVEL - 1), LosercraftExperienceCauldron.MAX_FILL_LEVEL);
        level.setBlockAndUpdate(pos, cauldronBlockState.setValue(LosercraftExperienceCauldron.LEVEL, fillLevel));
    }
}
