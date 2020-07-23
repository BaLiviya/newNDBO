package baliviya.com.github.NDBO.command.impl;

import baliviya.com.github.NDBO.command.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class id034_MapLocationSend extends Command {

    @Override
    public boolean execute() throws TelegramApiException {
        SendLocation sendLocation = new SendLocation();
        sendLocation.setLatitude(Float.parseFloat(propertiesDao.getPropertiesValue(5)));
        sendLocation.setLongitude(Float.parseFloat(propertiesDao.getPropertiesValue(6)));
        bot.execute(sendLocation.setChatId(chatId)).getMessageId();
        return EXIT;
    }
}
