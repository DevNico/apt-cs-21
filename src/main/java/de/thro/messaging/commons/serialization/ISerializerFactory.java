package de.thro.messaging.commons.serialization;

import com.google.gson.Gson;

/**
 * Das Interface ISerializerFactory stellt die Schnittstelle dar,
 * um außerhalb des Packages "serialization" Serializer-Objekte erhalten zu können
 *
 * @return Serializer-Objekt
 */

public interface ISerializerFactory {

    /**
     * Die Methode createSerializerJson() erstellt eine Instanz der Klasse SerializerJson
     *
     * @param cls - Instanzklasse, die beim Konvertieren verwendet werden soll
     * @return SerializerJson-Objekt
     */
    <T> ISerializer<T> createSerializerJson(Class<? extends T> cls);

    /**
     * Die Methode createSerializerJson() erstellt eine Instanz der Klasse SerializerJson
     *
     * @param cls  - Instanzklasse, die beim Konvertieren verwendet werden soll
     * @param gson - Instanz der Klasse Gson
     * @return SerializerJson-Objekt
     */
    <T> ISerializer<T> createSerializerJson(Class<? extends T> cls, Gson gson);
}
