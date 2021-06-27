package de.thro.messaging;

import de.thro.messaging.application.dependencies.messagequeue.IMessageQueue;
import de.thro.messaging.application.dependencies.messagequeue.MessageQueueConfiguration;
import de.thro.messaging.application.dependencies.messagequeue.exceptions.MessageQueueConnectionException;
import de.thro.messaging.application.service.ChatService;
import de.thro.messaging.application.service.IChatService;
import de.thro.messaging.application.service.UserManager;
import de.thro.messaging.common.DateTimeFactory;
import de.thro.messaging.common.confighandler.ConfigHandlerException;
import de.thro.messaging.common.confighandler.ConfigUser;
import de.thro.messaging.common.serialization.ISerializer;
import de.thro.messaging.common.serialization.ISerializerFactory;
import de.thro.messaging.common.serialization.SerializerJsonFactory;
import de.thro.messaging.domain.exceptions.UserNotExistsException;
import de.thro.messaging.domain.models.Message;
import de.thro.messaging.domain.models.User;
import de.thro.messaging.infrastructure.messagequeue.ActiveMQ;
import de.thro.messaging.view.MainMenu;
import de.thro.messaging.view.NewUserView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Startup {
    private static final Logger LOGGER = LogManager.getLogger(Startup.class);

    static ISerializerFactory serializerJsonFactory = new SerializerJsonFactory();
    static ISerializer<ConfigUser> userISerializer = serializerJsonFactory.createSerializerJson(ConfigUser.class);
    static ISerializer<Message> messageISerializer = serializerJsonFactory.createSerializerJson(Message.class);
    static UserManager um = new UserManager(userISerializer);
    static NewUserView userView = new NewUserView(um);
    static IMessageQueue messageQueue;
    static IChatService chatService;
    static MainMenu mainMenu;
    static User user;

    public static void main(String[] args) {
        LOGGER.info("Starting application ...");
        var userIsInConfig = false;

        //Prüfe ob User im Configfile vorhanden
        try {
            userIsInConfig = um.isMainUserInConfig();
        } catch (ConfigHandlerException e) {
            LOGGER.error("Couldn't find UserConfigFile.", e);
            System.exit(1);
        }

        //Wenn noch kein User gespeichert, starte Userverwaltung.
        if (!userIsInConfig) {
            userView.newUser();
        } else {
            //wenn es schon einen User gibt in Config
            userView.loadUser();
        }

        try {
            user = um.getMainUser();
        } catch (ConfigHandlerException | UserNotExistsException e) {
            LOGGER.error("Failed to load user.", e);
            System.exit(1);
        }

        // Erstellen einer neuen Messaginginstanz, geht erst mit User
        try {
            final var config = new MessageQueueConfiguration("localhost", "61616", "admin", "admin");
            messageQueue = new ActiveMQ(config, user);
        } catch (MessageQueueConnectionException e) {
            LOGGER.error("An error occurred while creating the messaging instance.",e);
            System.exit(1);
        }

        chatService = new ChatService(messageQueue, user, new DateTimeFactory());
        mainMenu = new MainMenu(chatService, user);
        mainMenu.start();
    }
}