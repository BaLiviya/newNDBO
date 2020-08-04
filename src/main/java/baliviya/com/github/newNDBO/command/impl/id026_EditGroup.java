package baliviya.com.github.newNDBO.command.impl;

import baliviya.com.github.newNDBO.command.Command;
import baliviya.com.github.newNDBO.entity.custom.Category;
import baliviya.com.github.newNDBO.entity.custom.CategoryGroup;
import baliviya.com.github.newNDBO.entity.custom.Group;
import baliviya.com.github.newNDBO.entity.enums.WaitingType;
import baliviya.com.github.newNDBO.utils.Const;
import baliviya.com.github.newNDBO.utils.ParserMessageEntity;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class id026_EditGroup extends Command {

    private String              commChangeRegGroup;
    private String              commSetting;
    private String              commWithoutTag;
    private String              commLink;
    private String              commSticker;
    private String              commPhoto;
    private String              commVideo;
    private String              commAudio;
    private String              commFile;
    private String              commHash;
    private String              commEditMessage;
    private String              commShowMessage;
    private String              changeTag;
    private String              commBack;
    private ParserMessageEntity parserMessageEntity = new ParserMessageEntity();
    private int                 deleteMessageId;
    private Group               group;

    @Override
    public boolean  execute()       throws TelegramApiException {
        if (!isAdmin() && !isMainAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        if (commChangeRegGroup == null) initCommand();
        switch (waitingType) {
            case START:
                deleteMessage(updateMessageId);
                group = groupDao.getGroupToId(Integer.parseInt(propertiesDao.getPropertiesValue(Const.GROUP_ID_FROM_PROPERTIES)));
                sendListGroup();
                return COMEBACK;
            case CHOOSE_GROUP:
                deleteMessage(updateMessageId);
                deleteMessage(deleteMessageId);
                if (hasMessageText()) {
                    if (isCommand(commChangeRegGroup)) {
                        group   .setRegistered(!group.isRegistered());
                        groupDao.update(group);
                        sendListGroup();
                    } else if (isCommand(commSetting)) {
                        sendInfoGroup();
                    }
                }
                return COMEBACK;
            case EDITION:
                deleteMessage(updateMessageId);
                deleteMessage(deleteMessageId);
                if (hasMessageText()) {
                    if (isCommand(commBack)) {
                        sendListGroup();
                    } else if (isCommand(commWithoutTag)) {
                        group.setCanWithoutTag(!group   .isCanWithoutTag());
                    } else if (isCommand(commLink)) {
                        group.setCanLink(!group         .isCanLink());
                    } else if (isCommand(commSticker)) {
                        group.setCanSticker(!group      .isCanSticker());
                    } else if (isCommand(commPhoto)) {
                        group.setCanPhoto(!group        .isCanPhoto());
                    } else if (isCommand(commVideo)) {
                        group.setCanVideo(!group        .isCanVideo());
                    } else if (isCommand(commAudio)) {
                        group.setCanAudio(!group        .isCanAudio());
                    } else if (isCommand(commFile)) {
                        group.setCanFile(!group         .isCanFile());
                    } else if (isCommand(commHash)) {
                        sendTags();
                        return COMEBACK;
                    } else if (isCommand(commEditMessage)) {
                        sendMessage(getText(Const.WELCOME_GROUP_TEXT_EDIT_MESSAGE) + next + "Назад - " + commBack);
                        waitingType = WaitingType.EDITION_MESSAGE;
                        return COMEBACK;
                    } else if (isCommand(commShowMessage)) {
                        sendMessage(group.getMessage() == null ? "Не задано" : group.getMessage());
                        sendInfoGroup();
                        return COMEBACK;
                    }
                    groupDao.update(group);
                    sendInfoGroup();
                }
                return COMEBACK;
            case EDIT_TAG:
                deleteMessage(updateMessageId);
                deleteMessage(deleteMessageId);
                if (hasMessageText()) {
                    if (isCommand(commBack)) {
                        sendInfoGroup();
                        return COMEBACK;
                    } else if (isCommand(changeTag)) {
                        List<CategoryGroup> categoryGroups = categoryGroupDao.get(getInt(), group.getChatId());
                        if (categoryGroups != null && categoryGroups.size() > 0) {
                            categoryGroupDao.deleteFromGroup(getInt(), group.getChatId());
                        } else {
                            categoryGroupDao.addGroup(getInt(), group.getChatId());
                        }
                    }
                    sendTags();
                }
                return COMEBACK;
            case EDITION_MESSAGE:
                deleteMessage(updateMessageId);
                deleteMessage(deleteMessageId);
                if (hasMessageText()) {
                    if (isCommand(commBack)) {
                        sendInfoGroup();
                        return COMEBACK;
                    }
                    if (updateMessageText.equalsIgnoreCase("удалить")) {
                        group.setMessage(null);
                    } else {
                        group.setMessage(parserMessageEntity.parseEntityToStringTag(updateMessage));
                    }
                    groupDao.update(group);
                    sendInfoGroup();
                }
                return COMEBACK;
        }
        return COMEBACK;
    }

    private void    initCommand() {
        commChangeRegGroup  = buttonDao.getButtonText(Const.TAG_GR_BUTTON);
        commSetting         = buttonDao.getButtonText(Const.TAG_ST_BUTTON);
        commWithoutTag      = buttonDao.getButtonText(Const.TAG_ED0_BUTTON);
        commLink            = buttonDao.getButtonText(Const.TAG_ED1_BUTTON);
        commSticker         = buttonDao.getButtonText(Const.TAG_ED2_BUTTON);
        commPhoto           = buttonDao.getButtonText(Const.TAG_ED3_BUTTON);
        commVideo           = buttonDao.getButtonText(Const.TAG_ED4_BUTTON);
        commAudio           = buttonDao.getButtonText(Const.TAG_ED5_BUTTON);
        commFile            = buttonDao.getButtonText(Const.TAG_ED6_BUTTON);
        commHash            = buttonDao.getButtonText(Const.TAG_TAG_BUTTON);
        commEditMessage     = buttonDao.getButtonText(Const.TAG_ED7_BUTTON);
        commShowMessage     = buttonDao.getButtonText(Const.TAG_MSG_BUTTON);
        changeTag           = buttonDao.getButtonText(Const.TAG_H_BUTTON);
        commBack            = buttonDao.getButtonText(Const.TAG_BACK_BUTTON);
    }

    private String  yesOrNot(boolean b) { return b ? Const.YES : Const.NO; }

    private String  getLinkGroup(Group group) { return "<a href = \"https://t.me/" + group.getUserName() + "/" + "\">" + group.getNames() + "</a>"; }

    private boolean isCommand(String command) { return updateMessageText.startsWith(command); }

    private void    sendListGroup() throws TelegramApiException {
        String formatMessage        = getText(Const.GROUP_CHANGE_INFO_MESSAGE);
        StringBuilder infoByGroups  = new StringBuilder();
        String format               = getText(Const.GROUP_EDIT_MESSAGE);
        infoByGroups.append(String.format(format, yesOrNot(group.isRegistered()), commChangeRegGroup, commSetting, getLinkGroup(group)));
        deleteMessageId             = sendMessage(String.format(formatMessage, infoByGroups.toString()));
        waitingType                 = WaitingType.CHOOSE_GROUP;
    }

    private void    sendInfoGroup() throws TelegramApiException {
        String format                       = getText(Const.GROUP_EDIT_INFO_MESSAGE);
        StringBuilder listCategory          = new StringBuilder();
        List<CategoryGroup> categoryGroups  = categoryGroupDao.getByGroupChatId(group.getChatId());
        if (categoryGroups != null && categoryGroups.size() > 0) {
            categoryGroups.forEach((category) -> listCategory.append(categoryDao.get(category.getId()).getName()).append(", "));
            listCategory  .deleteCharAt(listCategory.length() - 2);
            listCategory  .append(next);
        }
        String result = String.format(format,
                        yesOrNot(group.isRegistered()),
                        getLinkGroup(group),
                        yesOrNot(group.isCanWithoutTag())   + space + commWithoutTag,
                        yesOrNot(group.isCanLink())         + space + commLink,
                        yesOrNot(group.isCanSticker())      + space + commSticker,
                        yesOrNot(group.isCanPhoto())        + space + commPhoto,
                        yesOrNot(group.isCanVideo())        + space + commVideo,
                        yesOrNot(group.isCanAudio())        + space + commAudio,
                        yesOrNot(group.isCanFile())         + space + commFile,
                        commHash,
                        listCategory.toString(),
                        group.getMessage() == null ? "Не задано" : StringUtils.abbreviate(group.getMessage(), 100),
                        commEditMessage,
                        commShowMessage,
                        commBack
                );
        deleteMessageId = sendMessage(result);
        waitingType     = WaitingType.EDITION;
    }

    private void    sendTags()      throws TelegramApiException {
        List<Category>      include             = new ArrayList<>();
        List<Category>      exclude             = new ArrayList<>();
        List<Category>      categories          = categoryDao.getAll();
        List<CategoryGroup> categoryGroups      = categoryGroupDao.getByGroupChatId(group.getChatId());
        if (categoryGroups.size() > 0) {
            for (Category category : categories) {
                for (CategoryGroup group : categoryGroups) {
                    if (category.getId() == group.getId()) {
                        include.add(category);
                    } else {
                        exclude.add(category);
                    }
                }
            }
        } else {
            categories.forEach(category -> exclude.add(category));
        }
        StringBuilder info = new StringBuilder();
        info.append("Группа ").append(getBolt(group.getNames())).append(next).append(next);
        info.append("Теги включенные в группе: ").append(next);
        include.forEach(category -> info.append(changeTag).append(category.getId()).append(" - ").append(category.getName()).append(next));
        info.append(next).append("Доступные теги:").append(next);
        exclude.forEach(category -> info.append(changeTag).append(category.getId()).append(" - ").append(category.getName()).append(next));
        info.append(next).append("Назад: ").append(commBack);
        deleteMessageId = sendMessage(info.toString());
        waitingType     = WaitingType.EDIT_TAG;
    }

    private int     getInt() { return Integer.parseInt(updateMessageText.replaceAll("[^0-9]", "")); }
}
