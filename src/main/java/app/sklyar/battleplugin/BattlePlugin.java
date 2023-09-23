package app.sklyar.battleplugin;

import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.Parameters;
import app.sklyar.battleplugin.commands.BattleCommand;
import app.sklyar.battleplugin.commands.ItemsCommands;
import app.sklyar.battleplugin.commands.TestCommands;
import app.sklyar.battleplugin.listeners.*;
import app.sklyar.battleplugin.listeners.BlocksUsageListener;
import app.sklyar.battleplugin.tabCompletion.BattleTabCompletion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.Bukkit;

import java.util.HashMap;

public final class BattlePlugin extends JavaPlugin {
    private static BattlePlugin plugin;

    public static BattlePlugin getInstance() {
        return plugin;
    }
    @Override
    public void onEnable() {
        plugin = this;

        ItemManager.init();
        HashMap<ItemStack, Integer> shopItemsLvl2 = new HashMap<ItemStack, Integer>() {{
            put(ItemManager.excalibur, 3);
            put(ItemManager.robinsbow, 2);
        }};
        HashMap<ItemStack, Integer> shopItemsLvl1 = new HashMap<ItemStack, Integer>() {{
            put(ItemManager.elderwand, 2);
            put(ItemManager.healthhealer, 1);
        }};
        // Plugin startup logic
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Parameters parameters = new Parameters();
        BattleCommand battleCommand = new BattleCommand(parameters, scoreboard);
        BattleTabCompletion battleTabCompletion = new BattleTabCompletion(scoreboard);

        getCommand("battle").setExecutor(battleCommand);
        getCommand("battle").setTabCompleter(battleTabCompletion);

        getCommand("give_stormbreaker").setExecutor(new ItemsCommands());
        getCommand("give_elderwand").setExecutor(new ItemsCommands());
        getCommand("give_excalibur").setExecutor(new ItemsCommands());
        getCommand("open_shop").setExecutor(new TestCommands(shopItemsLvl1, shopItemsLvl2));

        getServer().getPluginManager().registerEvents(new ItemsUsageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(scoreboard), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(shopItemsLvl1, shopItemsLvl2), this);
        getServer().getPluginManager().registerEvents(new BlocksUsageListener(shopItemsLvl1, shopItemsLvl2), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);

        getServer().getPluginManager().registerEvents(new BlockBreakListener(parameters), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(parameters, scoreboard), this);
    }
}
