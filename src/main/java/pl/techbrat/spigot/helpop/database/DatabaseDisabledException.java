package pl.techbrat.spigot.helpop.database;

public class DatabaseDisabledException extends Exception{
    public DatabaseDisabledException(String errorMessage) {
        super(errorMessage);
    }
}
