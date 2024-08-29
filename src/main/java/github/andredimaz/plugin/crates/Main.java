package github.andredimaz.plugin.crates;

import github.andredimaz.plugin.crates.commands.CratesCommand;
import github.andredimaz.plugin.crates.listeners.KeyListener;
import github.andredimaz.plugin.crates.managers.CrateManager;
import github.andredimaz.plugin.crates.managers.KeyManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin implements Listener {

    private FileConfiguration menusConfig;
    private FileConfiguration cratesConfig;
    private KeyManager keyManager;


    @Override
    public void onEnable() {
        loadMenusConfig();  // Certifique-se de que isso é chamado antes de qualquer outra coisa
        loadCratesConfig();

        // Initialize the KeyManager with the Main plugin instance
        this.keyManager = new KeyManager(this);

        // Register the KeyListener with the properly initialized KeyManager
        getServer().getPluginManager().registerEvents(new KeyListener(this.keyManager), this);

        if (menusConfig == null) {
            getLogger().severe("O arquivo menus.yml não foi carregado corretamente. O plugin não pode ser habilitado.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Inicializa e registra o comando
        getCommand("crates").setExecutor(new CratesCommand(this));
        getLogger().info("Sentinel-Crates habilitado com sucesso!");

    }

    @Override
    public void onDisable() {
        getLogger().info("Sentinel-Crates desabilitado!");
    }

    public FileConfiguration getMenusConfig() {
        return menusConfig;
    }
    public FileConfiguration getCratesConfig() {
        return cratesConfig;
    }

    public void loadMenusConfig() {
        File menusFile = new File(getDataFolder(), "menus.yml");

        // Verifica se o arquivo não existe e, se não existir, o copia da pasta de resources
        if (!menusFile.exists()) {
            saveResource("menus.yml", false);  // Copia o arquivo da JAR para o diretório de dados do plugin
        }

        // Carrega o arquivo menus.yml
        menusConfig = YamlConfiguration.loadConfiguration(menusFile);
    }


    public void loadCratesConfig() {
        File cratesFile = new File(getDataFolder(), "crates.yml");

        // Verifica se o arquivo não existe e, se não existir, o copia da pasta de resources
        if (!cratesFile.exists()) {
            saveResource("crates.yml", false);  // Copia o arquivo da JAR para o diretório de dados do plugin
        }

        // Carrega o arquivo crates.yml (it seems this was a typo)
        cratesConfig = YamlConfiguration.loadConfiguration(cratesFile);
    }

}
