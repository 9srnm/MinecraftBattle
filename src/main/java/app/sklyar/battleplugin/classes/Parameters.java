package app.sklyar.battleplugin.classes;

import com.sun.org.apache.bcel.internal.generic.ARETURN;

public class Parameters {
    private static int gameDay = 0, spawnRadius = 100, dayLength = 1200, nightLength = 600, borderLength = 2000, borderShrinkTime = 600;

    public String getAllParameters() {
        return "gameDay: " + gameDay + "\nspawnRadius: " + spawnRadius + "\ndayLength: " + dayLength + "\nnightLength: " + nightLength + "\nborderLength: " + borderLength + "\nborderShrinkTime: " + borderShrinkTime;
    }

    public int getGameDay() {
        return gameDay;
    }
    public void changeGameDay(int newGameDay) {
        gameDay = newGameDay;
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
        dayLength = Math.max(100, newDayLength);
    }

    public int getNightLength() {
        return nightLength;
    }
    public void changeNightLength(int newNightLength) {
        nightLength = Math.max(100, newNightLength);
    }

    public int getBorderLength() {
        return borderLength;
    }
    public void changeBorderLength(int newBorderLength) {
        borderLength = Math.max(100, newBorderLength);
    }

    public int getBorderShrinkTime() {
        return borderShrinkTime;
    }
    public void changeBorderShrinkTime(int newBorderShrinkTime) {
        borderShrinkTime = Math.max(100, newBorderShrinkTime);
    }
}
