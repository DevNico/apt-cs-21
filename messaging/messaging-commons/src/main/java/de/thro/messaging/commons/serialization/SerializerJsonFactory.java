package de.thro.messaging.commons.serialization;

import com.google.gson.Gson;

/**
 * Die Klasse SerializerJsonFactory ist die Implementierung des Interfaces SerializerFactory
 * Mit dem Aufruf der Methoden in dieser Klasse werden SerializerJson-Instanzen erzeugt und zurückgegeben
 */

public class SerializerJsonFactory<T> implements ISerializerFactory<T> {

    /**
     * Die Methode createSerializerJson() erstellt eine Instanz der Klasse SerializerJson
     * @param cls - Instanzklasse, die beim Konvertieren verwendet werden soll
     * @return SerializerJson-Objekt
     */
    @Override
    public ISerializer<T> createSerializerJson(Class<? extends T> cls) {
        return new SerializerJson<T>(cls, new Gson());
    }

    /**
     * Die Methode createSerializerJson() erstellt eine Instanz der Klasse SerializerJson
     * @param cls - Instanzklasse, die beim Konvertieren verwendet werden soll
     * @param gson - Instanz der Klasse Gson
     * @return SerializerJson-Objekt
     */
    @Override
    public ISerializer<T> createSerializerJson(Class<? extends T> cls, Gson gson) {
        return new SerializerJson<T>(cls, gson);
    }
}
