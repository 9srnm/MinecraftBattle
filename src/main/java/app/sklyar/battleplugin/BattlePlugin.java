package app.sklyar.battleplugin;

import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.Base;
import app.sklyar.battleplugin.classes.Parameters;
import app.sklyar.battleplugin.commands.BattleCommand;
import app.sklyar.battleplugin.commands.ItemsCommands;
import app.sklyar.battleplugin.commands.TestCommands;
import app.sklyar.battleplugin.listeners.*;
import app.sklyar.battleplugin.listeners.BlocksUsageListener;

import app.sklyar.battleplugin.tabCompletion.BattleTabCompletion;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class BattlePlugin extends JavaPlugin {
    private static BattlePlugin plugin;

    public static BattlePlugin getInstance() {
        return plugin;
    }

    public void schematics(String filename, World world, int x, int y, int z) {
        Clipboard clipboard;
        File file = new File(filename);
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
            try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(x, y, z))
                        // configure here
                        .build();
                Operations.complete(operation);
            }
        }
        catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onEnable() {
        plugin = this;

        ItemManager.init();

        List<Entity> lst = new ArrayList<>();
        List<Base> baseList = new ArrayList<>();
        HashMap<ItemStack, Integer> shopItemsLvl2 = new HashMap<ItemStack, Integer>() {{
            put(ItemManager.robinsbow, 2);
        }};
        HashMap<ItemStack, Integer> shopItemsLvl1 = new HashMap<ItemStack, Integer>() {{
            put(ItemManager.healthhealer, 1);
            put(ItemManager.compassoftruth, 1);
            put(ItemManager.teleportationpotion, 1);
        }};
        // Plugin startup logic
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Parameters parameters = new Parameters();
        BattleCommand battleCommand = new BattleCommand(parameters, scoreboard, baseList);
        BattleTabCompletion battleTabCompletion = new BattleTabCompletion(scoreboard);

        getCommand("battle").setExecutor(battleCommand);
        getCommand("battle").setTabCompleter(battleTabCompletion);

        getCommand("give_stormbreaker").setExecutor(new ItemsCommands());
        getCommand("give_elderwand").setExecutor(new ItemsCommands());
        getCommand("give_excalibur").setExecutor(new ItemsCommands());
        getCommand("open_shop").setExecutor(new TestCommands(shopItemsLvl1, shopItemsLvl2));

        getServer().getPluginManager().registerEvents(new ItemsUsageListener(), this);

        getServer().getPluginManager().registerEvents(new ShopListener(shopItemsLvl1, shopItemsLvl2), this);
        getServer().getPluginManager().registerEvents(new BlocksUsageListener(shopItemsLvl1, shopItemsLvl2), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(lst), this);
        getServer().getPluginManager().registerEvents(new TrapBreakListener(lst), this);
        getServer().getPluginManager().registerEvents(new BaseInventoryListener(baseList, parameters), this);
        getServer().getPluginManager().registerEvents(new BaseUsageListener(baseList, parameters), this);

        getServer().getPluginManager().registerEvents(new PlayerDamageListener(scoreboard, parameters, baseList), this);

        getServer().getPluginManager().registerEvents(new SpawnBlockPlaceListener(parameters), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(parameters, scoreboard), this);
        getServer().getPluginManager().registerEvents(new EnchantmentTableEvent(parameters), this);
    }
}
