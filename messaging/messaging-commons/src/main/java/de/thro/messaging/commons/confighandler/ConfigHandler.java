package de.thro.messaging.commons.confighandler;
import de.thro.messaging.commons.serialization.ISerializer;

import java.io.*;

public class ConfigHandler<T> implements IConfigHandler<T>{

    //lokale Variable ISerializer, damit man die Daten persistieren kann in einem Format, die der Serializer bereitstellt.
    private final ISerializer<T> serializer;

    public ConfigHandler(ISerializer<T> serializer) {
        //überschreibe lokale Variable serializer
        this.serializer = serializer;
    }

    @Override
    public T readConfig(String path) throws ConfigHandlerException {
        try (Reader reader = new FileReader(path)){
            //read file into string
            String read = reader.toString();

            return serializer.deserialize(read);
        } catch (Exception ex) {
            throw new ConfigHandlerException("Error while reading config-file, error-message: " + ex.getMessage());
        }
    }

    @Override
    public void writeConfig(String path, T fileToSerialize) throws ConfigHandlerException {
        try (Writer writer = new FileWriter(path)){
            //write into File:
            writer.write(serializer.serialize(fileToSerialize));
        } catch (Exception ex) {
        throw new ConfigHandlerException("Error while creating config-file, error-message: " + ex.getMessage());
        }
    }

    @Override
    public boolean isFileAvailable(String path, T file) throws ConfigHandlerException {
        try (Reader reader = new FileReader(path)){
            String read = reader.toString();
            //abgleich ob strings gleich
            return read.equals(serializer.serialize(file));
        } catch (IOException ie) {
            //return false wenn es Probleme Gab die Datei zu finden
            return false;
        } catch (Exception ex) {
            throw new ConfigHandlerException("Error while reading config-file, error-message: " + ex.getMessage());
        }
    }

    //TODO: Hilfsklasse um Dateinamen zu erstellen
}
