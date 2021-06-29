# APT_Nachrichtensystem

[![pipeline status](https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/badges/master/pipeline.svg)](https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/-/commits/master)
[![coverage report](https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/badges/master/coverage.svg)](https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/-/commits/master)

Repository für APT Projekt:  <a href="https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/-/blob/master/Aufgabenstellung.pdf" target="_blank" rel="noopener noreferrer">Nachrichtensystem</a>

Systemvorraussetzungen bzw. Konfiguration: 

- <a href="https://www.docker.com/get-started" target="_blank" rel="noopener noreferrer">Docker</a>
- ActiveMQ als Nachrichtenbroker
    ``` bash
    # Docker Befehle
    # Installieren und startden des Containers als daemon Prozess (einmalige Ausführung reicht aus)
    docker run --name activemq -d -p 8161:8161 -p 61616:61616 webcenter/activemq
    # Starten des Containers (falls bereits vorhanden und nicht aktiv)
    docker start activemq
    # Zugriff auf ActiveMQ dann im Browser mit User="admin" und Passwort="admin" unter http://localhost:8161/
    ```
    
- Gradle Version 6.1
- JDK 11 (Gradle auch darauf einstellen bspw. in IntelliJ)

![gradleSettingJDK](/gradleSettingJDK.png)

Für weitere Informationen siehe <a href="https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/-/wikis/home" target="_blank" rel="noopener noreferrer">Wiki</a>

## Pfad zur User-Config
Beim erstmaligem Starten der Anwendung wird geprüft ob der User bereits schon einmal am System angemeldet war. Falls nicht, wird eine ConfigUser.json Datei unter folgendem Pfad (Windows) angelegt: C:\Users\XYZ\messaging\config 

## Benutzung des Loggers
- In diesem Projekt wird der Logger von Log4j2 benutz.<br />
  Die genaue Anleitung von Log4j2 liegt in der Ordnerstruktur src/main/resources und wird dort konfiguriert.
  
- Verwendung des Logger Log4j2:
  ```java
  Logger [variablen namen] = LogManager.getLogger(this.class);
  ``` 
    - Beim import des Loggers darauf achten, dass man das package **org.apache.logging.log4j.Logger** 
    verwendet.

## Starten des Projekts
Zum Starten des Projekts muss lediglich die main Methode in der Klasse MainMenu (de/thro/messaging/view/MainMenu.java) aufgerufen- und der Docker-Container für ActiveMQ gestartet werden.
