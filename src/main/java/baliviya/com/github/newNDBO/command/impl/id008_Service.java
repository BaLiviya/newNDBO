package baliviya.com.github.newNDBO.command.impl;

import baliviya.com.github.newNDBO.command.Command;
import baliviya.com.github.newNDBO.entity.custom.*;
import baliviya.com.github.newNDBO.entity.enums.Language;
import baliviya.com.github.newNDBO.entity.enums.WaitingType;
import baliviya.com.github.newNDBO.services.LanguageService;
import baliviya.com.github.newNDBO.utils.ButtonsLeaf;
import baliviya.com.github.newNDBO.utils.Const;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class id008_Service extends Command {

    private int                     deleteMessageId;
    private int                     secondDeleteMessageId;
    private ButtonsLeaf             buttonsLeaf;
    private int                     serviceTypeId;
    private List<String>            list = new ArrayList<>();
    private List<ServiceType>       serviceTypes;
    private List<Handling>          services;
    private List<String>            listAnswer;
    private Handling                service;
    private RegistrationHandling    registrationService;
    private Language                currentLanguage;
    private List<ServiceQuestion>   allQuestion;
    private List<QuestMessage>      allMessage;
    private ServiceQuestion         question;
    private ServiceSurveyAnswer     surveyAnswer;

    @Override
    public boolean  execute()               throws TelegramApiException {
        switch (waitingType) {
            case START:
                deleteMessage(updateMessageId);
                if(!isRecipient()) {
                    deleteMessageId         = registrationMessage();
                    return EXIT;
                }
                registrationService         = new RegistrationHandling();
                registrationService.setRegistrationDate(new Date());
                registrationService.setChatId(chatId);
                registrationService.setIin(Long.parseLong(recipientDao.getRecipientByChatId(chatId).getIin()));
                deleteMessageId             = getServiceType();
                waitingType                 = WaitingType.SET_SERVICE_TYPE;
                return COMEBACK;
            case SET_SERVICE_TYPE:
                delete();
                if (hasCallbackQuery()) {
                    serviceTypeId           = serviceTypes.get(Integer.parseInt(updateMessageText)).getId();
                    deleteMessageId         = getService();
                    waitingType             = WaitingType.SET_SERVICE;
                } else {
                    secondDeleteMessageId   = wrongData();
                    deleteMessageId         = getServiceType();
                }
                return COMEBACK;
            case SET_SERVICE:
                delete();
                if (hasCallbackQuery()) {
                    service                   = services.get(Integer.parseInt(updateMessageText));
                    registrationService       .setIdHandling(service.getId());
                    String formatMessage      = getText(Const.INFORMATION_TEXT_MESSAGE);
                    String result             = String.format(formatMessage, service.getFullName(), service.getText());
                    if (service.getPhoto() != null) secondDeleteMessageId = bot.execute(new SendPhoto().setChatId(chatId).setPhoto(service.getPhoto())).getMessageId();
                    deleteMessageId           = sendMessageWithKeyboard(result, Const.WRITE_IN_SERVICE_KEYBOARD);
                    waitingType               = WaitingType.SERVICE;
                } else {
                    secondDeleteMessageId     = wrongData();
                    deleteMessageId           = getService();
                }
                return COMEBACK;
            case SERVICE:
                delete();
                if (hasCallbackQuery()) {
                    if (isButton(Const.JOIN_BUTTON)) {
                        factory.getRegistrationHandlingDao().insertService(registrationService);
                        sendMessageToSpec();
                        deleteMessageId    = done();
                        return EXIT;
                    } else if (isButton(Const.QUEST_BUTTON)) {
                        currentLanguage    = LanguageService.getLanguage(chatId);
                        allQuestion        = serviceQuestionDao.getAllActive(currentLanguage, chatId, service.getId());
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
                        deleteMessageId = getServiceType();
                        waitingType     = WaitingType.SET_SERVICE_TYPE;
                    }
                } else {
                    String formatMessage      = getText(Const.INFORMATION_TEXT_MESSAGE);
                    String result             = String.format(formatMessage, service.getFullName(), service.getText());
                    if (service.getPhoto()   != null) {
                        secondDeleteMessageId = bot.execute(new SendPhoto().setChatId(chatId).setPhoto(service.getPhoto())).getMessageId();
                    }
                    deleteMessageId           = sendMessageWithKeyboard(result, Const.WRITE_IN_SERVICE_KEYBOARD);
                }
                return COMEBACK;
            case CHOOSE_QUESTION:
                delete();
                if (hasCallbackQuery()) {
                    question        = allQuestion    .get(Integer.parseInt(updateMessageText));
                    allMessage      = questMessageDao.getAllService(question.getId(), currentLanguage);
                    listAnswer      = new ArrayList<>();
                    allMessage.forEach((e) -> Collections.addAll(listAnswer, e.getRange().split(",")));
                    buttonsLeaf     = new ButtonsLeaf(listAnswer);
                    deleteMessageId = toDeleteKeyboard(sendMessageWithKeyboard(question.getQuestion(), buttonsLeaf.getListButton()));
                    waitingType     = WaitingType.CHOOSE_OPTION;
                } else {
                    deleteMessageId = toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.TEST_FROM_SERVICE_MESSAGE), buttonsLeaf.getListButton()));
                }
                return COMEBACK;
            case CHOOSE_OPTION:
                delete();
                if (hasCallbackQuery()) {
                    String answer = listAnswer.get(Integer.parseInt(updateMessageText));
                    for (QuestMessage questMessage : allMessage) {
                        for (String answerDb : questMessage.getRange().split(",")) {
                            if (answerDb.equals(answer)) {
                                surveyAnswer    = new ServiceSurveyAnswer();
                                surveyAnswer.setButton(answer);
                                surveyAnswer.setChatId(chatId);
                                surveyAnswer.setSurveyId(question.getId());
                                surveyAnswer.setHandlingType("SERVICE");
                                serviceSurveyAnswerDao.insert(surveyAnswer);
                                allQuestion     = serviceQuestionDao.getAllActive(currentLanguage, chatId, service.getId());
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

    private int     getServiceType()        throws TelegramApiException {
        list.clear();
        serviceTypes = factory.getServiceTypeDao().getAll();
        serviceTypes.forEach((e) -> list.add(e.getName()));
        buttonsLeaf  = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.SERVICE_TYPE_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getService()            throws TelegramApiException {
        list.clear();
        services    = handlingDao.getAllService(serviceTypeId);
        services.forEach((e) -> list.add(e.getFullName()));
        buttonsLeaf = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.CHOOSE_SPEC_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     sendMessageToSpec()     throws TelegramApiException { return botUtils.sendMessage(String.format(getText(Const.JOINED_TO_SERVICE_MESSAGE), userDao.getUserByChatId(registrationService.getChatId()).getFullName()), service.getHandlingTeacherId()); }

    private int     wrongData()             throws TelegramApiException { return botUtils.sendMessage(Const.WRONG_DATA_TEXT, chatId); }

    private int     registrationMessage()   throws TelegramApiException { return botUtils.sendMessage(Const.GO_TO_REGISTRATION_MESSAGE, chatId); }

    private int     done()                  throws TelegramApiException { return botUtils.sendMessage(Const.DONE_JOIN_MESSAGE, chatId); }

    private void    delete() {
        deleteMessage(updateMessageId);
        deleteMessage(deleteMessageId);
        deleteMessage(secondDeleteMessageId);
    }
}
