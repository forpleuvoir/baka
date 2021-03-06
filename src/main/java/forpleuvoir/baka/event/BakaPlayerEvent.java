package forpleuvoir.baka.event;

import forpleuvoir.baka.future.teleport.WarpPoint;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * 项目名 baka
 * <p>
 * 包名 forpleuvoir.baka.event
 * <p>
 * 文件名 BakaPlayerEvent
 * <p>
 * 创建时间 2021/10/21 20:36
 *
 * @author forpleuvoir
 */
public class BakaPlayerEvent {

    @SubscribeEvent
    public void onPlayerDeath(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            PlayerEntity player = event.getOriginal();
            WarpPoint.setBack((ServerPlayerEntity) player);
            ITextComponent text = new StringTextComponent("输入")
                    .appendSibling(new StringTextComponent(" §c/back ")
                            .modifyStyle(
                                    style -> style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("点击返回")))
                                            .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/back"))
                            )
                    )
                    .appendSibling(new StringTextComponent("返回死亡地点"));
            player.sendMessage(text, player.getUniqueID());
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
        String dimension = event.getTo().getLocation().toString();
        WarpPoint.setBackPreDimension(player.getCachedUniqueIdString(), dimension);
    }

    @SubscribeEvent
    public static void onXpChange(PlayerXpEvent.XpChange event) {
        PlayerEntity player = event.getPlayer();
        if (player.getEntityWorld() instanceof ServerWorld) {
            player.xpCooldown = 0;
        }
    }

    @SubscribeEvent
    public static void onPickupXp(PlayerXpEvent.PickupXp event) {
        PlayerEntity player = event.getPlayer();
        if (player.getEntityWorld() instanceof ServerWorld) {
            player.xpCooldown = 0;
        }
    }
}
