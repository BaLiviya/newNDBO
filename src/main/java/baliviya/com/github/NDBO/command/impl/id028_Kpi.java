package baliviya.com.github.NDBO.command.impl;

import baliviya.com.github.NDBO.command.Command;
import baliviya.com.github.NDBO.entity.custom.RegistrationHandling;
import baliviya.com.github.NDBO.entity.enums.WaitingType;
import baliviya.com.github.NDBO.utils.ButtonsLeaf;
import baliviya.com.github.NDBO.utils.Const;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class id028_Kpi extends Command {

    private List<String>            list = new ArrayList<>();
    private ButtonsLeaf             buttonsLeaf;
    private List<String>            handlingList;
    private String                  handling;
    private RegistrationHandling    registrationHandling;
    private int                     deleteMessageId;

    @Override
    public boolean execute() throws TelegramApiException {
        if (!isAdmin() && !isMainAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        switch (waitingType) {
            case START:
                deleteMessage(updateMessageId);
                handlingList = Arrays.asList(getText(Const.COUNT_HANDLING_TYPE_MESSAGE).split(Const.SPLIT));
                handlingList.forEach(s -> list.add(s));
                buttonsLeaf  = new ButtonsLeaf(list);
                toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.KPI_MESSAGE), buttonsLeaf.getListButton()));
                waitingType  = WaitingType.SET_HANDLING;
                return COMEBACK;
            case SET_HANDLING:
                deleteMessage(updateMessageId);
                if (hasCallbackQuery()) {
                    handling = handlingList.get(Integer.parseInt(updateMessageText));
                    switchType();
                }
                return COMEBACK;
            case SET_IIN:
                deleteMessage(updateMessageId);
                deleteMessage(deleteMessageId);
                if (hasMessageText()) {
                    
                }
                return COMEBACK;
        }
//        switch (waitingType) {
//            case START:
//                deleteMessage(updateMessageId);
//                list         = new ArrayList<>();
//                handlingList = Arrays.asList(getText(Const.COUNT_HANDLING_TYPE_MESSAGE).split(Const.SPLIT));
//                handlingList.forEach(s -> list.add(s));
//                buttonsLeaf  = new ButtonsLeaf(list);
//                toDeleteKeyboard(sendMessageWithKeyboard(getText(Const.KPI_MESSAGE), buttonsLeaf.getListButton()));
//                waitingType  = WaitingType.SET_HANDLING;
//                return COMEBACK;
//            case SET_HANDLING:
//                deleteMessage(updateMessageId);
//                if (hasCallbackQuery()) {
//                    handling    = handlingList.get(Integer.parseInt(updateMessageText));
//                    sendMessage(getText(Const.SET_COUNT_PEOPLE_MESSAGE));
//                    waitingType = WaitingType.SET_COUNT_PEOPLE;
//                }
//                return COMEBACK;
//            case SET_COUNT_PEOPLE:
//                deleteMessage(updateMessageId);
//                if (hasMessageText()) {
//                    int countPeople = Integer.parseInt(updateMessageText);
//                    switch (handling) {
//                        case "Создание рабочих мест":
//                            factory.getCountHandlingPlanDao().updateCount(29, countPeople);
//                            break;
//                        case "Создание бизнес проектов":
//                            factory.getCountHandlingPlanDao().updateCount(30, countPeople);
//                            break;
//                        case "Трудоустройство" :
//                            factory.getCountHandlingPlanDao().updateCount(31, countPeople);
//                            break;
//                        case "Самозанятые (ЕСП)":
//                            factory.getCountHandlingPlanDao().updateCount(32, countPeople);
//                            break;
//                        case "Гранты":
//                            factory.getCountHandlingPlanDao().updateCount(33, countPeople);
//                            break;
//                        case "Микрокредиты":
//                            factory.getCountHandlingPlanDao().updateCount(34, countPeople);
//                            break;
//                        case "Создание бизнеса через спонсоров":
//                            factory.getCountHandlingPlanDao().updateCount(35, countPeople);
//                            break;
//                    }
//                    sendMessage(Const.DONE_MESSAGE);
//                    return EXIT;
//                }
//                return COMEBACK;
//        }
        return EXIT;
    }

    private void switchType() throws TelegramApiException {
        switch (handling) {
            case "Гранты":
                registrationHandling = new RegistrationHandling();
                deleteMessageId      = sendMessage(Const.SEND_IIN_FROM_ADMIN_MESSAGE);
                waitingType          = WaitingType.SET_IIN;
                break;
        }
    }

}
