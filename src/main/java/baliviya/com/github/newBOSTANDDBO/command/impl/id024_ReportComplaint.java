package baliviya.com.github.newBOSTANDDBO.command.impl;

import baliviya.com.github.newBOSTANDDBO.command.Command;
import baliviya.com.github.newBOSTANDDBO.entity.enums.WaitingType;
import baliviya.com.github.newBOSTANDDBO.services.ComplaintReportService;
import baliviya.com.github.newBOSTANDDBO.utils.Const;
import baliviya.com.github.newBOSTANDDBO.utils.DateKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;

public class id024_ReportComplaint extends Command {

    private DateKeyboard    dateKeyboard;
    private Date            start;
    private Date            end;

    @Override
    public boolean  execute()       throws TelegramApiException {
        if (!isAdmin() && !isMainAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        switch (waitingType) {
            case START:
                deleteMessage(updateMessageId);
                dateKeyboard    = new DateKeyboard();
                sendStartDate();
                waitingType     = WaitingType.START_DATE;
                return COMEBACK;
            case START_DATE:
                deleteMessage(updateMessageId);
                if (hasCallbackQuery()) {
                    if (dateKeyboard.isNext(updateMessageText)) {
                        sendStartDate();
                    } else {
                        start           = dateKeyboard.getDateDate(updateMessageText);
                        start           .setHours(0);
                        start           .setMinutes(0);
                        start           .setSeconds(0);
                        sendEndDate();
                        waitingType     = WaitingType.END_DATE;
                    }
                }
                return COMEBACK;
            case END_DATE:
                deleteMessage(updateMessageId);
                if (hasCallbackQuery()) {
                    if (dateKeyboard.isNext(updateMessageText)) {
                        sendStartDate();
                    } else {
                        end     = dateKeyboard.getDateDate(updateMessageText);
                        end     .setHours(23);
                        end     .setMinutes(59);
                        end     .setSeconds(59);
                        sendReport();
                        waitingType = WaitingType.END_DATE;
                    }
                    return COMEBACK;
                }
                sendLightReport();
                return COMEBACK;
        }
        return EXIT;
    }

    private int     sendStartDate() throws TelegramApiException { return toDeleteKeyboard(sendMessageWithKeyboard(sendLightReport() + getText(Const.SELECT_START_DATE_MESSAGE), dateKeyboard.getCalendarKeyboard())); }

    private int     sendEndDate()   throws TelegramApiException { return toDeleteKeyboard(sendMessageWithKeyboard(Const.SELECT_END_DATE_MESSAGE, dateKeyboard.getCalendarKeyboard())); }

    private void    sendReport()    throws TelegramApiException {
        int preview                             = sendMessage(getText(Const.REPORT_DOING_MESSAGE));
        ComplaintReportService reportService    = new ComplaintReportService();
        reportService.sendSuggestionReport(chatId, bot, start, end, preview);
    }

    private String  sendLightReport() { return String.format(getText(Const.COMPLAINT_COUNT_MESSAGE), suggestionDao.getCount()); }
}
