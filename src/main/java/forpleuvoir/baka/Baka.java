package forpleuvoir.baka;

import forpleuvoir.baka.event.BakaEvent;
import forpleuvoir.baka.future.teleport.Tpa;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;


@Mod("baka")
public class Baka {

    private static final Logger LOGGER = LogManager.getLogger();

    public Baka() {
        MinecraftForge.EVENT_BUS.register(this);
        BakaEvent.registerEvent();
    }





}
