package de.thro.messaging.common.confighandler;

/**
 * Rudimentäre Klasse zum Persistieren der Messaging-Anmeldeinfos
 */
public class ConfigMessaging {

    //gleich mal Standardwerte setzen ;)
    private static final String DEFAULT_IP = "localhost";
    private static final String DEFAULT_PORT = "5672";
    private static final String DEFAULT_USERNAME = "guest";
    private static final String DEFAULT_PASSWORD = "guest";

    //lokale aktiv gesetzte Variablen
    private final String ip;
    private final String port;
    private final String username;
    private final String password;


    public ConfigMessaging(String ip, String port, String username, String password) {
        //wenn die Strings null sind dann Standardwerte setzen
        if (ip == null || port == null || username == null || password == null) {
            this.ip = DEFAULT_IP;
            this.port = DEFAULT_PORT;
            this.username = DEFAULT_USERNAME;
            this.password = DEFAULT_PASSWORD;
        } else {
            this.ip = ip;
            this.port = port;
            this.username = username;
            this.password = password;
        }
    }

    public ConfigMessaging() {
        this(DEFAULT_IP, DEFAULT_PORT, DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    //getter

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
