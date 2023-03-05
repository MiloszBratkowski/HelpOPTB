# HelpOPTB


## Description
Simple and modern Spigot plugin to help your players message online staff for help.
This plugin allows players to communicate with the administration via direct messages.
To send a message, the player must have the appropriate permissions (*helpoptb.command* and *helpoptb.report*).
Messages are sent using the */helpop message* command.

### JAVA 11+ IS REQUIRED (IF YOU WANT TO USE DATABASE)

## Commands
- **/helpop *message*** - sending message to administration.
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
- **helpoptb.report** - using */helpop message*.
###### Administration | receiving messages
- **helpoptb.receive** - displaying messages on chat.
- **helpoptb.receive.screen** - displaying messages on the screen (title with subtitle).
###### Administration | commands
- **helpoptb.command.check** - using */helpop check*.
- **helpoptb.command.history** - using */helpop history*.
- **helpoptb.command.clear.all** - using */helpop clear_all*.
- **helpoptb.command.clear.solved** - using */helpop clear_solved*.
- **helpoptb.command.help** - using */helpop help*.
- **helpoptb.command.reload** - using */helpop reload*.

**Warning!** If player doesn't have permission to use admin commands (check, history, clear_* and reload), after execute one of these arguments it will send a report with this content.

## BungeeCord messaging
First of all download library: [BungeeChannelTB](https://www.spigotmc.org/resources/bungeechanneltb.108382/).
To enable BungeeCord messaging set option *enable_bungee: true* in config.yml.

## Configuration file
###### For latest plugin version!
````
#Display information on the screen to admins (title/subtitle). Admin must have permission!
screen_information: true

#Sending reports with no any administrator on the server.
send_without_admin: true

#History of messages, this option add features:
# 1) if the administration solves the problem, can mark the report as solved
# 2) history of messages
# 3) receiving messages sent during the absence of the administration on the server, after entering
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
enable_bungee: true
````