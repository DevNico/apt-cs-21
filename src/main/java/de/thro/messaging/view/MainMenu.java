package de.thro.messaging.view;

import de.thro.messaging.application.exceptions.ApplicationException;
import de.thro.messaging.application.service.IChatService;
import de.thro.messaging.domain.models.Message;
import de.thro.messaging.domain.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

public class MainMenu {

    static final Logger LOGGER = LogManager.getLogger(MainMenu.class);

    enum UseCase {DIRECT_MESSAGE, BROADCAST, READ_MESSAGE, END_APP}

    /*
    Hier wird das Hauptmenü geöffnet und alle weiteren UC verwaltet.
    Nach jedem UC kehrt das Programm zum Hauptmenü zurück.
     */


    private final IChatService chatService;
    private final User user;

    public MainMenu(IChatService chatService, User user) {
        this.user = user;
        this.chatService = chatService;
    }

    /**
     * Startet Menüführung für einen Studenten.
     */
    public void start() {
        while (Thread.currentThread().isAlive()) {
            try {
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
                        endApp();
                        break;
                    default:
                        System.out.println("Das ist kein Menü");
                }
            } catch (ApplicationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Ein reader der für alle Usecases verwendet werden kann
    BufferedReader readerForUc = new BufferedReader(new InputStreamReader(System.in));

    /**
     * App wird Beendet.
     */
    private void endApp() {
        System.out.println("App Beendet.");
        System.exit(0);
    }

    /**
     * Was soll passieren, wenn der UC directMessage gerufen wird.
     * Hier kann die Logik für Direktnachrichten hin.
     */
    private void directMessage() throws ApplicationException {
        var receiver = "";
        var messageText = "";
        System.out.println("Schreiben Sie Ihre Nachricht und bestätigen Sie mit 'Enter'");
        try {
            messageText = readerForUc.readLine();
            System.out.println("Bitte geben Sie einen Empfänger ein. Danach wird die Nachricht versendet.");
            receiver = readerForUc.readLine();
        } catch (IOException e) {
            //Sollte die Eingabe ungültig sein, kehrt das System zum Hauptmenü zurück
            System.out.println("Das war keine Korrekte eingabe.");
            return;
        }
        this.chatService.sendDirectMessage(receiver, messageText);
    }

    /**
     * Was soll passieren, wenn der UC broadcast gerufen wird.
     * Hier kann die Logik für Broadcasts rein.
     */
    private void broadcast() throws ApplicationException {
        var messageText = "";
        System.out.println("Schreiben Sie Ihre Rundnachricht und versenden Sie mit 'Enter'");
        try {
            messageText = readerForUc.readLine();
        } catch (IOException e) {
            System.out.println("Das war keine Korrekte eingabe.");
        }
        this.chatService.sendBroadCast(messageText);
    }


    /**
     * Was soll passieren, wenn der UC readMessage gerufen wird.
     * Hier kann die Logik für readMessage rein.
     */
    private void readMessage() throws ApplicationException {
        System.out.println("Das sind Ihre Nachrichten");
        List<Message> messages = chatService.getMessages();
        for (Message m : messages) {
            System.out.println(m.getMessageText());
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
            final var scanner = new Scanner(System.in);
            var inputString = "";
            head("Hauptmenü");

            mainMenuText();

            inputString = scanner.nextLine();


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

