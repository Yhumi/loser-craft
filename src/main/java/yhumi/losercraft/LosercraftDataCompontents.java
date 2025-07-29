package yhumi.losercraft;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

public class LosercraftDataCompontents {
    public static final DataComponentType<Integer> EXP_HELD = 
        Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(Losercraft.MOD_ID, "experience_held"),
            DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT).build()
        );

    protected static void initialize() {
		Losercraft.LOGGER.info("Registering {} components", Losercraft.MOD_ID);
	}
}
