package com.SirBlobman.expansion.worldguard.utility.v6_1;

import org.bukkit.Location;
import org.bukkit.World;

import com.SirBlobman.combatlogx.utility.Util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class v6_1_WGUtil {
    private static final WorldGuardPlugin API = WorldGuardPlugin.inst();
    private static final StateFlag MOB_COMBAT = new StateFlag("mob-combat", false);
    private static final BooleanFlag NO_TAG = new BooleanFlag("no-tagging");

    public static void registerMobCombatFlag() {
        registerFlag(MOB_COMBAT);
    }
    
    public static void registerNoTagFlag() {
        registerFlag(NO_TAG);
    }
    
    @SuppressWarnings("SuspiciousSystemArraycopy")
    private static void registerFlag(Flag<?> flag) {
        try {
            Objects.requireNonNull(flag, "flag must not be null!");
            
            Class<DefaultFlag> class_DefaultFlag = DefaultFlag.class;
            Field field_flagsList = class_DefaultFlag.getDeclaredField("flagsList");
            
            Object flagsList = field_flagsList.get(null);
            int currentFlagLength = Array.getLength(flagsList);
        
            Class<?> class_flagsList = flagsList.getClass();
            Class<?> class_flagsList_component = class_flagsList.getComponentType();
            Object newFlagsList = Array.newInstance(class_flagsList_component, currentFlagLength + 1);
            
            System.arraycopy(flagsList, 0, newFlagsList, 0, currentFlagLength);
            Array.set(newFlagsList, currentFlagLength, flag);
            
            Class<Field> class_Field = Field.class;
            Field field_modifiers = class_Field.getDeclaredField("modifiers");
            field_modifiers.setAccessible(true);
            
            field_modifiers.setInt(field_flagsList, field_flagsList.getModifiers() & ~Modifier.FINAL);
            field_flagsList.setAccessible(true);
            field_flagsList.set(null, newFlagsList);
        } catch(Exception ex) {
            Util.print("&cAn error has been detected, the flag no-tag won't work properly!");
            ex.printStackTrace();
        }
    }

    private static ApplicableRegionSet getRegions(Location loc) {

        World world = loc.getWorld();
        RegionManager rm = API.getRegionManager(world);


        return rm.getApplicableRegions(loc);
    }

    public static boolean allowsPvP(Location loc) {
        ApplicableRegionSet regionSet = getRegions(loc);
        StateFlag.State state = regionSet.queryState(null, DefaultFlag.PVP);
        return (state != StateFlag.State.DENY);
    }

    public static boolean allowsMobCombat(Location loc) {
        ApplicableRegionSet regionSet = getRegions(loc);
        StateFlag.State state = regionSet.queryValue(null, MOB_COMBAT);
        return (state != StateFlag.State.DENY);
    }
    
    public static boolean allowsTagging(Location loc) {
        ApplicableRegionSet regions = getRegions(loc);
        Boolean noTagging = regions.queryValue(null, NO_TAG);
        return (noTagging == null || !noTagging);
    }
}