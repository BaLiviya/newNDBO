package baliviya.com.github.NDBO.command.impl;

import baliviya.com.github.NDBO.command.Command;
import baliviya.com.github.NDBO.entity.standart.Message;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class id031_StructureShowInfo extends Command {

    @Override
    public boolean execute() throws TelegramApiException {
        deleteMessage(updateMessageId);
        Message message = messageDao.getMessage(messageId);
        try {
            if (message.getFile() != null) {
                switch (message.getFileType()) {
                    case audio:
                        bot.execute(new SendAudio().setAudio(message.getFile()).setChatId(chatId));
                    case video:
                        bot.execute(new SendVideo().setVideo(message.getFile()).setChatId(chatId));
                    case document:
                        bot.execute(new SendDocument().setChatId(chatId).setDocument(message.getFile()));
                    case photo:
                        bot.execute(new SendPhoto().setChatId(chatId).setPhoto(message.getFile()));
                }
            }
        } catch (TelegramApiException e) {
            log.error("Exception by send file for message " + messageId, e);
        }
        sendMessage(messageId, chatId, null, message.getPhoto());
        return EXIT;
    }
}
