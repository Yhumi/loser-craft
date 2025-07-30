package yhumi.losercraft.block.custom.entity;

import org.apache.commons.lang3.math.Fraction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import yhumi.losercraft.Losercraft;
import yhumi.losercraft.LosercraftDataCompontents;
import yhumi.losercraft.block.custom.LosercraftExperienceCauldron;

public class LosercraftExperienceCauldronBlockEntity extends BlockEntity {
    int experienceHeld = 0;

    public LosercraftExperienceCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(LosercraftBlockEntities.EXPERIENCE_CAULDRON_BLOCK, pos, state);
	}

    @Override
    protected void applyImplicitComponents(DataComponentGetter dataComponentGetter) {
        super.applyImplicitComponents(dataComponentGetter);
        this.experienceHeld = dataComponentGetter.getOrDefault(LosercraftDataCompontents.EXP_HELD, 0);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(LosercraftDataCompontents.EXP_HELD, experienceHeld);
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
        return bottleAmount - amountTakenFromBottle;
    }

    //TODO: Take experience from cauldron logic for nearby enchanting tables.
    public void takeExperienceFromCauldron(Level level, BlockState cauldronBlockState, BlockPos pos) {

    }

    public void fillBottleFromCauldron(Level level, BlockState cauldronBlockState, BlockPos pos, ItemStack interactingItem) {

    }

    public void updateFillLevelFromExperienceHeld(Level level, BlockState cauldronBlockState, BlockPos pos) {
        int fillLevel = Math.min(1 + Mth.mulAndTruncate(Fraction.getFraction(experienceHeld, Losercraft.MAX_EXPERIENCE_IN_CAULDRON), LosercraftExperienceCauldron.MAX_FILL_LEVEL - 1), LosercraftExperienceCauldron.MAX_FILL_LEVEL);
        level.setBlockAndUpdate(pos, cauldronBlockState.setValue(LosercraftExperienceCauldron.LEVEL, fillLevel));
    }
}
