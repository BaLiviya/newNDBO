package baliviya.com.github.newNDBO.services;

import baliviya.com.github.newNDBO.dao.DaoFactory;
import baliviya.com.github.newNDBO.dao.impl.MessageDao;
import baliviya.com.github.newNDBO.entity.custom.QuestMessage;
import baliviya.com.github.newNDBO.entity.custom.Question;
import baliviya.com.github.newNDBO.entity.custom.SurveyAnswer;
import baliviya.com.github.newNDBO.entity.enums.Language;
import baliviya.com.github.newNDBO.utils.Const;
import baliviya.com.github.newNDBO.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SurveyReportService {

    private DaoFactory    daoFactory      = DaoFactory.getInstance();
    private MessageDao    messageDao      = daoFactory.getMessageDao();
    private XSSFWorkbook  workbook        = new XSSFWorkbook();
    private XSSFCellStyle style           = workbook.createCellStyle();
    private Language      currentLanguage = Language.ru;
    private Sheet         sheets;
    private Sheet         sheet;

    public void             sendSurveyReport(long chatId, DefaultAbsSender bot, int messagePrevReport) {
        currentLanguage = LanguageService.getLanguage(chatId);
        try {
            sendSurvey(chatId, bot, messagePrevReport);
        } catch (Exception e) {
            log.error("Can't create/send report", e);
            try {
                bot.execute(new SendMessage(chatId, "Ошибка при создании отчета"));
            } catch (TelegramApiException ex) {
                log.error("Can't send message", ex);
            }
        }
    }

    private void            sendSurvey(long chatId, DefaultAbsSender bot, int messagePrevReport) throws TelegramApiException {
        List<Question> all = daoFactory.getQuestionDao().getAll(currentLanguage);
        if (all == null || all.size() == 0) {
            bot.execute(new DeleteMessage(chatId, messagePrevReport));
            bot.execute(new SendMessage(chatId, "Опросов нет"));
            return;
        }
        all.forEach(question -> {
            sheets = workbook.createSheet(question.getName());
            sheet = sheets;
            List<QuestMessage> questMessageList = daoFactory.getQuestMessageDao().getAll(question.getId(), currentLanguage);
            List<SurveyAnswer> surveyAnswerList = daoFactory.getSurveyAnswerDao().getAll(question.getId());
            List<String> listOption = new ArrayList<>();
            for (QuestMessage questMessage : questMessageList) {
                listOption.addAll(Arrays.asList(questMessage.getRange().split(Const.SPLIT_RANGE)));
            }
            BorderStyle thin = BorderStyle.THIN;
            short black = IndexedColors.BLACK.getIndex();
            XSSFCellStyle styleTitle = setStyle(workbook, thin, black, style);
            int rowIndex = 0;
            createTitle(styleTitle, ++rowIndex, Arrays.asList("Вопрос", question.getDesc()));
            rowIndex++;
            createTitle(styleTitle, ++rowIndex, Arrays.asList("Вариант ответа", "Количество ответов"));
            for (String s : listOption) {
                createTitle(styleTitle, ++rowIndex, Arrays.asList(s, String.valueOf(surveyAnswerList.stream().filter(x -> x.getButton().equals(s)).collect(Collectors.toList()).size())));
            }
            rowIndex++;
            int cellIndex;
            for (QuestMessage questMessage : questMessageList) {
                createTitle(styleTitle, ++rowIndex, Arrays.asList("Группа:" + questMessage.getRange(), "Сообщение: " + questMessage.getMessage()));
                createTitle(styleTitle, ++rowIndex, Arrays.asList("Данные пользователя", "Ответ на сообщение", "Выбранный ответ"));
                List<String> strings = Arrays.asList(questMessage.getRange().split(Const.SPLIT_RANGE));
                for (SurveyAnswer surveyAnswer : surveyAnswerList) {
                    if (strings.contains(surveyAnswer.getButton())) {
                        sheets.createRow(++rowIndex);
                        insertToRow(rowIndex, Arrays.asList(getString(String.valueOf(surveyAnswer.getChatId())), getString(surveyAnswer.getText()), getString(surveyAnswer.getButton())), style);
                    }
                }
            }
            cellIndex = 0;
            sheets.setColumnWidth(cellIndex++,7000);
            sheets.setColumnWidth(cellIndex++,30000);
            sheets.setColumnWidth(cellIndex++,5000);
        });
        try {
            sendFile(chatId, bot);
        } catch (IOException e) {
            log.error("error send file", e);
        }
    }

    private void            createTitle(XSSFCellStyle styleTitle, int rowIndex, List<String> title) {
        sheets.createRow(rowIndex);
        insertToRow(rowIndex, title, styleTitle);
    }

    private void            insertToRow(int row, List<String> cellValues, CellStyle cellStyle) {
        int cellIndex = 0;
        for (String cellValue : cellValues) {
            addCellValue(row, cellIndex++, cellValue, cellStyle);
        }
    }

    private void            addCellValue(int rowIndex, int cellIndex, String cellValue, CellStyle cellStyle) {
        sheets.getRow(rowIndex).createCell(cellIndex).setCellValue(getString(cellValue));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(cellStyle);
    }

    private String          getString(String nullable) {
        if (nullable == null) return "";
        return nullable;
    }

    private XSSFCellStyle   setStyle(XSSFWorkbook workbook, BorderStyle thin, short black, XSSFCellStyle style) {
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setBorderTop(thin);
        style.setBorderBottom(thin);
        style.setBorderRight(thin);
        style.setBorderLeft(thin);
        style.setTopBorderColor(black);
        style.setRightBorderColor(black);
        style.setBottomBorderColor(black);
        style.setLeftBorderColor(black);

        BorderStyle tittle = BorderStyle.MEDIUM;
        XSSFCellStyle styleTitle = workbook.createCellStyle();
        styleTitle.setWrapText(true);
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);

        styleTitle.setBorderTop(tittle);
        styleTitle.setBorderBottom(tittle);
        styleTitle.setBorderRight(tittle);
        styleTitle.setBorderLeft(tittle);

        styleTitle.setTopBorderColor(black);
        styleTitle.setRightBorderColor(black);
        styleTitle.setBottomBorderColor(black);
        styleTitle.setLeftBorderColor(black);

        style.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 52, 94)));
        return styleTitle;
    }

    private void            sendFile(long chatId, DefaultAbsSender bot) throws IOException, TelegramApiException {
        String fileName = "Опросы - " + DateUtil.getDayDate(new Date()) + ".xlsx";
        String path     = "C:\\test\\" + fileName;
        path            += new Date().getTime();
        try (FileOutputStream stream = new FileOutputStream(path)) {
            workbook.write(stream);
        } catch (IOException e) {
            log.error("Can't send File error: ", e);
        }
        sendFile(chatId, bot, fileName, path);
    }

    private void            sendFile(long chatId, DefaultAbsSender bot, String fileName, String path) throws IOException, TelegramApiException {
        File file = new File(path);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            bot.execute(new SendDocument().setChatId(chatId).setDocument(fileName, fileInputStream));
        }
        file.delete();
    }
}