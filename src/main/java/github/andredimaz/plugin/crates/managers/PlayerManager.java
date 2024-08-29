package github.andredimaz.plugin.crates.managers;

import github.andredimaz.plugin.core.Main;
import github.andredimaz.plugin.crates.handlers.PlayerData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private final Main plugin;
    private HashMap<UUID, PlayerData> playerDataMap;

    public PlayerManager(Main plugin) {
        this.plugin = plugin;
        this.playerDataMap = new HashMap<>();
    }

    public void initializePlayer(Player player) {
        UUID uuid = player.getUniqueId();

        if (!playerDataMap.containsKey(uuid)) {
            PlayerData defaultData = new PlayerData(plugin.getConfig().getInt("limite-padrao"));
            playerDataMap.put(uuid, defaultData);
        }
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }

    public void setPlayerData(Player player, int limite){
        UUID uuid = player.getUniqueId();
        PlayerData data = playerDataMap.get(uuid);

        if (data != null) {
            data.setLimite(limite);
        } else {
            playerDataMap.put(uuid, new PlayerData(limite));
        }
    }

    public void removePlayerData(Player player) {
        playerDataMap.remove(player.getUniqueId());
    }
}