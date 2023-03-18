# HelpOPTB


## Description
Simple and modern Spigot plugin to help your players message online staff for help.
This plugin allows players to communicate with the administration via direct messages.
To send a message, the player must have the appropriate permissions (*helpoptb.command* and *helpoptb.command.report*).
Messages are sent using the */helpop message* command.

### JAVA 11+ IS REQUIRED (IF YOU WANT TO USE DATABASE)

## Commands
- **/helpop *message*** - sending message to administration.
- **/response *player message*** - responding player.
- **/helpop check** - marking reports as solved.
- **/helpop history** - displaying history of reports.
- **/helpop clear_all** - deleting all reports from database.
- **/helpop clear_solved** - deleting solved reports from database.
- **/helpop help** - displaying all commands.
- **/helpop reload** - reloading configuration file.

**Warning!** To use history of reports and checking solved, you have to set option *enable_history: true* in configuration file and type database's information.

## Permissions
###### Players and administration
- **helpoptb.command** - **main permission** to use */helpop* command.
###### Players
- **helpoptb.command.report** - using */helpop message*.
- **helpoptb.cooldown.*group*** - relating cooldown from config with player.
###### Administration | receiving messages
- **helpoptb.receive** - displaying messages and responds on chat.
- **helpoptb.receive.screen** - displaying messages on the screen (title with subtitle).
###### Administration | BungeeCord functionality
- **helpoptb.move** - Moving to bungee server after click on message.
###### Administration | commands
- **helpoptb.command.response** - using */response*.
- **helpoptb.command.check** - using */helpop check*.
- **helpoptb.command.history** - using */helpop history*.
- **helpoptb.command.clear.all** - using */helpop clear_all*.
- **helpoptb.command.clear.solved** - using */helpop clear_solved*.
- **helpoptb.command.help** - using */helpop help*.
- **helpoptb.command.reload** - using */helpop reload*.

**Warning!** If player doesn't have permission to use admin commands (check, history, clear_* and reload), after execute one of these arguments it will send a report with this content.

## BungeeCord
First of all download library: [BungeeChannelTB](https://www.spigotmc.org/resources/bungeechanneltb.108382/).
To enable BungeeCord messaging set option *enable_bungee: true* in config.yml.
Remember to receiving reports on other servers, admin must have permission on the server where he is.

## Configuration file
###### For latest plugin version!
````
#Display information on the screen to admins (title/subtitle). Admin must have permission!
#Only for 1.9+ versions!
screen_information: true

#Sending reports with no any administrator on the server.
send_without_admin: true

#Time between sending messages, it can be made dependent on permission.
#The basic permission is "helpoptb.cooldown.normal", IF THE PLAYER DOESN'T HAVE IT, HIS COOLDOWN IS 0 SECONDS.
#You can make your own time definitions, to refer to them set players permission "helpoptb.cooldown.name",
#and replace the name with the group name defined below, e.g. vip, sponsor.
cooldown:
  normal: 3.0
  #vip: 1 #Permission: helpoptb.cooldown.vip
  #sponsor: 0 #Permission: helpoptb.cooldown.sponsor

#History of messages, this option add features:
# 1) if the administration solves the problem, can mark the report as solved
# 2) history of messages
# 3) receiving messages sent during the absence of the administration on the server
enable_history: false
database:
  #Avaiable databases: SQLite, MySQL (recommended).
  type: mysql
  table: helpop

  #If you use SQLite, you can set file's name.
  filename: database.db

  #If you use MySQL, you have to set database parameters.
  host: localhost
  port: 3306
  database: helpop
  username: root
  password: ''
  ssl: false

#This option enabling use this plugin on all bungee servers.
#When player send report on server A and admin can see message on server B if he has permission "helpoptb.receive" on server B.
#WARNING! THAT FEATURE REQUIRES "BungeeChannelTB" PLUGIN!
enable_bungee: false

#Server name displaying on messages.
#If you have bungee mode enabled and want the name to be the same as the bungee server name, type server_name: BUNGEE.
server_name: this
````
## Messages
###### For latest plugin version!
````
#This is configuration file of style of messages.

#Prefix placeholder
prefix: "&7[&cHelpOP&7]"

#No permissions message:
no_permission: "<prefix> &cYou don't have permissions to use this command!"

players:
  #Message to player after badly use:
  incorrect_use: "<prefix> &cYou have to type issue! &7/helpop message"

  #Information to player when nobody of administrations is on the server. (only if history of messages is disabled):
  no_admins: "<prefix> &cThere is no administration on the server currently :/."

  #Information to player when message was sent:
  feedback: "<prefix> &7Your message was sent to administration!"

  #Information when a player sends a message too often. Cooldowns can be set by permission in config.yml
  cooldown: "<prefix> &cWait before send next message!"

admins:
  reports:
    #Format of messages on the admin's chat:
    report_format: "<prefix> &7(&3<server>&7) &6<player>&7: &f<message>"

    #Display information on the screen to admins (title/subtitle). Admin must have permission!:
    screen_title: "&cNew report from &6<player>&c!"
    screen_subtitle: "&7(&3<server>&7) &f<message>"

    #Hover label on the chat message sent to another bungee server
    bungee_send: "&aClick to join that server!"

  commands:
    reload: "&aConfig has reloaded!"
    history:
      title: "&7History of messages <page>/<all_pages>: (<amount>)"
      element: "<solved> &8[<id>] &7(&3<server>&7) &6<player>&7: &f<message>" #Available placeholders: <id>, <solved>, <solve_admin>, <date>, <player>, <message>, <server>
      page_rage: "&cPlease type page number from 1 to <all_pages>."
      click_solve: "&aClick to solve report!"
      hover_solve: "&7Solved by &a<player>&7."
    check:
      solved: "&7Report solved!"
      type_id: "&cPlease type id of report."
      incorrect_id: "&cIncorrect id of report."
      is_solved: "&cThis report has been solved."
    clear: "&7Reports deleted!"
    help: |
      &7All commands:
      &e/helpop check <id> &7- marking reports as solved,
      &e/helpop history [page] &7- displaying history of reports,
      &e/helpop clear_all &7- deleting all reports from database,
      &e/helpop clear_solved &7- deleting solved reports from database,
      &e/helpop reload &7- reloading configuration file.
````