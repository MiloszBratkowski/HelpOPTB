package pl.techbrat.spigot.helpop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.*;
import java.util.logging.Level;

public class Database {
    private static final HelpOPTB plugin = HelpOPTB.getInstance();

    private static Database instance;
    public static Database getInstance() {
        return instance;
    }

    public static void load() {
        ConfigData config = ConfigData.getInstance();
        String type = config.getDatabaseParams("type");
        plugin.getLogger().log(Level.INFO, "Loading database... (type: "+type+")");
        try {
            new Database(type.toUpperCase(), true);
            plugin.getLogger().info("Database successfully loaded and connected!");
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().severe("Database hasn't been connected!");
            plugin.getLogger().severe("Probable error: Incorrect database parameters in config.yml");
            plugin.stopPlugin();
        }
    }

    private String type, filename, host, database, username, password;
    private int port;
    private boolean ssl;
    private Connection connection;
    private Statement statement;

    public Database(String type, boolean closeLastCon) throws SQLException {
        this.type = type;
        ConfigData config = ConfigData.getInstance();
        if(type.equalsIgnoreCase("MYSQL")) {
            this.host = config.getDatabaseParams("host");
            this.port = Integer.parseInt(config.getDatabaseParams("port"));
            this.database = config.getDatabaseParams("database");
            this.username = config.getDatabaseParams("username");
            this.password = config.getDatabaseParams("password");
            this.ssl = Boolean.parseBoolean(config.getDatabaseParams("host"));
        } else {
            this.filename = config.getDatabaseParams("filename");
        }
        if(closeLastCon && instance != null && instance.connection.isValid(0)) instance.connection.close();
        instance = this;
        connect();
        checkConnect();
        createTable();
    }

    private boolean connect() throws SQLException {
        plugin.getLogger().log(Level.INFO, "Connecting to database...");
        if(connection != null && !connection.isClosed()) return false;
        if(type.equals("MYSQL")) {
            connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database+"?useSSL="+ssl, username, password);
        } else {
            connection = DriverManager.getConnection("jdbc:sqlite:"+plugin.getDataFolder()+"/"+filename);
        }
        statement = connection.createStatement();
        plugin.getLogger().log(Level.INFO, "Database connected.");
        return true;
    }

    private void checkConnect() {
        plugin.getLogger().log(Level.INFO, "Database checking connection.");
        if (type.equals("MYSQL")) execute("SHOW TABLES LIKE 'test'");
        else execute("SELECT name FROM sqlite_master WHERE type='table' AND name='test';");
        plugin.getLogger().log(Level.INFO, "Database connection checked.");
    }

    private void createTable() {
        try {
            plugin.getLogger().log(Level.INFO, "Creating table "+type+" if not exist.");
            File file = new File(plugin.getDataFolder()+"/table.temp");
            Files.copy(plugin.getResource("database/"+this.type+"_table.structure"), file.toPath());
            String query = Files.readString(file.toPath());
            file.delete();
            query = query.replaceAll("%tableName%", ConfigData.getInstance().getDatabaseParams("table"));
            update(query);
            plugin.getLogger().log(Level.INFO, "If table wasn't exist, it has been created.");
        } catch (Exception e) {
            e.printStackTrace();
        };
    }

    public String getType() {
        return type;
    }

    public ResultSet execute(String query) {
        try {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.stopPlugin();
            return null;
        }
    }

    //Funkcja od wykonywania update w bazie danych
    public int update(String query) {
        try {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.stopPlugin();
            return -1;
        }
    }
}
