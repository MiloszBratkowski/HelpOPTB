ALTER TABLE helpop ADD COLUMN player_prefix TEXT NOT NULL DEFAULT "";
ALTER TABLE helpop ADD COLUMN player_suffix TEXT NOT NULL DEFAULT "";
ALTER TABLE helpop ADD COLUMN player_display_name TEXT NOT NULL DEFAULT "";
ALTER TABLE helpop ADD COLUMN solver_prefix TEXT NOT NULL DEFAULT "";
ALTER TABLE helpop ADD COLUMN solver_suffix TEXT NOT NULL DEFAULT "";
ALTER TABLE helpop ADD COLUMN solver_display_name TEXT NOT NULL DEFAULT "";