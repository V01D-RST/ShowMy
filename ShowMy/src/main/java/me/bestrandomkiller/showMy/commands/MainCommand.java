package me.bestrandomkiller.showMy.commands;

import me.clip.placeholderapi.PlaceholderAPI;

import me.bestrandomkiller.showMy.ShowMy;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    private final String PREFIX_COLORED;
    private final ShowMy plugin;
    private final String PREFIX;

    public MainCommand(ShowMy plugin, String PREFIX, String PREFIX_COLORED) {
        this.plugin = plugin;
        this.PREFIX = PREFIX;
        this.PREFIX_COLORED = PREFIX_COLORED;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        FileConfiguration config = this.plugin.getConfig(); // We load the config file

        ConfigurationSection settings = config.getConfigurationSection("settings");

        if (settings != null) {

            boolean disabled = settings.getBoolean("disabled");
            boolean debug = settings.getBoolean("debug");
            String noPermissionMessage = settings.getString("noPermissionMessage");

            if (sender instanceof Player player) {
                if (args.length == 0) {
                    player.sendMessage(PREFIX_COLORED + ChatColor.RED + "You need to provide a sub-command!");
                } else if (args.length == 1) {

                    if (args[0].equals("reload")) {
                        if (player.hasPermission("showmy.reload")) {
                            this.plugin.reloadConfig(); // We reload the config
                            player.sendMessage(PREFIX_COLORED + ChatColor.GREEN + "Config file successfully reloaded!");

                            if (debug) {
                                this.plugin.getLogger().info(PREFIX + player.getName() + " issued /showmy " + String.join(" ", args));
                            }

                        } else {
                            if (noPermissionMessage != null) {
                                player.sendMessage(PREFIX_COLORED + PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', noPermissionMessage)));
                            } else {
                                player.sendMessage(PREFIX_COLORED + ChatColor.RED + "You do not have permission to execute that command!");
                            }

                            if (debug) {
                                this.plugin.getLogger().info(PREFIX + player.getName() + " issued /showmy " + String.join(" ", args) + " but didn't have the required permission: showmy.reload");
                            }
                        }
                    } else if (!disabled) {

                        ConfigurationSection modulesSection = config.getConfigurationSection("modules"); // Loads all the modules in the config

                        if (modulesSection != null) {
                            for (String moduleName : modulesSection.getKeys(false)) {
                                ConfigurationSection module = modulesSection.getConfigurationSection(moduleName); // Loads the current module
                                if (module != null) {
                                    String textToShow = module.getString("text-to-show");
                                    String permission = module.getString("permission");

                                    if (permission != null) {
                                        if (textToShow != null) {
                                            if (args[0].equals(moduleName)) {
                                                if (player.hasPermission(permission) | player.hasPermission("showmy.modules.*")) {
                                                    player.sendMessage(PREFIX_COLORED + PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', textToShow)));

                                                    if (debug) {
                                                        this.plugin.getLogger().info(PREFIX + player.getName() + " issued /showmy " + String.join(" ", args));
                                                    }

                                                } else {
                                                    if (noPermissionMessage != null) {
                                                        player.sendMessage(PREFIX_COLORED + PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', noPermissionMessage)));
                                                    } else {
                                                        player.sendMessage(PREFIX_COLORED + ChatColor.RED + "You do not have permission to execute that command!");
                                                    }

                                                    if (debug) {
                                                        this.plugin.getLogger().info(PREFIX + player.getName() + " issued /showmy " + String.join(" ", args) + " but didn't have the required permission: " + permission);
                                                    }
                                                }
                                                return true;
                                            }
                                        } else {
                                            this.plugin.getLogger().warning(PREFIX + "You have not provided a text to show in the module: " + moduleName);
                                            return true;
                                        }
                                    } else {
                                        this.plugin.getLogger().warning(PREFIX + "You have not provided a permission in the module: " + moduleName);
                                        return true;
                                    }
                                }
                            }

                            player.sendMessage(PREFIX_COLORED + ChatColor.RED + "Sub-command '" + args[0] + "' doesn't exist, please provide a valid sub-command.");

                            if (debug) {
                                this.plugin.getLogger().info(PREFIX + player.getName() + " issued /showmy " + String.join(" ", args) + " but that command doesn't exist.");
                            }

                        } else {
                            this.plugin.getLogger().warning(PREFIX + "You have not provided any modules on the config, this has the same effects as setting disabled to true and is not recommended.");
                        }
                    }
                } else {
                    player.sendMessage(PREFIX_COLORED + ChatColor.RED + "You must provide a valid command!");
                }
            } else if (sender instanceof ConsoleCommandSender console) {
                if (args.length == 0) {
                    console.sendMessage(PREFIX_COLORED + ChatColor.RED + "You need to provide a sub-command!");
                } else if (args.length == 1) {
                    if (args[0].equals("reload")) {
                        this.plugin.reloadConfig(); // We reload the config
                        console.sendMessage(PREFIX_COLORED + ChatColor.GREEN + "Config file successfully reloaded!");
                    } else if (debug) {
                        this.plugin.getLogger().info(PREFIX + "The console issued /showmy " + String.join(" ", args) + " but console module commands are not supported.");
                    }
                } else {
                    console.sendMessage(PREFIX_COLORED + ChatColor.RED + "You must provide a valid command!");
                }
                
            } else if (sender instanceof BlockCommandSender commandBlock) {
                if (debug) {
                    String worldName = commandBlock.getBlock().getLocation().getWorld().getName();
                    int x = commandBlock.getBlock().getLocation().getBlockX();
                    int y = commandBlock.getBlock().getLocation().getBlockY();
                    int z = commandBlock.getBlock().getLocation().getBlockZ();
                    this.plugin.getLogger().info(PREFIX + "A command block located in the world: " + worldName + ", at the " + x + " " + y + " " + z + " coordinates issued /showmy " + String.join(" ", args) + " but command block commands are not supported.");
                }
            }
        } else {
            this.plugin.getLogger().severe(PREFIX + "Missing settings in config.yml! This may lead to unintended behavior.");
        }
        return true;
    }
}