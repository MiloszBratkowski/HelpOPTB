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
- **/helpop notify** - on/off notification of reports.
- **/helpop check** - marking reports as solved.
- **/helpop history** - displaying history of reports.
- **/helpop clear_all** - deleting all reports from database.
- **/helpop clear_solved** - deleting solved reports from database.
- **/helpop help** - displaying all commands.
- **/helpop reload** - reloading configuration file.
- **/helpop back** - back to previous server (BungeeCord).
- **/helpop info** - displaying plugin configuration info.
- **/helpop update** - checking for updates.

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
###### Administration | BungeeCord/Velocity functionality
- **helpoptb.move** - Moving to bungee server after click on message and using */helpop back*.
###### Administration | commands
- **helpoptb.command.response** - using */response*.
- **helpoptb.command.notify** - using */helpop notify*.
- **helpoptb.command.check** - using */helpop check*.
- **helpoptb.command.history** - using */helpop history*.
- **helpoptb.command.clear.all** - using */helpop clear_all*.
- **helpoptb.command.clear.solved** - using */helpop clear_solved*.
- **helpoptb.command.help** - using */helpop help*.
- **helpoptb.command.reload** - using */helpop reload*.
- **helpoptb.command.info** - using */helpop info*.
- **helpoptb.command.update** - using */helpop update and update check on join*.

**Warning!** If player doesn't have permission to use admin commands (check, history, clear_* and reload), after execute one of these arguments it will send a report with this content.

## LuckPerms:
Helpop message can contain placeholders for prefix and suffix which will be replaced after send:
- placeholders for report messages: **<lp_player_prefix>**, **<lp_player_suffix>**
- placeholders for response messages: **<lp_admin_prefix>**, **<lp_admin_suffix>**, **<lp_player_prefix>**, **<lp_player_suffix>**
- placeholders for response button, hover label and discord message: **<lp_player_prefix>**, **<lp_player_suffix>**
- placeholders for history element: **<lp_player_prefix>**, **<lp_player_suffix>**, **<lp_solver_prefix>**, **<lp_solver_suffix>**
- placeholders for hover on solved report: **<lp_solver_prefix>**, **<lp_solver_suffix>**

All available placeholders are listed next to the massage in messages.yml.

If you use BungeeCord/Velocity you can **see on your server what player has prefix and suffix on the server from which he sends** the message. In the same way, the player sees your prefix that you have on the server where you executed the /response command.

