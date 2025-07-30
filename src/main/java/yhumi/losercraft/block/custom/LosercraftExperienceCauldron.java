package yhumi.losercraft.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteraction.InteractionMap;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import yhumi.losercraft.block.custom.entity.LosercraftExperienceCauldronBlockEntity;

public class LosercraftExperienceCauldron extends LayeredCauldronBlock implements EntityBlock {
    public static final MapCodec<LosercraftExperienceCauldron> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            propertiesCodec(),
            Precipitation.CODEC.fieldOf("precipitation").forGetter(experienceCauldronBlock -> experienceCauldronBlock.precipitationType),
            CauldronInteraction.CODEC.fieldOf("interactions").forGetter(experienceCauldronBlock -> experienceCauldronBlock.interactions)
        )
        .apply(instance, LosercraftExperienceCauldron::new)
    );

    private final Biome.Precipitation precipitationType;

    public LosercraftExperienceCauldron(Properties properties, Precipitation precipitation, InteractionMap interactionMap) {
        super(precipitation, interactionMap, properties);
        this.precipitationType = precipitation;
        this.registerDefaultState(this.defaultBlockState());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LosercraftExperienceCauldronBlockEntity(blockPos, blockState);
    }
}