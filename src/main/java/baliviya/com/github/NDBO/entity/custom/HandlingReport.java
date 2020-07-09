package baliviya.com.github.NDBO.entity.custom;

import lombok.Data;

@Data
public class HandlingReport {

    private int     id;
    private String  fullName;
    private String  iin;
    private String  phone;
    private String  status;
    private int     problemCount;
    private int     supportDocCount;
    private int     homeCount;
    private int     socialBenefitsCount;
    private int     educationForKidsCount;
    private int     foodsCount;
    private int     kindergartenCount;
    private int     wellnessCount;
    private int     medicalCount;
    private int     lifestyleCount;
    private int     jobsCount;
    private int     growUpYourselfCount;
    private int     parentingCount;
    private int     businessCount;
    private int     familyCount;
}
