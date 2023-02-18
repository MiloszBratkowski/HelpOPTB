# HelpOPTB

## Description
Simple spigot plugin
This plugin allows players to communicate with the administration via direct messages.
To send a message, the player must have the appropriate permissions (*helpoptb.command* and *helpoptb.report*).
Messages are sent using the */helpop message* command.

## Permissions
###### Players and administration
- **helpoptb.command** - **main permission** to use */helpop* command.
###### Players
- **helpoptb.report** - using */helpop message*.
###### Administration | receiving messages
- **helpoptb.receive** - displaying messages on chat.
- **helpoptb.receive.screen** - displaying messages on the screen (title with subtitle).
###### Administration | commands
- **helpoptb.check** - using */helpop check*.
- **helpoptb.history** - using */helpop history*.
- **helpoptb.reload** - using */helpop reload*.

## Commands
- **/helpop *message*** - sending message to administration.
- **/helpop check** - marking reports as resolved.
- **/helpop history** - displaying history of reports.
- **/helpop reload** - reloading configuration file.

**Waring!** To using history of reports and checking solved you have to set option *enable_history: true* in configuration file and type database's information.

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
no_permission_admin: "&cYou don't have permissions to use this command!"

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