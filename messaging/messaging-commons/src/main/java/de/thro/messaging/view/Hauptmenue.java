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


        private  void directMessage() {
            System.out.println("Write your direct message");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private  void broadcast() {
            System.out.println("Write your broadcast message");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private  void readMessage() {
            System.out.println("These are your messages");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
