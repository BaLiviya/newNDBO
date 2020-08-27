package baliviya.com.github.newBOSTANDDBO.command.impl;

import baliviya.com.github.newBOSTANDDBO.command.Command;
import baliviya.com.github.newBOSTANDDBO.entity.custom.Specialist;
import baliviya.com.github.newBOSTANDDBO.entity.standart.User;
import baliviya.com.github.newBOSTANDDBO.utils.Const;
import baliviya.com.github.newBOSTANDDBO.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;
import java.util.List;

@Slf4j
public class id035_SpecialistEdit extends Command {

    private StringBuilder   text;
    private List<Long>      allAdmins;
    private int             message;
    private static String   delete;
    private static String   deleteIcon;
    private static String   showIcon;

    @Override
    public boolean  execute()           throws TelegramApiException {
        if (!isAdmin() && !isMainAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        if (deleteIcon == null) {
            deleteIcon  = getText(1051);
            showIcon    = getText(1052);
            delete      = getText(1053);
        }
        if (message != 0) deleteMessage(message);
        if (hasContact()) {
            registerNewSpec();
            return COMEBACK;
        }
        if(updateMessageText.contains(delete)) {
            try {
                int numberAdminList = Integer.parseInt(updateMessageText.replaceAll("[^0-9]",""));
                specialistDao.delete(allAdmins.get(numberAdminList));
            } catch (NumberFormatException e) { e.printStackTrace(); }
        }
        sendEditorAdmin();
        return COMEBACK;
    }

    private boolean registerNewSpec()   throws TelegramApiException {
        long newSpecChatId     = update.getMessage().getContact().getUserID();
        if (!userDao.isRegistered(newSpecChatId)) {
            sendMessage("Пользователь не зарегистрирован в данном боте");
            return EXIT;
        } else {
            if (specialistDao.isSpecialist(newSpecChatId)) {
                sendMessage("Пользователь уже является специалистом");
                return EXIT;
            } else {
                User user       = userDao.getUserByChatId(newSpecChatId);
                specialistDao.insert(new Specialist().setChatId(newSpecChatId).setFullName(String.format("%s %s %s", user.getUserName(), user.getPhone(), DateUtil.getDbMmYyyyHhMmSs(new Date()))));
                User userAdmin  = userDao.getUserByChatId(chatId);
                log.info("{} added new spec - {} ", getInfoByUser(userAdmin), getInfoByUser(user));
                sendEditorAdmin();
            }
        }
        return COMEBACK;
    }

    private String  getInfoByUser(User user) { return String.format("%s %s %s", user.getFullName(), user.getPhone(), user.getChatId()); }

    private void    sendEditorAdmin()   throws TelegramApiException {
        deleteMessage(updateMessageId);
        try {
            getText(EXIT);
            message = sendMessage(String.format(getText(1159), text.toString()));
        } catch (TelegramApiException e) {
            getText(COMEBACK);
            message = sendMessage(String.format(getText(1159), text.toString()));
        }
        toDeleteMessage(message);
    }

    private void    getText(boolean withLink) {
        text        = new StringBuilder();
        allAdmins   = specialistDao.getAll();
////        allAdmins   = adminDao.getAll();
        int count   = 0;
        for (Long admin : allAdmins) {
            try {
                User user = userDao.getUserByChatId(admin);
//                if (allAdmins.size() == 1) {
//                    if (withLink) {
//                        text.append(getLinkForUser(user.getChatId(), user.getUserName())).append(space).append(next);
//                    } else {
//                        text.append(getInfoByUser(user)).append(space).append(next);
//                    }
//                    text.append("Должен быть минимум 1 администратор.").append(next);
//                } else {
                    if (withLink) {
                        text.append(delete).append(count++).append(deleteIcon).append(" - ").append(showIcon).append(getLinkForUser(user.getChatId(), user.getUserName())).append(space).append(next);
                    } else {
                        text.append(delete).append(count++).append(deleteIcon).append(" - ").append(getInfoByUser(user)).append(space).append(next);
                    }
//                }
//                count++;
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
