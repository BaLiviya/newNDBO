package baliviya.com.github.NDBO.command.impl;

import baliviya.com.github.NDBO.command.Command;
import baliviya.com.github.NDBO.entity.custom.*;
import baliviya.com.github.NDBO.entity.enums.WaitingType;
import baliviya.com.github.NDBO.entity.standart.User;
import baliviya.com.github.NDBO.utils.ButtonsLeaf;
import baliviya.com.github.NDBO.utils.Const;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class id027_AddHandling extends Command {

    private List<String>        list;
    private List<ServiceType>   serviceTypes;
    private List<CoursesType>   coursesTypes;
    private List<CoursesName>   coursesNames;
    private List<HandlingName>  trainingNames;
    private List<HandlingName>  businessNames;
    private List<CoursesType>   consultationTypes;
    private List<CoursesName>   consultationNames;
    private List<User>          users;
    private ButtonsLeaf         buttonsLeaf;
    private Handling            handling;
    private List<Handling>      handlingList;
    private int                 handlingId;
    private int                 serviceTypeId;
    private int                 courseTypeId;
    private int                 deleteMessageId;
    private int                 secondDeleteMessageId;
    private int                 consultationTypeId;
    private Handling            activeHandling;
    private boolean             isEdit = false;

    @Override
    public boolean  execute()                                   throws TelegramApiException {
        if (!isAdmin() && !isMainAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        switch (waitingType) {
            case START:
                deleteMessage(updateMessageId);
                list                        = new ArrayList<>();
                Arrays.asList(getText(Const.HANDLING_TYPE_MESSAGE).split(Const.SPLIT)).forEach(handling -> list.add(handling));
                buttonsLeaf                 = new ButtonsLeaf(list);
                toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.CHOOSE_HANDLING_TYPE_MESSAGE), buttonsLeaf.getListButton()));
                waitingType                 = WaitingType.SET_HANDLING;
                return COMEBACK;
            case SET_HANDLING:
                deleteMessage(updateMessageId);
                if (hasCallbackQuery()) {
                    handlingId              = Integer.parseInt(updateMessageText);
                    switch (handlingId) {
                        case 0:
                            getServiceType();
                            waitingType     = WaitingType.SET_SERVICE_TYPE;
                            break;
                        case 1:
                            getCoursesType();
                            waitingType     = WaitingType.SET_COURSES_TYPE;
                            break;
                        case 2:
                            getTrainingName();
                            waitingType     = WaitingType.SET_TRAINING_NAME;
                            break;
                        case 3:
                            getConsultationTypes();
                            waitingType     = WaitingType.SET_CONSULTATION_TYPE;
                            break;
                        default:
                            return COMEBACK;
                    }
                }
                return COMEBACK;
            case SET_SERVICE_TYPE:
                delete();
                if (hasCallbackQuery()) {
                    handling                = new Handling();
                    serviceTypeId           = serviceTypes.get(Integer.parseInt(updateMessageText)).getId();
                    handling.setHandlingTypeId(serviceTypeId);
                    secondDeleteMessageId   = sendButtonAdd();
                    deleteMessageId         = getAllService(serviceTypeId);
                    waitingType             = WaitingType.SERVICE_TYPE_OPTION;
                }
                return COMEBACK;
            case SET_COURSES_TYPE:
                delete();
                if (hasCallbackQuery()) {
                    courseTypeId            = coursesTypes.get(Integer.parseInt(updateMessageText)).getId();
                    getCourseName(courseTypeId);
                    waitingType             = WaitingType.SET_COURSES_NAME;
                }
                return COMEBACK;
            case SET_COURSES_NAME:
                delete();
                if (hasCallbackQuery()) {
                    handling                = new Handling();
                    handling.setHandlingTypeId(coursesNames.get(Integer.parseInt(updateMessageText)).getId());
                    secondDeleteMessageId   = sendButtonAdd();
                    deleteMessageId         = getAllCourse(coursesNames.get(Integer.parseInt(updateMessageText)).getId());
                    waitingType             = WaitingType.SERVICE_TYPE_OPTION;
                }
                return COMEBACK;
            case SET_TRAINING_NAME:
                delete();
                handling                    = new Handling();
                handling.setHandlingTypeId(trainingNames.get(Integer.parseInt(updateMessageText)).getId());
                secondDeleteMessageId       = sendButtonAdd();
                deleteMessageId             = getAllTraining(trainingNames.get(Integer.parseInt(updateMessageText)).getId());
                waitingType                 = WaitingType.SERVICE_TYPE_OPTION;
                return COMEBACK;
            case SET_BUSINESS_NAME:
                delete();
                handling                    = new Handling();
                handling.setHandlingTypeId(businessNames.get(Integer.parseInt(updateMessageText)).getId());
                secondDeleteMessageId       = sendButtonAdd();
                deleteMessageId             = getAllBusiness(businessNames.get(Integer.parseInt(updateMessageText)).getId());
                waitingType                 = WaitingType.SERVICE_TYPE_OPTION;
                return COMEBACK;
            case SET_CONSULTATION_TYPE:
                delete();
                if (hasCallbackQuery()) {
                    consultationTypeId      = consultationTypes.get(Integer.parseInt(updateMessageText)).getId();
                    getConsultationName(consultationTypeId);
                    waitingType             = WaitingType.SET_CONSULTATION_NAME;
                }
                return COMEBACK;
            case SET_CONSULTATION_NAME:
                delete();
                if (hasCallbackQuery()) {
                    handling                = new Handling();
                    handling.setHandlingTypeId(consultationNames.get(Integer.parseInt(updateMessageText)).getId());
                    secondDeleteMessageId   = sendButtonAdd();
                    deleteMessageId         = getAllConsultation(consultationNames.get(Integer.parseInt(updateMessageText)).getId());
                    waitingType             = WaitingType.SERVICE_TYPE_OPTION;
                }
                return COMEBACK;
            case SERVICE_TYPE_OPTION:
                delete();
                if (isButton(Const.ADD_HANDLING_BUTTON)) {
                    deleteMessageId         = getFullName();
                    waitingType             = WaitingType.SET_FULL_NAME;
                }
                if (hasCallbackQuery()) {
                    activeHandling          = handlingList.get(Integer.parseInt(updateMessageText));
                    sendListService();
                    waitingType             = WaitingType.EDITION_CHOICE;
                }
                return COMEBACK;
            case EDITION_CHOICE:
                delete();
                if (hasMessageText()) {
                    switch (updateMessageText) {
                        case "/back":
                            secondDeleteMessageId = sendButtonAdd();
                            switch (handlingId) {
                                case 0:
                                    toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.SERVICE_TYPE_MESSAGE), buttonsLeaf.getListButton()));
                                    break;
                                case 1:
                                    toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.COURSE_TYPE_MESSAGE), buttonsLeaf.getListButton()));
                                    break;
                                case 2:
                                    toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.TRAINING_MESSAGE), buttonsLeaf.getListButton()));
                                    break;
                                case 3:
                                    toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.CONSULTATION_TYPE_MESSAGE), buttonsLeaf.getListButton()));
                                    break;
                            }
                            waitingType     = WaitingType.SERVICE_TYPE_OPTION;
                            break;
                        case "/full":
                            isEdit          = true;
                            deleteMessageId = getFullName();
                            waitingType     = WaitingType.SET_FULL_NAME;
                            break;
                        case "/text":
                            isEdit          = true;
                            deleteMessageId = getHandlingText();
                            waitingType     = WaitingType.SET_TEXT;
                            break;
                        case "/photo":
                            isEdit          = true;
                            deleteMessageId = getPhoto();
                            waitingType     = WaitingType.SET_PHOTO;
                            break;
                        case "/del":
                            deleteHandling();
                            break;
                        case "/spec":
                            isEdit          = true;
                            deleteMessageId = getEmployeeContact();
                            waitingType     = WaitingType.HANDLING_EMPLOYEE;
                            break;
                        default:
                            return COMEBACK;
                    }
                }
                return COMEBACK;
            case SET_FULL_NAME:
                delete();
                if (hasMessageText()) {
                    if (isEdit) {
                        activeHandling.setFullName(updateMessageText);
                        update();
                    } else {
                        handling.setFullName(updateMessageText);
                        deleteMessageId = getHandlingText();
                        waitingType     = WaitingType.SET_TEXT;
                    }
                }
                return COMEBACK;
            case SET_TEXT:
                delete();
                if (hasMessageText()) {
                    if (isEdit) {
                        activeHandling.setText(updateMessageText);
                        update();
                    } else {
                        handling.setText(updateMessageText);
                        deleteMessageId = getPhoto();
                        waitingType     = WaitingType.SET_PHOTO;
                    }
                }
                return COMEBACK;
            case SET_PHOTO:
                delete();
                if (hasPhoto()) {
                    if (isEdit) {
                        activeHandling.setPhoto(updateMessagePhoto);
                        update();
                    } else {
                        handling.setPhoto(updateMessagePhoto);
                        users       = userDao.getAll();
                        list.clear();
                        users.forEach(user -> list.add(user.getFullName()));
                        buttonsLeaf = new ButtonsLeaf(list,6, prevButton, nextButton);
                        toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.SELECT_EMPLOYEE_FROM_HANDLING_MESSAGE), buttonsLeaf.getListButton()));
                        waitingType = WaitingType.HANDLING_EMPLOYEE;
                    }
                }
                return COMEBACK;
            case HANDLING_EMPLOYEE:
                delete();
                  if (hasCallbackQuery()) {
                      if (isEdit) {
                          activeHandling.setHandlingTeacherId(users.get(Integer.parseInt(updateMessageText)).getChatId());
                          update();
                      } else {
                          handling.setHandlingTeacherId(users.get(Integer.parseInt(updateMessageText)).getChatId());
                          switch (handlingId) {
                              case 0:
                                  handlingDao.insertService(handling);
                                  break;
                              case 1:
                                  handlingDao.insertCourse(handling);
                                  break;
                              case 2:
                                  handlingDao.insertTraining(handling);
                                  break;
                              case 3:
                                  handlingDao.insertConsultation(handling);
                                  break;
                          }
                          sendMessage(Const.DONE_MESSAGE);
                          return EXIT;
                      }
                }
                return COMEBACK;
        }
        return EXIT;
    }

    private int     getServiceType()                            throws TelegramApiException {
        list.clear();
        serviceTypes    = factory.getServiceTypeDao().getAll();
        serviceTypes    .forEach(serviceType -> list.add(serviceType.getName()));
        buttonsLeaf     = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.SERVICE_TYPE_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getAllService(int serviceTypeId)            throws TelegramApiException {
        list.clear();
        handlingList    = handlingDao.getAllService(serviceTypeId);
        handlingList    .forEach(service -> list.add(service.getFullName()));
        buttonsLeaf     = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.SERVICE_TYPE_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getAllCourse(int courseNameId)              throws TelegramApiException {
        list.clear();
        handlingList    = handlingDao.getAllCourse(courseNameId);
        handlingList    .forEach(service -> list.add(service.getFullName()));
        buttonsLeaf     = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.COURSE_TYPE_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getAllTraining(int trainingNameId)          throws TelegramApiException {
        list.clear();
        handlingList    = handlingDao.getAllTraining(trainingNameId);
        handlingList    .forEach(training -> list.add(training.getFullName()));
        buttonsLeaf     = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.TRAINING_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getAllBusiness(int businessNameId)          throws TelegramApiException {
        list.clear();
        handlingList    = handlingDao.getAllBusiness(businessNameId);
        handlingList    .forEach(training -> list.add(training.getFullName()));
        buttonsLeaf     = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.BUSINESS_TRAINING_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getAllConsultation(int consultationNameId)  throws TelegramApiException {
        list.clear();
        handlingList    = handlingDao.getAllConsultation(consultationNameId);
        handlingList    .forEach(training -> list.add(training.getFullName()));
        buttonsLeaf     = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.CONSULTATION_TYPE_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getCoursesType()                            throws TelegramApiException {
        list.clear();
        coursesTypes    = factory.getCoursesTypeDao().getAll();
        coursesTypes    .forEach(coursesType -> list.add(coursesType.getName()));
        buttonsLeaf     = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.COURSE_TYPE_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getCourseName(int courseTypeId)             throws TelegramApiException {
        list.clear();
        coursesNames = factory.getCoursesNameDao().getAll(courseTypeId);
        coursesNames .forEach(coursesName -> list.add(coursesName.getName()));
        buttonsLeaf  = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.COURSE_TYPE_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getTrainingName()                           throws TelegramApiException {
        list.clear();
        trainingNames   = factory.getHandlingNameDao().getAllTraining();
        trainingNames   .forEach(trainingName -> list.add(trainingName.getName()));
        buttonsLeaf     = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.TRAINING_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getBusinessName()                           throws TelegramApiException {
        list.clear();
        businessNames   = factory.getHandlingNameDao().getAllBusiness();
        businessNames   .forEach(businessName -> list.add(businessName.getName()));
        buttonsLeaf     = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.SUPPORT_BUSINESS_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getConsultationTypes()                      throws TelegramApiException {
        list.clear();
        consultationTypes   = factory.getCoursesTypeDao().getAllConsultation();
        consultationTypes   .forEach(consultationType -> list.add(consultationType.getName()));
        buttonsLeaf         = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.CONSULTATION_TYPE_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getConsultationName(int consultationId)     throws TelegramApiException {
        list.clear();
        consultationNames   = factory.getCoursesNameDao().getAllConsultation(consultationId);
        consultationNames   .forEach(consultationName -> list.add(consultationName.getName()));
        buttonsLeaf         = new ButtonsLeaf(list);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.CONSULTATION_TYPE_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     getFullName()                               throws TelegramApiException { return botUtils.sendMessage(Const.FULL_NAME_HANDLING_SERVICE_MESSAGE, chatId); }

    private int     getHandlingText()                           throws TelegramApiException { return botUtils.sendMessage(Const.HANDLING_INFO_MESSAGE, chatId); }

    private int     getPhoto()                                  throws TelegramApiException { return botUtils.sendMessage(Const.SEND_PHOTO_HANDLING_TEACHER, chatId); }

    private int     getEmployeeContact()                        throws TelegramApiException {
        list.clear();
        users       = userDao.getAll();
        users.forEach(user -> list.add(user.getFullName()));
        buttonsLeaf = new ButtonsLeaf(list,6, prevButton, nextButton);
        return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.SEND_USER_FROM_SPEC_MESSAGE), buttonsLeaf.getListButton()));
    }

    private int     sendButtonAdd()                             throws TelegramApiException { return botUtils.sendMessage(Const.ADD_BUTTON_MESSAGE, chatId); }

    private void    sendListService()                           throws TelegramApiException {
        String format   = getText(Const.EDIT_SERVICE_MESSAGE);
        deleteMessageId = sendMessage(String.format(format, "/full", activeHandling.getFullName(), "/text", activeHandling.getText(), "/photo", "/spec" ,"/del", "/back")); // TODO: 11.07.2020 проверить
    }

    private void    update()                                    throws TelegramApiException {
        switch (handlingId) {
            case 0:
                handlingDao.updateService(activeHandling);
                break;
            case 1:
                handlingDao.updateCourse(activeHandling);
                break;
            case 2:
                handlingDao.updateTraining(activeHandling);
                break;
            case 3:
//                handlingDao.updateBusiness(activeHandling);
//                break;
//            case 4:
                handlingDao.updateConsultation(activeHandling);
                break;
        }
        sendListService();
        waitingType     = WaitingType.EDITION_CHOICE;
    }

    private void    deleteHandling()                            throws TelegramApiException {
        switch (handlingId) {
            case 0:
                handlingDao.deleteService(activeHandling.getId());
                getServiceType();
                waitingType = WaitingType.SET_SERVICE_TYPE;
                break;
            case 1:
                handlingDao.deleteCourse(activeHandling.getId());
                getCoursesType();
                waitingType = WaitingType.SET_COURSES_TYPE;
                break;
            case 2:
                handlingDao.deleteTraining(activeHandling.getId());
                getTrainingName();
                waitingType = WaitingType.SET_TRAINING_NAME;
                break;
            case 3:
                handlingDao.deleteBusiness(activeHandling.getId());
                getBusinessName();
                waitingType = WaitingType.SET_BUSINESS_NAME;
                break;
            case 4:
                handlingDao.deleteConsultation(activeHandling.getId());
                getConsultationTypes();
                waitingType = WaitingType.SET_CONSULTATION_TYPE;
                break;
        }
    }

    private boolean isCommand(String command) { return updateMessageText.startsWith(command); }

    private void    delete() {
        deleteMessage(updateMessageId);
        deleteMessage(deleteMessageId);
        deleteMessage(secondDeleteMessageId);
    }
}
