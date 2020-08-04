package baliviya.com.github.newNDBO.command.impl;

import baliviya.com.github.newNDBO.command.Command;
import baliviya.com.github.newNDBO.entity.custom.Suggestion;
import baliviya.com.github.newNDBO.entity.enums.WaitingType;
import baliviya.com.github.newNDBO.entity.standart.User;
import baliviya.com.github.newNDBO.utils.Const;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;

public class id023_Complaint extends Command {

    private Suggestion  suggestion;
    private int         deleteMessageId;
    private User        user;

    @Override
    public boolean  execute()       throws TelegramApiException {
        switch (waitingType) {
            case START:
                deleteMessage(updateMessageId);
                user            = userDao.getUserByChatId(chatId);
                suggestion      = new Suggestion();
                suggestion      .setFullName(user.getFullName());
                suggestion      .setPostDate(new Date());
                suggestion      .setPhoneNumber(user.getPhone());
                deleteMessageId = getComplaint();
                waitingType     = WaitingType.SET_COMPLAINT;
                return COMEBACK;
            case SET_COMPLAINT:
                deleteMessage(updateMessageId);
                deleteMessage(deleteMessageId);
                if (hasMessageText()) {
                    suggestion      .setText(updateMessageText);
                    suggestionDao   .insertComplaint(suggestion);
                    sendMessage(Const.COMPLAINT_DONE_MESSAGE);
                    return EXIT;
                } else {
                    wrongData();
                    getComplaint();
                }
                return COMEBACK;
        }
        return EXIT;
    }

    private int     getComplaint()  throws TelegramApiException { return botUtils.sendMessage(Const.COMPLAINT_SEND_MESSAGE, chatId); }

    private int     wrongData()     throws TelegramApiException { return botUtils.sendMessage(Const.WRONG_DATA_TEXT, chatId); }
}
