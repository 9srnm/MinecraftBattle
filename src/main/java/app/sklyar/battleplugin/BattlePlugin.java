package app.sklyar.battleplugin;

import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.Parameters;
import app.sklyar.battleplugin.commands.BattleCommand;
import app.sklyar.battleplugin.commands.MagicWandCommand;
import app.sklyar.battleplugin.listeners.BlockBreakListener;
import app.sklyar.battleplugin.listeners.PlayerDeathListener;
import app.sklyar.battleplugin.listeners.WandUsageListener;
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
        ItemManager.init();

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Parameters parameters = new Parameters();
        BattleCommand battleCommand = new BattleCommand(parameters, scoreboard);
        BattleTabCompletion battleTabCompletion = new BattleTabCompletion(scoreboard);

        getCommand("battle").setExecutor(battleCommand);
        getCommand("battle").setTabCompleter(battleTabCompletion);

        getCommand("give_wand").setExecutor(new MagicWandCommand());
      
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(scoreboard), this);
        getServer().getPluginManager().registerEvents(new WandUsageListener(), this);

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(scoreboard), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(parameters), this);
    }
}
