package github.andredimaz.plugin.crates.commands.subcommands;

import github.andredimaz.plugin.crates.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadSubCommand {

    private final Main plugin;

    public ReloadSubCommand(Main plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player) || sender.hasPermission("sentinel.reload")) {
            try {
                // Recarrega config.yml
                plugin.reloadConfig();

                // Recarrega menus.yml
                plugin.loadMenusConfig();
                plugin.loadCratesConfig();

                sender.sendMessage("§aConfigurações recarregadas com sucesso!");
            } catch (Exception e) {
                sender.sendMessage("§cOcorreu um erro ao recarregar as configurações.");
                e.printStackTrace();
            }
        } else {
            sender.sendMessage("§cVocê não tem permissão para usar este comando.");
        }
    }
}
