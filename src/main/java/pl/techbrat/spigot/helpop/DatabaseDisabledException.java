package pl.techbrat.spigot.helpop;

public class DatabaseDisabledException extends Exception{
    public DatabaseDisabledException(String errorMessage) {
        super(errorMessage);
    }
}
