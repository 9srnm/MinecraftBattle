package app.sklyar.battleplugin.classes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Base {

    public final String name;
    public final Location loc;
    public Integer baseLvl = 1;
    public Integer maxBaseLvl = 5;
    public boolean isUnbreakable = false;
    public HashMap<Player, Integer> playersHistory1 = new HashMap<Player, Integer>();
    public HashMap<Player, Integer> playersHistory2 = new HashMap<Player, Integer>();
    public Integer[] lvlCosts = {5, 10, 15, 20};

    public Base(String name, Location loc){
        this.name = name;
        this.loc = loc;
    }


    public void LvlUp(){
        baseLvl += 1;
        if (baseLvl == 2){

        }
        else if (baseLvl == 3){

        }
        else if (baseLvl == 4){

        }
        else if (baseLvl == 5){

        }
    }

    public void LvlDown(){
        baseLvl -= 1;
    }


}
