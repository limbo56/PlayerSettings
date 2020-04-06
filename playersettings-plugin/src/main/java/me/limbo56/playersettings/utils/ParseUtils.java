package me.limbo56.playersettings.utils;


public class ParseUtils {

    public static Integer parseInt(String from) {
        try {
            return Integer.parseInt(from);
        } catch (NumberFormatException ignored) {
            switch (from.toLowerCase()) {
                case "off":
                    return 0;
                case "on":
                    return 1;
                default:
                    return null;
            }
        }
    }

}
