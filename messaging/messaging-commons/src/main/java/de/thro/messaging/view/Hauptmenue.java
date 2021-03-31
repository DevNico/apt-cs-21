package de.thro.messaging.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Hauptmenue {
    public static void main(String[] args) {
        MenueManagement mm = new MenueManagement();
        mm.start();
    }

    enum UseCase{DirectMessage, Broadcast, ReadMeassage}

    /*
    Hier wird das Hauptmenü geöfnet und alle weiteren UC verwaltet.
    Nach jedem UC kehrt das Programm zum Hauptmenü zurück.
     */
    public static class MenueManagement{
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
                        System.out.println("Dont know");
                }
            }
        }


        /**
         * Was soll passieren, wenn der UC directMessage gerufen wird.
         * Hier kann die Logik für Direktnachrichten hin.
         */
        private  void directMessage() {
            System.out.println("Write your direct message");
            try {
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
            System.out.println("Write your broadcast message");
            try {
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
            System.out.println("These are your messages");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * Das Tatsächliche Hauptmenü. Hier wird der Menütext ausgegeben und auf eine Usereingabe gewartet.
         * @return Usecase der angibt was der User tun möchte.
         */
        private UseCase mainMenue(){
            while (true){
                // create a BufferedReader using System.in
                BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
                String input = "";
                head("Main Menue");
                mainMenueText();
                try {
                     input = obj.readLine();
                }catch (IOException e){}

                switch (input.toUpperCase()){
                    case "D": return UseCase.DirectMessage;
                    case "B": return  UseCase.Broadcast;
                    case "R": return  UseCase.ReadMeassage;
                    default:
                        System.out.println("Don't know");
                        break;
                }
            }
        }

        /**
         * Allgemeiner Anfang für jede neue Seite.
         * @param Description Was soll im head stehen.
         */
        private  void head(String Description){
            System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
            System.out.println(Description);
            System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        }

        private void mainMenueText(){
            System.out.println("D for direct message");
            System.out.println("B for broadcast");
            System.out.println("R for read message");
        }
    }

}
