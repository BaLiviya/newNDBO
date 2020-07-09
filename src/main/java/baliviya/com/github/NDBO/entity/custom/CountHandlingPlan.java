package baliviya.com.github.NDBO.entity.custom;

import lombok.Data;

@Data
public class CountHandlingPlan {

    private int    id;
    private String handlingType;
    private int    courseTypeId;
    private int    courseNameId;
    private int    countPeople;
}
