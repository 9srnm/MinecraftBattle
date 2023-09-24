package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.Items.ItemManager;
import app.sklyar.battleplugin.classes.Base;
import app.sklyar.battleplugin.classes.Parameters;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.List;

public class FlagMoveListener implements Listener {
    private final List<Base> baseList;

    public FlagMoveListener(List<Base> baseList) {
        this.baseList = baseList;
    }
    @EventHandler
    @Deprecated
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack[] inventory = player.getInventory().getContents();
        boolean flag = false;
        String baseName = null;
        for (ItemStack target : inventory) {
            if (target != null && target.getType() == Material.END_PORTAL_FRAME){
                flag = true;
                baseName = target.getItemMeta().getDisplayName();
                player.getInventory().remove(Material.END_PORTAL_FRAME);
                break;
            }
        }
        if (flag)
            for(Base base : baseList) {
                if (base.loc.distance(player.getLocation()) <= 32 &&
                        base.name.equalsIgnoreCase(player.getScoreboard().getPlayerTeam(player).getName())){
                    for(Base losersBase : baseList) {
                        if (losersBase.name.equalsIgnoreCase(baseName)){
                            losersBase.baseRespawn = false;
                            break;
                        }
                    }
                    break;
                }
            }
    }
}
