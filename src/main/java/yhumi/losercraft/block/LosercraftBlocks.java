package yhumi.losercraft.block;

import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import yhumi.losercraft.Losercraft;
import yhumi.losercraft.block.custom.LosercraftExperienceCauldron;
import yhumi.losercraft.block.custom.LosercraftExperienceCauldronInteraction;

public class LosercraftBlocks {
    public static final Block EXPERIENCE_CAULDRON = registerCauldronBlock( 
        "experience_cauldron", 
        BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON), 
        Biome.Precipitation.NONE, 
        LosercraftExperienceCauldronInteraction.EXPERIENCE_CAULDRON_BEHAVIOR
    );

    private static ResourceKey<Block> keyOfBlock(String name) {
		return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Losercraft.MOD_ID, name));
	}
    
    private static Block registerCauldronBlock(String name, BlockBehaviour.Properties properties, Biome.Precipitation precipitation, CauldronInteraction.InteractionMap cauldronInteraction) {
        ResourceKey<Block> keyOfBlock = keyOfBlock(name);
        properties.setId(keyOfBlock);

        return Registry.register(BuiltInRegistries.BLOCK, keyOfBlock, new LosercraftExperienceCauldron(properties, precipitation, cauldronInteraction));
    }

    public static void initialize() {
        Losercraft.LOGGER.info("Registering ModBlocks for {}", Losercraft.MOD_ID);
    }
}
