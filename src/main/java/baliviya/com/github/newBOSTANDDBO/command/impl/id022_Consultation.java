package baliviya.com.github.newBOSTANDDBO.command.impl;

import baliviya.com.github.newBOSTANDDBO.command.Command;
import baliviya.com.github.newBOSTANDDBO.entity.custom.*;
import baliviya.com.github.newBOSTANDDBO.entity.enums.Language;
import baliviya.com.github.newBOSTANDDBO.entity.enums.WaitingType;
import baliviya.com.github.newBOSTANDDBO.services.LanguageService;
import baliviya.com.github.newBOSTANDDBO.utils.ButtonsLeaf;
import baliviya.com.github.newBOSTANDDBO.utils.Const;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class id022_Consultation extends Command {

    private RegistrationHandling    registrationHandling;
    private List<String>            list = new ArrayList<>();
    private List<CoursesType>       consultationTypes;
    private ButtonsLeaf             buttonsLeaf;
    private List<CoursesName>       consultationNames;
    private int                     consultationTypeId;
    private int                     consultationNameId;
    private List<Handling>          handlingList;
    private Handling                handling;
    private int                     consultationNameIdFromList;
    private Language                currentLanguage;
    private List<ServiceQuestion>   allQuestion;
    private ServiceQuestion         question;
    private List<QuestMessage>      allMessage;
    private List<String>            listAnswer;
    private ServiceSurveyAnswer     surveyAnswer;
    private int                     deleteMessageId;
    private int                     secondDeleteMessageId;

    @Override
    public boolean execute()                                throws TelegramApiException {
        switch (waitingType) {
            case START:
                if(!isRecipient()) {
                    deleteMessageId         = registrationMessage();
                    return EXIT;
                }
                deleteMessage(updateMessageId);
                registrationHandling        = new RegistrationHandling();
                registrationHandling    .setRegistrationDate(new Date());
                registrationHandling    .setChatId(chatId);
                registrationHandling    .setIin(Long.parseLong(recipientDao.getRecipientByChatId(chatId).getIin()));
                deleteMessageId             = getConsultationTypes();
                waitingType                 = WaitingType.SET_CONSULTATION_TYPE;
                return COMEBACK;
            case SET_CONSULTATION_TYPE:
                delete();
                if (hasCallbackQuery()) {
                    consultationTypeId      = consultationTypes.get(Integer.parseInt(updateMessageText)).getId();
                    deleteMessageId         = getConsultationName(consultationTypeId);
                    waitingType             = WaitingType.SET_CONSULTATION_NAME;
                } else {
                    secondDeleteMessageId   = wrongData();
                    deleteMessageId         = getConsultationTypes();
                }
                return COMEBACK;
            case SET_CONSULTATION_NAME:
                delete();
                if (hasCallbackQuery()) {
                    consultationNameIdFromList  = Integer.parseInt(updateMessageText);
                    consultationNameId          = consultationNames.get(consultationNameIdFromList).getId();
                    deleteMessageId             = getHandling();
                    waitingType                 = WaitingType.SET_HANDLING;
                } else {
                    secondDeleteMessageId       = wrongData();
                    deleteMessageId             = getConsultationName(consultationTypeId);
                }
                return COMEBACK;
            case SET_HANDLING:
                delete();
                if (hasCallbackQuery()) {
                    handling                  = handlingList.get(Integer.parseInt(updateMessageText));
                    String formatMessage      = getText(Const.INFORMATION_CONSULTATION_TEXT_MESSAGE);
                    String result             = String.format(formatMessage, consultationNames.get(consultationNameIdFromList).getName(), handling.getText());
                    if (handling.getPhoto()  != null) secondDeleteMessageId = bot.execute(new SendPhoto().setChatId(chatId).setPhoto(handling.getPhoto())).getMessageId();
                    deleteMessageId           = sendMessageWithKeyboard(result, Const.WRITE_IN_SERVICE_KEYBOARD);
                    waitingType               = WaitingType.SET_CONSULTATION;
                } else {
                    secondDeleteMessageId     = wrongData();
                    deleteMessageId           = getHandling();
                }
                return COMEBACK;
            case SET_CONSULTATION:
                delete();
                if (hasCallbackQuery()) {
                    if (isButton(Const.JOIN_BUTTON)) {
                        registrationHandling.setIdHandling(handling.getId());
                        factory             .getRegistrationHandlingDao().insertConsultation(registrationHandling);
                        sendMessageToSpec();
                        deleteMessageId     = done();
                        return EXIT;
                    } else if (isButton(Const.QUEST_BUTTON)) {
                        currentLanguage     = LanguageService.getLanguage(chatId);
                        allQuestion         = serviceQuestionDao.getAllActiveConsultation(currentLanguage, chatId, handling.getId());
                        if (allQuestion == null || allQuestion.size() == 0) {
                            sendMessage(Const.EMPTY_OR_FINISHED_SURVEY_MESSAGE);
                            return EXIT;
                        }  else {
                            List<String> questName  = new ArrayList<>();
                            allQuestion.forEach((e) -> questName.add(e.getName()));
                            buttonsLeaf             = new ButtonsLeaf(questName);
                            deleteMessageId         = toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.TEST_FROM_SERVICE_MESSAGE), buttonsLeaf.getListButton()));
                            waitingType             = WaitingType.CHOOSE_QUESTION;
                        }
                    } else if (isButton(Const.BACK_BUTTON)) {
                        deleteMessageId             = getConsultationTypes();
                        waitingType                 = WaitingType.SET_CONSULTATION_TYPE;
                    }
                } else {
                    String formatMessage      = getText(Const.INFORMATION_CONSULTATION_TEXT_MESSAGE);
                    String result             = String.format(formatMessage, consultationNames.get(consultationNameIdFromList).getName(), handling.getText());
                    if (handling.getPhoto()  != null) {
                        secondDeleteMessageId = bot.execute(new SendPhoto().setChatId(chatId).setPhoto(handling.getPhoto())).getMessageId();
                    }
                    deleteMessageId           = sendMessageWithKeyboard(result, Const.WRITE_IN_SERVICE_KEYBOARD);
                }
                return COMEBACK;
            case CHOOSE_QUESTION:
                delete();
                if (hasCallbackQuery()) {
                    question        = allQuestion.get(Integer.parseInt(updateMessageText));
                    allMessage      = questMessageDao.getAllConsultation(question.getId(), currentLanguage);
                    listAnswer      = new ArrayList<>();
                    allMessage      .forEach((e) -> Collections.addAll(listAnswer, e.getRange().split(",")));
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
                                surveyAnswer = new ServiceSurveyAnswer();
                                surveyAnswer.setButton(answer);
                                surveyAnswer.setChatId(chatId);
                                surveyAnswer.setSurveyId(question.getId());
                                surveyAnswer.setHandlingType("CONSULTATION");
                                serviceSurveyAnswerDao.insert(surveyAnswer);
                                allQuestion         = serviceQuestionDao.getAllActiveConsultation(currentLanguage, chatId, handling.getId());
                                if (allQuestion == null || allQuestion.size() == 0) {
                                    sendMessage(Const.EMPTY_OR_FINISHED_SURVEY_MESSAGE);
                                    return EXIT;
                                }  else {
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

    private int  getConsultationTypes()                     throws TelegramApiException {
        list.clear();
        consultationTypes   = factory.getCoursesTypeDao().getAllConsultation();
        consultationTypes.forEach((e) -> list.add(e.getName()));
        buttonsLeaf         = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.CONSULTATION_TYPE_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int  getConsultationName(int consultationId)    throws TelegramApiException {
        list.clear();
        consultationNames   = factory.getCoursesNameDao().getAllConsultation(consultationId);
        consultationNames.forEach((e) -> list.add(e.getName()));
        buttonsLeaf         = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.CONSULTATION_TYPE_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int  getHandling()                              throws TelegramApiException {
        list.clear();
        handlingList    = handlingDao.getAllConsultation(consultationNameId);
        handlingList.forEach((e) -> list.add(e.getFullName()));
        buttonsLeaf     = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.CHOOSE_SPEC_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int  sendMessageToSpec()                        throws TelegramApiException { return botUtils.sendMessage(String.format(getText(Const.JOINED_TO_SERVICE_MESSAGE), userDao.getUserByChatId(registrationHandling.getChatId()).getFullName()), handling.getHandlingTeacherId()); }

    private int  registrationMessage()                      throws TelegramApiException { return botUtils.sendMessage(Const.GO_TO_REGISTRATION_MESSAGE, chatId); }

    private int  wrongData()                                throws TelegramApiException { return botUtils.sendMessage(Const.WRONG_DATA_TEXT, chatId); }

    private int  done()                                     throws TelegramApiException { return botUtils.sendMessage(Const.DONE_JOIN_MESSAGE, chatId); }

    private void delete() {
        deleteMessage(updateMessageId);
        deleteMessage(deleteMessageId);
        deleteMessage(secondDeleteMessageId);
    }
}
