package com.gamunhagol.genesismod.events;

import com.gamunhagol.genesismod.main.GenesisMod;
import com.gamunhagol.genesismod.network.GenesisNetwork;
import com.gamunhagol.genesismod.network.PacketSyncStats;
import com.gamunhagol.genesismod.stats.StatApplier;
import com.gamunhagol.genesismod.stats.StatCapabilityProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = GenesisMod.MODID)
public class GenesisCommands {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("genesis")
                .requires(source -> source.hasPermission(2)) // 관리 권한 필요
                .then(Commands.literal("stats")
                        .then(Commands.literal("set")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(createStatNode("vigor", "target"))
                                        .then(createStatNode("mind", "target"))
                                        .then(createStatNode("endurance", "target"))
                                        .then(createStatNode("strength", "target"))
                                        .then(createStatNode("dexterity", "target"))
                                        .then(createStatNode("intelligence", "target"))
                                        .then(createStatNode("faith", "target"))
                                        .then(createStatNode("arcane", "target"))
                                        .then(Commands.literal("mental")
                                                .then(Commands.argument("value", FloatArgumentType.floatArg(0))
                                                        .executes(context -> setMental(
                                                                context.getSource(),
                                                                EntityArgument.getPlayer(context, "target"),
                                                                FloatArgumentType.getFloat(context, "value")))
                                                )
                                        )
                                )
                        )
                )

                .then(Commands.literal("blessing")
                        .then(Commands.literal("reset")
                                .then(Commands.argument("target", EntityArgument.player())

                                        .then(Commands.argument("statueId", StringArgumentType.string())
                                                .executes(context -> resetStatueNodes(
                                                        context.getSource(),
                                                        EntityArgument.getPlayer(context, "target"),
                                                        StringArgumentType.getString(context, "statueId")))


                                                .then(Commands.argument("nodeId", IntegerArgumentType.integer())
                                                        .executes(context -> resetSingleNode(
                                                                context.getSource(),
                                                                EntityArgument.getPlayer(context, "target"),
                                                                StringArgumentType.getString(context, "statueId"),
                                                                IntegerArgumentType.getInteger(context, "nodeId")))
                                                )
                                        )
                                )
                        )

                        .then(Commands.literal("reset_all")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(context -> resetAllNodes(
                                                context.getSource(),
                                                EntityArgument.getPlayer(context, "target")))
                                )
                        )
                )
        );
    }

    private static com.mojang.brigadier.builder.LiteralArgumentBuilder<CommandSourceStack> createStatNode(String name, String targetName) {
        return Commands.literal(name)
                .then(Commands.argument("value", IntegerArgumentType.integer(0))
                        .executes(context -> setStat(
                                context.getSource(),
                                EntityArgument.getPlayer(context, targetName),
                                name,
                                IntegerArgumentType.getInteger(context, "value")))
                );
    }

    private static int setStat(CommandSourceStack source, ServerPlayer target, String statName, int value) {
        target.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
            switch (statName) {
                case "vigor" -> { stats.setVigor(value); StatApplier.applyVigor(target, value); }
                case "mind" -> { stats.setMind(value); stats.updateMaxMental(); }
                case "endurance" -> { stats.setEndurance(value); StatApplier.applyEndurance(target, value); }
                case "strength" -> stats.setStrength(value);
                case "dexterity" -> stats.setDexterity(value);
                case "intelligence" -> { stats.setIntelligence(value); stats.updateMaxMental(); }
                case "faith" -> stats.setFaith(value);
                case "arcane" -> { stats.setArcane(value); StatApplier.applyArcane(target, value); }
            }
        });

        LOGGER.info("[GenesisMod] Stat Update: {} set {}'s {} to {}",
                source.getTextName(), target.getScoreboardName(), statName, value);

        return 1;
    }

    private static int setMental(CommandSourceStack source, ServerPlayer target, float value) {
        target.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> stats.setMental(value));

        LOGGER.info("[GenesisMod] Mental Update: {} set {}'s mental to {}",
                source.getTextName(), target.getScoreboardName(), value);

        return 1;
    }


    private static int resetStatueNodes(CommandSourceStack source, ServerPlayer target, String statueId) {
        target.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
            stats.resetStatueAllNodes(statueId);
            GenesisNetwork.sendToPlayer(new PacketSyncStats(stats), target);
            source.sendSuccess(() -> Component.translatable("commands.genesis.blessing.reset_statue.success", statueId, target.getScoreboardName()), true);
        });
        return 1;
    }

    private static int resetSingleNode(CommandSourceStack source, ServerPlayer target, String statueId, int nodeId) {
        target.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
            stats.resetNode(statueId, nodeId);
            GenesisNetwork.sendToPlayer(new PacketSyncStats(stats), target);
            source.sendSuccess(() -> Component.translatable("commands.genesis.blessing.reset_node.success", nodeId, statueId, target.getScoreboardName()), true);
        });
        return 1;
    }

    private static int resetAllNodes(CommandSourceStack source, ServerPlayer target) {
        target.getCapability(StatCapabilityProvider.STAT_CAPABILITY).ifPresent(stats -> {
            stats.resetAllNodes();
            GenesisNetwork.sendToPlayer(new PacketSyncStats(stats), target);
            source.sendSuccess(() -> Component.translatable("commands.genesis.blessing.reset_all.success", target.getScoreboardName()), true);
        });
        return 1;
    }
}