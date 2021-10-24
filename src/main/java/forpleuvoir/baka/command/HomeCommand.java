package forpleuvoir.baka.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import forpleuvoir.baka.future.teleport.WarpPoint;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Home指令
 * <p>
 * 项目名 baka
 * <p>
 * 包名 forpleuvoir.baka.command
 * <p>
 * 文件名 HomeCommand
 * <p>
 * 创建时间 2021/10/19 20:58
 *
 * @author forpleuvoir
 */
public class HomeCommand {

    private static final SimpleCommandExceptionType homeException=new SimpleCommandExceptionType(()->"你还没有设置家");

    public static void register(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("home").executes(HomeCommand::home));
        dispatcher.register(Commands.literal("sethome").executes(HomeCommand::setHome));
    }

    private static int home(CommandContext<CommandSource> context)throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().asPlayer();
        if(!WarpPoint.home(player)){
            throw homeException.create();
        }else {
            context.getSource().sendFeedback(new StringTextComponent("欢迎回家"),false);
        }
        return 1;
    }

    private static int setHome(CommandContext<CommandSource> context)throws CommandSyntaxException {
        CommandSource source = context.getSource();
        ServerPlayerEntity player = source.asPlayer();
        WarpPoint.setHome(player);
        source.sendFeedback(new StringTextComponent("已设置家"),false);
        return 1;
    }
}
