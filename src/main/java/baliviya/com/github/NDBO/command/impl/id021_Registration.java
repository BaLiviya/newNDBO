package baliviya.com.github.NDBO.command.impl;

import baliviya.com.github.NDBO.command.Command;
import baliviya.com.github.NDBO.services.Registration;
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
