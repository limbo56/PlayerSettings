package Methods;

import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;

import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;

/**
 * 
 * @author lim_bo56
 *
 */

public class Methods1_7_R3 implements MethodsClass {

	public ItemStack addGlow(ItemStack item) {
		net.minecraft.server.v1_7_R3.ItemStack handle = CraftItemStack.asNMSCopy(item);

		if (handle == null) {
			return item;
		}

		if (handle.tag == null) {
			handle.tag = new NBTTagCompound();
		}

		NBTTagList tag = handle.getEnchantments();
		if (tag == null) {
			tag = new NBTTagList();
			handle.tag.set("ench", tag);
		}

		return CraftItemStack.asCraftMirror(handle);
	}

}
