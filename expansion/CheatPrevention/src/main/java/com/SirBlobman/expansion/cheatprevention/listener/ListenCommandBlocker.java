package com.SirBlobman.expansion.cheatprevention.listener;

import java.util.List;

import com.SirBlobman.combatlogx.config.ConfigLang;
import com.SirBlobman.combatlogx.utility.CombatUtil;
import com.SirBlobman.combatlogx.utility.Util;
import com.SirBlobman.expansion.cheatprevention.config.ConfigCheatPrevention;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ListenCommandBlocker implements Listener {
    private void debug(String message) {
        String msg = "[Cheat Prevention] [Command Blocker] " + message;
        Util.debug(msg);
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        String command = e.getMessage();
        debug("Detected PlayerCommandPreProcessEvent for player '" + playerName + "' and command '" + command + "'.");
        
        if(!CombatUtil.isInCombat(player)) {
            debug("Player is not in combat, ignoring event.");
            return;
        }
        
        if(startsWithAny(command, ConfigCheatPrevention.ALLOWED_COMMANDS_LIST)) {
            debug("Allowed Command List: " + ConfigCheatPrevention.ALLOWED_COMMANDS_LIST);
            debug("The allowed command list contains the command, ignoring event.");
            return;
        }
        
        if(!startsWithAny(command, ConfigCheatPrevention.BLOCKED_COMMANDS_LIST)) {
            debug("Blocked Command List: " + ConfigCheatPrevention.BLOCKED_COMMANDS_LIST);
            debug("The blocked command list does not contain the command, ignoring event.");
            return;
        }
        
        e.setCancelled(true);
        String message = ConfigLang.getWithPrefix("messages.expansions.cheat prevention.command.not allowed").replace("{command}", command);
        Util.sendMessage(player, message);
    }
    
    private boolean startsWithAny(String string, List<String> valueList) {
        if(string == null || string.isEmpty() || valueList == null || valueList.isEmpty()) return false;
        if(valueList.contains("*") || valueList.contains("/*")) return true;
        
        String lowerString = string.toLowerCase();
        for(String value : valueList) {
            String lowerValue = value.toLowerCase();
            if(!lowerString.startsWith(lowerValue)) continue;
            
            return true;
        }
        
        return false;
    }
}