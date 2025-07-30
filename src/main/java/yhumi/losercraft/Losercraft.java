package yhumi.losercraft;

import net.fabricmc.api.ModInitializer;
import yhumi.losercraft.block.LosercraftBlocks;
import yhumi.losercraft.block.custom.entity.LosercraftBlockEntities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Losercraft implements ModInitializer {
	public static final String MOD_ID = "loser-craft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static int MAX_EXPERIENCE_IN_BOTTLE = 465;
	public static int MAX_EXPERIENCE_IN_CAULDRON = 1395;

	@Override
	public void onInitialize() {
		LosercraftDataCompontents.initialize();
		LosercraftBlocks.initialize();
		LosercraftBlockEntities.initialize();
	}
}