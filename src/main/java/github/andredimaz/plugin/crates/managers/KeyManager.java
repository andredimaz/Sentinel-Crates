package github.andredimaz.plugin.crates.managers;


import github.andredimaz.plugin.core.utils.basics.ColorUtils;
import github.andredimaz.plugin.core.utils.objects.ItemBuilder;
import github.andredimaz.plugin.crates.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KeyManager {

    private final Main plugin;

    public KeyManager(Main plugin) {
        this.plugin = plugin;
    }

    public ItemStack createKey(String keyType) {
        FileConfiguration config = plugin.getCratesConfig();

        String path = "chaves." + keyType;
        if (!config.contains(path)) {
            plugin.getLogger().severe("Key type '" + keyType + "' not found in crates.yml!");
            return null;
        }

        int materialId = config.getInt(path + ".material", 131);
        Material material = Material.getMaterial(materialId);

        if (material == null) {
            material = Material.TRIPWIRE_HOOK;
            return null;
        }

        String name = ColorUtils.colorize(config.getString(path + ".nome", "§fChave"));
        List<String> lore = ColorUtils.colorize(config.getStringList(path + ".lore"));

        return new ItemBuilder(material)
                .setDisplayName(name)
                .setLore(lore)
                .addNBT("key_type", keyType)  // Ensure NBT is set
                .build();
    }




    // Método para dar a chave ao jogador
    public boolean giveKey(Player player, String keyType) {
        ItemStack key = createKey(keyType);
        if (key == null) {
            return false;
        }
        player.getInventory().addItem(key);
        return true;
    }

    public boolean isValidKey(ItemStack item, String keyType) {
        if (item == null || item.getType() != Material.TRIPWIRE_HOOK) {
            return false;
        }

        ItemBuilder itemBuilder = new ItemBuilder(item);
        String itemKeyType = itemBuilder.getNBT("key_type");

        if (itemKeyType == null) {
            return false;  // No NBT data found, not a valid key
        }


        return keyType.equals(itemKeyType);
    }


}
