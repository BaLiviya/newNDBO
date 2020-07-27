package baliviya.com.github.NDBO.entity.custom;

import lombok.Data;

@Data
public class Handling {

    private int     id;
    private String  photo;
    private String  text;
    private int     handlingTypeId;
    private String  fullName;
    private long    handlingTeacherId;
    private int     langId;
}
