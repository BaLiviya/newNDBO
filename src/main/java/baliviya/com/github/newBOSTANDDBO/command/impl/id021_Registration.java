package baliviya.com.github.newBOSTANDDBO.command.impl;

import baliviya.com.github.newBOSTANDDBO.command.Command;
import baliviya.com.github.newBOSTANDDBO.services.Registration;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class id021_Registration extends Command {

    private Registration registration = new Registration();

    @Override
    public boolean execute() throws TelegramApiException {
        deleteMessage(updateMessageId);
        if (!isRegistered()) {
            if (!registration.isRegistration(update, botUtils)) {
                return COMEBACK;
            } else {
                userDao.insert(registration.getUser());
                sendMessageWithAddition();
                return EXIT;
            }
        } else {
            sendMessageWithAddition();
            return EXIT;
        }
    }
}
