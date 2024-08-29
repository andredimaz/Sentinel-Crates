package github.andredimaz.plugin.crates.handlers;

import github.andredimaz.plugin.core.utils.basics.PlayerUtils;
import github.andredimaz.plugin.crates.menus.MenuHandler;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ActionHandler {

    private final JavaPlugin plugin;
    private final MenuHandler menuHandler;  // Reference to the dynamic menu manager
    private final Map<String, Consumer<Player>> actions = new HashMap<>();

    public ActionHandler(JavaPlugin plugin, MenuHandler menuHandler) {
        this.plugin = plugin;
        this.menuHandler = menuHandler;  // Pass the dynamic menu instance
    }

    // Register a new action
    public void registerAction(String actionName, Consumer<Player> action) {
        actions.put(actionName.toUpperCase(), action);
    }

    // Process any action
    public void processAction(Player player, String action) {
        if (action.startsWith("[console]")) {
            String command = action.replace("[console] ", "").replace("{player}", player.getName());
            ConsoleCommandSender console = plugin.getServer().getConsoleSender();
            plugin.getServer().dispatchCommand(console, command);
        } else if (action.startsWith("[player]")) {
            String command = action.replace("[player] ", "");
            player.performCommand(command);
        } else if (action.startsWith("[sound]")) {
            String[] parts = action.replace("[sound] ", "").split(" ");
            String soundName = parts[0];
            playSound(player, soundName);
        } else if (action.startsWith("[open-menu]")) {
            String menuName = action.replace("[open-menu] ", "").trim();
            openMenu(player, menuName);
        } else {
            executeAction(player, action);
        }
    }

    // Execute a specific action
    public void executeAction(Player player, String actionName) {
        String[] parts = actionName.split(" ");
        String actionType = parts[0].replace("[", "").replace("]", "").toUpperCase();
        String actionData = parts.length > 1 ? parts[1] : "";

        Consumer<Player> action = actions.get(actionType);
        if (action != null) {
            action.accept(player);
        } else if (actionType.equals("SOUND")) {
            playSound(player, actionData);
        } else {
            player.sendMessage("Ação desconhecida: " + actionName);
        }
    }

    private void openMenu(Player player, String menuName) {
        // NEM EU ENTENDI COMO FUNCIONOU, MAS O QUE IMPORTA É QUE FUNCIONA
        if (!menuHandler.openMenu(player, menuName)) {

        }
    }

    // Action that plays a sound
    private void playSound(Player player, String soundName) {
        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            Location location = player.getLocation();
            PlayerUtils.playSound(location, PlayerUtils.getNearbyPlayers(location, 5), sound, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            player.sendMessage("Som desconhecido: " + soundName);
        }
    }
}
