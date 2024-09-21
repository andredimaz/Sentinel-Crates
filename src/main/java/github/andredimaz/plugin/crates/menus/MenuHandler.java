package github.andredimaz.plugin.crates.menus;

import github.andredimaz.plugin.core.utils.basics.ColorUtils;
import github.andredimaz.plugin.core.utils.inventories.InventoryButtons;
import github.andredimaz.plugin.crates.Main;
import github.andredimaz.plugin.crates.handlers.ActionHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MenuHandler implements Listener {

    private final JavaPlugin plugin;
    private final ActionHandler actionHandler;
    private final Map<String, InventoryButtons> menus = new HashMap<>();

    public MenuHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.actionHandler = new ActionHandler(plugin, this);

        // Load menus.yml configuration
        FileConfiguration menusConfig = ((Main) plugin).getMenusConfig();
        configureMenus(menusConfig);

        // Register click event handler
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // Configure all menus based on the menus.yml configuration
    private void configureMenus(FileConfiguration menusConfig) {
        menusConfig.getKeys(false).forEach(menuId -> {
            String title = ColorUtils.colorize(menusConfig.getString(menuId + ".titulo", "&8"));
            int rows = menusConfig.getInt(menuId + ".linhas", 6) * 9;

            InventoryButtons inventoryButtons = new InventoryButtons(rows, title);
            configureMenuButtons(inventoryButtons, menusConfig, menuId);

            menus.put(menuId, inventoryButtons);
        });
    }

    // Configure buttons for a specific menu
    private void configureMenuButtons(InventoryButtons inventoryButtons, FileConfiguration menusConfig, String menuId) {
        menusConfig.getConfigurationSection(menuId + ".butoes").getKeys(false).forEach(key -> {
            String materialString = menusConfig.getString(menuId + ".butoes." + key + ".material", "STONE");
            String name = ColorUtils.colorize(menusConfig.getString(menuId + ".butoes." + key + ".nome", "&fBotão"));
            List<String> lore = ColorUtils.colorize(menusConfig.getStringList(menuId + ".butoes." + key + ".lore"));
            int slot = menusConfig.getInt(menuId + ".butoes." + key + ".slot", 0);
            List<String> actions = menusConfig.getStringList(menuId + ".butoes." + key + ".acoes");

            InventoryButtons.Button button = new InventoryButtons.Button(materialString, name, lore, new int[]{slot}, player -> {
                // Process the actions configured using ActionHandler
                for (String action : actions) {
                    actionHandler.processAction(player, action);
                }
            });

            inventoryButtons.addButton(button);
        });
    }

    // Open a menu based on its identifier
    public boolean openMenu(Player player, String menuId) {
        InventoryButtons menu = menus.get(menuId);
        if (menu != null) {
            player.openInventory(menu.getInventory());
        } else {
            player.sendMessage("Menu not found: " + menuId);
        }
        return false;
    }

    // Handle inventory click events
    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        menus.values().forEach(menu -> menu.handleClick(event)); // Cancels item movement and processes the click
    }
}
