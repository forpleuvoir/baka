package forpleuvoir.baka.event;

import net.minecraftforge.common.MinecraftForge;

/**
 * 项目名 baka
 * <p>
 * 包名 forpleuvoir.baka.event
 * <p>
 * 文件名 BakaEvent
 * <p>
 * 创建时间 2021/10/21 22:24
 *
 * @author forpleuvoir
 */
public class BakaEvent {
    public static void registerEvent() {
        register(new BakaPlayerEvent());
        register(new BakaServerEvent());
        register(new BakaVillagerEvent());
    }

    private static void register(Object target) {
        MinecraftForge.EVENT_BUS.register(target);
    }
}
