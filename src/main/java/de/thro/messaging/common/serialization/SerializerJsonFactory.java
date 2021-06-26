package de.thro.messaging.common.serialization;

import com.google.gson.Gson;

/**
 * Die Klasse SerializerJsonFactory ist die Implementierung des Interfaces SerializerFactory
 * Mit dem Aufruf der Methoden in dieser Klasse werden SerializerJson-Instanzen erzeugt und zurückgegeben
 */

public class SerializerJsonFactory implements ISerializerFactory {

    /**
     * Die Methode createSerializerJson() erstellt eine Instanz der Klasse SerializerJson
     *
     * @param cls - Instanzklasse, die beim Konvertieren verwendet werden soll
     * @return SerializerJson-Objekt
     */
    @Override
    public <T> ISerializer<T> createSerializerJson(Class<? extends T> cls) {
        return new SerializerJson<>(cls, new Gson());
    }

    /**
     * Die Methode createSerializerJson() erstellt eine Instanz der Klasse SerializerJson
     *
     * @param cls  - Instanzklasse, die beim Konvertieren verwendet werden soll
     * @param gson - Instanz der Klasse Gson
     * @return SerializerJson-Objekt
     */
    @Override
    public <T> ISerializer<T> createSerializerJson(Class<? extends T> cls, Gson gson) {
        return new SerializerJson<>(cls, gson);
    }
}
