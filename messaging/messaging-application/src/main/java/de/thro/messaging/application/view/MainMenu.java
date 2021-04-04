package de.thro.messaging.application.view;

import de.thro.messaging.application.viewController.ViewController;
import de.thro.messaging.commons.domain.IMessage;
import de.thro.messaging.commons.domain.UserType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainMenu {
    public static void main(String[] args) {
        //MenuManagement mm = new MenuManagement();
        //mm.start();
        //mm.newUser();
    }

    enum UseCase{DirectMessage, Broadcast, ReadMessage, EndApp}

    /*
    Hier wird das Hauptmenü geöffnet und alle weiteren UC verwaltet.
    Nach jedem UC kehrt das Programm zum Hauptmenü zurück.
     */
    public static class MenuManagement{

        private ViewController vc;

        public MenuManagement(ViewController vc ){
            this.vc = vc;
        }

        /**
         * Startet Menüführung für einen Studenten.
         */
        public void startStudent(){
            boolean end = false;
            while (!end){
                UseCase uc = mainMenu(true);
                switch (uc){
                    case DirectMessage:
                        directMessage();
                        break;
                    case Broadcast:
                        broadcast();
                        break;
                    case ReadMessage:
                        readMessage();
                        break;
                    case EndApp:
                        endApp();
                        break;
                    default:
                        System.out.println("Das ist kein Menü");
                }
            }
        }

        /**
         * Startet Menüführung für einen Professor.
         */
        public void startTeacher(){
            boolean end = false;
            while (!end){
                UseCase uc = mainMenu(false);
                switch (uc){
                    case Broadcast:
                        broadcast();
                        break;
                    case EndApp:
                        endApp();
                        break;
                    default:
                        System.out.println("Das ist kein Menü");
                }
            }
        }

        /**
         * Wenn der User noch nie angemeldet war,
         * wird ein Menü zur eingabe eine User aufgerufen.
         */
        private  void newUser(){
            System.out.println("Geben Sie einen Benutzernamen an");
            String userName = "";
            String typ = "";
            UserType userType = null;

            BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));

            //Nutzername von Console einlesen
            try {
                userName = obj.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //UserTyp von Console einlesen
            boolean typeIsSet = false;
            while (!typeIsSet) {
                typ = getUserType();
                if (typ.toUpperCase().equals("S")) {
                    userType = UserType.STUDENT;
                    typeIsSet = true;
                } else if (typ.toUpperCase().equals("P")) {
                    userType = UserType.TEACHER;
                    typeIsSet = true;
                } else {
                    System.out.println("Das entspricht keiner Rolle. Versuchen Sie es erneut.");
                }
            }
            System.out.println(userName + " " + userType);
        }

        /**
         * Liest die Eingebe der Konsole ein und gibt den Buchstaben zurück der zur Nutzerrolle gehört.
         * @return String Buchstabe für den Usertyp
         */
        private String getUserType(){
            String typ = "";
            BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Geben Sie Ihre Rolle an. Professor = [P] Student = [S]");
            try {
                typ = obj.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return typ;
        }


        //Ein reader der für alle Usecases verwendet werden kann
        BufferedReader readerForUc = new BufferedReader(new InputStreamReader(System.in));
        /**
         * App wird Beendet.
         */
        private void endApp(){
            System.out.println("App Beendet.");
            vc.endApp();
        }

        /**
         * Was soll passieren, wenn der UC directMessage gerufen wird.
         * Hier kann die Logik für Direktnachrichten hin.
         */
        private  void directMessage() {
            String receiver = "";
            String messageText = "";
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
            IMessage message = vc.createDirektMessage(receiver, messageText);
            vc.sendDirect(message);
        }

        /**
         * Was soll passieren, wenn der UC broadcast gerufen wird.
         * Hier kann die Logik für Broadcasts rein.
         */
        private  void broadcast() {
            String messageText = "";
            System.out.println("Schreiben Sie Ihre Rundnachricht und versenden Sie mit 'Enter'");
            try {
                messageText = readerForUc.readLine();
            } catch (IOException e) {
                System.out.println("Das war keine Korrekte eingabe.");
            }
            IMessage message = vc.createBroadcastMessage(messageText);
            vc.sendBroadcast(message);
        }


        /**
         * Was soll passieren, wenn der UC readMessage gerufen wird.
         * Hier kann die Logik für readMessage rein.
         */
        private  void readMessage() {
            System.out.println("Das sind Ihre Nachrichten");
            List<IMessage> messages = vc.displayReceivedMessages();
            for (IMessage m: messages) {
                System.out.println(m);
            }
        }

        /**
         * Das tatsächliche Hauptmenü. Hier wird der Menütext ausgegeben und auf eine Usereingabe gewartet.
         * @return Usecase der angibt was der User tun möchte.
         */
        private UseCase mainMenu(boolean isStudent){
            while (true){
                // create a BufferedReader using System.in
                BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
                String input = "";
                head("Hauptmenü");

                //ist der Nutzer ein Student?
                if(isStudent) {
                    mainMenuTextStudent();
                    try {
                        input = obj.readLine();
                    } catch (IOException e) {
                    }

                    switch (input.toUpperCase()) {
                        case "D":
                            return UseCase.DirectMessage;
                        case "B":
                            return UseCase.Broadcast;
                        case "R":
                            return UseCase.ReadMessage;
                        case "X":
                            return UseCase.EndApp;
                        default:
                            System.out.println("Das ist kein Menü");
                            break;
                    }
                }else{
                    mainMenuTextTeacher();
                    try {
                        input = obj.readLine();
                    } catch (IOException e) {
                    }

                    switch (input.toUpperCase()) {
                        case "B":
                            return UseCase.Broadcast;
                        case "X":
                            return UseCase.EndApp;
                        default:
                            System.out.println("Das ist kein Menü");
                            break;
                    }
                }
            }
        }


        /**
         * Allgemeiner Anfang für jede neue Seite.
         * @param Description Was soll im Head stehen?
         */
        private  void head(String Description){
            System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
            System.out.println(Description);
            System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        }

        private void mainMenuTextStudent(){
            System.out.println("D um eine Direktnachricht zu schreiben");
            System.out.println("B um eine Rundnachricht zu schreiben");
            System.out.println("R um die Nachrichten anzuzeigen");
            System.out.println("X um Programm zu beenden");
        }

        private void mainMenuTextTeacher(){
            System.out.println("B um eine Rundnachricht zu schreiben");
            System.out.println("X um Programm zu beenden");
        }
    }

}
