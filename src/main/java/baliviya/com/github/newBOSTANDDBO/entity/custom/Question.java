package baliviya.com.github.newBOSTANDDBO.entity.custom;

import lombok.Data;

@Data
public class Question {

    private int     id;
    private String  name;
    private String  desc;
    private int     languageId;
    private boolean isHide;
}
