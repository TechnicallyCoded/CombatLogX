package com.SirBlobman.expansion.notifier.utility.scoreboard;

import java.util.List;
import java.util.UUID;

import com.SirBlobman.combatlogx.CombatLogX;
import com.SirBlobman.combatlogx.api.nms.NMS_Handler;
import com.SirBlobman.combatlogx.api.utility.MessageUtil;
import com.SirBlobman.combatlogx.expansion.Expansions;
import com.SirBlobman.combatlogx.utility.Util;
import com.SirBlobman.expansion.notifier.Notifier;
import com.SirBlobman.expansion.notifier.config.ConfigNotifier;
import com.SirBlobman.expansion.notifier.hook.PlaceholderHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import org.apache.commons.lang.Validate;

public class CustomScoreBoard {
    private final Notifier expansion;
    private final UUID playerId;
    private final List<CustomLine> lineList = Util.newList();
    private final Scoreboard scoreboard;
    private Objective objective;
    public CustomScoreBoard(Notifier expansion, Player player) {
        Validate.notNull(player, "player must not be null!");

        this.expansion = expansion;
        this.playerId = player.getUniqueId();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        createObjective();
        initializeScoreboard();
    }

    private void createObjective() {
        String scoreboardTitle = MessageUtil.color(ConfigNotifier.SCORE_BOARD_TITLE);
        this.objective = NMS_Handler.getHandler().createScoreboardObjective(scoreboard, "combatlogx", "dummy", scoreboardTitle);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.playerId);
    }

    public void enableScoreboard() {
        Player player = getPlayer();
        if(player == null) return;

        player.setScoreboard(this.scoreboard);
    }

    public void disableScoreboard() {
        Player player = getPlayer();
        if(player == null) return;

        Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        if(mainScoreboard == null) return;

        player.setScoreboard(mainScoreboard);
    }

    private void initializeScoreboard() {
        ChatColor[] colorArray = ChatColor.values();
        for(int i = 0; i < colorArray.length; i++) {
            ChatColor color = colorArray[i];
            Team team = this.scoreboard.registerNewTeam("line" + i);
            team.addEntry(color.toString());

            CustomLine line = new CustomLine(color, i, team);
            this.lineList.add(line);
        }
    }

    private CustomLine getLine(int line) {
        return this.lineList.stream().filter(custom -> custom.getLine() == line).findFirst().orElse(null);
    }

    private void setLine(int line, String value) {
        CustomLine custom = getLine(line);
        Validate.notNull(custom, "Could not find scoreboard line with index '" + line + "'.");

        this.objective.getScore(custom.getColor().toString()).setScore(line);

        int maxLength = getMaxLineLength();
        int valueLength = value.length();
        if(valueLength > maxLength) {
            String part1 = value.substring(0, maxLength);
            String part2 = value.substring(maxLength);

            String colorCodes = ChatColor.getLastColors(part1);
            part2 = colorCodes + part2;

            if(part1.endsWith("\u00A7")) {
                part1 = part1.substring(0, part1.length() - 1);
                part2 = "\u00A7" + part2;
            }

            if(part2.length() > maxLength) {
                part2 = part2.substring(0, maxLength);
            }

            Team team = custom.getTeam();
            team.setPrefix(part1);
            team.setSuffix(part2);
            return;
        }

        Team team = custom.getTeam();
        team.setPrefix(value);
    }

    private void removeLine(int line) {
        CustomLine custom = getLine(line);
        Validate.notNull(custom, "Could not find scoreboard line with index '" + line + "'.");

        this.scoreboard.resetScores(custom.getColor().toString());
    }

    public void updateScoreboard() {
        List<String> scoreboardLineList = ConfigNotifier.SCORE_BOARD_LINES;
        final int scoreboardLineListSize = scoreboardLineList.size();

        int index = 16;
        for(int i = 0; i < 16; i++) {
            if(i >= scoreboardLineListSize) {
                removeLine(index);
                continue;
            }

            String line = scoreboardLineList.get(i);
            line = MessageUtil.color(line);
            line = replacePlaceholders(line);
            setLine(index, line);
            index--;
        }
    }

    private String replacePlaceholders(String string) {
        CombatLogX plugin = CombatLogX.INSTANCE;
        Player player = getPlayer();
        if(player == null) return string;

        if(Expansions.isEnabled("CompatPlaceholders")) {
            PlaceholderHandler handler = new PlaceholderHandler();
            string = handler.replaceAllPlaceholders(player, string);
        }

        return string;
    }

    private int getMaxLineLength() {
        int minorVersion = NMS_Handler.getMinorVersion();
        if(minorVersion <= 12) return 16;

        return 64;
    }
}