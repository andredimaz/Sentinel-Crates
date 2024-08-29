package github.andredimaz.plugin.crates.commands;

import github.andredimaz.plugin.crates.Main;
import github.andredimaz.plugin.crates.commands.subcommands.GiveKeySubCommand;
import github.andredimaz.plugin.crates.commands.subcommands.GiveCrateSubCommand;
import github.andredimaz.plugin.crates.commands.subcommands.EditCrateSubCommand;
import github.andredimaz.plugin.crates.commands.subcommands.ReloadSubCommand;
import github.andredimaz.plugin.crates.menus.MenuHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CratesCommand implements CommandExecutor {

    private final MenuHandler cratesMenu;
    private final GiveKeySubCommand giveKeySubCommand;
    private final GiveCrateSubCommand giveCrateSubCommand;
    private final EditCrateSubCommand editCrateSubCommand;
    private final ReloadSubCommand reloadSubCommand;

    public CratesCommand(Main plugin) {
        this.cratesMenu = new MenuHandler(plugin);
        this.giveKeySubCommand = new GiveKeySubCommand(plugin);
        this.giveCrateSubCommand = new GiveCrateSubCommand(plugin);
        this.editCrateSubCommand = new EditCrateSubCommand();
        this.reloadSubCommand = new ReloadSubCommand(plugin);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Apenas jogadores podem executar este comando.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // Abre o menu se nenhum subcomando for passado
            cratesMenu.openMenu(player, "principal");
            return true;
        }

        // Delegar para o subcomando correspondente
        switch (args[0].toLowerCase()) {
            case "givekey":
                giveKeySubCommand.execute(player, args);
                break;
            case "givecrate":
                giveCrateSubCommand.execute(player, args);
                break;
            case "edit":
                editCrateSubCommand.execute(player, args);
                break;
            case "reload":
                reloadSubCommand.execute(sender, cmd, label, args);
                break;
            default:
                player.sendMessage("Subcomando desconhecido. Use /crates <givekey | givecrate | edit | reload>");
                break;
        }

        return true;
    }
}
