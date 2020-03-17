package com.SirBlobman.expansion.cheatprevention.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.SirBlobman.combatlogx.config.Config;
import com.SirBlobman.combatlogx.utility.PluginUtil;
import com.SirBlobman.combatlogx.utility.Util;
import com.SirBlobman.expansion.cheatprevention.CheatPrevention;
import com.SirBlobman.expansion.cheatprevention.utility.CMIUtil;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigCheatPrevention extends Config {
	private static YamlConfiguration config = new YamlConfiguration();

	public static void load() {
		File folder = CheatPrevention.FOLDER;
		File file = new File(folder, "cheat prevention.yml");
		if(!file.exists()) copyFromJar("cheat prevention.yml", folder);

		config = load(file);
		defaults();
	}

	public static boolean TELEPORTATION_ALLOW_DURING_COMBAT;
	public static boolean TELEPORTATION_ENDER_PEARLS_RESTART_TIMER;
	public static List<String> TELEPORTATION_ALLOWED_CAUSES;

	public static boolean FLIGHT_ALLOW_DURING_COMBAT;
	public static boolean FLIGHT_PREVENT_FALL_DAMAGE;
	public static boolean FLIGHT_ALLOW_ELYTRAS;
	public static boolean FLIGHT_ALLOW_RIPTIDE;
	public static String FLIGHT_ENABLE_PERMISSION;

	public static boolean GAMEMODE_CHANGE_WHEN_TAGGED;
	public static String GAMEMODE_GAMEMODE;

	public static List<String> BLOCKED_COMMANDS_LIST;
	public static List<String> ALLOWED_COMMANDS_LIST;

	public static boolean INVENTORY_CLOSE_ON_COMBAT;
	public static boolean INVENTORY_PREVENT_OPENING;

	public static boolean CHAT_ALLOW_DURING_COMBAT;

	public static List<String> BLOCKED_POTIONS;

	public static boolean BLOCK_BREAKING_DURING_COMBAT;
	public static boolean BLOCK_PLACING_DURING_COMBAT;
	public static boolean PREVENT_BLOCK_RIGHT_CLICK;

	public static boolean ITEM_DROPPING_DURING_COMBAT;
	public static boolean ITEM_PICK_UP_DURING_COMBAT;
	public static boolean ITEM_PREVENT_TOTEMS;

	public static boolean ENTITY_PREVENT_INTERACTION;

	private static void defaults() {
		TELEPORTATION_ALLOW_DURING_COMBAT = get(config, "teleportation.allow during combat", false);
		TELEPORTATION_ENDER_PEARLS_RESTART_TIMER = get(config, "teleportation.ender pearls restart timer", false);
		TELEPORTATION_ALLOWED_CAUSES = get(config, "teleportation.allowed causes", Util.newList("ENDER_PEARL", "PLUGIN"));

		FLIGHT_ALLOW_DURING_COMBAT = get(config, "flight.allow during combat", false);
		FLIGHT_PREVENT_FALL_DAMAGE = get(config, "flight.prevent fall damage", false);
		FLIGHT_ALLOW_ELYTRAS = get(config, "flight.allow elytras", false);
		FLIGHT_ALLOW_RIPTIDE = get(config, "flight.allow riptide", false);
		FLIGHT_ENABLE_PERMISSION = get(config, "flight.enable permission", "combatlogx.flight.enable");

		GAMEMODE_CHANGE_WHEN_TAGGED = get(config, "gamemode.change", true);
		GAMEMODE_GAMEMODE = get(config, "gamemode.gamemode", "SURVIVAL").toUpperCase();

		INVENTORY_CLOSE_ON_COMBAT = get(config, "inventories.close on tag", true);
		INVENTORY_PREVENT_OPENING = get(config, "inventories.prevent opening", true);

		CHAT_ALLOW_DURING_COMBAT = get(config, "chat.allow during combat", true);

		BLOCKED_POTIONS = get(config, "potions.blocked potions", Util.newList("INVISIBILITY", "INCREASE_DAMAGE"));

		BLOCK_BREAKING_DURING_COMBAT = get(config, "blocks.allow breaking", false);
		BLOCK_PLACING_DURING_COMBAT = get(config, "blocks.allow placing", false);
		PREVENT_BLOCK_RIGHT_CLICK = get(config, "blocks.prevent right-click", true);

		ITEM_DROPPING_DURING_COMBAT = get(config, "items.allow dropping", false);
		ITEM_PICK_UP_DURING_COMBAT = get(config, "items.allow picking up", false);
		ITEM_PREVENT_TOTEMS = get(config, "items.prevent totem usage", false);

		ENTITY_PREVENT_INTERACTION = get(config, "entities.prevent interaction", true);

		ALLOWED_COMMANDS_LIST = get(config, "commands.allowed-commands", new ArrayList<>());
		BLOCKED_COMMANDS_LIST = get(config, "commands.blocked-commands", new ArrayList<>());

		fixCommands();
		detectAllAliases();
		fixAliases();

		Util.debug("[Cheat Prevention] [Command Blocker] Final Command List: " + BLOCKED_COMMANDS_LIST);
	}

	private static void fixCommands() {
		List<String> allowedCommandList = new ArrayList<>();
		List<String> blockedCommandList = new ArrayList<>();
		
		for (String command : BLOCKED_COMMANDS_LIST) {
			if(command.equals("*")) {
				blockedCommandList.add("*");
				continue;
			}
			
			if (command.startsWith("/")) {
				blockedCommandList.add(command);
				continue;
			}
			
			String newCommand = ("/" + command);
			blockedCommandList.add(newCommand);
		}
		BLOCKED_COMMANDS_LIST = blockedCommandList;
		
		for(String command : ALLOWED_COMMANDS_LIST) {
			if(command.equals("*")) {
				allowedCommandList.add("*");
				continue;
			}
			
			if(command.startsWith("/")) {
				allowedCommandList.add(command);
				continue;
			}
			
			String newCommand = ("/" + command);
			allowedCommandList.add(newCommand);
		}
		ALLOWED_COMMANDS_LIST = allowedCommandList;
	}

	private static final List<String> ALL_COMMANDS = Util.newList();
	private static final List<String> ALL_ALIASES = Util.newList();
	private static final Map<String, String> ALIAS_TO_COMMAND = Util.newMap();
	private static void detectAllAliases() {
		Map<String, String[]> aliasMap = Bukkit.getCommandAliases();
		Set<Entry<String, String[]>> entrySet = aliasMap.entrySet();
		
		for(Entry<String, String[]> entry : entrySet) {
			String commandName = entry.getKey();
			ALL_COMMANDS.add(commandName);
			
			String[] aliases = entry.getValue();
			for(String alias : aliases) {
				ALL_ALIASES.add(alias);
				ALIAS_TO_COMMAND.put(alias, commandName);
			}
		}
	}

	private static void fixAliases() {
		List<String> blockedCommandList = new ArrayList<>();
		for(String command : BLOCKED_COMMANDS_LIST) {
			String withoutBeginning = (command.contains(" ") ? command.substring(command.indexOf(' ')) : "");
			List<String> aliasList = getAliases(command);
			
			for(String alias : aliasList) {
				String newCommand = ("/" + alias + withoutBeginning);
				blockedCommandList.add(newCommand);
			}
		}
		BLOCKED_COMMANDS_LIST.addAll(blockedCommandList);
		
		List<String> allowedCommandList = new ArrayList<>();
		for(String command : ALLOWED_COMMANDS_LIST) {
			String withoutBeginning = (command.contains(" ") ? command.substring(command.indexOf(' ')) : "");
			List<String> aliasList = getAliases(command);
			
			for(String alias : aliasList) {
				String newCommand = ("/" + alias + withoutBeginning);
				allowedCommandList.add(newCommand);
			}
		}
		ALLOWED_COMMANDS_LIST.addAll(allowedCommandList);
	}

	private static List<String> getAliases(String command) {
		if(command.startsWith("/")) command = command.substring(1);
		if(command.contains(" ")) command = command.substring(0, command.indexOf(" "));
		if(ALL_ALIASES.contains(command)) command = ALIAS_TO_COMMAND.getOrDefault(command, command);

		List<String> aliasList = Util.newList();
		Map<String, String[]> allAliases = Bukkit.getCommandAliases();
		String[] aliases = allAliases.getOrDefault(command, new String[0]);
		
		if(PluginUtil.isEnabled("CMI")) {
			aliasList.addAll(CMIUtil.getAliases(command));
		}

		aliasList.addAll(Util.newList(aliases));
		return aliasList;
	}
}