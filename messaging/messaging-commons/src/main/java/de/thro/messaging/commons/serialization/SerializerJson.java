package de.thro.messaging.commons.serialization;

import com.google.gson.Gson;

/**
 * Serializer/Deserializer for JSON
 * @param <T>
 */

public class SerializerJson<T> implements ISerializer<T>{

    private Class<T> cls;
    private Gson gson;

    private SerializerJson(Class<T> cls, Gson gson){
        this.cls = cls;
        this.gson = gson;
    }

    public static <T> SerializerJson<T> forClass(Class<T> cls){
        return new SerializerJson<T>(cls, new Gson());
    }

    public static <T> SerializerJson<T> forClass(Class<T> cls, Gson gson){
        return new SerializerJson<T>(cls, gson);
    }

    @Override
    public String serialize(T data){
        String dataAsJson = gson.toJson(data);
        return dataAsJson;
    }

    @Override
    public T deserialize(String data){
        T deserializedData = gson.fromJson(data, cls);
        return deserializedData;
    }

    public String getFileName(){
        return cls.getSimpleName() + ".json";
    }

}

