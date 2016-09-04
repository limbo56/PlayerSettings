package src.me.lim_bo56.settings.v1_9_R2;

import me.lim_bo56.settings.versionmanager.IMount;
import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.PacketPlayOutMount;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;


/**
 * Created by lim_bo56
 * On 9/2/2016
 * At 5:49 PM
 */
public class Mount implements IMount {

    @Override
    public void sendMountPacket(Player player) {
        PacketPlayOutMount packet = new PacketPlayOutMount(((CraftPlayer) player).getHandle());
        EntityPlayer nmsplayer = ((CraftPlayer) player).getHandle();
        nmsplayer.playerConnection.sendPacket(packet);
    }

}
