package me.limbo56.playersettings.configuration.parser;

public class Parsers {
  public static final SettingsDatabaseParser SETTING_DATABASE_PARSER = new SettingsDatabaseParser();
  public static final SettingParser SETTING_PARSER = new SettingParser();
  public static final SettingCallbackParser CALLBACK_PARSER = new SettingCallbackParser();
  public static final ValueAliasesParser VALUE_ALIASES_PARSER = new ValueAliasesParser();
  public static final MenuItemParser MENU_ITEM_PARSER = new MenuItemParser();
  public static final SettingItemParser SETTING_ITEM_PARSER = new SettingItemParser();
}
