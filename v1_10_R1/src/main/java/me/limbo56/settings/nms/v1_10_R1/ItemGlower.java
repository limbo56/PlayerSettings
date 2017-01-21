package me.limbo56.settings.nms.v1_10_R1;

import me.limbo56.settings.version.IItemGlower;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.NBTTagList;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * Created by lim_bo56
 * On 9/2/2016
 * At 5:46 PM
 */
public class ItemGlower implements IItemGlower {

    @Override
    public ItemStack glow(ItemStack itemStack) {
        net.minecraft.server.v1_10_R1.ItemStack localItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound localNBTTagCompound = null;
        if (!localItemStack.hasTag()) {
            localNBTTagCompound = new NBTTagCompound();
            localItemStack.setTag(localNBTTagCompound);
        }
        if (localNBTTagCompound == null)
            localNBTTagCompound = localItemStack.getTag();

        NBTTagList localNBTTagList = new NBTTagList();
        localNBTTagCompound.set("ench", localNBTTagList);
        localItemStack.setTag(localNBTTagCompound);
        return CraftItemStack.asCraftMirror(localItemStack);
    }

}
