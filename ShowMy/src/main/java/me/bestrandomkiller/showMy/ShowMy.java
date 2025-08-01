package me.bestrandomkiller.showMy;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bstats.bukkit.Metrics;

import me.bestrandomkiller.showMy.commands.MainCommand;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShowMy extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();

        int pluginId = 26724; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);

        this.getCommand("showmy").setExecutor(new MainCommand(this, "[ShowMy] ", ChatColor.YELLOW + "[ShowMy] " + ChatColor.RESET));

        getLogger().info("[ShowMy] Successfully enabled!");

    }
}
