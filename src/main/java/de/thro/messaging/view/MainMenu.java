package de.thro.messaging.view;

import de.thro.messaging.viewcontroller.ViewController;
import de.thro.messaging.domain.models.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainMenu {

    static final Logger LOGGER = LogManager.getLogger(MainMenu.class);

    enum UseCase {DIRECT_MESSAGE, BROADCAST, READ_MESSAGE, END_APP}

    /*
    Hier wird das Hauptmenü geöffnet und alle weiteren UC verwaltet.
    Nach jedem UC kehrt das Programm zum Hauptmenü zurück.
     */


    private final ViewController viewController;

    public MainMenu(ViewController viewController) {
        this.viewController = viewController;
    }

    /**
     * Startet Menüführung für einen Studenten.
     */
    public void start() {
        var isInterrupted = false;
        while (!isInterrupted) {
            UseCase uc = mainMenu();
            switch (uc) {
                case DIRECT_MESSAGE:
                    directMessage();
                    break;
                case BROADCAST:
                    broadcast();
                    break;
                case READ_MESSAGE:
                    readMessage();
                    break;
                case END_APP:
                    isInterrupted = true;
                    endApp();
                    break;
                default:
                    LOGGER.error("Das ist kein Menü");
            }
        }
    }

    //Ein reader der für alle Usecases verwendet werden kann
    BufferedReader readerForUc = new BufferedReader(new InputStreamReader(System.in));

    /**
     * App wird Beendet.
     */
    private void endApp() {
        LOGGER.info("App Beendet.");
        viewController.endApp();
    }

    /**
     * Was soll passieren, wenn der UC directMessage gerufen wird.
     * Hier kann die Logik für Direktnachrichten hin.
     */
    private void directMessage() {
        var receiver = "";
        var messageText = "";
        LOGGER.info("Schreiben Sie Ihre Nachricht und bestätigen Sie mit 'Enter'");
        try {
            messageText = readerForUc.readLine();
            LOGGER.info("Bitte geben Sie einen Empfänger ein. Danach wird die Nachricht versendet.");
            receiver = readerForUc.readLine();
        } catch (IOException e) {
            //Sollte die Eingabe ungültig sein, kehrt das System zum Hauptmenü zurück
            LOGGER.error("Das war keine Korrekte eingabe.");
            return;
        }
        Message message = viewController.createDirektMessage(receiver, messageText);
        viewController.sendDirect(message);
    }

    /**
     * Was soll passieren, wenn der UC broadcast gerufen wird.
     * Hier kann die Logik für Broadcasts rein.
     */
    private void broadcast() {
        var messageText = "";
        LOGGER.info("Schreiben Sie Ihre Rundnachricht und versenden Sie mit 'Enter'");
        try {
            messageText = readerForUc.readLine();
        } catch (IOException e) {
            LOGGER.error("Das war keine Korrekte eingabe.");
        }
        Message message = viewController.createBroadcastMessage(messageText);
        viewController.sendBroadcast(message);
    }


    /**
     * Was soll passieren, wenn der UC readMessage gerufen wird.
     * Hier kann die Logik für readMessage rein.
     */
    private void readMessage() {
        LOGGER.info("Das sind Ihre Nachrichten");
        List<Message> messages = viewController.displayReceivedMessages();
        for (Message m : messages) {
            if (!m.getIsBroadcast()) {
                LOGGER.info(m);
            }
        }
    }

    /**
     * Das tatsächliche Hauptmenü. Hier wird der Menütext ausgegeben und auf eine Usereingabe gewartet.
     *
     * @return Usecase der angibt was der User tun möchte.
     */
    private UseCase mainMenu() {
        while (true) {
            // create a BufferedReader using System.in
            var bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            var inputString = "";
            head("Hauptmenü");

            mainMenuText();
            try {
                inputString = bufferedReader.readLine();
            } catch (IOException ignored) {}

            switch (inputString.toUpperCase()) {
                case "D":
                    return UseCase.DIRECT_MESSAGE;
                case "B":
                    return UseCase.BROADCAST;
                case "R":
                    return UseCase.READ_MESSAGE;
                case "X":
                    return UseCase.END_APP;
                default:
                    LOGGER.error("Das ist kein Menü");
                    break;
            }

        }
    }


    /**
     * Allgemeiner Anfang für jede neue Seite.
     *
     * @param description Was soll im Head stehen?
     */
    private void head(String description) {
        System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        System.out.println(description);
        System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
    }

    private void mainMenuText() {
        System.out.println("D um eine Direktnachricht zu schreiben");
        System.out.println("B um eine Rundnachricht zu schreiben");
        System.out.println("R um die Nachrichten anzuzeigen");
        System.out.println("X um Programm zu beenden");
    }
}

