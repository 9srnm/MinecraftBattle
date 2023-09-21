package app.sklyar.battleplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;


public class PlayerDeathListener implements Listener {

    private Scoreboard scoreboard;

    public PlayerDeathListener(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    @Deprecated
    @EventHandler
    public void PlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        double playerMaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if (playerMaxHealth == 2){
            player.setGameMode(GameMode.SPECTATOR);
        }
        else{
            player.setMaxHealth(playerMaxHealth - 2);
        }
        if(event.getEntity().getKiller() == null) {
            double minHealth = 100;
            Player minPlayer = null;
            Team player_team = scoreboard.getEntryTeam(player.getName());
            for(Team team : scoreboard.getTeams()){
                if (!(team.equals(player_team)) ){
                    for(String targetName : team.getEntries()){
                        Player target = Bukkit.getPlayerExact(targetName);
                        if (target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() < minHealth){
                            minHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                            minPlayer = target;
                        }
                    }
                }
            }
            if (minPlayer != null){
                double min_player_maxHealth = minPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                minPlayer.setMaxHealth(min_player_maxHealth + 2);
            }


            return;
        }
        if (event.getEntity().getKiller().getType() == EntityType.PLAYER){
            Player killer = event.getEntity().getKiller();
            double killer_maxHealth = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            killer.setMaxHealth(killer_maxHealth + 2);
        }
    }
}