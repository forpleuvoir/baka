package forpleuvoir.baka.event;

import forpleuvoir.baka.future.teleport.Tpa;
import forpleuvoir.baka.future.teleport.WarpPoint;
import forpleuvoir.baka.utils.ReflectionUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

/**
 * 项目名 baka
 * <p>
 * 包名 forpleuvoir.baka.event
 * <p>
 * 文件名 BakaServerEvent
 * <p>
 * 创建时间 2021/10/20 21:17
 *
 * @author forpleuvoir
 */
public class BakaServerEvent {

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        SaveFormat.LevelSave anvilConverterForAnvilFile = (SaveFormat.LevelSave) ReflectionUtil.getPrivateFieldValueByType(server, MinecraftServer.class, SaveFormat.LevelSave.class, 0);
        WarpPoint.initialize(anvilConverterForAnvilFile);
        Tpa.initialize();
    }

}
