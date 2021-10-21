package forpleuvoir.baka.future.teleport;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import forpleuvoir.baka.utils.FileUtil;
import forpleuvoir.baka.utils.JsonUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.FolderName;
import net.minecraft.world.storage.SaveFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Objects;


/**
 * @author forpleuvoir
 * @project_name suikamod
 * @package com.forpleuvoir.suika.server.data
 * @class_name WarpPoint
 * @create_time 2020/11/22 14:08
 */

public class WarpPoint {

    private static final Logger log = LogManager.getLogger(WarpPoint.class);

    public static final Map<String, Pos> warpPoints = Maps.newHashMap();
    public static final Map<String, Pos> homePoints = Maps.newHashMap();
    public static final Map<String, Pos> backPoints = Maps.newHashMap();
    private static File file;
    private static File filePath;

    public static void initialize(SaveFormat.LevelSave anvilConverterForAnvilFile) {
        Constructor<FolderName> constructor;
        try {
            constructor = FolderName.class.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            FolderName worldSavePath = constructor.newInstance("baka");
            assert anvilConverterForAnvilFile != null;
            filePath = anvilConverterForAnvilFile.resolveFilePath(worldSavePath).toFile();
            file = new File(filePath, "warp_point.json");
            load();
        } catch (Exception e) {
            log.warn("baka mod warp_point load failed...");
            if (!file.exists()) {
                try {
                    createFile();
                } catch (IOException ioException) {
                    log.warn("baka mod warp_point create failed...");
                    ioException.printStackTrace();
                }
            }
        }
    }

    private static void createFile() throws IOException {
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public static void setBack(String uuid, Pos pos) {
        backPoints.put(uuid, pos);
        save();
    }

    public static void setBackPreDimension(String uuid, String dimension) {
        String pre = uuid + "pre";
        backPoints.put(pre, new Pos(null, dimension));
        save();
    }

    public static void setDeathBack(String uuid, Pos pos) {
        String pre = uuid + "pre";
        Pos newPos = pos;
        if (backPoints.containsKey(pre)) {
            newPos = new Pos(pos.position, backPoints.get(pre).dimension);
        }
        setBack(uuid, newPos);
        save();
    }

    public static void setBack(ServerPlayerEntity player) {
        backPoints.put(player.getCachedUniqueIdString(), new Pos(player.getPositionVec(), getDimension(player)));
        save();
    }

    public static void addWarp(String key, ServerPlayerEntity player) {
        warpPoints.put(key, new Pos(player.getPositionVec(), getDimension(player)));
        save();
    }

    public static void remove(String arg) {
        warpPoints.remove(arg);
        save();
    }

    public static void setHome(ServerPlayerEntity player) {
        homePoints.put(player.getCachedUniqueIdString(), new Pos(player.getPositionVec(), getDimension(player)));
        save();
    }

    public static String getDimension(ServerPlayerEntity player) {
        DimensionType type = player.getServerWorld().getDimensionType();
        return type.getEffects().toString();
    }

    private static void save() {
        JsonObject warps = JsonUtil.gson.toJsonTree(warpPoints).getAsJsonObject();
        JsonObject homes = JsonUtil.gson.toJsonTree(homePoints).getAsJsonObject();
        JsonObject backs = JsonUtil.gson.toJsonTree(backPoints).getAsJsonObject();
        JsonObject json = new JsonObject();
        json.add("warps", warps);
        json.add("homes", homes);
        json.add("backs", backs);
        try {
            FileUtil.writeFile(file, JsonUtil.gson.toJson(json), false);
        } catch (IOException e) {
            log.warn("baka mod warp_point file write failed...");
        }
    }

    private static void load() throws FileNotFoundException {
        JsonObject json = new JsonParser().parse(new FileReader(file)).getAsJsonObject();
        warpPoints.clear();
        homePoints.clear();
        backPoints.clear();
        Map<String, Pos> homes = JsonUtil.fromJson(json.get("homes"), new TypeToken<Map<String, Pos>>() {
        }.getType());
        Map<String, Pos> warps = JsonUtil.fromJson(json.get("warps"), new TypeToken<Map<String, Pos>>() {
        }.getType());
        Map<String, Pos> backs = JsonUtil.fromJson(json.get("backs"), new TypeToken<Map<String, Pos>>() {
        }.getType());
        warpPoints.putAll(warps);
        homePoints.putAll(homes);
        backPoints.putAll(backs);

    }

    public static void back(ServerPlayerEntity player) {
        String uuid = player.getCachedUniqueIdString();
        if (backPoints.isEmpty())
            return;
        if (backPoints.containsKey(uuid)) {
            Pos pos = backPoints.get(uuid);
            ServerWorld serverWorld = pos.getWorld(Objects.requireNonNull(player.getServer()));
            Vector3d vec3d = pos.position;
            teleport(player, serverWorld, vec3d.getX(), vec3d.getY(), vec3d.getZ(), player.rotationYaw, player.rotationPitch);
        }
    }

    public static boolean home(ServerPlayerEntity player) {
        String uuid = player.getCachedUniqueIdString();
        if (homePoints.isEmpty())
            return false;
        if (homePoints.containsKey(uuid)) {
            Pos pos = homePoints.get(uuid);
            ServerWorld serverWorld = pos.getWorld(Objects.requireNonNull(player.getServer()));
            Vector3d vec3d = pos.position;
            teleport(player, serverWorld, vec3d.getX(), vec3d.getY(), vec3d.getZ(), player.rotationYaw, player.rotationPitch);
            return true;
        }
        return false;
    }


    public static boolean warp(ServerPlayerEntity player, String key) {
        if (warpPoints.isEmpty()) {
            return false;
        }
        if (!warpPoints.containsKey(key))
            return false;
        Pos pos = warpPoints.get(key);
        ServerWorld serverWorld = pos.getWorld(Objects.requireNonNull(player.getServer()));
        Vector3d vec3d = pos.position;
        teleport(player, serverWorld, vec3d.getX(), vec3d.getY(), vec3d.getZ(), player.rotationYaw, player.rotationPitch);
        return true;
    }


    public static class Pos {
        public Vector3d position;
        public String dimension;

        public Pos(Vector3d position, String dimension) {
            this.position = position;
            this.dimension = dimension;
        }

        public ServerWorld getWorld(MinecraftServer server) {
            RegistryKey<World> worldKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(dimension));
            return server.getWorld(worldKey);
        }
    }

    public static void teleport(ServerPlayerEntity player, ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch) {
        setBack(player);
        player.teleport(targetWorld, x, y, z, yaw, pitch);
        player.giveExperiencePoints(0);
    }
}
