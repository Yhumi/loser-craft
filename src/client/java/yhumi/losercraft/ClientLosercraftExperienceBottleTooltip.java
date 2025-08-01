package yhumi.losercraft;

import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class ClientLosercraftExperienceBottleTooltip implements ClientTooltipComponent{
    private static final ResourceLocation PROGRESSBAR_BORDER_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_border");
	private static final ResourceLocation PROGRESSBAR_FILL_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_fill");
	private static final ResourceLocation PROGRESSBAR_FULL_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/bundle_progressbar_full");

    private static final int PROGRESSBAR_HEIGHT = 13;
	private static final int PROGRESSBAR_WIDTH = 96;

    private static final String BUNDLE_FILLING_TEXT = "item.loser-craft.experience-bottle.filling";
	private static final String BUNDLE_EMPTY_TEXT = "item.loser-craft.experience-bottle.empty";

    private final int MAX_EXPERIENCE_IN_BOTTLE = 465;
    private final int experienceHeld; 

    public ClientLosercraftExperienceBottleTooltip(int experienceHeld) {
		this.experienceHeld = experienceHeld;
	}

    private int getContentXOffset(int i) {
		return (i - 96) / 2;
	}

    @Override
	public int getHeight(Font font) {
		return PROGRESSBAR_HEIGHT + 8;
	}

	@Override
	public int getWidth(Font font) {
		return PROGRESSBAR_WIDTH;
	}

    @Override
	public void renderImage(Font font, int i, int j, int k, int l, GuiGraphics guiGraphics) {
		this.drawProgressbar(i + this.getContentXOffset(k), j + 4, font, guiGraphics);
	}

    private void drawProgressbar(int i, int j, Font font, GuiGraphics guiGraphics) {
		guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.getProgressBarTexture(), i + 1, j, this.getProgressBarFill(), 13);
		guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESSBAR_BORDER_SPRITE, i, j, 96, 13);
		Component component = this.getProgressBarFillText();
		if (component != null) {
			guiGraphics.drawCenteredString(font, component, i + 48, j + 3, -1);
		}
	}

    private int getProgressBarFill() {
		return Mth.clamp(Mth.mulAndTruncate(Fraction.getFraction(this.experienceHeld, MAX_EXPERIENCE_IN_BOTTLE), 94), 0, 94);
	}

	private ResourceLocation getProgressBarTexture() {
		return Fraction.getFraction(this.experienceHeld, MAX_EXPERIENCE_IN_BOTTLE).compareTo(Fraction.ONE) >= 0 ? PROGRESSBAR_FULL_SPRITE : PROGRESSBAR_FILL_SPRITE;
	}

    @Nullable
	private Component getProgressBarFillText() {
		if (this.experienceHeld == 0) {
			return Component.translatable(BUNDLE_EMPTY_TEXT, MAX_EXPERIENCE_IN_BOTTLE);
		} else {
			return Component.translatable(BUNDLE_FILLING_TEXT, this.experienceHeld, MAX_EXPERIENCE_IN_BOTTLE);
		}
	}
}
