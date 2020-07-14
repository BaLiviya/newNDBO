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

public class id005_Courses extends Command {

    private List<String>          list = new ArrayList<>();
    private List<CoursesType>     coursesTypes;
    private List<CoursesName>     coursesNames;
    private List<Handling>        handlingList;
    private int                   deleteMessageId;
    private int                   secondDeleteMessageId;
    private ButtonsLeaf           buttonsLeaf;
    private int                   courseTypeId;
    private Handling              handling;
    private int                   courseId;
    private int                   courseNameId;
    private RegistrationHandling  registrationHandling;
    private Language              currentLanguage;
    private List<ServiceQuestion> allQuestion;
    private List<QuestMessage>    allMessage;
    private ServiceQuestion       question;
    private ServiceSurveyAnswer   surveyAnswer;
    private List<String>          listAnswer;

    @Override
    public boolean  execute()                           throws TelegramApiException {
        switch (waitingType) {
            case START:
                if(!isRecipient()) {
                    deleteMessageId = registrationMessage();
                    return EXIT;
                }
                deleteMessage(updateMessageId);
                registrationHandling                = new RegistrationHandling();
                registrationHandling.setRegistrationDate(new Date());
                registrationHandling.setChatId(chatId);
                registrationHandling.setIin(Long.parseLong(recipientDao.getRecipientByChatId(chatId).getIin()));
                deleteMessageId                     = getCoursesType();
                waitingType                         = WaitingType.SET_COURSES_TYPE;
                return COMEBACK;
            case SET_COURSES_TYPE:
                delete();
                if (hasCallbackQuery()) {
                    courseTypeId                    = coursesTypes.get(Integer.parseInt(updateMessageText)).getId();
                    deleteMessageId                 = getCoursesName(courseTypeId);
                    waitingType                     = WaitingType.SET_COURSES_NAME;
                } else {
                    secondDeleteMessageId           = wrongData();
                    deleteMessageId                 = getCoursesType();
                }
                return COMEBACK;
            case SET_COURSES_NAME:
                delete();
                if (hasCallbackQuery()) {
                    courseId                        = Integer.parseInt(updateMessageText);
                    courseNameId                    = coursesNames.get(courseId).getId();
                    deleteMessageId                 = getHandling();
                    waitingType                     = WaitingType.SET_HANDLING;
                } else {
                    secondDeleteMessageId           = wrongData();
                    deleteMessageId                 = getCoursesName(courseTypeId);
                }
                return COMEBACK;
            case SET_HANDLING:
                delete();
                if (hasCallbackQuery()) {
                    handling                        = handlingList.get(Integer.parseInt(updateMessageText));
                    String formatMessage            = getText(Const.INFORMATION_COURSE_TEXT_MESSAGE);
                    String result                   = String.format(formatMessage, coursesNames.get(courseId).getName(), handling.getText());
                    if (handling.getPhoto()  != null) secondDeleteMessageId = bot.execute(new SendPhoto().setChatId(chatId).setPhoto(handling.getPhoto())).getMessageId();
                    deleteMessageId                 = sendMessageWithKeyboard(result, Const.WRITE_IN_SERVICE_KEYBOARD);
                    waitingType                     = WaitingType.SET_COURSE;
                } else {
                    secondDeleteMessageId           = wrongData();
                    deleteMessageId                 = getHandling();
                }
                return COMEBACK;
            case SET_COURSE:
                delete();
                if (hasCallbackQuery()) {
                    if (isButton(Const.JOIN_BUTTON)) {
                        registrationHandling.setIdHandling(coursesNames.get(courseId).getId());
                        factory.getRegistrationHandlingDao().insertCourse(registrationHandling);
                        sendMessageToSpec();
                        deleteMessageId             = done();
                        return EXIT;
                    } else if (isButton(Const.QUEST_BUTTON)) {
                        currentLanguage             = LanguageService.getLanguage(chatId);
                        allQuestion                 = serviceQuestionDao.getAllActiveCourse(currentLanguage, chatId, handling.getId());
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
                        deleteMessageId             = getCoursesType();
                        waitingType                 = WaitingType.SET_COURSES_TYPE;
                    }
                } else {
                    String formatMessage            = getText(Const.INFORMATION_COURSE_TEXT_MESSAGE);
                    String result                   = String.format(formatMessage, coursesNames.get(courseId).getName(), handling.getText());
                    if (handling.getPhoto()  != null) secondDeleteMessageId = bot.execute(new SendPhoto().setChatId(chatId).setPhoto(handling.getPhoto())).getMessageId();
                    deleteMessageId                 = sendMessageWithKeyboard(result, Const.WRITE_IN_SERVICE_KEYBOARD);
                }
                return COMEBACK;
            case CHOOSE_QUESTION:
                delete();
                if (hasCallbackQuery()) {
                    question                        = allQuestion.get(Integer.parseInt(updateMessageText));
                    allMessage                      = questMessageDao.getAllCourses(question.getId(), currentLanguage);
                    listAnswer                      = new ArrayList<>();
                    allMessage.forEach((e) -> Collections.addAll(listAnswer, e.getRange().split(Const.SPLIT_RANGE)));
                    buttonsLeaf                     = new ButtonsLeaf(listAnswer);
                    deleteMessageId                 = toDeleteKeyboard(sendMessageWithKeyboard(question.getQuestion(), buttonsLeaf.getListButton()));
                    waitingType                     = WaitingType.CHOOSE_OPTION;
                } else { deleteMessageId            = toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.TEST_FROM_SERVICE_MESSAGE), buttonsLeaf.getListButton())); }
                return COMEBACK;
            case CHOOSE_OPTION:
                delete();
                if (hasCallbackQuery()) {
                    String answer                   = listAnswer.get(Integer.parseInt(updateMessageText));
                    for (QuestMessage questMessage : allMessage) {
                        for (String answerDb : questMessage.getRange().split(Const.SPLIT_RANGE)) {
                            if (answerDb.equals(answer)) {
                                surveyAnswer        = new ServiceSurveyAnswer();
                                surveyAnswer.setButton(answer);
                                surveyAnswer.setChatId(chatId);
                                surveyAnswer.setSurveyId(question.getId());
                                surveyAnswer.setHandlingType("COURSE");
                                serviceSurveyAnswerDao.insert(surveyAnswer);
                                allQuestion         = serviceQuestionDao.getAllActive(currentLanguage, chatId, handling.getId());
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
                } else { deleteMessageId = toDeleteKeyboard(sendMessageWithKeyboard(question.getQuestion(), buttonsLeaf.getListButton())); }
                return COMEBACK;
        }
        return EXIT;
    }

    private int     getCoursesType()                    throws TelegramApiException {
        list.clear();
        coursesTypes    = factory.getCoursesTypeDao().getAll();
        coursesTypes.forEach((e) -> list.add(e.getName()));
        buttonsLeaf     = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.COURSE_TYPE_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getCoursesName(int courseTypeId)    throws TelegramApiException {
        list.clear();
        coursesNames    = factory.getCoursesNameDao().getAll(courseTypeId);
        coursesNames.forEach((e) -> list.add(e.getName()));
        buttonsLeaf     = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.COURSE_TYPE_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getHandling()                       throws TelegramApiException {
        list.clear();
        handlingList    = handlingDao.getAllCourse(courseNameId);
        handlingList.forEach((e) -> list.add(e.getFullName()));
        buttonsLeaf     = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.CHOOSE_SPEC_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     sendMessageToSpec()                 throws TelegramApiException { return botUtils.sendMessage(String.format(getText(Const.JOINED_TO_SERVICE_MESSAGE), userDao.getUserByChatId(registrationHandling.getChatId()).getFullName()), handling.getHandlingTeacherId()); }

    private int     wrongData()                         throws TelegramApiException { return botUtils.sendMessage(Const.WRONG_DATA_TEXT, chatId); }

    private int     registrationMessage()               throws TelegramApiException { return botUtils.sendMessage(Const.GO_TO_REGISTRATION_MESSAGE, chatId); }

    private int     done()                              throws TelegramApiException { return botUtils.sendMessage(Const.DONE_JOIN_MESSAGE, chatId); }

    private void    delete() {
        deleteMessage(updateMessageId);
        deleteMessage(deleteMessageId);
        deleteMessage(secondDeleteMessageId);
    }
}
