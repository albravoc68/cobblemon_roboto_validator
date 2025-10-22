package cl.roboto.validator.item;

import cl.roboto.validator.config.JsonConfig;
import cl.roboto.validator.config.ModConfig;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class ValidatorMachineBlock extends Block {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<Part> PART = EnumProperty.of("part", Part.class);
    public static final EnumProperty<MachineState> STATE = EnumProperty.of("state", MachineState.class);

    public ValidatorMachineBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(PART, Part.TOP)
                .with(STATE, MachineState.OFF)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, STATE);
    }

    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.validator.validator_machine"));
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState neighborState, Direction side) {
        if (!neighborState.isOf(this)) return false;
        Part part = state.get(PART);
        Part neighborPart = neighborState.get(PART);
        if (part == Part.TOP && neighborPart == Part.BOTTOM && side == Direction.DOWN) return true;
        return part == Part.BOTTOM && neighborPart == Part.TOP && side == Direction.UP;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!world.isClient && state.isOf(this) && !newState.isOf(this)) {
            if (state.get(PART) == Part.TOP) {
                BlockPos below = pos.down();
                BlockState belowState = world.getBlockState(below);
                if (belowState.isOf(this) && belowState.get(PART) == Part.BOTTOM) {
                    world.setBlockState(below, Blocks.AIR.getDefaultState(), 35);
                }
            } else {
                BlockPos above = pos.up();
                BlockState aboveState = world.getBlockState(above);
                if (aboveState.isOf(this) && aboveState.get(PART) == Part.TOP) {
                    world.setBlockState(above, Blocks.AIR.getDefaultState(), 35);
                }
            }
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        BlockPos topPos;
        BlockPos bottomPos;
        if (state.get(PART) == Part.TOP) {
            topPos = pos;
            bottomPos = pos.down();
        } else {
            bottomPos = pos;
            topPos = pos.up();
        }

        BlockState topState = world.getBlockState(topPos);
        BlockState bottomState = world.getBlockState(bottomPos);

        if (!topState.isOf(this) || topState.get(PART) != Part.TOP || !bottomState.isOf(this) || bottomState.get(PART) != Part.BOTTOM) {
            return ActionResult.FAIL;
        }
        if (topState.get(STATE) != MachineState.OFF) {
            return ActionResult.SUCCESS;
        }

        player.sendMessage(Text.literal("§3Evaluating Team:"), false);

        ServerPlayerEntity playerEntity = (ServerPlayerEntity) player;
        PartyStore partyStore = Cobblemon.INSTANCE.getStorage().getParty(playerEntity);
        List<String> bannedPokemon = ModConfig.jsonConfig.getBANNED_POKEMON();
        List<String> bannedSpecialForms = ModConfig.jsonConfig.getBANNED_SPECIAL_FORMS();
        List<String> bannedMoves = ModConfig.jsonConfig.getBANNED_MOVES();
        List<String> bannedItems = ModConfig.jsonConfig.getBANNED_ITEMS();
        List<String> bannedAbilities = ModConfig.jsonConfig.getBANNED_ABILITIES();
        boolean ohKOClause = ModConfig.jsonConfig.getOH_KO_CLAUSE();
        List<String> ohKoMoves = JsonConfig.getOHKOMoves();
        boolean evasionClause = ModConfig.jsonConfig.getEVASION_CLAUSE();
        List<String> evasionMoves = JsonConfig.getEvasionMoves();
        boolean itemClause = ModConfig.jsonConfig.getITEM_CLAUSE();
        boolean speciesClause = ModConfig.jsonConfig.getSPECIES_CLAUSE();

        List<Pokemon> party = Arrays.asList(partyStore.get(0), partyStore.get(1), partyStore.get(2), partyStore.get(3), partyStore.get(4), partyStore.get(5));
        party = party.stream().filter(Objects::nonNull).toList();
        boolean isValid = true;
        if (itemClause) {
            List<String> teamItems = party.stream()
                    .filter(p -> !"air".equalsIgnoreCase(p.getHeldItem$common().getItem().getName().getString()))
                    .map(p -> p.getHeldItem$common().getItem().getName().getString())
                    .toList();
            Set<String> watcher = new HashSet<>();
            Set<String> duplicates = teamItems.stream().filter(i -> !watcher.add(i)).collect(Collectors.toSet());
            if (!duplicates.isEmpty()) {
                player.sendMessage(Text.literal("Repeated items " + duplicates + " break Item Clause"), false);
                isValid = false;
            }
        }
        if (speciesClause) {
            List<String> teamSpecies = party.stream().map(p -> p.getSpecies().getName()).toList();
            Set<String> watcher = new HashSet<>();
            Set<String> duplicates = teamSpecies.stream().filter(i -> !watcher.add(i)).collect(Collectors.toSet());
            if (!duplicates.isEmpty()) {
                player.sendMessage(Text.literal("Repeated species " + duplicates + " break Species Clause"), false);
                isValid = false;
            }
        }
        for (Pokemon pokemon : party) {
            String specie = pokemon.getSpecies().getName();
            String form = pokemon.getForm().getName();
            List<String> moves = pokemon.getMoveSet().getMoves().stream().map(Move::getName).toList();
            String item = pokemon.getHeldItem$common().getItem().getName().getString();
            String ability = pokemon.getAbility().getName();
            if (bannedPokemon.stream().anyMatch(name -> name.equalsIgnoreCase(specie))) {
                player.sendMessage(Text.literal(specie + " is banned from the current meta"), false);
                isValid = false;
            }
            List<String> bannedForms = bannedSpecialForms.stream()
                    .filter(sf -> sf.split(":-:")[0]
                            .equalsIgnoreCase(specie))
                    .map(sf -> sf.split(":-:")[1])
                    .toList();
            if (bannedForms.stream().anyMatch(name -> name.equalsIgnoreCase(form))) {
                player.sendMessage(Text.literal(specie + " form " + form + " is banned from the current meta"), false);
                isValid = false;
            }
            for (String move : moves) {
                if (bannedMoves.stream().anyMatch(name -> name.equalsIgnoreCase(move))) {
                    player.sendMessage(Text.literal("Move " + move + " is banned from the current meta"), false);
                    isValid = false;
                }
                if (ohKOClause && ohKoMoves.stream().anyMatch(name -> name.equalsIgnoreCase(move))) {
                    player.sendMessage(Text.literal("OHKO Moves like " + move + " are banned from the current meta"), false);
                    isValid = false;
                }
                if (evasionClause && evasionMoves.stream().anyMatch(name -> name.equalsIgnoreCase(move))) {
                    player.sendMessage(Text.literal("Evasion Moves like " + move + " are banned from the current meta"), false);
                    isValid = false;
                }
            }
            if (bannedItems.stream().anyMatch(name -> name.equalsIgnoreCase(item))) {
                player.sendMessage(Text.literal("Item " + item + " is banned from the current meta"), false);
                isValid = false;
            }
            if (bannedAbilities.stream().anyMatch(name -> name.equalsIgnoreCase(ability))) {
                player.sendMessage(Text.literal("Ability " + ability + " is banned from the current meta"), false);
                isValid = false;
            }
        }

        player.sendMessage(Text.literal(isValid ? "§3Congratulations, your team is valid for this season" : "§4Oh no, your team is not valid for the season"), false);

        if (topState.get(STATE) == MachineState.OFF) {
            world.setBlockState(topPos, topState.with(STATE, isValid ? MachineState.SUCCESS : MachineState.FAIL), 3);
            SoundEvent sound = isValid ? CobblemonSounds.EVOLUTION_NOTIFICATION : CobblemonSounds.MOVE_QUICKATTACK_TARGET;
            world.playSound(null, pos, sound, SoundCategory.BLOCKS, 0.8f, 1.0f);
            world.scheduleBlockTick(topPos, this, 40);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(STATE) != MachineState.OFF) {
            world.setBlockState(pos, state.with(STATE, MachineState.OFF), 3);
            if (state.get(PART) == Part.TOP) {
                world.playSound(null, pos, CobblemonSounds.PC_OFF, SoundCategory.BLOCKS, 0.8f, 1.0f);
            }
        }
    }

    public enum Part implements StringIdentifiable {
        TOP("top"),
        BOTTOM("bottom");

        private final String name;

        Part(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }

    public enum MachineState implements StringIdentifiable {
        ON("on"),
        SUCCESS("success"),
        FAIL("fail"),
        OFF("off");

        private final String name;

        MachineState(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }

}
