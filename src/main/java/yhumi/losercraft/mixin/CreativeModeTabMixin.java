package yhumi.losercraft.mixin;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import yhumi.losercraft.util.LosercraftUtil;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Set;

@Mixin(CreativeModeTab.class)
public class CreativeModeTabMixin {

    @Shadow private Collection<ItemStack> displayItems;
    @Shadow private Set<ItemStack> displayItemsSearchTab;

    @Inject(method = "buildContents", at = @At("TAIL"))
    private void removeDisabledEnchantmentBooks(CallbackInfo ci) {
        filter(displayItems);
        filter(displayItemsSearchTab);
    }

    @Unique
    private void filter(Collection<ItemStack> displayItems) {
        displayItems.removeIf(LosercraftUtil::filterStacks);
    }
}