package cl.roboto.validator.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ValidatorMachineItem extends BlockItem {

    public ValidatorMachineItem(ValidatorMachineBlock block, Item.Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();

        if (world.isClient) return ActionResult.SUCCESS;

        if (!world.isAir(pos) || !world.isAir(pos.up())) {
            return ActionResult.FAIL;
        }

        PlayerEntity player = context.getPlayer();
        Direction facing = Direction.NORTH;
        if (player != null) {
            facing = player.getHorizontalFacing();
        }

        ValidatorMachineBlock block = (ValidatorMachineBlock) this.getBlock();

        BlockState bottomState = block.getDefaultState()
                .with(ValidatorMachineBlock.FACING, facing)
                .with(ValidatorMachineBlock.PART, ValidatorMachineBlock.Part.BOTTOM)
                .with(ValidatorMachineBlock.STATE, ValidatorMachineBlock.MachineState.OFF);

        BlockState topState = block.getDefaultState()
                .with(ValidatorMachineBlock.FACING, facing)
                .with(ValidatorMachineBlock.PART, ValidatorMachineBlock.Part.TOP)
                .with(ValidatorMachineBlock.STATE, ValidatorMachineBlock.MachineState.OFF);

        world.setBlockState(pos, bottomState, 3);
        world.setBlockState(pos.up(), topState, 3);

        if (player != null && !player.getAbilities().creativeMode) {
            context.getStack().decrement(1);
        }

        return ActionResult.SUCCESS;
    }

}
