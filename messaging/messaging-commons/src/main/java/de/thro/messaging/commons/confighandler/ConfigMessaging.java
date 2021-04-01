package de.thro.messaging.commons.confighandler;

/**
 * Rudimentäre Klasse zum Persistieren der Messaging-Anmeldeinfos
 */
public class ConfigMessaging{

    //gleich mal Standardwerte setzen ;)
    private static final String default_ip = "localhost";
    private static final String default_port = "5672";
    private static final String default_username = "guest";
    private static final String default_password = "guest";

    //lokale aktiv gesetzte Variablen
    private final String ip;
    private final String port;
    private final String username;
    private final String password;


    public ConfigMessaging(String ip, String port, String username, String password) {
        //wenn die Strings null sind dann Standardwerte setzen
        if(ip == null || port == null || username == null || password == null) {
            this.ip = default_ip;
            this.port = default_port;
            this.username = default_username;
            this.password = default_password;
        } else {
            this.ip = ip;
            this.port = port;
            this.username = username;
            this.password = password;
        }
    }

    public ConfigMessaging() {
        this(default_ip, default_port, default_username, default_password);
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
