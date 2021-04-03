package de.thro.messaging.view;

import de.thro.messaging.commons.domain.UserType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Hauptmenue {
    public static void main(String[] args) {
        MenueManagement mm = new MenueManagement();
        //mm.start();
        mm.newUser();
    }

    enum UseCase{DirectMessage, Broadcast, ReadMeassage}

    /*
    Hier wird das Hauptmenü geöffnet und alle weiteren UC verwaltet.
    Nach jedem UC kehrt das Programm zum Hauptmenü zurück.
     */
    public static class MenuManagement{
        public void start(){
            boolean end = false;
            while (!end){
                UseCase uc = mainMenue();
                switch (uc){
                    case DirectMessage:
                        directMessage();
                        break;
                    case Broadcast:
                        broadcast();
                        break;
                    case ReadMeassage:
                        readMessage();
                        break;
                    default:
                        System.out.println("Das ist kein Menü");
                }
            }
        }


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
                typ = getUserTyp();
                if (typ.toUpperCase().equals("S")) {
                    userType = UserType.STUDENT;
                    typeIsSet = true;
                } else if (typ.toUpperCase().equals("T")) {
                    userType = UserType.TEACHER;
                    typeIsSet = true;
                } else {
                    System.out.println("Das entspricht keiner Rolle. Versuchen Sie es erneut.");
                }
            }
            System.out.println(userName + " " + userType);
        }

        private String getUserTyp(){
            String typ = "";
            BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Geben Sie Ihre Rolle an. Professor = [T] Student = [S]");
            try {
                typ = obj.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return typ;
        }

        /**
         * Was soll passieren, wenn der UC directMessage gerufen wird.
         * Hier kann die Logik für Direktnachrichten hin.
         */
        private  void directMessage() {
            System.out.println("Schreiben Sie Ihre Nachricht");
            try {
                //TODO mach des gscheid
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * Was soll passieren, wenn der UC broadcast gerufen wird.
         * Hier kann die Logik für Broadcasts rein.
         */
        private  void broadcast() {
            System.out.println("Schreiben Sie Ihre Rundnachricht");
            try {
                //TODO mach des gscheid
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        /**
         * Was soll passieren, wenn der UC readMessage gerufen wird.
         * Hier kann die Logik für readMessage rein.
         */
        private  void readMessage() {
            System.out.println("Das sind Ihre Nachrichten");
            try {
                //TODO mach des gscheid
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * Das tatsächliche Hauptmenü. Hier wird der Menütext ausgegeben und auf eine Usereingabe gewartet.
         * @return Usecase der angibt was der User tun möchte.
         */
        private UseCase mainMenue(){
            while (true){
                // create a BufferedReader using System.in
                BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
                String input = "";
                head("Haupmenü");
                mainMenueText();
                try {
                     input = obj.readLine();
                }catch (IOException e){}

                switch (input.toUpperCase()){
                    case "D": return UseCase.DirectMessage;
                    case "B": return  UseCase.Broadcast;
                    case "R": return  UseCase.ReadMeassage;
                    default:
                        System.out.println("Das ist kein Menü");
                        break;
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

        private void mainMenueText(){
            System.out.println("D um eine Direktnachricht zu schreiben");
            System.out.println("B um eine Rundnachricht zu schreiben");
            System.out.println("R um die Nachrichten anzuzeigen");
        }
    }

}
