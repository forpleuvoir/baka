package forpleuvoir.baka.future.teleport;

import com.google.common.collect.Maps;
import com.sun.javafx.geom.Vec3d;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;
import java.util.Objects;

/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.server.data
 * @class_name Tpa
 * @create_time 2020/11/30 11:24
 */

public class Tpa {
    //String是目标玩家的uuid
    public static Map<String, Tpa> tpas = null;
    private static final Long time = 20 * 120L;

    public static void initialize() {
        tpas = Maps.newHashMap();
    }

    private final ServerPlayerEntity sender;
    private final ServerPlayerEntity target;
    private final Long expireTime;

    public Tpa(ServerPlayerEntity sender, ServerPlayerEntity target, Long nowTime) {
        this.sender = sender;
        this.target = target;
        this.expireTime = nowTime + time;
    }


    public boolean tpa(long nowTime) {
        if (canTp(nowTime)) {
            teleport(this.sender, target.getServerWorld(), target.getPositionVec());
            return true;
        } else {
            return false;
        }
    }

    private boolean canTp(long nowTime) {
        return this.expireTime > nowTime;
    }


    public ServerPlayerEntity getSender() {
        return sender;
    }


    public Long getExpireTime() {
        return expireTime;
    }

    private static void teleport(ServerPlayerEntity player, ServerWorld serverWorld, Vector3d pos) {
        WarpPoint.teleport(player, serverWorld, pos.x, pos.y, pos.z, player.rotationYaw, player.rotationPitch);
    }
}
