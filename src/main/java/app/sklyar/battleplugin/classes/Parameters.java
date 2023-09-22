package app.sklyar.battleplugin.classes;

import com.sun.org.apache.bcel.internal.generic.ARETURN;
import org.bukkit.ChatColor;

public class Parameters {
    private static int gameDay = 0, spawnRadius = 100, dayLength = 1200, nightLength = 600, borderLength = 2000, borderShrinkTime = 600;
    private static long previousTime = 23999;
    private static boolean gameRuns = false;
    private static String prefix = "" + ChatColor.AQUA + ChatColor.BOLD + "[BATTLE] " + ChatColor.RESET;

    public String getPrefix() {
        return prefix;
    }

    public int getGameDay() {
        return gameDay;
    }
    public void changeGameDay(int newGameDay) {
        gameDay = newGameDay;
    }

    public boolean getGameRuns() {
        return gameRuns;
    }
    public void changeGameRuns(boolean newGameRuns) {
        gameRuns = newGameRuns;
    }

    public long getPreviousTime() {
        return previousTime;
    }
    public void changePreviousTime(long newPreviousTime) {
        previousTime = newPreviousTime;
    }

    public int getSpawnRadius() {
        return spawnRadius;
    }
    public void changeSpawnRadius(int newSpawnRadius) {
        spawnRadius = Math.max(0, newSpawnRadius);
    }

    public int getDayLength() {
        return dayLength;
    }
    public void changeDayLength(int newDayLength) {
        dayLength = Math.max(1, newDayLength);
    }

    public int getNightLength() {
        return nightLength;
    }
    public void changeNightLength(int newNightLength) {
        nightLength = Math.max(1, newNightLength);
    }

    public int getBorderLength() {
        return borderLength;
    }
    public void changeBorderLength(int newBorderLength) {
        borderLength = Math.max(1, newBorderLength);
    }

    public int getBorderShrinkTime() {
        return borderShrinkTime;
    }
    public void changeBorderShrinkTime(int newBorderShrinkTime) {
        borderShrinkTime = Math.max(1, newBorderShrinkTime);
    }
}
