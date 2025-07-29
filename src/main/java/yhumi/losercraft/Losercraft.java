package yhumi.losercraft;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Losercraft implements ModInitializer {
	public static final String MOD_ID = "loser-craft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static int MAX_EXPERIENCE_IN_BOTTLE = 465;

	@Override
	public void onInitialize() {
		LosercraftDataCompontents.initialize();
	}
}