# ShowMy

**ShowMy** is a lightweight Minecraft plugin that lets players view personalized stats and information about themselves using natural, readable commands like:

```
/showmy rank
/showmy balance
/showmy kills
```
It's like saying: “Show my rank” — and that’s exactly what the plugin does. It’s simple, intuitive, and powered by PlaceholderAPI for dynamic, real-time content.
## ⚠️ Requirements
- **This plugin requires PlaceholderAPI 2.11.6**—All dynamic content is powered by it, and ShowMy will not work without it.
- Java Version: 21 or higher.
## Features
- **Natural command structure** — /showmy <something> feels like normal speech
- **Displays personalized info** — rank, balance, stats, links, and more
- **Fully configurable modules** in config.yml
- **Permission-based access** to each module
- **In-game reload command** to update the config instantly without restarting the server
- **PlaceholderAPI integration** for real-time dynamic data
- **Optional debug logging** for admin insight
## Example Use Cases
|Command|Reult Shown|
|---|---|
|/showmy rank|Your current rank is: %luckperms_primary_group%|
|/showmy balance|Your balance is: %vault_eco_balance%|
|/showmy kills|You've got %statistic_player_kills% kills!|
|/showmy coords|Your current coords are: %player_x% %player_y% %player_z%|

Note that **these commands don't come in the plugin by default**, you will need to create them manually in the config.

<details>
<summary>Config.yml</summary>


```
# Modules are the different pieces of information a player can see by running /showmy module
modules:
  example:
    # The text that is shown to the player when running the /showmy module command. Supports PlaceholderAPI placeholders
    text-to-show: 'This is an example!'
    # The permission required to execute the command
    permission: showmy.modules.example

settings:
  # Whether the plugin is disabled or not
  disabled: false

  # Whether debugging information should be logged to the console.
  debug: false

  # The message shown when a player executes a command without the required permission. Supports PlaceholderAPI placeholders
  noPermissionMessage: 'You do not have permission to execute that command!'
```


</details>

## 🛡️ Permissions
|Node|Description|Default|
|---|---|---|
|showmy.use|Use the base /showmy command|true|
|showmy.reload|Reload the plugin’s config|op|
|showmy.modules.*|Access to all module commands|op|
|showmy.modules.<module>|Access to a specific module like rank, kills, etc.|(your choice)|
|showmy.*|Grants every permission related to ShowMy|op|

## Support
**Need help?** DM me on Discord, my username is bestrandomkiller.

📜 This plugin is licensed under a custom license that allows free use and modification but prohibits selling forks or paid versions. See [LICENSE](LICENSE) for full terms.
