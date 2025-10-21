package cl.roboto.validator.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ValidatorBlocks {

    public static final Block VALIDATOR_BLOCK = new ValidatorMachineBlock(Block.Settings.create().strength(1.0f).nonOpaque());

    public static void registerModItems() {
        Registry.register(Registries.BLOCK, Identifier.of("robotovalidator", "validator_machine"), VALIDATOR_BLOCK);
        Registry.register(Registries.ITEM, Identifier.of("robotovalidator", "validator_machine"),
                new ValidatorMachineItem((ValidatorMachineBlock) VALIDATOR_BLOCK, new Item.Settings()));
    }
}
