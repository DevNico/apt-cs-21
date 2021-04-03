package de.thro.messaging.commons.serialization;

import com.google.gson.Gson;

/**
 * Die Klasse SerializerJson ist die eigentliche Implementierung,
 * um Objekte serialisieren bzw. Json-Zeichenketten deserialisieren zu können.
 * Hierbei wird sich dem Google-Framework Gson als Hilfsmittel zum Serialisieren/Deserialiseren von Objekten bedient.
 * Außerdem wird mit der Methode getFileName() der Name einer Klasse als Dateiname sowie die Dateiendung ".json" (hardgecoded) zurückgegeben
 */

class SerializerJson<T> implements ISerializer<T>{

    private Class<? extends T> cls;
    private Gson gson;

    /**
     * Konstruktor der Klasse SerializerJson
     * Sichtbarkeit nur package-private, da sonst keine Instanzen mehr in der SerializerJsonFactory erzeugt werden können
     * @param cls
     * @param gson
     */
    SerializerJson(Class<? extends T> cls, Gson gson){
        this.cls = cls;
        this.gson = gson;
    }

    public static <T> SerializerJson<T> forClass(Class<? extends T> cls){
        return new SerializerJson<T>(cls, new Gson());
    }

    public static <T> SerializerJson<T> forClass(Class<? extends T> cls, Gson gson){
        return new SerializerJson<T>(cls, gson);
    }

    /**
     * Die Methode serialize() wandelt eine Instanz in eine Json-Zeichenfolge um
     * @param data
     * @return Gibt die der Methode serialize() übergebene Instanz als Json-Zeichenfolge zurück
     */
    @Override
    public String serialize(T data){
        String dataAsJson = gson.toJson(data);
        return dataAsJson;
    }

    /**
     * Die Methode deserialize() wandelt eine Json-Zeichenfolge in eine entsprechende Java-Instanz um
     * @param data
     * @return Gibt die übergebene Json-Zeichenfolge als Java-Instanz zurück
     */
    @Override
    public T deserialize(String data){
        T deserializedData = gson.fromJson(data, cls);
        return deserializedData;
    }

    /**
     * Die Methode getFileName() gibt den Klassennamen als Dateinamen und die Dateiendung hardcodiert als ".json" aus
     * @return Dateiname + Dateiendung(.json)
     */
    public String getFileName(){
        return cls.getSimpleName() + ".json";
    }

}

