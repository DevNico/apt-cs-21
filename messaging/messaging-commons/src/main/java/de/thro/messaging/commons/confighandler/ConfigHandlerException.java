package de.thro.messaging.commons.confighandler;

/**
 * Exception falls es probleme beim Auslesen oder Schreiben der Configuration gab
 */
public class ConfigHandlerException extends Exception{
    /**
     * Constructor für die ConfigHandlerException
     * @param message Nachricht zum Übergeben
     */
    public ConfigHandlerException (String message) {
        super(message);
    }

    /**
     * Constructor für die ConfigHandlerException
     */
    public ConfigHandlerException () {
        super();
    }
}
