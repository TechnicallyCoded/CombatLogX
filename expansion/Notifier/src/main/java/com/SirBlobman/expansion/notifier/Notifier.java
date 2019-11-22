package com.SirBlobman.expansion.notifier;

import java.io.File;

import com.SirBlobman.combatlogx.expansion.CLXExpansion;
import com.SirBlobman.combatlogx.utility.PluginUtil;
import com.SirBlobman.expansion.notifier.config.ConfigNotifier;
import com.SirBlobman.expansion.notifier.listener.ListenNotifier;
import com.SirBlobman.expansion.notifier.utility.ActionBarUtil;
import com.SirBlobman.expansion.notifier.utility.BossBarUtil;
import com.SirBlobman.expansion.notifier.utility.scoreboard.ScoreboardHandler;

import org.bukkit.Bukkit;

public class Notifier implements CLXExpansion {
    public static File FOLDER;
    
    public String getUnlocalizedName() {
        return "Notifier";
    }
    
    public String getVersion() {
        return "14.15";
    }
    
    @Override
    public void enable() {
        FOLDER = getDataFolder();
        ConfigNotifier.load();
        PluginUtil.regEvents(new ListenNotifier(this));
    }
    
    @Override
    public void disable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            BossBarUtil.removeBossBar(player, true);
            ActionBarUtil.removeActionBar(player, true);
            if(!ScoreboardHandler.isDisabled(player)) ScoreboardHandler.disableScoreboard(this, player);
        });
    }
    
    @Override
    public void onConfigReload() {
        ConfigNotifier.load();
    }
}