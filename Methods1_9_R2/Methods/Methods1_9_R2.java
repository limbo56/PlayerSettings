package Methods;

import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.NBTTagList;

/**
 * 
 * @author lim_bo56
 *
 */

public class Methods1_9_R2 implements MethodsClass {

	@Override
	public ItemStack addGlow(ItemStack paramItemStack) {

		net.minecraft.server.v1_9_R2.ItemStack localItemStack = CraftItemStack.asNMSCopy(paramItemStack);
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
