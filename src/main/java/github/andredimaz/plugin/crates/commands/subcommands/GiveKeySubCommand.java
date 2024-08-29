package github.andredimaz.plugin.crates.commands.subcommands;

import github.andredimaz.plugin.crates.Main;
import github.andredimaz.plugin.crates.managers.KeyManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GiveKeySubCommand {

    private final KeyManager keyManager;

    public GiveKeySubCommand(Main plugin) {
        this.keyManager = new KeyManager(plugin);  // Inicializa o KeyManager
    }

    public void execute(Player sender, String[] args) {
        if (sender.hasPermission("sentinel.crates.admin")) {
            if (args.length < 4) {  // Verifica se há argumentos suficientes
                sender.sendMessage("§cUso correto: /crates givekey <player> <tipo_da_chave> <quantia>");
                return;
            }

            // Obtém o jogador pelo nome
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§cJogador não encontrado.");
                return;
            }

            String keyType = args[2];  // Tipo da chave
            int quantity;

            try {
                quantity = Integer.parseInt(args[3]);  // Quantidade da chave
            } catch (NumberFormatException e) {
                sender.sendMessage("§cQuantia inválida. Por favor, insira um número.");
                return;
            }

            // Dá a chave ao jogador especificado
            for (int i = 0; i < quantity; i++) {
                boolean success = keyManager.giveKey(target, keyType);
                if (!success) {
                    sender.sendMessage("§cErro: Tipo de chave '" + keyType + "' não encontrado no arquivo de configuração.");
                    return;  // Stop execution if key type is invalid
                }
            }

            sender.sendMessage("§aVocê deu " + quantity + " chave(s) do tipo " + keyType + " para " + target.getName() + ".");
            target.sendMessage("§aVocê recebeu " + quantity + " chave(s) do tipo " + keyType + ".");
        } else {
            sender.sendMessage("§cVocê não tem permissão para usar este comando.");
        }
    }
}
