package de.thro.messaging;

import de.thro.messaging.view.MainMenu;
import de.thro.messaging.view.NewUserView;
import de.thro.messaging.viewcontroller.ViewController;
import de.thro.messaging.application.service.UserManager;
import de.thro.messaging.domain.exceptions.UserNotExistsException;
import de.thro.messaging.commons.confighandler.ConfigHandlerException;
import de.thro.messaging.commons.confighandler.ConfigMessaging;
import de.thro.messaging.commons.confighandler.ConfigUser;
import de.thro.messaging.domain.models.Message;
import de.thro.messaging.domain.models.User;
import de.thro.messaging.application.dependencies.messagequeue.IMessageQueue;
import de.thro.messaging.infrastructure.messagequeue.RabbitMessageQueue;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConnectionException;
import de.thro.messaging.commons.serialization.ISerializer;
import de.thro.messaging.commons.serialization.ISerializerFactory;
import de.thro.messaging.commons.serialization.SerializerJsonFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Startup {
    static final Logger LOGGER = LogManager.getLogger(Startup.class);

    static ISerializerFactory factory = new SerializerJsonFactory();
    static ISerializer<ConfigUser> userISerializer = factory.createSerializerJson(ConfigUser.class);
    static ISerializer<Message> messageISerializer = factory.createSerializerJson(Message.class);
    static UserManager um = new UserManager(userISerializer);
    static NewUserView userView = new NewUserView(um);
    static ConfigMessaging configMessaging = new ConfigMessaging();
    static IMessageQueue rmqMessaging;
    static MainMenu mainMenu;
    static User user;


    public static void main(String[] args) {
        LOGGER.info("Starting application ...");
        var userIsInConfig = false;

        //Prüfe ob User im Configfile vorhanden
        try {
            userIsInConfig = um.isMainUserInConfig();
        } catch (ConfigHandlerException e) {
            e.printStackTrace();
        }

        //Wenn noch kein User gespeichert, starte Userverwaltung.
        if (!userIsInConfig) {
            userView.newUser();
        } else //wenn es schon einen User gibt in Config
        {
            userView.loadUser();
        }

        // Erstellen einer neuen Messaginginstanz, geht erst mit User
        try {
            rmqMessaging = new RabbitMessageQueue(configMessaging, um.getMainUser(), messageISerializer);
        } catch (MessageQueueConnectionException | ConfigHandlerException | UserNotExistsException e) {
            e.printStackTrace();
        }

        ViewController vc = new ViewController(um, rmqMessaging);
        mainMenu = new MainMenu(vc);
        try {
            user = um.getMainUser();
        } catch (ConfigHandlerException | UserNotExistsException e) {
            e.printStackTrace();
        }

        mainMenu.start();
    }
}