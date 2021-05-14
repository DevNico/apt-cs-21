package de.thro.messaging.application;

import de.thro.messaging.application.view.MainMenu;
import de.thro.messaging.application.view.NewUserView;
import de.thro.messaging.application.viewController.ViewController;
import de.thro.messaging.commons.UserManager.IUserManager;
import de.thro.messaging.commons.UserManager.UserManager;
import de.thro.messaging.commons.UserManager.UserNotExistsException;
import de.thro.messaging.commons.confighandler.ConfigHandlerException;
import de.thro.messaging.commons.confighandler.ConfigMessaging;
import de.thro.messaging.commons.confighandler.ConfigUser;
import de.thro.messaging.commons.domain.IMessage;
import de.thro.messaging.commons.domain.IUser;
import de.thro.messaging.commons.domain.UserType;
import de.thro.messaging.commons.domain.impl.Message;
import de.thro.messaging.commons.network.IMessaging;
import de.thro.messaging.commons.network.MessagingRabbitMQ;
import de.thro.messaging.commons.network.NetworkException;
import de.thro.messaging.commons.serialization.ISerializer;
import de.thro.messaging.commons.serialization.ISerializerFactory;
import de.thro.messaging.commons.serialization.SerializerJsonFactory;

public class Startup
{
  static ISerializerFactory factory = new SerializerJsonFactory();
  static ISerializer<ConfigUser> userISerializer = factory.createSerializerJson(ConfigUser.class);
  static ISerializer<IMessage> messageISerializer = factory.createSerializerJson(Message.class);
  static IUserManager um = new UserManager(userISerializer);
  static NewUserView userView = new NewUserView(um);
  static ConfigMessaging configMessaging = new ConfigMessaging();
  static IMessaging rmqMessaging;
  static MainMenu mainMenu;
  static IUser user;


  public static void main(String[] args)  {

    System.out.println("Starting application ...");
    boolean userIsInConfig = false;

    //Prüfe ob User im Configfile vorhanden
    try {
      userIsInConfig = um.isMainUserInConfig();
    } catch (ConfigHandlerException e) {
      e.printStackTrace();
    }

    //Wenn noch kein User gespeichert, starte Userverwaltung.
    if(!userIsInConfig){
      userView.newUser();
    }else //wenn es schon einen User gibt in Config
      {
        userView.loadUser();
      }

    // Erstellen einer neuen Messaginginstanz, geht erst mit User
    try {
      rmqMessaging = new MessagingRabbitMQ(configMessaging, um.getMainUser(), messageISerializer);
    } catch (NetworkException  | ConfigHandlerException | UserNotExistsException e) {
      e.printStackTrace();
    }

    ViewController vc = new ViewController(um, rmqMessaging);
    mainMenu = new MainMenu(vc);
    try {
      user = um.getMainUser();
    } catch (ConfigHandlerException e) {
      e.printStackTrace();
    } catch (UserNotExistsException e) {
      e.printStackTrace();
    }

    mainMenu.start();
  }
}