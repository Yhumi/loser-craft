package yhumi.losercraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientActivePlayersTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import yhumi.losercraft.ClientLosercraftExperienceBottleTooltip;
import yhumi.losercraft.LosercraftExperienceBottleTooltip;

@Mixin(ClientTooltipComponent.class)
public interface ClientTooltipComponentMixin {
    @Overwrite
    static ClientTooltipComponent create(TooltipComponent tooltipComponent) {
        return (ClientTooltipComponent)(switch (tooltipComponent) {
			case BundleTooltip bundleTooltip -> new ClientBundleTooltip(bundleTooltip.contents());
			case ClientActivePlayersTooltip.ActivePlayersTooltip activePlayersTooltip -> new ClientActivePlayersTooltip(activePlayersTooltip);
            case LosercraftExperienceBottleTooltip experienceTooltip -> new ClientLosercraftExperienceBottleTooltip(experienceTooltip.experienceHeld());
			default -> throw new IllegalArgumentException("Unknown TooltipComponent");
		});
    }
}