## BungeeCord
First of all download library: ***[BungeeChannelTB](https://www.spigotmc.org/resources/bungeechanneltb.108382/)***.
To enable BungeeCord messaging set option ***enable_bungee: true*** in config.yml.
Remember to receiving reports on other servers, admin must have permission on the server where he is.

## Velocity
If server is based on Velocity Proxy you can use all features from BungeeCord support, but on Velocity server must be downloaded ***[VelocityChannelTB](https://www.spigotmc.org/resources/velocity-channel-tb.112745/)*** (not BungeeChannelTB).
All configuration is the same as BungeeCord (enable_bungee: true, etc.).
Don't worry about BungeeCord signatures in the plugin configuration file, these options also correspond to the Velocity system.


## Discord
The plugin allows you to forward reports to the discord server using a webhook.
To enable this feature, you need to set ***discord.enable: true*** in config.yml.
Then enter the ***webhook url***. In messages.yml, you can personalize the style of messages sent on the discord server.

## PlaceholderAPI
You can get information about notify status for player.
For do that add PlacehoderAPI to plugin folder and use ***%helpoptb_notify_status%*** placeholder.
This placeholder display label from messages.yml -> placeholderapi section, you can type your own text which closely related with player notify status.

## Configuration file
###### For latest plugin version!
````
#CONFIG GENERATED FOR VERSION:
#1.4.3

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
  normal: 3
  #vip: 1 #Permission: helpoptb.cooldown.vip
  #sponsor: 0 #Permission: helpoptb.cooldown.sponsor


### DATABASE SECTION ###
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
  filename: helpoptb.db

  #If you use MySQL, you have to set database parameters.
  host: localhost
  port: 3306
  database: helpoptb
  username: root
  password: ''
  ssl: false


### BUNGEECORD SECTION ###
#This option enabling use this plugin on all bungee servers.
#When player send report on server A and admin can see message on server B if he has permission "helpoptb.receive" on server B.
#WARNING! THAT FEATURE REQUIRES "BungeeChannelTB" PLUGIN!
enable_bungee: false

#Server name displaying on messages.
#If you have bungee mode enabled and want the name to be the same as the bungee server name, type server_name: BUNGEE.
server_name: this

#BungeeCord - player displayname, prefix or suffix (LuckPerms) taken from:
#true - sender server (ex. staffs sees player's prefix taken from player's server and vice versa)
#false - receiver server (ex. staffs sees player's prefix taken from the server they are on and vice versa)
#That option isn't related with history - all values in history are taking from sender server
receive_player_nickname_format: true
receive_admin_nickname_format: true


### DISCORD SECTION ###
#Discord webhook - this option enables sending reports on discord messages channel by webhook.
discord:
  enable: false
  webhook_url: ""
  sender_avatar: true
````
## Messages
###### For latest plugin version!
````
#MESSAGES GENERATED FOR VERSION:
#1.4.3

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

  #Style of text in responded message
  response: "<prefix> &c<admin>&7: &d<message>" #Available placeholders: <prefix>, <message>, <admin>, <admin_display_name>, <lp_admin_prefix>, <lp_admin_suffix>, <player>, <player_display_name>, <lp_player_prefix>, <lp_player_suffix>

admins:
  reports:
    #Format of messages on the admin's chat:
    report_format: | #Available placeholders: <prefix>, <message>, <server>, <player>, <player_display_name>, <lp_player_prefix>, <lp_player_suffix>, <move_button>, <response_button>
      ""
      "<prefix> &7(&3<server>&7) &6<player>&7: &f<message>"
      "<move_button> <response_button>"
      ""

    #Display information on the screen to admins (title/subtitle). Admin must have permission!:
    screen_title: "&cNew report from &6<player>&c!" #Available placeholders: <prefix>, <message>, <server>, <player>, <player_display_name>, <lp_player_prefix>, <lp_player_suffix>
    screen_subtitle: "&7(&3<server>&7) &f<message>" #Available placeholders: <prefix>, <message>, <server>, <player>, <player_display_name>, <lp_player_prefix>, <lp_player_suffix>

    #Buttons customization
    move_button: "&a[MOVE]" #Available placeholders: <server>
    response_button: "&b[RESPONSE]" #Available placeholders: <player>, <player_display_name>, <lp_player_prefix>, <lp_player_suffix>

    #Button's hover labels
    bungee_send: "&aClick to join that server!" #Available placeholders: <prefix>, <server>
    bungee_current: "&aYou're on this server!" #Available placeholders: <prefix>, <server>
    response_info: "&aClick to response <player>" #Available placeholders: <prefix>, <player>, <player_display_name>, <lp_player_prefix>, <lp_player_suffix>


  commands:
    only_player: "&cThat command can't be executed in console!"
    response:
      format: "&c<admin> &7-> &6<player>&7: &d<message>" #Available placeholders: <prefix>, <message>, <admin>, <admin_display_name>, <lp_admin_prefix>, <lp_admin_suffix>, <player>, <player_display_name>, <lp_player_prefix>, <lp_player_suffix>
      type_player: "&cType player to response!"
      type_message: "&cType response message!"
      offline_player: "&cResponse wasn't sent, because &6<player> &cis offline!" #Available placeholders: <prefix>, <player>
    notify:
      enabled: "&aReceiving reports has been enabled!"
      disabled: "&6Receiving reports has been disabled!"
    reload: "&aPlugin has reloaded!"
    history:
      title: "&7History of messages <page>/<all_pages>: (<amount>)" #Available placeholders: <prefix>, <page>, <all_pages>, <amount>
      element: "<solved> &8[<id>] &7(&3<server>&7) &6<player>&7: &f<message>" #Available placeholders: <id>, <solved>, <solver>, <date>, <message>, <server>, <player>, <player_display_name>, <lp_player_prefix>, <lp_player_suffix>, <solver>, <solver_display_name>, <lp_solver_prefix>, <lp_solver_suffix>
      page_rage: "&cPlease type page number from 1 to <all_pages>." #Available placeholders: <prefix>, <all_pages>
      click_solve: "&aClick to solve report!"
      hover_solve: "&7Solved by &a<solver>&7." #Available placeholders: <prefix>, <solver>, <solver_display_name>, <lp_solver_prefix>, <lp_solver_suffix>
    check:
      solved: "&7Report solved!"
      type_id: "&cPlease type id of report."
      incorrect_id: "&cIncorrect id of report."
      is_solved: "&cThis report has been solved."
    clear: "&7Reports deleted!"
    move:
      moved: "<prefix> &7Moved to &6<player> &7server! (<server>)" #Available placeholders: <prefix>, <server>, <player>, <player_display_name>, <lp_player_prefix>, <lp_player_suffix>
      admin_moved: "<prefix> &7Admin &c<admin> &7has moved to &6<player> &7server. (<server>)" #Available placeholders: <prefix>, <server>, <player>, <player_display_name>, <lp_player_prefix>, <lp_player_suffix>, <admin>, <admin_display_name>, <lp_admin_prefix>, <lp_admin_suffix>
    back:
      no_server: "&cCan't find back server!"
    help: |
      &7All commands:
      &e/helpop notify &7- on/off notification of reports,
      &e/helpop check <id> &7- marking reports as solved,
      &e/helpop history [page] &7- displaying history of reports,
      &e/helpop clear_all &7- deleting all reports from database,
      &e/helpop clear_solved &7- deleting solved reports from database,
      &e/helpop reload &7- reloading configuration file.
      &e/helpop back &7- back to previous server (BungeeCord).
      &e/helpop info &7- displaying plugin configuration info.
      &e/helpop update &7- checking for updates.

#Discord messages stylization
discord: #Available placeholders: <prefix>, <message>, <server>, <player>, <player_display_name>, <lp_player_prefix>, <lp_player_suffix>
  bot_name: "<player> <prefix>"
  content: ""
  author: "<lp_player_prefix> <player> <lp_player_suffix> (<server>)"
  title: "<message>"
  footer: "<date>"
  color: "red"

#PlaceholderAPI placeholder labels
placeholderapi:
  notify_status:
    enabled: "&aENABLED"
    disabled: "&cDISABLED"
````