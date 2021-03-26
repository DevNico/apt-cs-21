package de.thro.messaging.commons.confighandler;

/**
 * Dieses Interface stellt die Schnittstelle zum ConfigHandler bereit.
 * @author Tristan Eckstaller
 */
public interface IConfigHandler<T> {
    /**
     * Liest Config-File aus.
     * @param path beschreibt den Dateienpfad als String, aus dem gelesen werden soll
     * @return gibt Objekt vom Typ T zurück
     * @throws ConfigHandlerException Exception falls es Probleme beim lesen der Configuration gab
     */
        T readConfig(String path) throws ConfigHandlerException;

    /**
     * schreibt T in Config-File im Pfad path
     * @param path Dateienpfad als String, in den Gespeichert werden soll
     * @param fileToSerialize Datei, die als Configdatei persistiert werden soll
     * @throws ConfigHandlerException Exception falls es Probleme beim schreiben der Configuration gab
     */
        void writeConfig(String path, T fileToSerialize) throws ConfigHandlerException;

    /**
     * Überprüft ob bereits eine Configdatei für T existiert
     * @param path gibt den Dateipfad an
     * @param file Datei die überprüft werden soll
     * @return boolean-Wert ob es die Configdatei gibt
     * @throws ConfigHandlerException Exception falls es Probleme beim lesen der Configuration gab
     */
        boolean isFileAvailable(String path, T file) throws ConfigHandlerException;
}
