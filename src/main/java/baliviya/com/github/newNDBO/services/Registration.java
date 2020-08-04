package baliviya.com.github.newNDBO.services;

import baliviya.com.github.newNDBO.dao.DaoFactory;
import baliviya.com.github.newNDBO.dao.impl.MessageDao;
import baliviya.com.github.newNDBO.entity.enums.WaitingType;
import baliviya.com.github.newNDBO.entity.standart.User;
import baliviya.com.github.newNDBO.utils.BotUtil;
import baliviya.com.github.newNDBO.utils.ButtonsLeaf;
import baliviya.com.github.newNDBO.utils.Const;
import baliviya.com.github.newNDBO.utils.UpdateUtil;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Registration {

    private User            user;
    private long            chatId;
    private BotUtil         botUtil;
    private List<String>    list;
    private ButtonsLeaf     buttonsLeaf;
    private WaitingType     waitingType = WaitingType.START;
    private DaoFactory      factory     = DaoFactory.getInstance();
    private MessageDao      messageDao  = factory.getMessageDao();
    private boolean         COMEBACK    = false;
    private boolean         EXIT        = true;

    public  boolean isRegistration(Update update, BotUtil botUtil)  throws TelegramApiException {
        if (botUtil == null || chatId == 0) {
            chatId       = UpdateUtil.getChatId(update);
            this.botUtil = botUtil;
        }
        switch (waitingType) {
            case START:
                user        = new User();
                user.setChatId(chatId);
                user.setEmail(Const.TABLE_NAME);
                getName();
                waitingType = WaitingType.SET_FULL_NAME;
                return COMEBACK;
            case SET_FULL_NAME:
                if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                    user.setFullName(update.getMessage().getText());
                    getPhone();
                    waitingType = WaitingType.SET_PHONE_NUMBER;
                } else {
                    wrongData();
                    getName();
                }
                return COMEBACK;
            case SET_PHONE_NUMBER:
                if (botUtil .hasContactOwner(update)) {
                    user    .setPhone(update.getMessage().getContact().getPhoneNumber());
                    user    .setUserName(UpdateUtil.getFrom(update));
                    getStatus();
                    waitingType  = WaitingType.SET_STATUS;
                } else {
                    wrongData();
                    getPhone();
                }
                return COMEBACK;
            case SET_STATUS:
                if (update.hasCallbackQuery()) {
                    if (list.get(Integer.parseInt(update.getCallbackQuery().getData())).equals(getText(Const.OTHERS_MESSAGE))) {
                        getOther();
                        waitingType = WaitingType.OTHER_STATUS;
                    } else {
                        user.setStatus(list.get(Integer.parseInt(update.getCallbackQuery().getData())));
                        return EXIT;
                    }
                } else {
                    wrongData();
                    getStatus();
                }
                return COMEBACK;
            case OTHER_STATUS:
                if (update.hasMessage() && update.getMessage().hasText()) {
                    user.setStatus(update.getMessage().getText());
                    return EXIT;
                } else {
                    wrongData();
                    getOther();
                }
                return COMEBACK;
        }
        return true;
    }

    private int     wrongData()                                     throws TelegramApiException { return botUtil.sendMessage(Const.WRONG_DATA_TEXT, chatId); }

    private int     getName()                                       throws TelegramApiException { return botUtil.sendMessage(Const.SET_FULL_NAME_MESSAGE, chatId); }

    private int     getPhone()                                      throws TelegramApiException { return botUtil.sendMessage(Const.SEND_CONTACT_MESSAGE, chatId); }

    private int     getIin()                                        throws TelegramApiException { return botUtil.sendMessage(Const.SET_IIN_MESSAGE, chatId); }

    private int     wrongIinNotNumber()                             throws TelegramApiException { return botUtil.sendMessage(Const.IIN_WRONG_MESSAGE, chatId); }

    private int     getStatus()                                     throws TelegramApiException {
        list        = new ArrayList<>();
        Arrays.asList(getText(Const.STATUS_TYPE_MESSAGE).split(Const.SPLIT)).forEach((e) -> list.add(e));
        list.add(getText(Const.OTHERS_MESSAGE));
        buttonsLeaf = new ButtonsLeaf(list);
        return botUtil.sendMessageWithKeyboard(getText(Const.STATUS_MESSAGE), buttonsLeaf.getListButton(), chatId);
    }

    private int     getOther()                                      throws TelegramApiException { return botUtil.sendMessage(Const.SET_YOUR_OPTION_MESSAGE, chatId); }

    private String  getText(int messageIdFromDb) { return messageDao.getMessageText(messageIdFromDb); }

    public  User    getUser() { return user; }
}
