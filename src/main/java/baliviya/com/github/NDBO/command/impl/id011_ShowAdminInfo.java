package baliviya.com.github.NDBO.command.impl;

import baliviya.com.github.NDBO.command.Command;
import baliviya.com.github.NDBO.entity.standart.Message;
import baliviya.com.github.NDBO.utils.Const;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class id011_ShowAdminInfo extends Command {

    @Override
    public boolean execute() throws TelegramApiException {
        if (!isAdmin() && !isMainAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        deleteMessage(updateMessageId);
        Message message = messageDao.getMessage(messageId);
        sendMessage(messageId, chatId, null, message.getPhoto());
        if (message.getFile() != null) {
            switch (message.getFileType()) {
                case audio:
                    bot.execute(new SendAudio().setAudio(message.getFile()).setChatId(chatId));
                case video:
                    bot.execute(new SendVideo().setVideo(message.getFile()).setChatId(chatId));
                case document:
                    bot.execute(new SendDocument().setDocument(message.getFile()).setChatId(chatId));
            }
        }
        return EXIT;
    }
}
