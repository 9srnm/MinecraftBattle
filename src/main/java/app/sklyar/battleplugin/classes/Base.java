package app.sklyar.battleplugin.classes;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;

public class Base {

    public final String name;
    public final Location loc;
    public Integer baseLvl = 1;
    public Integer maxBaseLvl = 5;
    public Integer chestCount = 0;
    public Integer chestMaxCount = 2;
    public boolean isUnbreakable = false;
    public boolean baseRespawn = true;
    public HashMap<Player, Integer> playersHistory1 = new HashMap<Player, Integer>();
    public HashMap<Player, Integer> playersHistory2 = new HashMap<Player, Integer>();
    public Integer[] lvlCosts = {5, 10, 15, 20};

    public PotionEffect[][] effects = {
            {new PotionEffect(PotionEffectType.FAST_DIGGING, PotionEffect.INFINITE_DURATION, 1, true, true, true)},
            {new PotionEffect(PotionEffectType.FAST_DIGGING, PotionEffect.INFINITE_DURATION, 2, true, true, true), new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 1, true, true, true)},
            {new PotionEffect(PotionEffectType.FAST_DIGGING, PotionEffect.INFINITE_DURATION, 2, true, true, true), new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 2, true, true, true), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, PotionEffect.INFINITE_DURATION, 1, true, true, true)},
            {new PotionEffect(PotionEffectType.FAST_DIGGING, PotionEffect.INFINITE_DURATION, 2, true, true, true), new PotionEffect(PotionEffectType.SPEED, PotionEffect.INFINITE_DURATION, 2, true, true, true), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, PotionEffect.INFINITE_DURATION, 2, true, true, true), new PotionEffect(PotionEffectType.REGENERATION, PotionEffect.INFINITE_DURATION, 1, true, true, true)},
    };

    public Base(String name, Location loc){
        this.name = name;
        this.loc = loc;
    }


    public void setLvl(int lvl) {
        this.baseLvl = lvl;
    }


}
