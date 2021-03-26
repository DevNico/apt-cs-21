package de.thro.messaging.commons.serialization;

/**
 * Interface für die Klasse SerializerJson
 * @param <T>
 */
public interface ISerializer <T> {

    public String serialize(T data);

    public T deserialize(String data);
}
