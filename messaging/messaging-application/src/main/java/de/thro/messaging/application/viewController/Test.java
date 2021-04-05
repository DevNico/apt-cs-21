package de.thro.messaging.application.viewController;

import com.google.gson.Gson;
import de.thro.messaging.commons.UserManager.IUserManager;
import de.thro.messaging.commons.UserManager.UserAlreadyExistsException;
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

import java.util.List;

public class Test {
    /*
    public static void main(String[] args) {

        ISerializer<ConfigUser> userISerializer = new SerializerJson<>(ConfigUser.class, new Gson());
        IUserManager um = new UserManager(userISerializer);

        try {
            um.createMainUser("Patrick", UserType.STUDENT);
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        } catch (ConfigHandlerException e) {
            e.printStackTrace();
        }

        ConfigMessaging cm = new ConfigMessaging();

        IUser user = null;
        try {
            user = um.getMainUser();
        } catch (ConfigHandlerException e) {
            e.printStackTrace();
        } catch (UserNotExistsException e) {
            e.printStackTrace();
        }


        ISerializer<IMessage> serializer = new SerializerJson<>(Message.class, new Gson());

        IMessaging m = null;
        try {
            m = new MessagingRabbitMQ(cm, user, serializer);
        } catch (NetworkException e) {
            e.printStackTrace();
        }

        ViewController vc = new ViewController(um, m);
        IUser someUser = vc.loadUser();
        IMessage messageToSb = vc.createDirektMessage(someUser.getName(), "Hauptsache da steht was");
        IMessage broadcastMessage = vc.createBroadcastMessage("Das ist ein Rundschreiben");
        vc.sendDirect(messageToSb);
        vc.sendBroadcast(broadcastMessage);
        List<String[]> myMessages = vc.displayReceivedMessages();
        System.out.println(myMessages.get(0)[2]);
        System.out.println(myMessages.get(1)[2]);

    }
*/
}