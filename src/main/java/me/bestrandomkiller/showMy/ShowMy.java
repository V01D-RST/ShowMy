package me.bestrandomkiller.showMy;

import org.bstats.bukkit.Metrics;

import me.bestrandomkiller.showMy.commands.MainCommand;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShowMy extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        try {

            saveDefaultConfig();
            reload("[ShowMy] ");

            ConfigurationSection settings = getConfig().getConfigurationSection("settings");

            boolean telemetry;

            if (settings != null) {
                telemetry = settings.getBoolean("allowTelemetry", true); // We load the config file
            } else {
                getLogger().warning("[ShowMy] Config file is missing the settings section!");
                telemetry = true;
            }

            int pluginId = 26724; // <-- Replace with the id of your plugin!
            if (telemetry) {
                Metrics metrics = new Metrics(this, pluginId);
            }

            this.getCommand("showmy").setExecutor(new MainCommand(this, "[ShowMy] ", ChatColor.YELLOW + "[ShowMy] " + ChatColor.RESET));

        } catch (Exception e) {
            getLogger().severe("[FATAL] Failed to load ShowMy! " + e.getMessage());
            return;
        }

        getLogger().info("[ShowMy] Successfully enabled!");

    }

    public void reload(String prefix) {

        reloadConfig();

        ConfigurationSection modules = getConfig().getConfigurationSection("modules");

        if (modules != null) {
            if (!modules.getKeys(false).isEmpty()) {
                for (String moduleName : modules.getKeys(false)) {
                    ConfigurationSection module = modules.getConfigurationSection(moduleName);

                    if (moduleName.equalsIgnoreCase("reload") || moduleName.equalsIgnoreCase("list")) {
                        getLogger().warning(prefix + "There's a module with a forbidden name! The module will be ignored.");
                        continue;
                    }

                    String textToShow = module.getString("text-to-show");
                    String permission = module.getString("permission");

                    if (permission == null) {
                        getLogger().warning(prefix + "You have not provided a permission in the module: " + moduleName);
                    }

                    if (textToShow == null) {
                        getLogger().warning(prefix + "You have not provided a text to show in the module: " + moduleName);
                    }

                }
            } else {
                getLogger().warning(prefix + "You have not provided any modules on the config, this has the same effects as setting disabled to true and is not recommended.");
            }

        } else {
            getLogger().warning(prefix + "Config file is missing the modules section!");
        }

    }

}
