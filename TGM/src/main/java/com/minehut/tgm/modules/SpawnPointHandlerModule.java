package com.minehut.tgm.modules;

import com.minehut.tgm.TGM;
import com.minehut.tgm.join.MatchJoinEvent;
import com.minehut.tgm.map.SpawnPoint;
import com.minehut.tgm.match.Match;
import com.minehut.tgm.match.MatchModule;
import com.minehut.tgm.match.MatchStatus;
import com.minehut.tgm.modules.team.MatchTeam;
import com.minehut.tgm.modules.team.TeamChangeEvent;
import com.minehut.tgm.modules.team.TeamManagerModule;
import com.minehut.tgm.user.PlayerContext;
import com.minehut.tgm.util.Players;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SpawnPointHandlerModule extends MatchModule implements Listener {
    @Getter private TeamManagerModule teamManagerModule;
    @Getter private SpectatorModule spectatorModule;

    @Override
    public void load(Match match) {
        this.teamManagerModule = match.getModule(TeamManagerModule.class);
        this.spectatorModule = match.getModule(SpectatorModule.class);
    }

    @EventHandler
    public void onTeamChange(TeamChangeEvent event) {
        if (TGM.get().getMatchManager().getMatch().getMatchStatus() == MatchStatus.MID) {
            spawnPlayer(event.getPlayerContext(), event.getTeam());
        }
        //player is joining the server
        else if (event.getOldTeam() == null) {
            spawnPlayer(event.getPlayerContext(), event.getTeam());
        }
        //player is swapping teams pre/post match.
        else {
            //we don't need to teleport them in this case. Let them stay in their position.
        }
    }

    private void spawnPlayer(PlayerContext playerContext, MatchTeam matchTeam) {
        Players.reset(playerContext.getPlayer(), true);

        if (matchTeam.isSpectator()) {
            spectatorModule.applySpectatorKit(playerContext);
            playerContext.getPlayer().teleport(getTeamSpawn(matchTeam).getLocation());
        } else {
            matchTeam.getKits().get(0).apply(playerContext.getPlayer(), matchTeam);
            playerContext.getPlayer().teleport(getTeamSpawn(matchTeam).getLocation());
        }
    }

    @Override
    public void enable() {
        for (MatchTeam matchTeam : TGM.get().getModule(TeamManagerModule.class).getTeams()) {
            if (!matchTeam.isSpectator()) {
                for (PlayerContext player : matchTeam.getMembers()) {
                    spawnPlayer(player, matchTeam);
                }
            }
        }
    }

    private SpawnPoint getTeamSpawn(MatchTeam matchTeam) {
        //todo: actually randomize spawn points instead of grabbing first one.
        return matchTeam.getSpawnPoints().get(0);
    }
}
