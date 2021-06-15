# APT_Nachrichtensystem

[![pipeline status](https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/badges/master/pipeline.svg)](https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/-/commits/master)
[![coverage report](https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/badges/master/coverage.svg)](https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/-/commits/master)

Repository für APT Projekt:  <a href="https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/-/blob/master/Aufgabenstellung.pdf" target="_blank" rel="noopener noreferrer">Nachrichtensystem</a>

Systemvorraussetzungen bzw. Konfiguration: 

- <a href="https://www.docker.com/get-started" target="_blank" rel="noopener noreferrer">Docker</a>
- RabbitMQ als Nachrichtenbroker (<a href="https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/-/blob/master/Aufgabenstellung.pdf" target="_blank" rel="noopener noreferrer">Anleitung</a>)
    ``` bash
    # Docker Befehle
    # Installieren des Containers (einmalige Ausführung reicht aus)
    docker run --name rabbitmq -d -p 15672:15672 -p 5672:5672 rabbitmq:3-management
    # Starten des Containers (falls bereits vorhanden und nicht aktiv)
    docker start rabbitmq   
    # Zugriff auf RabbitMQ dann im Browser mit User="guest" und Passwort="guest" unter http://localhost:15672/
    ```
    
- Gradle Version 6.1
- JDK 11 (Gradle auch darauf einstellen bspw. in IntelliJ)

![gradleSettingJDK](/gradleSettingJDK.png)

Für weitere Informationen siehe <a href="https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/-/wikis/home" target="_blank" rel="noopener noreferrer">Wiki</a>

## Benutzung des Loggers
- In diesem Projekt wird der Logger von Log4j2 benutz.<br />
  Die genaue Anleitung von Log4j2 liegt in der Ordnerstruktur src/main/resources und wird dort konfiguriert.
  
- Verwendung des Logger Log4j2:
  ```java
  Logger [variablen namen] = LogManager.getLogger(this.class);
  ``` 
    - Beim import des Loggers darauf achten, dass man das package **org.apache.logging.log4j.Logger** 
    verwendet.
