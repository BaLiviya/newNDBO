package baliviya.com.github.newBOSTANDDBO.config;

import baliviya.com.github.newBOSTANDDBO.command.Command;
import baliviya.com.github.newBOSTANDDBO.dao.DaoFactory;
import baliviya.com.github.newBOSTANDDBO.dao.impl.MessageDao;
import baliviya.com.github.newBOSTANDDBO.entity.enums.Language;
import baliviya.com.github.newBOSTANDDBO.entity.standart.Message;
import baliviya.com.github.newBOSTANDDBO.exceptions.CommandNotFoundException;
import baliviya.com.github.newBOSTANDDBO.services.CommandService;
import baliviya.com.github.newBOSTANDDBO.services.LanguageService;
import baliviya.com.github.newBOSTANDDBO.utils.Const;
import baliviya.com.github.newBOSTANDDBO.utils.DateUtil;
import baliviya.com.github.newBOSTANDDBO.utils.SetDeleteMessages;
import baliviya.com.github.newBOSTANDDBO.utils.UpdateUtil;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;

@Slf4j
public class Conversation {

    private        Long           chatId;
    private        DaoFactory     factory        = DaoFactory.getInstance();
    private        MessageDao     messageDao;
    private static long           currentChatId;
    private        Command        command;
    private        CommandService commandService = new CommandService();

    public          void handleUpdate(Update update, DefaultAbsSender bot) throws TelegramApiException {
        printUpdate(update);
        chatId          = UpdateUtil.getChatId(update);
        currentChatId   = chatId;
        messageDao      = factory.getMessageDao();
        checkLanguage(chatId);
        try {
            command     = commandService.getCommand(update);
            if (command != null) {
                SetDeleteMessages.deleteKeyboard(chatId, bot);
                SetDeleteMessages.deleteMessage(chatId, bot);
            }
        } catch (CommandNotFoundException e) {
            if (chatId < 0) return;
            if (command == null) {
                SetDeleteMessages.deleteKeyboard(chatId, bot);
                SetDeleteMessages.deleteMessage(chatId, bot);
                Message message = messageDao.getMessage(Const.COMMAND_NOT_FOUND);
                bot.execute(new SendMessage().setChatId(chatId).setText(message.getName()));
            }
        }
        if (command != null) {
            if (command.isInitNormal(update, bot)) {
                clear();
                return;
            }
            boolean commandFinished = command.execute();
            if (commandFinished) clear();
        }
    }

    public static   long getCurrentChatId() { return currentChatId; }

    private         void checkLanguage(long chatId) {
        if (LanguageService.getLanguage(chatId) == null) LanguageService.setLanguage(chatId, Language.ru);
    }

    private         void printUpdate(Update update) {
        String dataMessage = "";
        if (update.hasMessage()) dataMessage = DateUtil.getDbMmYyyyHhMmSs(new Date((long) update.getMessage().getDate() * 1000));
        log.debug("New update get {} -> send response {}", dataMessage, DateUtil.getDbMmYyyyHhMmSs(new Date()));
        log.debug(UpdateUtil.toString(update));
    }

    private         void clear() {
        command.clear();
        command = null;
    }
}
