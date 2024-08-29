package github.andredimaz.plugin.crates.listeners;


import github.andredimaz.plugin.core.utils.objects.ItemBuilder;
import github.andredimaz.plugin.crates.managers.KeyManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class KeyListener implements Listener {

    private final KeyManager keyManager;

    public KeyListener(KeyManager keyManager) {
        this.keyManager = keyManager;
    }

    @EventHandler
    public void onPlayerUseKey(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();

        ItemBuilder itemBuilder = new ItemBuilder(item);
        String itemKeyType = itemBuilder.getNBT("key_type");


        if (keyManager.isValidKey(item, itemKeyType) && event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (item.getAmount() == 1 || player.isSneaking()) {
                player.getInventory().removeItem(item);
                event.setCancelled(true);
            } else {
                item.setAmount(item.getAmount() - 1);
                event.setCancelled(true);
            }
        } else if (keyManager.isValidKey(item, itemKeyType) && event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if (item.getAmount() == 1 || player.isSneaking()) {
                player.getInventory().removeItem(item); //Adicionar metodo para remover chave e abrir crate
                event.setCancelled(true);
            } else {
                item.setAmount(item.getAmount() - 1);
                event.setCancelled(true);
            }
        }
    }


}

