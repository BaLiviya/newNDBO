package baliviya.com.github.newNDBO.entity.custom;

import lombok.Data;

@Data
public class ServiceSurveyAnswer {

    private int    id;
    private int    surveyId;
    private long   chatId;
    private String button;
    private String handlingType;
}
