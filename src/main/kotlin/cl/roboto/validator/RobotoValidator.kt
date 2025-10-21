package cl.roboto.validator

import cl.roboto.validator.config.ModConfig
import cl.roboto.validator.item.ValidatorBlocks
import net.fabricmc.api.ModInitializer

object RobotoValidator : ModInitializer {

	override fun onInitialize() {
        ModConfig.loadConfig();
        ValidatorBlocks.registerModItems();
	}

}