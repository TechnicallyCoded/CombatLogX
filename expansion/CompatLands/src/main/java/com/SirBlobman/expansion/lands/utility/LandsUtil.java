package com.SirBlobman.expansion.lands.utility;

import com.SirBlobman.combatlogx.CombatLogX;
import com.SirBlobman.combatlogx.utility.Util;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.LandChunk;
import me.angeschossen.lands.api.role.enums.RoleSetting;

public class LandsUtil extends Util {
    private static LandsIntegration ADDON = null;
    private static String KEY = null;
    public static LandsIntegration getAddon() {
        if(ADDON != null) return ADDON;

        ADDON = new LandsIntegration(CombatLogX.INSTANCE, false);
        KEY = ADDON.initialize();
        return getAddon();
    }
    
    public static boolean isSafeZone(Player player, Location location) {
        LandChunk landChunk = ADDON.getLandChunk(location);
        if(landChunk == null) return false;

        return !landChunk.canAction(player, RoleSetting.ATTACK_PLAYER, true);
    }
    
    public static void onDisable() {
        if(ADDON == null || KEY == null) return;

        ADDON.disable(KEY);
    }
}