package forpleuvoir.baka.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import forpleuvoir.baka.future.teleport.Tpa;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

/**
 * 项目名 baka
 * <p>
 * 包名 forpleuvoir.baka.command
 * <p>
 * 文件名 SuicideCommand
 * <p>
 * 创建时间 2021/10/20 20:45
 *
 * @author forpleuvoir
 */
public class SuicideCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("suicide").executes(SuicideCommand::suicide));
    }

    private static int suicide(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        ServerPlayerEntity player = source.asPlayer();
        player.setHealth(0);
        source.getServer().sendMessage(new StringTextComponent("§c" + player.getScoreboardName() + "§r进入了幻想乡"), player.getUniqueID());
        source.sendFeedback(new StringTextComponent("你结束了自己的生命"),false);
        return 1;
    }
}
