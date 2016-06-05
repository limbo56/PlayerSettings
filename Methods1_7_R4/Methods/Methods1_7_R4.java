package Methods;

import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.NBTTagList;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;

/**
 * 
 * @author lim_bo56
 *
 */

public class Methods1_7_R4 implements MethodsClass {

	@Override
	public ItemStack addGlow(ItemStack paramItemStack) {

		net.minecraft.server.v1_7_R4.ItemStack localItemStack = CraftItemStack.asNMSCopy(paramItemStack);
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
