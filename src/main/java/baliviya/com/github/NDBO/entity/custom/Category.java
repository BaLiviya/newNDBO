package baliviya.com.github.NDBO.entity.custom;

import lombok.Data;

@Data
//@Entity
public class Category {

    private int     id;
    private String  name;
    private boolean language;
    private boolean isHide;
}
