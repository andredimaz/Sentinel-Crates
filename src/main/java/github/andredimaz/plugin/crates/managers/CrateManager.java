package github.andredimaz.plugin.crates.managers;


import github.andredimaz.plugin.core.utils.basics.ColorUtils;
import github.andredimaz.plugin.core.utils.basics.PacketAS;
import github.andredimaz.plugin.crates.Main;
import github.andredimaz.plugin.core.utils.objects.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CrateManager implements Listener {

    private final Main plugin;
    private final PacketAS armorstand;
    private final ItemStack crateItem;

    public CrateManager(Main plugin, String crateType) {
        this.plugin = plugin;
        this.armorstand = new PacketAS(plugin);
        this.crateItem = createCrateItem(crateType);  // Create the special crate item

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // Create the crate item with NBT data using ItemBuilder
    private ItemStack createCrateItem(String crateType) {
        FileConfiguration config = plugin.getCratesConfig();

        String path = "chaves." + crateType;
        if (!config.contains(path)) {
            plugin.getLogger().severe("Key type '" + crateType + "' not found in crates.yml!");
            return null;
        }

        int materialId = config.getInt(path + ".crate-item.material", 131);
        Material material = Material.getMaterial(materialId);

        if (material == null) {
            material = Material.TRIPWIRE_HOOK;
            return null;
        }

        String name = ColorUtils.colorize(config.getString(path + ".crate-item.nome", "§fChave"));
        List<String> lore = ColorUtils.colorize(config.getStringList(path + ".crate-item.lore"));

        return new ItemBuilder(material)
                .setDisplayName(name)
                .setLore(lore)
                .addNBT("crate-item", 1)  // Add custom NBT tag to identify the crate
                .build();
    }

    public boolean giveCrate(Player player, String crateType) {
        ItemStack crate = createCrateItem(crateType);
        if (crate == null) {
            return false;
        }
        player.getInventory().addItem(crate);
        return true;
    }

    // Get the crate item for giving to players or other uses
    public ItemStack getCrateItem() {
        return this.crateItem;
    }



    // Handle block placement to detect crate item placement
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack itemInHand = event.getItemInHand();

        if (isCrateItem(itemInHand)) {
            Location location = event.getBlock().getLocation().add(0.5, -1.0, 0.5);
            armorstand.spawnAll(location, itemInHand, true, 5.0f, true);
            event.setCancelled(true);
        } else {
            event.setCancelled(false);
            plugin.getLogger().info("Normal block placed by " + event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e){

    }

    // Check if the given item is a crate by verifying its NBT data
    private boolean isCrateItem(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }
        ItemBuilder builder = new ItemBuilder(itemStack);
        String nbtValue = builder.getNBT("crate-item");
        return nbtValue != null && nbtValue.equals("1");
    }
}

