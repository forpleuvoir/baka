package forpleuvoir.baka.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import forpleuvoir.baka.future.teleport.WarpPoint;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.HashSet;
import java.util.Set;

/**
 * 项目名 baka
 * <p>
 * 包名 forpleuvoir.baka.command
 * <p>
 * 文件名 WarpCommand
 * <p>
 * 创建时间 2021/10/20 20:51
 *
 * @author forpleuvoir
 */
public class WarpCommand {
    private static final SimpleCommandExceptionType warpException = new SimpleCommandExceptionType(() -> "传送点不存在");

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("warp")
                .then(Commands.argument("warp", StringArgumentType.string())
                        .suggests((c, b) -> {
                            Set<String> keys = new HashSet<>();
                            Set<String> strings = WarpPoint.warpPoints.keySet();
                            strings.forEach(s -> {
                                keys.add("\"" + s + "\"");
                            });
                            return ISuggestionProvider.suggest(keys, b);
                        })
                        .executes(WarpCommand::warp))
        );
        dispatcher.register(Commands.literal("warps").executes(WarpCommand::warps));
        dispatcher.register(Commands.literal("setwarp")
                .then(Commands.argument("warp", StringArgumentType.string())
                        .executes(WarpCommand::setWarp)).requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(0)));
        dispatcher.register(Commands.literal("removewarp")
                .then(Commands.argument("warp", StringArgumentType.string())
                        .suggests((c, b) -> {
                            Set<String> keys = new HashSet<>();
                            Set<String> strings = WarpPoint.warpPoints.keySet();
                            strings.forEach(s -> {
                                keys.add("\"" + s + "\"");
                            });
                            return ISuggestionProvider.suggest(keys, b);
                        })
                        .executes(WarpCommand::removeWarp)).requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(0)));
    }

    private static int warp(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        ServerPlayerEntity player = source.asPlayer();
        String arg = StringArgumentType.getString(context, "warp");
        if (WarpPoint.warp(player, arg))
            source.sendFeedback(new TranslationTextComponent("将玩家§b" + player.getScoreboardName() + "传送到 §b" + arg), false);
        else {
            throw warpException.create();
        }
        return 1;
    }

    private static int removeWarp(CommandContext<CommandSource> context) {
        String arg = StringArgumentType.getString(context, "warp");
        WarpPoint.remove(arg);
        return 1;
    }

    private static int setWarp(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        String arg = StringArgumentType.getString(context, "warp");
        WarpPoint.addWarp(arg, player);
        return 1;
    }

    private static int warps(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        TextComponent text = new TranslationTextComponent("世界传送点 : ");
        WarpPoint.warpPoints.keySet().forEach(e -> text.appendSibling(new StringTextComponent(e)).appendSibling(new StringTextComponent(",")));
        source.sendFeedback(text, false);
        return 1;
    }
}
