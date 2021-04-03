package de.thro.messaging.commons.serialization;

/**
 * Dieses Interface stellt die Schnittstelle dar,
 * um auf die Methoden der Klasse SerializerJson zugreifen zu können.
 */

public interface ISerializer <T> {

    /**
     * Die Methode serialize() wandelt übergebene Instanzen in Json-Zeichenfolgen um
     * @param data
     * @return Json-Zeichenfolge
     */
    public String serialize(T data);

    /**
     * Die Methode deserialize() wandelt eine Json-Zeichenfolge in eine Java-Instanz um
     * @param data
     * @return Java-Instanz
     */
    public T deserialize(String data);

    /**
     * Diese Methode getFileName() stellt den Namen der verwendeten Klasse als Dateinamen zur Verfügung
     * und gibt zusätzlich als Dateiendung .json hardcodiert zurück
     * @return Dateiname und Dateiendung
     */
    public String getFileName();
}
