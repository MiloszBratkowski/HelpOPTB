package pl.techbrat.spigot.helpop.database;

import pl.techbrat.spigot.helpop.ConfigData;
import pl.techbrat.spigot.helpop.HelpOPTB;

import java.io.File;
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
        String type = config.getDatabaseParams("type").toLowerCase();
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

    private final String type;
    private String filename;
    private String host;
    private String database;
    private String username;
    private String password;
    private final String table;
    private int port;
    private boolean ssl;
    private Connection connection;
    private Statement statement;

    protected Database(String type, boolean closeLastCon) throws Exception {
        this.type = type;
        ConfigData config = ConfigData.getInstance();
        this.table = config.getDatabaseParams("table");
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
        if(closeLastCon && instance != null && instance.connection.isValid(0)) instance.disconnect();
        instance = this;
        connect();
        checkConnect();
        createTable();
    }

    private void connect() throws SQLException, ClassNotFoundException {
        plugin.getLogger().log(Level.INFO, "Connecting to database...");
        if(connection != null && !connection.isClosed()) return;
        if(type.equals("MYSQL")) {
            connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database+"?autoReconnect=true&useSSL="+ssl, username, password);
        } else {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:"+plugin.getDataFolder()+"/"+filename);
        }
        statement = connection.createStatement();
        plugin.getLogger().log(Level.INFO, "Database connected.");
    }

    public void disconnect() {
        try {
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            Files.copy(plugin.getResource("database/"+this.type+"_helpop_create.sql"), file.toPath());
            String query = Files.readString(file.toPath());
            file.delete();
            query = query.replaceAll("helpop", table);
            update(query);
            plugin.getLogger().log(Level.INFO, "If table wasn't exist, it has been created.");
            updateTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTable() {
        try {
            if (type.equals("SQLITE")) {
                ResultSet result = execute("PRAGMA table_info("+table+");");
                while (result.next()) {
                    if (result.getString("name").equals("player_prefix")) {
                        return;
                    }
                }
            }
            File file = new File(plugin.getDataFolder()+"/table_update.temp");
            Files.copy(plugin.getResource("database/"+this.type+"_helpop_update.sql"), file.toPath());
            String query = Files.readString(file.toPath());
            file.delete();
            query = query.replaceAll("helpop", table);
            update(query);
        } catch (Exception ignored) {
        }
    }

    public String getTable() {
        return table;
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
    public void update(String query) {
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.stopPlugin();
        }
    }
}
