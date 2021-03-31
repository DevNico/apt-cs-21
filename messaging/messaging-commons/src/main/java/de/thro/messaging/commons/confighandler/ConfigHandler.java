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
        try (Reader reader = new FileReader(buildPathFile(path))){
            //read file into string
            String read = reader.toString();

            return serializer.deserialize(read);
        } catch (Exception ex) {
            throw new ConfigHandlerException("Error while reading config-file, error-message: " + ex.getMessage());
        }
    }

    @Override
    public T readConfig() throws ConfigHandlerException {
        return this.readConfig(null);
    }

    @Override
    public void writeConfig(String path, T fileToSerialize) throws ConfigHandlerException {
        File file = new File(buildPathFile(path));
        //generate Path:
        if(!file.getParentFile().mkdirs())
            throw new ConfigHandlerException("Error while creating parent folders. ");
        System.out.println("Path" + file.getParentFile().getPath() + " succesfully created. ");#
        //try to write the configFile
        try (Writer writer = new FileWriter(file)){
            //write into File:
            writer.write(serializer.serialize(fileToSerialize));
            System.out.println("File succesfully saved at " + buildPathFile(path));
        } catch (Exception ex) {
        throw new ConfigHandlerException("Error while creating config-file, error-message: " + ex.getMessage());
        }
    }

    @Override
    public void writeConfig(T fileToSerialize) throws ConfigHandlerException {
        this.writeConfig(null, fileToSerialize);
    }


    @Override
    public boolean isFileAvailable(String path) throws ConfigHandlerException {
        try (Reader reader = new FileReader(buildPathFile(path))){
            String read = reader.toString();
            return true;
        } catch (IOException ie) {
            //return false wenn es Probleme Gab die Datei zu finden
            return false;
        } catch (Exception ex) {
            throw new ConfigHandlerException("Error while reading config-file, error-message: " + ex.getMessage());
        }
    }

    @Override
    public boolean isFileAvailable() throws ConfigHandlerException {
        return this.isFileAvailable(null);
    }

    /**
     * Hilfsklasse um den Pfad in ein korrektes, OS-spezifisches Format zu bekommen. Keine checks ob korrekte Zeichen
     * verwendet werden! Kann null und leere String-Eingaben handeln.
     * @param path eingabe des Pfadstrings
     * @return gibt korrekten Pfad aus
     */
    private String buildPathFile(String path) {

        //Userverzeichnis vom System über Systemvariable ausgeben lassen
        String home = System.getProperty("user.home");

        //fileseperator vom System ausgeben lassen
        String fileSeperator = System.getProperty("file.separator");

        //Stringbuilder mit Home initialisieren
        StringBuilder pathfile = new StringBuilder(home);

        //gibt's 'nen filesperator am Ende vom Home String?
        if (pathfile.charAt(pathfile.length()-1) != fileSeperator.charAt(0))
            pathfile.append(fileSeperator);

        //die Datei bekommt ihre entsprechende Endung vom Serializer
        String filename = serializer.getFileName();

        //wenn path == null dann soll er einfach nen ordner in home erstellen und das dort ablegen.
        if(path==null || path.isEmpty()) {
            pathfile.append("messaging").append(fileSeperator).append("config")
                    .append(fileSeperator).append(filename);
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
            if (c == '\\' || c == '/')
                pathfile.append(fileSeperator);
            else
                pathfile.append(c);
        }

        //gibt's 'nen filesperator am Ende?
        if (pathfile.charAt(pathfile.length()-1) != fileSeperator.charAt(0))
            pathfile.append(fileSeperator);

        //Dateipfad mit der Datei verbinden
        pathfile.append(filename);

        //pathfile ausgeben - endlich :D
        return pathfile.toString();
    }
}
