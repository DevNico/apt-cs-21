package de.thro.messaging.commons.serialization;

import com.google.gson.Gson;

/**
 * Diese Klasse ist die Implementierung des Interfaces SerializerFactory
 * Mit dem Aufruf der Methoden in dieser Klasse werden SerializerJson-Instanzen erzeugt und zurückgegeben
 */

class SerializerJsonFactory<T> implements SerializerFactory<T>{

    /**
     * Diese Methode erstellt eine Instanz der Klasse SerializerJson
     * @param cls
     * @return SerializerJson-Objekt
     */
    @Override
    public ISerializer<T> createSerializerJson(Class<? extends T> cls) {
        return new SerializerJson<T>(cls, new Gson());
    }

    /**
     * Diese Methode erstellt eine Instanz der Klasse SerializerJson
     * @param cls
     * @param gson
     * @return SerializerJson-Objekt
     */
    @Override
    public ISerializer<T> createSerializerJson(Class<? extends T> cls, Gson gson) {
        return new SerializerJson<T>(cls, gson);
    }
}
