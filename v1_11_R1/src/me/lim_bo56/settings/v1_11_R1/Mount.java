package src.me.lim_bo56.settings.v1_11_R1;

import me.lim_bo56.settings.version.IMount;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.PacketPlayOutMount;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by lim_bo56
 * On 11/22/2016
 * At 8:41 PM
 */
public class Mount implements IMount {

    @Override
    public void sendMountPacket(Player player) {
        PacketPlayOutMount packet = new PacketPlayOutMount(((CraftPlayer) player).getHandle());
        EntityPlayer nmsplayer = ((CraftPlayer) player).getHandle();
        nmsplayer.playerConnection.sendPacket(packet);
    }

}
