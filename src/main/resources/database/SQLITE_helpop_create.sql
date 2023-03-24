CREATE TABLE IF NOT EXISTS "helpop" (
	"id"	INTEGER NOT NULL UNIQUE,
	"player_name"	TEXT NOT NULL,
	"player_uuid"	TEXT NOT NULL,
	"message"	TEXT NOT NULL,
	"solved"	TEXT NOT NULL DEFAULT -1,
	"date"	TEXT NOT NULL DEFAULT -1,
	"server"	TEXT NOT NULL,
	"player_prefix"	TEXT NOT NULL DEFAULT "",
	"player_suffix"	TEXT NOT NULL DEFAULT "",
	"player_display_name"	TEXT NOT NULL DEFAULT "",
	"solver_prefix"	TEXT NOT NULL DEFAULT "",
	"solver_suffix"	TEXT NOT NULL DEFAULT "",
	"solver_display_name"	TEXT NOT NULL DEFAULT "",
	PRIMARY KEY("id" AUTOINCREMENT)
);