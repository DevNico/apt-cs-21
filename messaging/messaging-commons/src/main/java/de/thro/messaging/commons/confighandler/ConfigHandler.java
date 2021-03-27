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

    private String buildPathFile(String path, T file) {

        //Userverzeichnis vom System über Systemvariable ausgeben lassen
        String home = System.getProperty("user.home");

        //fileseperator vom System ausgeben lassen
        String fileSeperator = System.getProperty("file.separator");

        //Stringbuilder mit Home initialisieren
        StringBuilder pathfile = new StringBuilder(home);

        //gibt's 'nen filesperator am Ende vom Home String?
        if (pathfile.charAt(pathfile.length() + 1) != fileSeperator.charAt(0))
            pathfile.append(fileSeperator);

        //String file =

        //wenn path == null dann soll er einfach nen ordner in home erstellen und das dort ablegen.
        if(path==null) {
            pathfile.append("messaging").append(fileSeperator).append("config");
            return pathfile.toString();
        }

        //für path abchecken obs nen seperator am Anfang gibt
        String pathchecked;
        if (path.charAt(0) != '/' && path.charAt(0) != '\\')
            pathchecked = path;
        else pathchecked = path.substring(1);

        //wenn der path die falschen fileSeperator benutzt,
        // dann bitte für das System austauschen und gleich dem pathfile anhängen
        for (char c: pathchecked.toCharArray()) {
            if(fileSeperator.charAt(0) == '/') {
                if(c == '\\') pathfile.append(fileSeperator);
            } else if(fileSeperator.charAt(0) == '\\') {
                if(c == '/') pathfile.append(fileSeperator);
            } else pathfile.append(c);
        }

        //gibt's 'nen filesperator am Ende?
        if (pathfile.charAt(pathfile.length() + 1) != fileSeperator.charAt(0))
            pathfile.append(fileSeperator);



        //pathfile ausgeben - endlich :D
        return pathfile.toString();
    }
}
