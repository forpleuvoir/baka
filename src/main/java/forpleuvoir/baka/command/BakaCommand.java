package forpleuvoir.baka.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 项目名 baka
 * <p>
 * 包名 forpleuvoir.baka.command
 * <p>
 * 文件名 BakaCommand
 * <p>
 * 创建时间 2021/10/19 20:54
 *
 * @author forpleuvoir
 */
@Mod.EventBusSubscriber
public class BakaCommand {

    private static final Logger log = LogManager.getLogger(BakaCommand.class);

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event){
        log.info("baka 指令注册");
        CommandDispatcher<CommandSource> dispatcher=event.getServer().getCommandManager().getDispatcher();
        BackCommand.register(dispatcher);
        HomeCommand.register(dispatcher);
        TpaCommand.register(dispatcher);
        WarpCommand.register(dispatcher);
        SuicideCommand.register(dispatcher);
    }
}
