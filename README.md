ShowMy is a lightweight Minecraft plugin that lets players view personalized stats and information about themselves using natural, readable commands like:


/showmy rank  
/showmy balance  
/showmy kills

## Requirements
- **This plugin requires PlaceholderAPI 2.12.3:** All dynamic content is powered by it, and ShowMy will not work without it.
- Java Version: 21 or higher.
## Features
- Beginner-friendly command structure: /showmy \<something> feels like normal speech
- Displays personalized info: rank, balance, stats, links, and more
- Fully configurable modules in config.yml
- Permission-based access to each module
- In-game reload command to update the config instantly without restarting the server
- PlaceholderAPI integration for real-time dynamic data
- Optional debug logging for admin insight
## Example Use Cases
|Command|Reult Shown|
|---|---|
|/showmy rank|Your current rank is: %luckperms_primary_group%|
|/showmy balance|Your balance is: %vault_eco_balance%|
|/showmy kills|You've got %statistic_player_kills% kills!|
|/showmy coords|Your current coords are: %player_x% %player_y% %player_z%|

Note that these commands don't come in the plugin by default, you will need to create them manually in the config.

<details>
<summary>Config.yml</summary>


```
# Modules are the different pieces of information a player can see by running /showmy module
# Forbidden module names include 'reload' and 'list'
modules:
  example:
    # The text that is shown to the player when running the /showmy module command. Supports PlaceholderAPI placeholders
    text-to-show: 'This is an example!'
    # The permission required to execute the command
    permission: showmy.modules.example

settings:
  # Whether the plugin is disabled or not
  disabled: false

  # The message shown when the plugin is disabled and someone attempts to run a command. Supports PlaceholderAPI placeholders
  # Leave blank to not show any message
  disabledMessage: 'The plugin is disabled!'

  # Whether debugging information should be logged to the console.
  debug: false

  # The message shown when a player executes a command without the required permission. Supports PlaceholderAPI placeholders
  noPermissionMessage: 'You do not have permission to execute that command!'

  # Whether to allow the collection of anonymous data for improving the project through bstats (https://bstats.org)
  allowTelemetry: true
```


</details>

## Permissions
|Node|Description|Default|
|---|---|---|
|showmy.use|Use the base /showmy command|true|
|showmy.reload|Reload the plugin’s config|op|
|showmy.modules.*|Access to all module commands|op|
|showmy.modules.<module>|Access to a specific module like rank, kills, etc.|(your choice)|
|showmy.*|Grants every permission related to ShowMy|op|

## Support
**Need help?** DM me on Discord, my username is bestrandomkiller.

 This plugin is licensed under the GNU General Public License v3.0 . See [LICENSE](LICENSE) for full terms.
