package Methods;

import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.minecraft.server.v1_9_R1.NBTTagList;

public class Methods1_9_R1 implements MethodsClass {

	@Override
	public ItemStack addGlow(ItemStack paramItemStack) {
		
		net.minecraft.server.v1_9_R1.ItemStack localItemStack = CraftItemStack.asNMSCopy(paramItemStack);
	    NBTTagCompound localNBTTagCompound = null;
	    if (!localItemStack.hasTag()) {
	      localNBTTagCompound = new NBTTagCompound();
	      localItemStack.setTag(localNBTTagCompound);
	    }
	    if (localNBTTagCompound == null) localNBTTagCompound = localItemStack.getTag();

	    NBTTagList localNBTTagList = new NBTTagList();
	    localNBTTagCompound.set("ench", localNBTTagList);
	    localItemStack.setTag(localNBTTagCompound);
	    return CraftItemStack.asCraftMirror(localItemStack);
	  }

}
