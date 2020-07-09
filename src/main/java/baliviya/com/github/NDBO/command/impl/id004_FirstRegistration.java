package baliviya.com.github.NDBO.command.impl;

import baliviya.com.github.NDBO.command.Command;
import baliviya.com.github.NDBO.entity.custom.Recipient;
import baliviya.com.github.NDBO.entity.enums.WaitingType;
import baliviya.com.github.NDBO.entity.standart.User;
import baliviya.com.github.NDBO.utils.ButtonsLeaf;
import baliviya.com.github.NDBO.utils.Const;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class id004_FirstRegistration extends Command {

    private Recipient         recipient;
    private int               deleteMessageId;
    private int               secondDeleteMessageId;
    private ButtonsLeaf       buttonsLeaf;
    private ArrayList<String> list               = new ArrayList<>();;
    private ArrayList<String> socialBenefitsList = new ArrayList<>();
    private User              user;
    private boolean           isUpdate           = false;

    @Override
    public boolean  execute()                       throws TelegramApiException {
        switch (waitingType) {
            case START:
                deleteMessage(updateMessageId);
                user          = userDao.getUserByChatId(chatId);
                if (!isRecipient()) {
                    recipient = new Recipient();
                    recipient.setChatId(chatId);
                    recipient.setRegistrationDate(new Date());
                    recipient.setFullName   (user.getFullName());
                    recipient.setPhoneNumber(user.getPhone());
                    recipient.setIin        (user.getIin());
                    recipient.setStatus     (user.getStatus());
                } else {
                    recipient   = recipientDao.getRecipientByChatId(chatId);
                    isUpdate    = true;
                }
                deleteMessageId = getAddress();
                waitingType     = WaitingType.SET_ADDRESS;
                return COMEBACK;
            case SET_ADDRESS:
                delete();
                if (hasCallbackQuery()) {
                } else if (hasMessageText()) {
                    recipient.setAddress(updateMessageText);
                    if (isUpdate) recipientDao.update(recipient);
                } else {
                    secondDeleteMessageId   = wrongData();
                    deleteMessageId         = getAddress();
                    return COMEBACK;
                }
                deleteMessageId             = getVisa();
                waitingType                 = WaitingType.SET_VISA;
                return COMEBACK;
            case SET_VISA:
                delete();
                if (hasCallbackQuery()) {
                    if (!list.get(Integer.parseInt(updateMessageText)).equals(getText(Const.SKIP_MESSAGE))) {
                        recipient.setVisa(list.get(Integer.parseInt(updateMessageText)));
                        if (isUpdate) recipientDao.update(recipient);
                    }
                    deleteMessageId         = getApartment();
                    waitingType             = WaitingType.SET_APARTMENT;
                } else {
                    secondDeleteMessageId   = wrongData();
                    deleteMessageId         = getVisa();
                }
                return COMEBACK;
            case SET_APARTMENT:
                delete();
                if (hasCallbackQuery()) {
                    if (!list.get(Integer.parseInt(updateMessageText)).equals(getText(Const.SKIP_MESSAGE))) {
                        if (list.get(Integer.parseInt(updateMessageText)).equals(getText(Const.OTHERS_MESSAGE))) {
                            deleteMessageId  = getOther();
                            waitingType      = WaitingType.OTHER_APARTMENT;
                            return COMEBACK;
                        } else {
                            recipient.setApartment(list.get(Integer.parseInt(updateMessageText)));
                            if (isUpdate) recipientDao.update(recipient);
                        }
                    }
                    deleteMessageId         = getChildren();
                    waitingType             = WaitingType.ACTION_MENU;
                } else {
                    secondDeleteMessageId   = wrongData();
                    deleteMessageId         = getApartment();
                }
                return COMEBACK;
            case OTHER_APARTMENT:
                delete();
                if (hasMessageText()) {
                    recipient.setApartment(updateMessageText);
                    if (isUpdate) recipientDao.update(recipient);
                    deleteMessageId         = getChildren();
                    waitingType             = WaitingType.ACTION_MENU;
                } else {
                    secondDeleteMessageId   = wrongData();
                    deleteMessageId         = getOther();
                }
                return COMEBACK;
            case ACTION_MENU:
                delete();
                if (isButton(Const.NEXT_BUTTON)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (isUpdate) {
                        if (recipient.getChildren() != null) socialBenefitsList.add(0, recipient.getChildren());
                    }
                    for (String status : socialBenefitsList) {
                        stringBuilder.append(status).append(Const.SPLIT).append(space);
                    }
                    if (!stringBuilder.toString().equals("")) recipient.setChildren(stringBuilder.toString().substring(0, stringBuilder.length() - 2));
                    if (isUpdate) recipientDao.update(recipient);
                    deleteMessageId         = getSocialBenefits();
                    waitingType             = WaitingType.SOCIAL_BENEFITS;
                    socialBenefitsList.clear();
                } else if (isButton(Const.ADD_KIDS_BUTTON)) {
                    deleteMessageId         = getChildrenIin();
                    waitingType             = WaitingType.SET_CHILDREN;
                } else {
                    secondDeleteMessageId   = wrongData();
                    deleteMessageId         = getChildren();
                }
                return COMEBACK;
            case SET_CHILDREN:
                delete();
                if (hasMessageText()) {
                    socialBenefitsList.add(updateMessageText);
                    deleteMessageId         = getChildren();
                    waitingType             = WaitingType.ACTION_MENU;
                } else {
                    secondDeleteMessageId   = wrongData();
                    deleteMessageId         = getChildrenIin();
                }
                return COMEBACK;
            case SOCIAL_BENEFITS:
                delete();
                if (hasCallbackQuery()) {
                    if (!list.get(Integer.parseInt(updateMessageText)).equals(getText(Const.SKIP_MESSAGE))) {
                        if (list.get(Integer.parseInt(updateMessageText)).equals(getText(Const.NEXT_MESSAGE))) {
                            StringBuilder stringBuilder = new StringBuilder();
                            if (isUpdate) {
                                if (recipient.getSocialBenefits() != null) socialBenefitsList.add(0, recipient.getSocialBenefits());
                            }
                            for (String socialBenefits : socialBenefitsList) {
                                stringBuilder.append(socialBenefits).append(Const.SPLIT).append(space);
                            }
                            if (!stringBuilder.toString().equals("")) recipient.setSocialBenefits(stringBuilder.toString().substring(0, stringBuilder.length() - 2));
                            if (isUpdate) recipientDao.update(recipient);
                            deleteMessageId     = getMaritalStatus();
                            waitingType         = WaitingType.SET_MARITAL_STATUS;
                            socialBenefitsList.clear();
                        } else if (list.get(Integer.parseInt(updateMessageText)).equals(getText(Const.OTHERS_MESSAGE))) {
                            deleteMessageId     = getOther();
                            waitingType         = WaitingType.OTHER_SOCIAL_BENEFITS;
                            return COMEBACK;
                        } else {
                            socialBenefitsList.add(list.get(Integer.parseInt(updateMessageText)));
                            deleteMessageId     = getSocialBenefits();
                            waitingType         = WaitingType.SOCIAL_BENEFITS;
                        }
                    } else {
                        deleteMessageId         = getMaritalStatus();
                        waitingType             = WaitingType.SET_MARITAL_STATUS;
                    }
                } else {
                    secondDeleteMessageId       = wrongData();
                    deleteMessageId             = getSocialBenefits();
                }
                return COMEBACK;
            case OTHER_SOCIAL_BENEFITS:
                delete();
                if (hasMessageText()) {
                    socialBenefitsList.add(updateMessageText);
                    deleteMessageId             = getSocialBenefits();
                    waitingType                 = WaitingType.SOCIAL_BENEFITS;
                } else {
                    secondDeleteMessageId       = wrongData();
                    deleteMessageId             = getOther();
                }
                return COMEBACK;
//            case SET_STATUS:
//                delete();
//                if (hasCallbackQuery()) {
//                    if (list.get(Integer.parseInt(updateMessageText)).equals(getText(Const.NEXT_MESSAGE))) {
//                        StringBuilder stringBuilder = new StringBuilder();
//                        for (String status : socialBenefitsList) {
//                            stringBuilder.append(status).append(";").append(space);
//                        }
//                        recipient.setStatus(stringBuilder.toString());
//                        deleteMessageId = getMaritalStatus();
//                        waitingType     = WaitingType.SET_MARITAL_STATUS;
//                        socialBenefitsList.clear();
//                    } else if (list.get(Integer.parseInt(updateMessageText)).equals(getText(Const.OTHERS_MESSAGE))) {
//                        deleteMessageId = getOther();
//                        waitingType     = WaitingType.OTHER_STATUS;
//                        return COMEBACK;
//                    } else {
//                        socialBenefitsList.add(list.get(Integer.parseInt(updateMessageText)));
//                        deleteMessageId = getStatus();
//                        waitingType     = WaitingType.SET_STATUS;
//                    }
//                } else {
//                    secondDeleteMessageId = wrongData();
//                    deleteMessageId       = getStatus();
//                }
//                return COMEBACK;
//            case OTHER_STATUS:
//                delete();
//                if (hasMessageText()) {
//                    socialBenefitsList.add(updateMessageText);
//                    deleteMessageId = getStatus();
//                    waitingType     = WaitingType.SET_STATUS;
//                } else {
//                    secondDeleteMessageId = wrongData();
//                    deleteMessageId       = getOther();
//                }
//                return COMEBACK;
            case SET_MARITAL_STATUS:
                delete();
                if (hasCallbackQuery()) {
                    if (!list.get(Integer.parseInt(updateMessageText)).equals(getText(Const.SKIP_MESSAGE))) {
                        recipient.setMaritalStatus(list.get(Integer.parseInt(updateMessageText)));
                        if (isUpdate) recipientDao.update(recipient);
                        if (    !list.get(Integer.parseInt(updateMessageText)).equals("Замужем/Женат") &&
                                !list.get(Integer.parseInt(updateMessageText)).equals("Сожительство")  &&
                                !list.get(Integer.parseInt(updateMessageText)).equals("Не замужем/не женат")) {
                            deleteMessageId     = getAliments();
                            waitingType         = WaitingType.SET_ALIMENTS;
                        } else {
                            recipient.setAliments("Не получаю");
                            if (isUpdate) recipientDao.update(recipient);
                            deleteMessageId     = getEmploymentType();
                            waitingType         = WaitingType.EMPLOYMENT_TYPE;
                        }
                    } else {
                        deleteMessageId         = getEmploymentType();
                        waitingType             = WaitingType.EMPLOYMENT_TYPE;
                    }
                } else {
                    secondDeleteMessageId       = wrongData();
                    deleteMessageId             = getMaritalStatus();
                }
                return COMEBACK;
            case SET_ALIMENTS:
                delete();
                if (hasCallbackQuery()) {
                    if (!list.get(Integer.parseInt(updateMessageText)).equals(getText(Const.SKIP_MESSAGE))) {
                        recipient.setAliments(list.get(Integer.parseInt(updateMessageText)));
                        if (isUpdate) recipientDao.update(recipient);
                    }
                    deleteMessageId             = getEmploymentType();
                    waitingType                 = WaitingType.EMPLOYMENT_TYPE;
                } else {
                    secondDeleteMessageId       = wrongData();
                    deleteMessageId             = getAliments();
                }
                return COMEBACK;
            case EMPLOYMENT_TYPE:
                delete();
                if (hasCallbackQuery()) {
                    if (!list.get(Integer.parseInt(updateMessageText)).equals(getText(Const.SKIP_MESSAGE))) {
                        if (list.get(Integer.parseInt(updateMessageText)).equals("Работаю")) {
                            recipient.setEmploymentType(list.get(Integer.parseInt(updateMessageText)));
                            deleteMessageId     = getEmployment();
                            waitingType         = WaitingType.EMPLOYMENT;
                        } else if (list.get(Integer.parseInt(updateMessageText)).equals(getText(Const.OTHERS_MESSAGE))) {
                            deleteMessageId     = getOther();
                            waitingType         = WaitingType.OTHER_EMPLOYMENT_TYPE;
                        } else {
                            recipient.setEmploymentType(list.get(Integer.parseInt(updateMessageText)));
                            if (isUpdate) recipientDao.update(recipient);
                            deleteMessageId     = getEducation();
                            waitingType         = WaitingType.EDUCATION;
                        }
                    } else {
                        deleteMessageId         = getEducation();
                        waitingType             = WaitingType.EDUCATION;
                    }
                } else {
                    secondDeleteMessageId       = wrongData();
                    deleteMessageId             = getEmploymentType();
                }
                return COMEBACK;
            case EMPLOYMENT:
                delete();
                if (hasMessageText()) {
                    recipient.setEmployment(updateMessageText);
                    if (isUpdate) recipientDao.update(recipient);
                    deleteMessageId         = getEducation();
                    waitingType             = WaitingType.EDUCATION;
                } else {
                    secondDeleteMessageId   = wrongData();
                    deleteMessageId         = getEmployment();
                }
                return COMEBACK;
            case OTHER_EMPLOYMENT_TYPE:
                delete();
                if (hasMessageText()) {
                    recipient.setEmploymentType(updateMessageText);
                    if (isUpdate) recipientDao.update(recipient);
                    deleteMessageId         = getEducation();
                    waitingType             = WaitingType.EDUCATION;
                } else {
                    secondDeleteMessageId   = wrongData();
                    deleteMessageId         = getOther();
                }
                return COMEBACK;
            case EDUCATION:
                delete();
                if (hasCallbackQuery()) {
                    if (!list.get(Integer.parseInt(updateMessageText)).equals(getText(Const.SKIP_MESSAGE))) {
                        if (list.get(Integer.parseInt(updateMessageText)).equals("Школа")) {
                            recipient.setEducation(list.get(Integer.parseInt(updateMessageText)));
                            if (isUpdate) recipientDao.update(recipient);
                            deleteMessageId     = getDisabilityType();
                            waitingType         = WaitingType.DISABILITY_TYPE;
                        } else {
                            recipient.setEducation(list.get(Integer.parseInt(updateMessageText)));
                            deleteMessageId     = getNameOrSpeciality();
                            waitingType         = WaitingType.SET_EDUCATION_NAME;
                        }
                    } else {
                        deleteMessageId         = getDisabilityType();
                        waitingType             = WaitingType.DISABILITY_TYPE;
                    }
                } else {
                    secondDeleteMessageId       = wrongData();
                    deleteMessageId             = getEducation();
                }
                return COMEBACK;
            case SET_EDUCATION_NAME:
                delete();
                if (hasMessageText()) {
                    recipient.setEducationName(updateMessageText);
                    if (isUpdate) recipientDao.update(recipient);
                    deleteMessageId             = getDisabilityType();
                    waitingType                 = WaitingType.DISABILITY_TYPE;
                } else {
                    secondDeleteMessageId       = wrongData();
                    deleteMessageId             = getNameOrSpeciality();
                }
                return COMEBACK;
            case DISABILITY_TYPE:
                delete();
                if (hasCallbackQuery()) {
                    if (!list.get(Integer.parseInt(updateMessageText)).equals(getText(Const.SKIP_MESSAGE))) {
                        if (list.get(Integer.parseInt(updateMessageText)).equals("Инвалидность мамы/папы")) {
                            recipient.setDisabilityType(list.get(Integer.parseInt(updateMessageText)));
                            if (isUpdate) recipientDao.update(recipient);
                            deleteMessageId     = sendMessage("ФИО, группа");
                            waitingType         = WaitingType.SET_MOTHER_FATHER_FULL_NAME;
                        } else if (list.get(Integer.parseInt(updateMessageText)).equals("Инвалидность ребенка")) {
                            recipient.setDisabilityType(list.get(Integer.parseInt(updateMessageText)));
                            if (isUpdate) recipientDao.update(recipient);
                            deleteMessageId     = sendMessage("ФИО, группа, диагноз");
                            waitingType         = WaitingType.SET_MOTHER_FATHER_FULL_NAME;
                        } else if (list.get(Integer.parseInt(updateMessageText)).equals("Инвалидность других членов семьи (бабушка, дедушка и т.д.)")) {
                            recipient.setDisabilityType(list.get(Integer.parseInt(updateMessageText)));
                            if (isUpdate) recipientDao.update(recipient);
                            deleteMessageId     = sendMessage("ФИО, год рождения");
                            waitingType         = WaitingType.SET_MOTHER_FATHER_FULL_NAME;
                        } else {
                            deleteMessageId     = getCreditHistory();
                            waitingType         = WaitingType.SET_CREDIT_HISTORY;
                        }
                    } else {
                        deleteMessageId         = getCreditHistory();
                        waitingType             = WaitingType.SET_CREDIT_HISTORY;
                    }
                } else {
                    secondDeleteMessageId       = wrongData();
                    deleteMessageId             = getDisabilityType();
                }
                return COMEBACK;
            case SET_MOTHER_FATHER_FULL_NAME:
                delete();
                if (hasMessageText()) {
                    recipient.setDisability(updateMessageText);
                    if (isUpdate) recipientDao.update(recipient);
                    deleteMessageId            = getCreditHistory();
                    waitingType                = WaitingType.SET_CREDIT_HISTORY;
                } else {
                    deleteMessageId            = sendMessage("ФИО, группа");
                }
                return COMEBACK;
            case SET_CREDIT_HISTORY:
                delete();
                if (hasCallbackQuery()) {
                    if (list.get(Integer.parseInt(updateMessageText)).equals("есть кредит")) {
                        recipient.setCreditHistory(list.get(Integer.parseInt(updateMessageText)));
                        deleteMessageId = getCreditInfo();
                        waitingType     = WaitingType.SET_CREDIT_INFO;
                    } else if (list.get(Integer.parseInt(updateMessageText)).equals("есть задолжность")) {
                        recipient.setCreditHistory(list.get(Integer.parseInt(updateMessageText)));
                        deleteMessageId = getBankName();
                        waitingType     = WaitingType.SET_CREDIT_INFO_OTHER;
                    } else if (list.get(Integer.parseInt(updateMessageText)).equals("коллекторы (какой банк)")) {
                        recipient.setCreditHistory(list.get(Integer.parseInt(updateMessageText)));
                        deleteMessageId = getBankName();
                        waitingType     = WaitingType.SET_CREDIT_INFO_OTHER;
                    } else if (list.get(Integer.parseInt(updateMessageText)).equals("Другое")) {
                        deleteMessageId = getOther();
                        waitingType     = WaitingType.SET_OTHER_CREDIT_HISTORY;
                    } else {
                        recipient.setCreditHistory(list.get(Integer.parseInt(updateMessageText)));
                        if (isUpdate) {
                            recipientDao.update(recipient);
                        } else { recipientDao.insert(recipient); }
                        sendMessage(Const.DONE_JOIN_MESSAGE);
                        return EXIT;
                    }
                } else {
                    secondDeleteMessageId   = wrongData();
                    deleteMessageId         = getCreditHistory();
                }
                return COMEBACK;
            case SET_OTHER_CREDIT_HISTORY:
                delete();
                if (hasMessageText()) {
                    recipient.setCreditHistory(updateMessageText);
                    if (isUpdate) {
                        recipientDao.update(recipient);
                    } else { recipientDao.insert(recipient); }
                    sendMessage(Const.DONE_JOIN_MESSAGE);
                    return EXIT;
                } else {
                    secondDeleteMessageId   = wrongData();
                    deleteMessageId         = getOther();
                }
                return COMEBACK;
            case SET_CREDIT_INFO:
                delete();
                if (hasMessageText()) {
                    recipient.setCreditInfo(updateMessageText);
                    if (isUpdate) {
                        recipientDao.update(recipient);
                    } else { recipientDao.insert(recipient); }
                    sendMessage(Const.DONE_JOIN_MESSAGE);
                    return EXIT;
                } else {
                    secondDeleteMessageId   = wrongData();
                    deleteMessageId         = getCreditInfo();
                }
                return COMEBACK;
            case SET_CREDIT_INFO_OTHER:
                delete();
                if (hasMessageText()) {
                    recipient.setCreditInfo(updateMessageText);
                    if (isUpdate) {
                        recipientDao.update(recipient);
                    } else { recipientDao.insert(recipient); }
                    sendMessage(Const.DONE_JOIN_MESSAGE);
                    return EXIT;
                } else {
                    secondDeleteMessageId   = wrongData();
                    deleteMessageId         = getBankName();
                }
                return COMEBACK;
        }
        return EXIT;
    }

    private int     wrongData()                     throws TelegramApiException { return botUtils.sendMessage(Const.WRONG_DATA_TEXT, chatId); }

    private int     getAddress()                    throws TelegramApiException {
        if (isUpdate) {
            list            = new ArrayList<>();
            list.add(getText(Const.SKIP_MESSAGE));
            buttonsLeaf     = new ButtonsLeaf(list);
            return toDeleteKeyboard(sendMessageWithKeyboard(getUpdateText(recipient.getAddress(), getText(Const.SET_ADDRESS_MESSAGE)), buttonsLeaf.getListButton()));
        } else {
            return botUtils.sendMessage(Const.SET_ADDRESS_MESSAGE, chatId);
        }
    }

    private int     getVisa()                       throws TelegramApiException {
        list.clear();
        Arrays.asList(getText(Const.REGISTRATION_TYPE_MESSAGE).split(Const.SPLIT)).forEach((e) -> list.add(e));
        if (isUpdate) list.add(getText(Const.SKIP_MESSAGE));
        buttonsLeaf = new ButtonsLeaf(list);
        if (isUpdate) {
            return toDeleteKeyboard(sendMessageWithKeyboard(getUpdateText(recipient.getVisa(), getText(Const.TYPE_OF_REGISTRATION)), buttonsLeaf.getListButton()));
        } else {
            return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.TYPE_OF_REGISTRATION), buttonsLeaf.getListButton()));
        }
    }

    private int     getApartment()                  throws TelegramApiException {
        list.clear();
        Arrays.asList(getText(Const.APARTMENT_MESSAGE).split(Const.SPLIT)).forEach((e) -> list.add(e));
        list.add(getText(Const.OTHERS_MESSAGE));
        if (isUpdate) list.add(getText(Const.SKIP_MESSAGE));
        buttonsLeaf = new ButtonsLeaf(list);
        if (isUpdate) {
            return toDeleteKeyboard(sendMessageWithKeyboard(getUpdateText(recipient.getApartment(), getText(Const.HOUSING_AVAILABILITY_MESSAGE)), buttonsLeaf.getListButton()));
        } else {
            return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.HOUSING_AVAILABILITY_MESSAGE), buttonsLeaf.getListButton()));
        }
    }

    private int     getOther()                      throws TelegramApiException { return botUtils.sendMessage(Const.SET_YOUR_OPTION_MESSAGE, chatId); }

    private int     getChildren()                   throws TelegramApiException { return botUtils.sendMessage(Const.SET_CHILDREN_MESSAGE, chatId); }

    private int     getChildrenIin()                throws TelegramApiException { return botUtils.sendMessage(Const.NAME_CHILDREN_MESSAGE, chatId); }

    private int     getSocialBenefits()             throws TelegramApiException {
        list.clear();
        Arrays.asList(getText(Const.SOCIAL_BENEFITS_MESSAGE).split(Const.SPLIT)).forEach((e) -> list.add(e));
        list.add(getText(Const.OTHERS_MESSAGE));
        list.add(getText(Const.NEXT_MESSAGE));
        if (isUpdate)list.add(getText(Const.SKIP_MESSAGE));
        buttonsLeaf = new ButtonsLeaf(list);
        if (isUpdate) {
            return toDeleteKeyboard(sendMessageWithKeyboard(getUpdateText(recipient.getSocialBenefits(), getText(Const.SOCIAL_PENSION_MESSAGE)), buttonsLeaf.getListButton()));
        } else {
            return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.SOCIAL_PENSION_MESSAGE), buttonsLeaf.getListButton()));
        }
    }

    private int     getMaritalStatus()              throws TelegramApiException {
        list.clear();
        Arrays.asList(getText(Const.MARRIED_TYPE_MESSAGE).split(Const.SPLIT)).forEach((e) -> list.add(e));
        if (isUpdate)list.add(getText(Const.SKIP_MESSAGE));
        buttonsLeaf = new ButtonsLeaf(list);
        if (isUpdate) {
            return toDeleteKeyboard(sendMessageWithKeyboard(getUpdateText(recipient.getMaritalStatus(), getText(Const.MARITAL_STATUS_MESSAGE)), buttonsLeaf.getListButton()));
        } else {
            return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.MARITAL_STATUS_MESSAGE), buttonsLeaf.getListButton()));
        }
    }

    private int     getAliments()                   throws TelegramApiException {
        list.clear();
        Arrays.asList(getText(Const.ALIMENT_TYPE_MESSAGE).split(Const.SPLIT)).forEach((e) -> list.add(e));
        if (isUpdate)list.add(getText(Const.SKIP_MESSAGE));
        buttonsLeaf = new ButtonsLeaf(list);
        if (isUpdate) {
            return toDeleteKeyboard(sendMessageWithKeyboard(getUpdateText(recipient.getAliments(), getText(Const.ALIMENTS_MESSAGE)), buttonsLeaf.getListButton()));
        } else {
            return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.ALIMENTS_MESSAGE), buttonsLeaf.getListButton()));
        }
    }

    private int     getEmploymentType()             throws TelegramApiException {
        list.clear();
        Arrays.asList(getText(Const.EMPLOYEE_TYPE_MESSAGE).split(Const.SPLIT)).forEach((e) -> list.add(e));
        list.add(getText(Const.OTHERS_MESSAGE));
        if (isUpdate)list.add(getText(Const.SKIP_MESSAGE));
        buttonsLeaf = new ButtonsLeaf(list);
        if (isUpdate) {
            return toDeleteKeyboard(sendMessageWithKeyboard(getUpdateText(recipient.getEmploymentType(), getText(Const.EMPLOYMENT_MESSAGE)), buttonsLeaf.getListButton()));
        } else {
            return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.EMPLOYMENT_MESSAGE), buttonsLeaf.getListButton()));
        }
    }

    private int     getEmployment()                 throws TelegramApiException { return botUtils.sendMessage(Const.WORK_PLACE_MESSAGE, chatId); }

    private int     getEducation()                  throws TelegramApiException {
        list.clear();
        Arrays.asList(getText(Const.EDUCATION_TYPE_CHOOSE_MESSAGE).split(Const.SPLIT)).forEach((e) -> list.add(e));
        if (isUpdate)list.add(getText(Const.SKIP_MESSAGE));
        buttonsLeaf = new ButtonsLeaf(list);
        if (isUpdate) {
            return toDeleteKeyboard(sendMessageWithKeyboard(getUpdateText(recipient.getEducation(), getText(Const.EDUCATION_MESSAGE)), buttonsLeaf.getListButton()));
        } else {
            return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.EDUCATION_MESSAGE), buttonsLeaf.getListButton()));
        }
    }

    private int     getDisabilityType()             throws TelegramApiException {
        list.clear();
        Arrays.asList(getText(Const.DISABILITY_CHOOSE_TYPE_MESSAGE).split(Const.SPLIT)).forEach((e) -> list.add(e));
        list.add(getText(Const.SKIP_MESSAGE));
        buttonsLeaf = new ButtonsLeaf(list);
        if (isUpdate) {
            return toDeleteKeyboard(sendMessageWithKeyboard(getUpdateText(recipient.getDisabilityType(), getText(Const.DISABILITY_TYPE_MESSAGE)), buttonsLeaf.getListButton()));
        } else {
            return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.DISABILITY_TYPE_MESSAGE), buttonsLeaf.getListButton()));
        }
    }

    private int     getNameOrSpeciality()           throws TelegramApiException { return botUtils.sendMessage(Const.NAME_OR_SPECIALITY_MESSAGE, chatId); }

    private int     getCreditHistory()              throws TelegramApiException {
        list.clear();
        Arrays.asList(getText(Const.CREDIT_TYPE_MESSAGE).split(Const.SPLIT)).forEach((e) -> list.add(e));
        list.add(getText(Const.OTHERS_MESSAGE));
//        if (isUpdate)list.add(getText(Const.SKIP_MESSAGE));
        buttonsLeaf = new ButtonsLeaf(list);
//        if (isUpdate) {
//            return toDeleteKeyboard(sendMessageWithKeyboard(getUpdateText(recipient.getCreditHistory(), getText(Const.CREDIT_HISTORY_MESSAGE)), buttonsLeaf.getListButton()));
//        } else {
            return toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.CREDIT_HISTORY_MESSAGE), buttonsLeaf.getListButton()));
//        }
    }

    private int     getCreditInfo()                 throws TelegramApiException { return botUtils.sendMessage(Const.CREDIT_INFO_MESSAGE, chatId); }

    private int     getBankName()                   throws TelegramApiException { return botUtils.sendMessage(Const.BANK_NAME_MESSAGE, chatId); }

    private String  getUpdateText(String currentInfo, String sendMessage) {
        String format = getText(Const.UPDATE_REGISTRATION_MESSAGE);
        return String.format(format, currentInfo != null ? currentInfo : " ", sendMessage);
    }

    private void    delete() {
        deleteMessage(updateMessageId);
        deleteMessage(deleteMessageId);
        deleteMessage(secondDeleteMessageId);
    }
}
