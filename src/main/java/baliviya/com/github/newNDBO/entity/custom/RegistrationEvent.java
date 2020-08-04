package baliviya.com.github.newNDBO.entity.custom;

import lombok.Data;

import java.util.Date;

@Data
public class RegistrationEvent {

    private int     id;
    private long    chatId;
    private long    eventId;
    private Date    registrationDate;
    private boolean isCome;
}
