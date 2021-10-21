package forpleuvoir.baka.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import forpleuvoir.baka.future.teleport.WarpPoint;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

/**
 * Back指令
 * <p>
 * 项目名 baka
 * <p>
 * 包名 forpleuvoir.baka.command
 * <p>
 * 文件名 BackCommand
 * <p>
 * 创建时间 2021/10/19 20:59
 *
 * @author forpleuvoir
 */
public class BackCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("back").executes(BackCommand::back));
    }

    private static int back(CommandContext<CommandSource> context)throws CommandSyntaxException {
        CommandSource source = context.getSource();
        ServerPlayerEntity player = source.asPlayer();
        WarpPoint.back(player);
        source.sendFeedback(new StringTextComponent("已返回上一个记录点"),false);
        return 1;
    }
}
