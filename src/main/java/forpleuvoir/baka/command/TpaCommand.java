package forpleuvoir.baka.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import forpleuvoir.baka.future.teleport.Tpa;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

/**
 * TPA指令
 * <p>
 * 项目名 baka
 * <p>
 * 包名 forpleuvoir.baka.command
 * <p>
 * 文件名 TpaCommand
 * <p>
 * 创建时间 2021/10/19 20:58
 *
 * @author forpleuvoir
 */
public class TpaCommand {
    private static final SimpleCommandExceptionType tpaException = new SimpleCommandExceptionType(() -> "如蜜传如蜜啊");

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("tpa")
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(TpaCommand::tpa)));
        dispatcher.register(Commands.literal("tpahere")
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(TpaCommand::tpahere)));
        dispatcher.register(Commands.literal("tpaccept").executes(TpaCommand::tpaccept));
    }

    private static int tpahere(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        ServerPlayerEntity sender = source.asPlayer();
        ServerPlayerEntity target = EntityArgument.getPlayer(context, "player");
        if (sender.getCachedUniqueIdString().equals(target.getCachedUniqueIdString())) {
            throw tpaException.create();
        }
        Tpa.tpas.put(target.getCachedUniqueIdString(), new Tpa(target, sender, source.getWorld().getGameTime()));
        target.sendMessage(new StringTextComponent("玩家 §b" + sender.getScoreboardName() + "§r 请求将你传送到ta的位置。"), sender.getUniqueID());
        target.sendMessage(new StringTextComponent("在120秒内输入§c /tpaccept §r接受请求 ")
                        .appendSibling(new StringTextComponent("§a[√]").modifyStyle(
                                style -> style.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tpaccept"))
                                        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("点击接受")))
                        ))
                , sender.getUniqueID());
        source.sendFeedback(new TranslationTextComponent("请求已发送"), false);
        return 1;
    }

    private static int tpa(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        ServerPlayerEntity sender = source.asPlayer();
        ServerPlayerEntity target = EntityArgument.getPlayer(context, "player");
        if (sender.getCachedUniqueIdString().equals(target.getCachedUniqueIdString())) {
            throw tpaException.create();
        }
        Tpa.tpas.put(target.getCachedUniqueIdString(), new Tpa(sender, target, source.getWorld().getGameTime()));
        target.sendMessage(new StringTextComponent("玩家 §b" + sender.getScoreboardName() + "§r 请求传送到你的位置。"), sender.getUniqueID());
        target.sendMessage(new StringTextComponent("在120秒内输入§c /tpaccept §r接受请求")
                        .appendSibling(new StringTextComponent("§a[√]").modifyStyle(
                                style -> style.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tpaccept"))
                                        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("点击接受")))
                        ))
                , sender.getUniqueID());
        source.sendFeedback(new TranslationTextComponent("请求已发送"), false);
        return 1;
    }

    private static int tpaccept(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        ServerPlayerEntity player = source.asPlayer();
        if (Tpa.tpas.containsKey(player.getCachedUniqueIdString())) {
            boolean canTp = Tpa.tpas.get(player.getCachedUniqueIdString()).tpa(source.getWorld().getGameTime());
            if (!canTp) {
                player.sendMessage(new StringTextComponent("没有等待接受的请求"), player.getUniqueID());
            }
            Tpa.tpas.remove(player.getCachedUniqueIdString());
        } else {
            player.sendMessage(new StringTextComponent("没有等待接受的请求"), player.getUniqueID());
        }
        return 1;
    }
}
