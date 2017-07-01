package me.limbo56.settings.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by limbo56(David)
 * On 6/22/2017
 * At 10:59 AM
 */
public class ReflectionUtils {

    /**
     * Send the mount packet to a player
     *
     * @param player Player which the packet will be sent to.
     */
    public static void sendMountPacket(Player player) {
        try {
            Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Constructor<?> packetConstructor = getNMSClass("PacketPlayOutMount").getConstructor(getNMSClass("Entity"));
            Object packet = packetConstructor.newInstance(nmsPlayer);

            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a packet to a player
     *
     * @param player Player which the packet will be sent to.
     * @param packet The packet object that will be sent.
     */
    public static void sendPacket(Player player, Object packet) {
        try {
            Method getHandle = player.getClass().getMethod("getHandle");
            Object nmsPlayer = getHandle.invoke(player);
            Object playerConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);

            playerConnection.getClass().getDeclaredMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get NMS class
     *
     * @param name Class name
     * @return NMS class
     */
    public static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
