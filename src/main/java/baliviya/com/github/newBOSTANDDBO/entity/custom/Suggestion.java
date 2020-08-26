package baliviya.com.github.newBOSTANDDBO.entity.custom;

import lombok.Data;

import java.util.Date;

@Data
public class Suggestion {

    private int    id;
    private String fullName;
    private String phoneNumber;
    private String text;
    private Date   postDate;
}
