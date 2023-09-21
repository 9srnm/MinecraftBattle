package app.sklyar.battleplugin;

import app.sklyar.battleplugin.classes.Parameters;
import app.sklyar.battleplugin.commands.BattleCommand;
import app.sklyar.battleplugin.tabCompletion.BattleTabCompletion;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.Bukkit;

public final class BattlePlugin extends JavaPlugin {
    private static BattlePlugin plugin;

    public static BattlePlugin getInstance() {
        return plugin;
    }
    @Override
    public void onEnable() {
        plugin = this;
        // Plugin startup logic
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        Parameters parameters = new Parameters();
        BattleCommand battleCommand = new BattleCommand(parameters, scoreboard);
        BattleTabCompletion battleTabCompletion = new BattleTabCompletion(battleCommand, scoreboard);
        getCommand("battle").setExecutor(battleCommand);
        getCommand("battle").setTabCompleter(battleTabCompletion);
    }
}
