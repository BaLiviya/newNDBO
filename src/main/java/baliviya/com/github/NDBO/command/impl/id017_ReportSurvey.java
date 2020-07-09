package baliviya.com.github.NDBO.command.impl;

import baliviya.com.github.NDBO.command.Command;
import baliviya.com.github.NDBO.services.SurveyReportService;
import baliviya.com.github.NDBO.utils.Const;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class id017_ReportSurvey extends Command {

    @Override
    public boolean execute() throws TelegramApiException {
        if (!isAdmin() && !isMainAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        deleteMessage(updateMessageId);
        sendReport();
        return EXIT;
    }

    private void sendReport() throws TelegramApiException {
        int preview = sendMessage("Список подготавливается...");
        SurveyReportService reportService = new SurveyReportService();
        reportService.sendSurveyReport(chatId, bot, preview);
    }
}
