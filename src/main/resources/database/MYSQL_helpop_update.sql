ALTER TABLE helpop
ADD COLUMN IF NOT EXISTS player_prefix VARCHAR(128) NOT NULL DEFAULT "",
ADD COLUMN IF NOT EXISTS player_suffix VARCHAR(128) NOT NULL DEFAULT "",
ADD COLUMN IF NOT EXISTS player_display_name VARCHAR(128) NOT NULL DEFAULT "",
ADD COLUMN IF NOT EXISTS solver_prefix VARCHAR(128) NOT NULL DEFAULT "",
ADD COLUMN IF NOT EXISTS solver_suffix VARCHAR(128) NOT NULL DEFAULT "",
ADD COLUMN IF NOT EXISTS solver_display_name VARCHAR(128) NOT NULL DEFAULT "";