package network.warzone.tgm.modules.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import network.warzone.tgm.map.SpawnPoint;
import network.warzone.tgm.modules.kit.Kit;
import network.warzone.tgm.user.PlayerContext;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke on 4/28/17.
 */
@AllArgsConstructor @Getter
public class MatchTeam {

    private final String id;
    private String alias;
    private ChatColor color;
    private GameMode gamemode;
    private final boolean spectator;
    private int max;
    private int min;
    @Setter private boolean friendlyFire;
    private final List<PlayerContext> members = new ArrayList<>();

    private final List<Kit> kits = new ArrayList<>();

    //filled onload
    private final List<SpawnPoint> spawnPoints = new ArrayList<>();

    public void addPlayer(PlayerContext playerContext) {
        members.add(playerContext);
    }

    public void removePlayer(PlayerContext playerContext) {
        members.remove(playerContext);
    }

    public boolean containsPlayer(Player player) {
        for (PlayerContext playerContext : members) {
            if (playerContext.getPlayer() == player) {
                return true;
            }
        }
        return false;
    }

    public void addKit(Kit kit) {
        this.kits.add(kit);
    }

    public void addSpawnPoint(SpawnPoint spawnPoint) {
        this.spawnPoints.add(spawnPoint);
    }

    public void setMax(int max) {
        this.max = max;
        Bukkit.getPluginManager().callEvent(new TeamUpdateEvent(this));
    }

    public void setMin(int min) {
        this.min = min;
        Bukkit.getPluginManager().callEvent(new TeamUpdateEvent(this));
    }

    public void setAlias(String alias) {
        this.alias = alias;
        Bukkit.getPluginManager().callEvent(new TeamUpdateEvent(this));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MatchTeam)) return false;
        return ((MatchTeam) other).getId().equals(id);
    }
}
