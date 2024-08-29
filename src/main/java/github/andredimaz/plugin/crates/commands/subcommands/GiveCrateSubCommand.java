package github.andredimaz.plugin.crates.commands.subcommands;

import github.andredimaz.plugin.crates.Main;
import github.andredimaz.plugin.crates.managers.CrateManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GiveCrateSubCommand {

    private final Main plugin;
    private final CrateManager crateManager;  // Initialize in the constructor

    public GiveCrateSubCommand(Main plugin) {
        this.plugin = plugin;
        this.crateManager = new CrateManager(plugin, null);
    }

    public void execute(Player sender, String[] args) {
        if (!sender.hasPermission("sentinel.crates.admin")) {
            sender.sendMessage("§cVocê não tem permissão para usar este comando.");
            return;
        }

        if (args.length < 3) {  // Check if there are enough arguments
            sender.sendMessage("§cUso correto: /crates givecrate <player> <tipo_da_chave>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cJogador não encontrado.");
            return;
        }

        String crateType = args[2];  // Crate type

        // Give crate to the target player instead of sender
        boolean success = crateManager.giveCrate(target, crateType);
        if (!success) {
            sender.sendMessage("§cErro: Tipo de chave '" + crateType + "' não encontrado no arquivo de configuração.");
            return;  // Stop execution if crate type is invalid
        }

        target.sendMessage("§aVocê recebeu uma crate!");
        sender.sendMessage("§aCrate entregue a " + target.getName() + ".");
    }
}
