package app.sklyar.battleplugin.listeners;

import app.sklyar.battleplugin.classes.Parameters;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

public class PlayerMoveListener implements Listener {
    private final Parameters parameters;
    private final Scoreboard scoreboard;

    public PlayerMoveListener(Parameters p, Scoreboard s) {
        parameters = p;
        scoreboard = s;
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (parameters.getGameDay() == 1) {
            Player player = e.getPlayer();
            int sectorsAmount = Integer.highestOneBit(scoreboard.getTeams().size() - 1) * 2;
            int zLength = Math.pow((int) Math.sqrt(sectorsAmount), 2) == sectorsAmount ? (int) Math.sqrt(sectorsAmount) : (int) Math.sqrt(sectorsAmount / 2);
            int xLength = sectorsAmount / zLength;
            Team[] teams = new Team[scoreboard.getTeams().size()];
            scoreboard.getTeams().toArray(teams);
            for (int i = 0; i < teams.length; i++) {
                if (teams[i].equals(scoreboard.getPlayerTeam(player))) {
                    int zSector = i / xLength;
                    int xSector = i % xLength;
                    Location spawn = player.getWorld().getSpawnLocation();
                    int coordinateX = (int) spawn.getX() - parameters.getBorderLength() / 2 + parameters.getBorderLength() * xSector / xLength + parameters.getBorderLength() / (2 * xLength);
                    int coordinateZ = (int) spawn.getZ() - parameters.getBorderLength() / 2 + parameters.getBorderLength() * zSector / zLength + parameters.getBorderLength() / (2 * zLength);
                    double distanceFrom = Math.pow(e.getFrom().getX() - coordinateX, 2) + Math.pow(e.getFrom().getZ() - coordinateZ, 2);
                    double distanceTo = Math.pow(e.getTo().getX() - coordinateX, 2) + Math.pow(e.getTo().getZ() - coordinateZ, 2);
                    if ((Math.abs(player.getLocation().getX() - coordinateX) > parameters.getBorderLength() / (2 * xLength) ||
                        Math.abs(player.getLocation().getZ() - coordinateZ) > parameters.getBorderLength() / (2 * zLength)) && (distanceFrom < distanceTo)) {
                        double speed = 10;
                        double xV = (coordinateX - player.getLocation().getX()) / distanceTo * speed;
                        double zV = (coordinateZ - player.getLocation().getZ()) / distanceTo * speed;
                        double yV = 0.5;
                        player.setVelocity(new Vector(xV, yV, zV));
                    }
                }
            }
        }
    }
}
