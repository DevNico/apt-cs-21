package de.thro.messaging.commons.confighandler;

public class ConfigHandler implements IConfigHandler{
    //lokale Variable ISerializer, damit man die Daten persistieren kann in einem Format, die der Serializer bereitstellt.
    private final ISerializer<T> serializer;
    public ConfigHandler(ISerializer<T> serializer) {
        //überschreibe lokale Variable serializer
        this.serializer = serializer;
    }

    @Override
    public <T> T readConfig(String path) throws ConfigHandlerException {
        return null;
    }

    @Override
    public <T> void writeConfig(String path, T fileToSerialize) throws ConfigHandlerException {

    }

    @Override
    public <T> boolean isFileAvailable(String path, T file) throws ConfigHandlerException {
        return false;
    }
}
