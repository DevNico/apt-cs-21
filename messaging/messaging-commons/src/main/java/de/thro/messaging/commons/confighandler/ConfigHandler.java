package de.thro.messaging.commons.confighandler;
import de.thro.messaging.commons.serialization.ISerializer;

public class ConfigHandler<T> implements IConfigHandler<T>{
    //lokale Variable ISerializer, damit man die Daten persistieren kann in einem Format, die der Serializer bereitstellt.
    private final ISerializer<T> serializer;
    public ConfigHandler(ISerializer<T> serializer) {
        //überschreibe lokale Variable serializer
        this.serializer = serializer;
    }

    @Override
    public T readConfig(String path) throws ConfigHandlerException {
        return null;
    }

    @Override
    public void writeConfig(String path, T fileToSerialize) throws ConfigHandlerException {

    }

    @Override
    public boolean isFileAvailable(String path, T file) throws ConfigHandlerException {
        return false;
    }
}
