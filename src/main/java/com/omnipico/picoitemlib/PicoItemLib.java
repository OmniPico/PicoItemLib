package com.omnipico.picoitemlib;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PicoItemLib extends JavaPlugin {
    Chat chat;
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        chat = getServer().getServicesManager().load(Chat.class);
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
