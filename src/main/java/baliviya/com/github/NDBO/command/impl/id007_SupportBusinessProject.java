package baliviya.com.github.NDBO.command.impl;

import baliviya.com.github.NDBO.command.Command;
import baliviya.com.github.NDBO.entity.custom.*;
import baliviya.com.github.NDBO.entity.enums.Language;
import baliviya.com.github.NDBO.entity.enums.WaitingType;
import baliviya.com.github.NDBO.services.LanguageService;
import baliviya.com.github.NDBO.utils.ButtonsLeaf;
import baliviya.com.github.NDBO.utils.Const;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class id007_SupportBusinessProject extends Command {

    private int                   deleteMessageId;
    private int                   secondDeleteMessageId;
    private List<String>          list = new ArrayList<>();
    private List<HandlingName>    handlingNames;
    private ButtonsLeaf           buttonsLeaf;
    private Handling              handling;
    private RegistrationHandling  registrationHandling;
    private int                   businessId;
    private List<Handling>        handlingList;
    private Language              currentLanguage;
    private List<ServiceQuestion> allQuestion;
    private List<QuestMessage>    allMessage;
    private ServiceQuestion       question;
    private List<String>          listAnswer;
    private ServiceSurveyAnswer   surveyAnswer;

    @Override
    public boolean execute() throws TelegramApiException {
        switch (waitingType) {
            case START:
                if(!isRecipient()) {
                    deleteMessageId      = registrationMessage();
                    return EXIT;
                }
                deleteMessage(updateMessageId);
                registrationHandling = new RegistrationHandling();
                registrationHandling.setRegistrationDate(new Date());
                registrationHandling.setChatId(chatId);
                registrationHandling.setIin(Long.parseLong(userDao.getUserByChatId(chatId).getIin()));
                deleteMessageId      = getBusinessName();
                waitingType          = WaitingType.SET_BUSINESS_NAME;
                return COMEBACK;
            case SET_BUSINESS_NAME:
                delete();
                if (hasCallbackQuery()) {
                    businessId      = handlingNames.get(Integer.parseInt(updateMessageText)).getId();
                    deleteMessageId = getHandling();
                    waitingType     = WaitingType.SET_HANDLING;
                } else {
                    secondDeleteMessageId = wrongData();
                    deleteMessageId       = getBusinessName();
                }
                return COMEBACK;
            case SET_HANDLING:
                delete();
                if (hasCallbackQuery()) {
                    handling                  = handlingList.get(Integer.parseInt(updateMessageText));
                    String formatMessage      = getText(Const.INFORMATION_BUSINESS_TEXT_MESSAGE);
                    String result             = String.format(formatMessage, handlingNames.get(businessId).getName(), handling.getText());
                    if (handling.getPhoto()  != null) {
                        secondDeleteMessageId = bot.execute(new SendPhoto().setChatId(chatId).setPhoto(handling.getPhoto())).getMessageId();
                    }
                    deleteMessageId           = sendMessageWithKeyboard(result, Const.WRITE_IN_SERVICE_KEYBOARD);
                    waitingType               = WaitingType.SET_BUSINESS;
                } else {
                    secondDeleteMessageId     = wrongData();
                    deleteMessageId           = getHandling();
                }
                return COMEBACK;
            case SET_BUSINESS:
                delete();
                if (hasCallbackQuery()) {
                    if (isButton(Const.JOIN_BUTTON)) {
                        registrationHandling.setIdHandling(handling.getId());
                        registrationHandling.setCome(false);
                        factory             .getRegistrationHandlingDao().insertBusiness(registrationHandling);
                        deleteMessageId     = done();
                        return EXIT;
                    } else if (isButton(Const.QUEST_BUTTON)) {
                        currentLanguage = LanguageService.getLanguage(chatId);
                        allQuestion     = serviceQuestionDao.getAllActiveBusiness(currentLanguage, chatId, handling.getId());
                        if (allQuestion == null || allQuestion.size() == 0) {
                            sendMessage(Const.EMPTY_OR_FINISHED_SURVEY_MESSAGE);
                            return EXIT;
                        } else {
                            List<String> questName  = new ArrayList<>();
                            allQuestion.forEach((e) -> questName.add(e.getName()));
                            buttonsLeaf             = new ButtonsLeaf(questName);
                            deleteMessageId         = toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.TEST_FROM_SERVICE_MESSAGE), buttonsLeaf.getListButton()));
                            waitingType             = WaitingType.CHOOSE_QUESTION;
                        }
                    } else if (isButton(Const.BACK_BUTTON)) {
                        deleteMessageId = getBusinessName();
                        waitingType     = WaitingType.SET_BUSINESS_NAME;
                    }
                } else {
                    String formatMessage      = getText(Const.INFORMATION_BUSINESS_TEXT_MESSAGE);
                    String result             = String.format(formatMessage, handlingNames.get(businessId).getName(), handling.getText());
                    if (handling.getPhoto()  != null) {
                        secondDeleteMessageId = bot.execute(new SendPhoto().setChatId(chatId).setPhoto(handling.getPhoto())).getMessageId();
                    }
                    deleteMessageId           = sendMessageWithKeyboard(result, Const.WRITE_IN_SERVICE_KEYBOARD);
                }
                return COMEBACK;
            case CHOOSE_QUESTION:
                delete();
                if (hasCallbackQuery()) {
                    question                = allQuestion.get(Integer.parseInt(updateMessageText));
                    allMessage              = questMessageDao.getAllBusiness(question.getId(), currentLanguage);
                    listAnswer              = new ArrayList<>();
                    allMessage.forEach((e) -> Collections.addAll(listAnswer, e.getRange().split(",")));
                    buttonsLeaf             = new ButtonsLeaf(listAnswer);
                    deleteMessageId         = toDeleteKeyboard(sendMessageWithKeyboard(question.getQuestion(), buttonsLeaf.getListButton()));
                    waitingType             = WaitingType.CHOOSE_OPTION;
                } else {
                    deleteMessageId         = toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.TEST_FROM_SERVICE_MESSAGE), buttonsLeaf.getListButton()));
                }
                return COMEBACK;
            case CHOOSE_OPTION:
                delete();
                if (hasCallbackQuery()) {
                    String answer   = listAnswer.get(Integer.parseInt(updateMessageText));
                    for (QuestMessage questMessage : allMessage) {
                        for (String answerDb : questMessage.getRange().split(",")) {
                            if (answerDb.equals(answer)) {
                                surveyAnswer          = new ServiceSurveyAnswer();
                                surveyAnswer          .setButton(answer);
                                surveyAnswer          .setChatId(chatId);
                                surveyAnswer          .setSurveyId(question.getId());
                                surveyAnswer          .setHandlingType("BUSINESS");
                                serviceSurveyAnswerDao.insert(surveyAnswer);
                                allQuestion           = serviceQuestionDao.getAllActiveBusiness(currentLanguage, chatId, handling.getId());
                                if (allQuestion == null || allQuestion.size() == 0) {
                                    sendMessage(Const.EMPTY_OR_FINISHED_SURVEY_MESSAGE);
                                    return EXIT;
                                } else {
                                    List<String> questName  = new ArrayList<>();
                                    allQuestion.forEach((e) -> questName.add(e.getName()));
                                    buttonsLeaf             = new ButtonsLeaf(questName);
                                    deleteMessageId         = toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.TEST_FROM_SERVICE_MESSAGE), buttonsLeaf.getListButton()));
                                    waitingType             = WaitingType.CHOOSE_QUESTION;
                                }
                            }
                        }
                    }
                } else {
                    deleteMessageId = toDeleteKeyboard(sendMessageWithKeyboard(question.getQuestion(), buttonsLeaf.getListButton()));
                }
                return COMEBACK;
        }
        return EXIT;
    }

    private int getBusinessName()       throws TelegramApiException {
        list.clear();
        handlingNames = factory.getHandlingNameDao().getAllBusiness();
        handlingNames.forEach((e) -> list.add(e.getName()));
        buttonsLeaf = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.SUPPORT_BUSINESS_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int getHandling()           throws TelegramApiException {
        list.clear();
        handlingList = handlingDao.getAllBusiness(businessId);
        handlingList.forEach((e) -> list.add(e.getFullName()));
        buttonsLeaf = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.CHOOSE_SPEC_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int registrationMessage()   throws TelegramApiException {
        return botUtils.sendMessage(Const.GO_TO_REGISTRATION_MESSAGE, chatId);
    }

    private int wrongData()             throws TelegramApiException {
        return botUtils.sendMessage(Const.WRONG_DATA_TEXT, chatId);
    }

    private int done()                  throws TelegramApiException {
        return botUtils.sendMessage(Const.DONE_JOIN_MESSAGE, chatId);
    }

    private void delete() {
        deleteMessage(updateMessageId);
        deleteMessage(deleteMessageId);
        deleteMessage(secondDeleteMessageId);
    }
}
