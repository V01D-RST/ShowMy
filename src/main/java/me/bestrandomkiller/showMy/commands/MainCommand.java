/*
 * ShowMy - a bukkit plugin
 * Copyright (C) 2025  BestRandomKiller
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.bestrandomkiller.showMy.commands;

import me.clip.placeholderapi.PlaceholderAPI;

import me.bestrandomkiller.showMy.ShowMy;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

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
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {

        FileConfiguration config = this.plugin.getConfig(); // We load the config file

        ConfigurationSection settings = config.getConfigurationSection("settings");

        if (settings != null) {

            boolean disabled = settings.getBoolean("disabled", false);
            boolean debug = settings.getBoolean("debug", false);
            String noPermissionMessage = settings.getString("noPermissionMessage", "You do not have permission to execute that command!");
            String disabledMessage = settings.getString("disabledMessage", "The plugin is disabled!");

            if (sender instanceof Player player) {
                if (args.length == 0) {
                    player.sendMessage(PREFIX_COLORED + ChatColor.RED + "You need to provide a sub-command!");
                } else if (args.length == 1) {

                    if (args[0].equalsIgnoreCase("reload")) {
                        if (player.hasPermission("showmy.reload")) {
                            this.plugin.reload("[ShowMy] "); // We reload the config
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
                    } else if (args[0].equalsIgnoreCase("list")) {

                        ConfigurationSection modules = config.getConfigurationSection("modules");

                        if (modules == null) {
                            return true;
                        }

                        if (modules.getKeys(false).isEmpty()) {
                            player.sendMessage(PREFIX_COLORED + ChatColor.RED + "No modules found!");
                        }

                        List<String> moduleNames = new ArrayList<>();

                        for (String moduleName : modules.getKeys(false)) {
                            ConfigurationSection module = modules.getConfigurationSection(moduleName);

                            if (moduleName.equalsIgnoreCase("reload") || moduleName.equalsIgnoreCase("list")) {
                                continue;
                            }

                            String permission = module.getString("permission");

                            if (permission == null) {
                                continue;
                            }

                            if (player.hasPermission(permission) || player.hasPermission("showmy.modules.*")) {
                                moduleNames.add(StringUtils.capitalize(moduleName.toLowerCase()));
                            }

                        }

                        String listOfModules = String.join("\n", moduleNames);
                        player.sendMessage(PREFIX_COLORED + "List of available modules:\n" + listOfModules);

                    } else if (!disabled) {
                        ConfigurationSection modules = settings.getConfigurationSection("modules");
                        if (modules == null) {
                            player.sendMessage(PREFIX_COLORED + ChatColor.RED + "Config file is missing the modules section!");
                            return true;
                        }
                        ConfigurationSection module = modules.getConfigurationSection(args[0]); // Loads the requested module

                        if (module != null) {
                            String textToShow = module.getString("text-to-show");
                            String permission = module.getString("permission");

                            if (permission == null || textToShow == null) {
                                player.sendMessage(PREFIX_COLORED + ChatColor.RED + "The module '" + args[0] + "' is not configured properly!");
                                return true;
                            }

                            if (player.hasPermission(permission) || player.hasPermission("showmy.modules.*")) {
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

                        player.sendMessage(PREFIX_COLORED + ChatColor.RED + "Module '" + args[0] + "' doesn't exist, please provide a valid module name!.");

                        if (debug) {
                            this.plugin.getLogger().info(PREFIX + player.getName() + " issued /showmy " + String.join(" ", args) + ", but that command doesn't exist.");
                        }
                    } else {
                        if (disabledMessage != null && !disabledMessage.isEmpty()) {
                            player.sendMessage(PREFIX_COLORED + PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', disabledMessage)));
                        }
                    }
                } else {
                    player.sendMessage(PREFIX_COLORED + ChatColor.RED + "You must provide a valid command!");
                }
            } else if (sender instanceof ConsoleCommandSender console) {
                if (args.length == 0) {
                    console.sendMessage(PREFIX_COLORED + ChatColor.RED + "You need to provide a sub-command!");
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        this.plugin.reload("[ShowMy] "); // We reload the config
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