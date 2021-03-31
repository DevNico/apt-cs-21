package de.thro.messaging.commons.confighandler;

/**
 * Dieses Interface stellt die Schnittstelle zum ConfigHandler bereit.
 * ConfigHandler ist dafür da, die Klasse in eine Datei zu speichern oder auszulesen.
 * @author Tristan Eckstaller
 */
public interface IConfigHandler<T> {
    /**
     * Liest Config-File aus. Man muss keinen Dateinamen bereitstellen, nur den Pfad.
     * @param path beschreibt den Dateienpfad als String, aus dem gelesen werden soll. Wenn null oder leerer String wird ein Pfad im Userverzeichnis abgecheckt
     * @return gibt Objekt vom Typ T zurück
     * @throws ConfigHandlerException Exception falls es Probleme beim lesen der Configuration gab
     */
        T readConfig(String path) throws ConfigHandlerException;

    /**
     * Liest Config-File aus. Generiert automatisch Pfad im Userverzeichnis. Man muss keinen Dateinamen bereitstellen.
     * @return gibt Objekt vom Typ T zurück
     * @throws ConfigHandlerException Exception falls es Probleme beim lesen der Configuration gab
     */
        T readConfig() throws ConfigHandlerException;

    /**
     * schreibt T in Config-File im Pfad path
     * @param path Dateienpfad als String, in den Gespeichert werden soll. Wenn null oder leerer String wird ein Pfad im Userverzeichnis angelegt
     * @param fileToSerialize Datei, die als Configdatei persistiert werden soll
     * @throws ConfigHandlerException Exception falls es Probleme beim schreiben der Configuration gab
     */
        void writeConfig(String path, T fileToSerialize) throws ConfigHandlerException;

    /**
     * schreibt T in Config-File in einem automatisch generierten Pfad.
     * @param fileToSerialize Datei, die als Configdatei persistiert werden soll
     * @throws ConfigHandlerException Exception falls es Probleme beim schreiben der Configuration gab
     */
        void writeConfig(T fileToSerialize) throws ConfigHandlerException;

    /**
     * Überprüft ob bereits eine Configdatei für T existiert
     * @param path gibt den Dateipfad an. Wenn null oder leerer String wird ein Pfad im Userverzeichnis abgecheckt
     * @param file Datei die überprüft werden soll
     * @return boolean-Wert ob es die Configdatei gibt
     * @throws ConfigHandlerException Exception falls es Probleme beim lesen der Configuration gab
     */
        boolean isFileAvailable(String path, T file) throws ConfigHandlerException;

    /**
     * Überprüft ob bereits eine Configdatei für T existiert. Man muss keinen Pfad angeben.
     * @param file Datei die überprüft werden soll
     * @return boolean-Wert ob es die Configdatei gibt
     * @throws ConfigHandlerException Exception falls es Probleme beim lesen der Configuration gab
     */
        boolean isFileAvailable(T file) throws ConfigHandlerException;
}
