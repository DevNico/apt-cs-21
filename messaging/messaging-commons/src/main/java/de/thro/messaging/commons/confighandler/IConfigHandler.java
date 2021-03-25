package de.thro.messaging.commons.confighandler;

/**
 * Dieses Interface stellt die Schnittstelle zum ConfigHandler bereit.
 * @author Tristan Eckstaller
 */
public interface IConfigHandler {
    /**
     * Liest Config-File aus.
     * @param path beschreibt den Dateienpfad als String, aus dem gelesen werden soll
     * @param <T> welches Objekt soll ausgelesen werden
     * @return gibt Objekt vom Typ T zurück
     */
        <T> T readConfig(String path);

    /**
     * schreibt T in Config-File im Pfad path
     * @param path Dateienpfad als String, in den Gespeichert werden soll
     * @param fileToSerialize Datei, die als Configdatei persistiert werden soll
     * @param <T> welches Objekt soll persistiert werden
     */
        <T> void writeConfig(String path, T fileToSerialize);

    /**
     * Überprüft ob bereits eine Configdatei für T existiert
     * @param path gibt den Dateipfad an
     * @param file Datei die überprüft werden soll
     * @param <T> welches Objekt soll darauf geprüft werden ob eine Configdatei davon existiert?
     * @return boolean-Wert ob es die Configdatei gibt
     */
        <T> boolean isFileAvailable(String path, T file);
}
