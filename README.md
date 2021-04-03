# APT_Nachrichtensystem

Repository für APT Projekt:  <a href="https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/-/blob/master/Aufgabenstellung.pdf" target="_blank" rel="noopener noreferrer">Nachrichtensystem</a>

Systemvorraussetzungen bzw. Konfiguration: 

- RabbitMQ (üblicherweise über Docker) als Nachrichtenbroker (<a href="https://inf-git.fh-rosenheim.de/studlinnth6233/apt_nachrichtensystem/-/blob/master/Aufgabenstellung.pdf" target="_blank" rel="noopener noreferrer">Anleitung</a>)
    ``` bash
    # Installieren des Containers (einmalige Ausführung reicht aus)
    docker run --name rabbitmq -d -p 15672:15672 -p 5672:5672 rabbitmq:3-management
    # Starten des Containers (falls bereits vorhanden und nicht aktiv)
    docker start rabbitmq   
    # Zugriff auf RabbitMQ dann im Browser mit User="Guest" und Passwort="Guest" unter http://localhost:15672/
    ```
    
- Gradle
- JDK 11 (Gradle auch darauf einstellen bspw. in IntelliJ)

![gradleSettingJDK](/gradleSettingJDK.png)
