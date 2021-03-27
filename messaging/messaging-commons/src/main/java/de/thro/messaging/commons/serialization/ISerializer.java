package de.thro.messaging.commons.serialization;

/**
 * Interface for the class SerializerJson
 * @param <T>
 */
public interface ISerializer <T> {

    public String serialize(T data);

    public T deserialize(String data);

    public String getFormatExtension();
}
