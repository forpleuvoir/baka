package forpleuvoir.baka.event;

import forpleuvoir.baka.utils.ReflectionUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

/**
 * 项目名 baka
 * <p>
 * 包名 forpleuvoir.baka.event
 * <p>
 * 文件名 BakaVillagerEvent
 * <p>
 * 创建时间 2021/10/21 22:54
 *
 * @author forpleuvoir
 */
public class BakaVillagerEvent {

    private static final Field usesField = ReflectionUtil.getFieldByType(MerchantOffer.class, int.class,0); // uses
    private static final Field maxUsesField = ReflectionUtil.getFieldByType(MerchantOffer.class, int.class,1); // uses

    @SubscribeEvent
    public void onVillagerClick(PlayerInteractEvent.EntityInteract e) {
        World world = e.getWorld();
        if (world.isRemote) {
            return;
        }
        Entity target = e.getTarget();
        MerchantOffers offers = null;

        if (!(target instanceof VillagerEntity)) {
            if (target instanceof WanderingTraderEntity) {
                WanderingTraderEntity wanderer = (WanderingTraderEntity) target;
                offers = wanderer.getOffers();
            }
        } else {
            VillagerEntity villager = (VillagerEntity) target;
            offers = villager.getOffers();
        }

        if (offers == null) {
            return;
        }

        for (MerchantOffer offer : offers) {
            try {
                assert usesField != null;
                assert maxUsesField != null;
                usesField.setAccessible(true);
                maxUsesField.setAccessible(true);
                usesField.set(offer, 0);
                maxUsesField.set(offer, 99999);
            } catch (Exception ex) {
                return;
            }
        }
    }
}
