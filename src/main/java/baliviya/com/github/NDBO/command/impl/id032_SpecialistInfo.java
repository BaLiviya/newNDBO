package baliviya.com.github.NDBO.command.impl;

import baliviya.com.github.NDBO.command.Command;
import baliviya.com.github.NDBO.entity.custom.Handling;
import baliviya.com.github.NDBO.entity.custom.RegistrationHandling;
import baliviya.com.github.NDBO.entity.enums.WaitingType;
import baliviya.com.github.NDBO.utils.ButtonsLeaf;
import baliviya.com.github.NDBO.utils.Const;
import baliviya.com.github.NDBO.utils.DateKeyboard;
import baliviya.com.github.NDBO.utils.DateUtil;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class id032_SpecialistInfo extends Command {

    private DateKeyboard                dateKeyboard;
    private Date                        start;
    private Date                        end;
    private Handling                    handling;
    private List<RegistrationHandling>  registrationHandlings;
    private RegistrationHandling        registrationHandling;
    private List<String>                userNameList = new ArrayList<>();
    private ButtonsLeaf                 buttonsLeaf;
    private int                         registrationType;


    @Override
    public boolean                  execute()       throws TelegramApiException {
        if (!specialistDao.isSpecialist(chatId)) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        switch (waitingType) {
            case START:
                deleteMessage(updateMessageId);
                if (isButton(66)) {
                    String text             = update.getCallbackQuery().getMessage().getText();
                    int id                  = Integer.parseInt(text.split(next)[0].replaceAll("[^0-9]", ""));
                    registrationHandling    = getRegistration(id);
                    if (registrationHandling != null) {
                        registrationHandling.setCome(true);
                        switch (registrationType) {
                            case 0:
                                factory.getRegistrationHandlingDao().updateCourse(registrationHandling);
                                break;
                            case 1:
                                factory.getRegistrationHandlingDao().updateService(registrationHandling);
                                break;
                            case 2:
                                factory.getRegistrationHandlingDao().updateTraining(registrationHandling);
                                break;
                            case 3:
                                factory.getRegistrationHandlingDao().updateConsultation(registrationHandling);
                                break;
                        }
                        sendMessage(Const.DONE_JOIN_MESSAGE);
                        return EXIT;
                    }
                }
                dateKeyboard                = new DateKeyboard();
                sendStartDate();
                waitingType                 = WaitingType.START_DATE;
                return COMEBACK;
            case START_DATE:
                deleteMessage(updateMessageId);
                if (hasCallbackQuery()) {
                    if (dateKeyboard.isNext(updateMessageText)) {
                        sendStartDate();
                    } else {
                        start               = dateKeyboard.getDateDate(updateMessageText);
                        start.setHours(0);
                        start.setMinutes(0);
                        start.setSeconds(0);
                        sendEndDate();
                        waitingType         = WaitingType.END_DATE;
                    }
                }
                return COMEBACK;
            case END_DATE:
                deleteMessage(updateMessageId);
                if (hasCallbackQuery()) {
                    if (dateKeyboard.isNext(updateMessageText)) {
                        sendStartDate();
                    } else {
                        end                 = dateKeyboard.getDateDate(updateMessageText);
                        end.setHours(23);
                        end.setMinutes(59);
                        end.setSeconds(59);
                        getRegistrationList();
                        if (registrationHandlings == null) {
                            sendMessage(Const.SERVICE_LIST_EMPTY_MESSAGE);
                            return EXIT;
                        } else {
                            registrationHandlings.forEach(registration -> userNameList.add(userDao.getUserByChatId(registration.getChatId()).getFullName() + " | " + DateUtil.getDayDate(registration.getRegistrationDate())));
                            buttonsLeaf     = new ButtonsLeaf(userNameList,6, prevButton, nextButton);
                            toDeleteKeyboard(sendMessageWithKeyboard(Const.SERVICE_LIST_MESSAGE, buttonsLeaf.getListButton()));
                            waitingType     = WaitingType.SET_SERVICE;
                        }
                    }
                }
                return COMEBACK;
            case SET_SERVICE:
                deleteMessage(updateMessageId);
                if (hasCallbackQuery()) {
                    registrationHandling    = registrationHandlings.get(Integer.parseInt(updateMessageText));
                    StringBuilder message   = new StringBuilder();
                    message.append("<b>Номер регистрации : </b>" + registrationHandling.getId()).append(next);
                    message.append("<b>Фамилия Имя Отчество : </b>").append(userDao.getUserByChatId(registrationHandling.getChatId()).getFullName()).append(next);
                    message.append("<b>ИИН : </b>").append(registrationHandling.getIin()).append(next);
                    message.append("<b>Дата и время регистрации : </b>").append(registrationHandling.getRegistrationDate()).append(next);
                    message.append("<b>Статус : </b>").append(registrationHandling.isCome() ? "<i>Пришел</i>" : "<i>Не пришел</i>");
                    if (registrationHandling.isCome()) {
                        toDeleteKeyboard(sendMessageWithKeyboard(message.toString(),27));
                    } else {
                        toDeleteKeyboard(sendMessageWithKeyboard(message.toString(),26));
                    }
                    waitingType             = WaitingType.CHOOSE_OPTION;
                }
                return COMEBACK;
            case CHOOSE_OPTION:
                deleteMessage(updateMessageId);
                if (isButton(Const.BACK_BUTTON)) {
                    toDeleteKeyboard(sendMessageWithKeyboard(Const.SERVICE_LIST_MESSAGE, buttonsLeaf.getListButton()));
                    waitingType             = WaitingType.SET_SERVICE;
                }
                return COMEBACK;
        }
        return EXIT;
    }

    private int                     sendStartDate() throws TelegramApiException { return toDeleteKeyboard(sendMessageWithKeyboard(Const.CHOOSE_DATE_MESSAGE, dateKeyboard.getCalendarKeyboard())); }

    private int                     sendEndDate()   throws TelegramApiException { return toDeleteKeyboard(sendMessageWithKeyboard(Const.SELECT_END_DATE_MESSAGE, dateKeyboard.getCalendarKeyboard())); }

    private void                    getRegistrationList() {
        if (handlingDao.isCourseTeacher(chatId)) {
            registrationHandlings = factory.getRegistrationHandlingDao(). getAllCoursesTeacherByTime(start, end,  handlingDao.getCourseByChatId(chatId).getId());
        } else if (handlingDao.isServiceTeacher(chatId)) {
            registrationHandlings = factory.getRegistrationHandlingDao().getAllServicesTeacherByTime(start, end,  handlingDao.getServiceByChatId(chatId).getId());
        } else if (handlingDao.isTrainingTeacher(chatId)) {
            registrationHandlings = factory.getRegistrationHandlingDao().getAllTrainingsTeacherByTime(start, end, handlingDao.getTrainingByChatId(chatId).getId());
        } else if (handlingDao.isConsultationTeacher(chatId)) {
            registrationHandlings = factory.getRegistrationHandlingDao().getAllConsultationsTeacherByTime(start, end, handlingDao.getConsultationByChatId(chatId).getId());
        } else {
            registrationHandlings = null;
        }
    }

    private RegistrationHandling    getRegistration(int id) {
        RegistrationHandling registrationHandling;
        if (handlingDao.isCourseTeacher(chatId)) {
            registrationType        = 0;
            registrationHandling    = factory.getRegistrationHandlingDao().getCourseById(id);
        } else if (handlingDao.isServiceTeacher(chatId)) {
            registrationType        = 1;
            registrationHandling    = factory.getRegistrationHandlingDao().getServiceById(id);
        } else if (handlingDao.isTrainingTeacher(chatId)) {
            registrationType        = 2;
            registrationHandling    = factory.getRegistrationHandlingDao().getTrainingById(id);
        } else if (handlingDao.isConsultationTeacher(chatId)) {
            registrationType        = 3;
            registrationHandling    = factory.getRegistrationHandlingDao().getConsultationById(id);
        } else {
            registrationHandling = null;
        }
        return registrationHandling;
    }
}
