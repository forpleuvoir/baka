package forpleuvoir.baka.event;

import forpleuvoir.baka.future.teleport.Tpa;
import forpleuvoir.baka.future.teleport.WarpPoint;
import forpleuvoir.baka.utils.ReflectionUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.event.ServerChatEvent;
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

    @SubscribeEvent
    public static void onServerChatEvent(ServerChatEvent event) {
        String message = event.getMessage();
        message = message.replace("&", "§");
        IFormattableTextComponent text = new StringTextComponent(message);
        //将手持物品发送到聊天栏
        if (event.getMessage().contains("[i]")) {
            String[] context = message.split("[i]");
            ServerPlayerEntity player = event.getPlayer();
            ItemStack item = player.getHeldItemMainhand();
            IFormattableTextComponent text1 = new StringTextComponent("");
            if (!item.getItem().equals(Items.AIR)) {
                for (String s : context) {
                    text1.appendString(s);
                    if (!s.equals(context[context.length - 1])) {
                        text1.appendSibling(item.getTextComponent());
                    }
                }
                text = text1;
            }
        }
        event.setComponent(new TranslationTextComponent("chat.type.text", event.getPlayer().getScoreboardName(), text));
    }

}
