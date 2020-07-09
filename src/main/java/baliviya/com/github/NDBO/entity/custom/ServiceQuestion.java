package baliviya.com.github.NDBO.entity.custom;

import lombok.Data;

@Data
public class ServiceQuestion {

    private int     id;
    private String  name;
    private String  question;
    private int     languageId;
    private int     serviceId;
    private boolean isHide;
    private String  handlingType;
}
