package app.sklyar.battleplugin;

import app.sklyar.battleplugin.commands.BattleCommand;
import app.sklyar.battleplugin.tabCompletion.BattleTabCompletion;
import org.bukkit.plugin.java.JavaPlugin;

public final class BattlePlugin extends JavaPlugin {
    private static BattlePlugin plugin;

    public static BattlePlugin getInstance() {
        return plugin;
    }
    @Override
    public void onEnable() {
        plugin = this;
        // Plugin startup logic
        BattleCommand battleCommand = new BattleCommand();
        BattleTabCompletion battleTabCompletion = new BattleTabCompletion(battleCommand);
        getCommand("battle").setExecutor(battleCommand);
        getCommand("battle").setTabCompleter(battleTabCompletion);
    }
}
