package com.SirBlobman.combatlogx.api.expansion.region;

import java.util.logging.Logger;

import com.SirBlobman.combatlogx.api.ICombatLogX;
import com.SirBlobman.combatlogx.api.expansion.Expansion;
import com.SirBlobman.combatlogx.api.expansion.ExpansionManager;

public abstract class RegionExpansion extends Expansion {
    private boolean enabledSuccessfully;
    public RegionExpansion(ICombatLogX plugin) {
        super(plugin);
        this.enabledSuccessfully = false;
    }

    @Override
    public void onLoad() {
        // Do Nothing
    }

    @Override
    public final void onEnable() {
        if(!checkDependencies()) {
            Logger logger = getLogger();
            logger.info("Some dependencies for this expansion are missing!");

            ICombatLogX plugin = getPlugin();
            ExpansionManager expansionManager = plugin.getExpansionManager();
            expansionManager.disableExpansion(this);
            return;
        }

        RegionForceField regionForceField = new RegionForceField(this);
        registerListener(regionForceField);

        this.enabledSuccessfully = true;
        afterEnable();
    }

    @Override
    public final void onDisable() {
        if(!this.enabledSuccessfully) return;

        afterDisable();
        this.enabledSuccessfully = false;
    }

    public abstract boolean checkDependencies();
    public abstract void afterEnable();
    public abstract void afterDisable();

    public abstract RegionHandler getRegionHandler();
}