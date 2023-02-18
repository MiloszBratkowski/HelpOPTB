# HelpOPTB


## Description
Simple spigot plugin
This plugin allows players to communicate with the administration via direct messages.
To send a message, the player must have the appropriate permissions (*helpoptb.command* and *helpoptb.report*).
Messages are sent using the */helpop message* command.

### JAVA 11+ IS REQUIRED (IF YOU WANT TO USE DATABASE)

## Commands
- **/helpop *message*** - sending message to administration.
- **/helpop check** - marking reports as resolved.
- **/helpop history** - displaying history of reports.
- **/helpop clear_all** - deleting all reports from database.
- **/helpop clear_solved** - deleting solved reports from database.
- **/helpop reload** - reloading configuration file.

**Warning!** To use history of reports and checking solved, you have to set option *enable_history: true* in configuration file and type database's information.

## Permissions
###### Players and administration
- **helpoptb.command** - **main permission** to use */helpop* command.
###### Players
- **helpoptb.report** - using */helpop message*.
###### Administration | receiving messages
- **helpoptb.receive** - displaying messages on chat.
- **helpoptb.receive.screen** - displaying messages on the screen (title with subtitle).
###### Administration | commands
- **helpoptb.command.check** - using */helpop check*.
- **helpoptb.command.history** - using */helpop history*.
- **helpoptb.command.clear.all** - using */helpop clear_all*.
- **helpoptb.command.clear.solved** - using */helpop clear_solved*.
- **helpoptb.command.reload** - using */helpop reload*.

**Warning!** If player doesn't have permission to use admin commands (check, history, clear_* and reload), after execute one of these arguments it will send a report with this content.

## Configuration file
````
admin_message_format: "&7[&cHelpOP&7] &6<player>&7: &f<message>"

incorrect_use: "&cYou have to type issue! &7/helpop message"

no_admins: "&cThere is no administration on the server currently :/."

feedback: "&7Your message was sent to administration!"

screen_information: true
screen_title: "&cNew report from &6<player>&c!"
screen_subtitle: "&f<message>"

no_permission_player: "&cYou don't have permissions to use this command!"

config_reloaded: "&aConfig has reloaded!"
history: "&7History of messages <site>/<all_sites>:"
check_report: "&7Report solved!"

enable_history: false
database:
  type: sqlite
  table: helpop

  filename: database.db

  host: localhost
  port: 3306
  database: helpop
  username: helpop
  password: ''
  ssl: false
````