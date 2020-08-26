package baliviya.com.github.newBOSTANDDBO.services;

import baliviya.com.github.newBOSTANDDBO.dao.DaoFactory;
import baliviya.com.github.newBOSTANDDBO.entity.enums.Language;
import baliviya.com.github.newBOSTANDDBO.entity.standart.LanguageUser;

import java.util.HashMap;
import java.util.Map;

public class LanguageService {

    private static Map<Long, Language> languageMap = new HashMap<>();

    public static Language  getLanguage(long chatId) {
        Language language = languageMap.get(chatId);
        if (language == null) {
            LanguageUser languageUser = DaoFactory.getInstance().getLanguageUserDao().getByChatId(chatId);
            if (languageUser != null) {
                language = languageUser.getLanguage();
                languageMap.put(chatId, language);
            }
        }
        return language;
    }

    public static void      setLanguage(long chatId, Language language) {
        languageMap.put(chatId, language);
        DaoFactory.getInstance().getLanguageUserDao().insertOrUpdate(new LanguageUser(chatId, language));
    }

}
