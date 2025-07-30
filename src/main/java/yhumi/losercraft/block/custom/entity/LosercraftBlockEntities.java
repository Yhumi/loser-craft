package yhumi.losercraft.block.custom.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import yhumi.losercraft.Losercraft;
import yhumi.losercraft.block.LosercraftBlocks;

public class LosercraftBlockEntities {
    public static BlockEntityType<LosercraftExperienceCauldronBlockEntity> EXPERIENCE_CAULDRON_BLOCK =
        register("experience_cauldron_entity", LosercraftExperienceCauldronBlockEntity::new, LosercraftBlocks.EXPERIENCE_CAULDRON);

    private static <T extends BlockEntity> BlockEntityType<T> register(
		String name,
		FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
		Block... blocks
    ) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Losercraft.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static void initialize() {
        Losercraft.LOGGER.info("Registering {} Block Entities", Losercraft.MOD_ID);
    }
}
