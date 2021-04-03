package de.thro.messaging.commons.serialization;

import com.google.gson.Gson;

/**
 * Dieses Interface stellt die Schnittstelle dar,
 * um außerhalb des Packages "serialization" Serializer-Objekte erhalten zu können
 * @return Serializer-Objekt
 */

public interface SerializerFactory <T> {

    /**
     * Diese Methode erstellt eine Instanz der Klasse SerializerJson
     * @param cls
     * @return SerializerJson-Objekt
     */
    public ISerializer<T> createSerializerJson(Class<? extends T> cls);

    /**
     * Diese Methode erstellt eine Instanz der Klasse SerializerJson
     * @param cls
     * @param gson
     * @return SerializerJson-Objekt
     */
    public ISerializer<T> createSerializerJson(Class<? extends T> cls, Gson gson);
}
