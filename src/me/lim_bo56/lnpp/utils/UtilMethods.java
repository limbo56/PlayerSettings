package me.lim_bo56.lnpp.utils;

import org.bukkit.Bukkit;

public class UtilMethods {

	private static UtilMethods instance = new UtilMethods();

	public static UtilMethods getInstance() {
		return instance;
	}

	/**
	 * Check if version is equal to 1.9
	 * 
	 * @return
	 */
	public boolean Is1_9() {
		String version;

		try {

			version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

		} catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
			return false;
		}

		if (version.equals("v1_9_R1") || version.equals("v1_9_R2")) {
			return true;
		} else {
			return false;
		}
	}
}
