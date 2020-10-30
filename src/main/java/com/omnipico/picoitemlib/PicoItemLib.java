package com.omnipico.picoitemlib;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PicoItemLib extends JavaPlugin {
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        //this.getCommand("pk").setExecutor(commandPK);
        //this.getCommand("pk").setTabCompleter(commandPK);
        getServer().getPluginManager().registerEvents(new PicoItemListener(), this);
    }

    @Override
    public void onDisable() {
        //Fired when the server stops and disables all plugins
    }
}
