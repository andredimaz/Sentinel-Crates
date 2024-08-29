package github.andredimaz.plugin.crates.commands.subcommands;

import org.bukkit.entity.Player;

public class EditCrateSubCommand {

    public void execute(Player player, String[] args) {
        if (player.hasPermission("sentinel.crates.admin")) {
            // Lógica para editar uma crate
            player.sendMessage("Modo de edição de crates ativado!");
        } else {
            player.sendMessage("Você não tem permissão para usar este comando.");
        }
    }
}

