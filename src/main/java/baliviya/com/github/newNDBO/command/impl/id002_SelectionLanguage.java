package baliviya.com.github.newNDBO.command.impl;

import baliviya.com.github.newNDBO.command.Command;
import baliviya.com.github.newNDBO.entity.enums.Language;
import baliviya.com.github.newNDBO.services.LanguageService;
import baliviya.com.github.newNDBO.utils.Const;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class id002_SelectionLanguage extends Command {

    @Override
    public boolean  execute() throws TelegramApiException {
        deleteMessage(updateMessageId);
        chosenLanguage();
        toDeleteMessage(sendMessage(Const.WELCOME_TEXT_WHEN_START));
        return EXIT;
    }

    private void    chosenLanguage() {
        if (isButton(Const.RU_LANGUAGE)) LanguageService.setLanguage(chatId, Language.ru);
        if (isButton(Const.KZ_LANGUAGE)) LanguageService.setLanguage(chatId, Language.kz);
    }
}
