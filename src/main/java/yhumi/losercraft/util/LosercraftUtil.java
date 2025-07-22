package yhumi.losercraft.util;

import java.util.List;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class LosercraftUtil {
    public static final List<ResourceLocation> DISABLED_ENCHANTMENTS = List.of(
        ResourceLocation.withDefaultNamespace("mending")
    );

    public static boolean filterStacks(ItemStack stack) {
        for (ResourceLocation rl : DISABLED_ENCHANTMENTS) {
            ItemEnchantments storedEnchantments = stack.get(DataComponents.STORED_ENCHANTMENTS);
            if (storedEnchantments != null) {
                if (storedEnchantments.keySet().stream().anyMatch(holder -> holder.is(rl))) return true;
            }
        }
        return false;
    }

}
