package baliviya.com.github.NDBO.command.impl;

import baliviya.com.github.NDBO.command.Command;
import baliviya.com.github.NDBO.entity.custom.ReminderTask;
import baliviya.com.github.NDBO.entity.enums.WaitingType;
import baliviya.com.github.NDBO.utils.Const;
import baliviya.com.github.NDBO.utils.DateKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;
import java.util.List;

public class id029_Reminder extends Command {

    private List<ReminderTask>  reminderTaskList;
    private int                 deleteMessageId;
    private int                 reminderTaskId;
    private DateKeyboard        dateKeyboard;
    private Date                dateStart;
    private boolean             isUpdate = false;
    private ReminderTask        reminderTask;

    @Override
    public boolean  execute()               throws TelegramApiException {
        if (!isAdmin() && !isMainAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        switch (waitingType) {
            case START:
                deleteMessage(updateMessageId);
                sendListReminder();
                waitingType = WaitingType.CHOOSE_OPTION;
                return COMEBACK;
            case CHOOSE_OPTION:
                deleteMessage(deleteMessageId);
                deleteMessage(updateMessageId);
                if (hasMessageText()) {
                    if (isCommand("/new")) {
                        dateKeyboard = new DateKeyboard();
                        sendStartDate();
                        waitingType  = WaitingType.START_DATE;
                    } else if (isCommand("/del")) {
                        reminderTaskId   = reminderTaskList.get(Integer.parseInt(updateMessageText.replaceAll("[^0-9]",""))).getId();
                        factory.getReminderTaskDao().delete(reminderTaskId);
                        sendListReminder();
                        waitingType = WaitingType.CHOOSE_OPTION;
                    } else if (isCommand("/st")) {
                        reminderTaskId   = reminderTaskList.get(Integer.parseInt(updateMessageText.replaceAll("[^0-9]",""))).getId();
                        dateKeyboard = new DateKeyboard();
                        sendStartDate();
                        isUpdate = true;
                        waitingType = WaitingType.START_DATE;
                    }
                }
                return COMEBACK;
            case START_DATE:
                deleteMessage(updateMessageId);
                if (hasCallbackQuery()) {
                    if (dateKeyboard.isNext(updateMessageText)) {
                        sendStartDate();
                        return COMEBACK;
                    }
                    dateStart = dateKeyboard.getDateDate(updateMessageText);
                    dateStart.setHours(0);
                    dateStart.setMinutes(0);
                    dateStart.setSeconds(0);
                    if (isUpdate) {
                        reminderTask = factory.getReminderTaskDao().getById(reminderTaskId);
                    } else {
                        reminderTask = new ReminderTask();
                    }
                    reminderTask.setDateBegin(dateStart);
                    sendMessage(Const.SEND_MESSAGE_TEXT_MESSAGE);
                    waitingType = WaitingType.SET_TEXT;
                }
                return COMEBACK;
            case SET_TEXT:
                deleteMessage(updateMessageId);
                if (hasMessageText()) {
                    reminderTask.setText(updateMessageText);
                    if (isUpdate) {
                        factory.getReminderTaskDao().update(reminderTask);
                        sendMessageToGroup();
                    } else {
                        factory.getReminderTaskDao().insert(reminderTask);
                        sendMessageToGroup();
                    }
                    sendListReminder();
                    waitingType = WaitingType.CHOOSE_OPTION;
                }
                return COMEBACK;
        }
        return EXIT;
    }

    private void    sendListReminder()      throws TelegramApiException {
        String formatMessage            = getText(Const.REMINDER_EDIT_MESSAGE);
        StringBuilder stringBuilder     = new StringBuilder();
        reminderTaskList                = factory.getReminderTaskDao().getAll();
        String format                   = getText(Const.REMINDER_EDIT_SINGLE_MESSAGE);
        for (int i = 0; i < reminderTaskList.size(); i++) {
            ReminderTask reminderTask   = reminderTaskList.get(i);
            stringBuilder.append(String.format(format, "/del" + i, "/st" + i, reminderTask.getText())).append(next);
        }
        deleteMessageId                 = sendMessage(String.format(formatMessage, stringBuilder.toString(), "/new"));
    }

    private int     sendStartDate()         throws TelegramApiException { return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.SELECT_START_DATE_MESSAGE), dateKeyboard.getCalendarKeyboard())); }

    private void    sendMessageToGroup()    throws TelegramApiException {
            long groupChatId = factory.getGroupDao().getGroupToId(Integer.parseInt(factory.getPropertiesDao().getPropertiesValue(3))).getChatId();
            sendMessage(reminderTask.getText(), groupChatId);
    }

    private boolean isCommand(String command) { return updateMessageText.startsWith(command); }
}
