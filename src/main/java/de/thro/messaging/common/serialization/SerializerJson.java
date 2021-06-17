package de.thro.messaging.common.serialization;

import com.google.gson.Gson;

/**
 * Die Klasse SerializerJson ist die eigentliche Implementierung,
 * um Objekte serialisieren bzw. Json-Zeichenketten deserialisieren zu können.
 * Hierbei wird sich dem Google-Framework Gson als Hilfsmittel zum Serialisieren/Deserialiseren von Objekten bedient.
 * Außerdem wird mit der Methode getFileName() der Name einer Klasse als Dateiname sowie die Dateiendung ".json" (hardgecoded) zurückgegeben
 */

public class SerializerJson<T> implements ISerializer<T> {

    private final Class<? extends T> cls;
    private final Gson gson;

    /**
     * Konstruktor der Klasse SerializerJson
     * Sichtbarkeit nur package-private, da sonst keine Instanzen mehr in der SerializerJsonFactory erzeugt werden können
     *
     * @param cls
     * @param gson
     */
    public SerializerJson(Class<? extends T> cls, Gson gson) {
        this.cls = cls;
        this.gson = gson;
    }

    /**
     * Die Methode serialize() wandelt eine Instanz in eine Json-Zeichenfolge um
     *
     * @param data - Instanz, die in eine Json-Zeichenkette konvertiert werden soll
     * @return Gibt die der Methode serialize() übergebene Instanz als Json-Zeichenfolge zurück
     */
    @Override
    public String serialize(T data) {
        return gson.toJson(data);
    }

    /**
     * Die Methode deserialize() wandelt eine Json-Zeichenfolge in eine entsprechende Java-Instanz um
     *
     * @param data - Json-Zeichenkette, die in eine Java-Instanz konvertiert werden soll
     * @return Gibt die übergebene Json-Zeichenfolge als Java-Instanz zurück
     */
    @Override
    public T deserialize(String data) {
        return gson.fromJson(data, cls);
    }

    /**
     * Die Methode getFileName() gibt den Klassennamen als Dateinamen und die Dateiendung hardcodiert als ".json" aus
     *
     * @return Dateiname + Dateiendung(.json)
     */
    public String getFileName() {
        return cls.getSimpleName() + ".json";
    }

}

