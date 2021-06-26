package de.thro.messaging.common.serialization;

/**
 * Dieses Interface stellt die Schnittstelle dar,
 * um auf die Methoden der Klasse SerializerJson zugreifen zu können.
 */

public interface ISerializer<T> {

    /**
     * Die Methode serialize() wandelt übergebene Instanzen in Json-Zeichenfolgen um
     *
     * @param data - Instanz, die in eine Json-Zeichenkette konvertiert werden soll
     * @return Json-Zeichenfolge
     */
    String serialize(T data);

    /**
     * Die Methode deserialize() wandelt eine Json-Zeichenfolge in eine Java-Instanz um
     *
     * @param data - Json-Zeichenkette, die in eine Java-Instanz konvertiert werden soll
     * @return Java-Instanz
     */
    T deserialize(String data);

    /**
     * Die Methode getFileName() stellt den Namen der verwendeten Klasse als Dateinamen zur Verfügung
     * und gibt zusätzlich als Dateiendung .json hardcodiert zurück
     *
     * @return Dateiname und Dateiendung
     */
    String getFileName();
}
