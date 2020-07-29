package baliviya.com.github.NDBO.command;

import baliviya.com.github.NDBO.command.impl.*;
import baliviya.com.github.NDBO.exceptions.NotRealizedMethodException;

public class CommandFactory {

    public  static Command getCommand(long id) {
        Command result = getCommandWithoutReflection((int) id);
        if (result == null) throw new NotRealizedMethodException("Not realized for type: " + id);
        return result;
    }

    private static Command getCommandWithoutReflection(int id) {
        switch (id) {
            case 1:
                return new id001_ShowInfo();
            case 2:
                return new id002_SelectionLanguage();
            case 3:
                return new id003_Suggestion();
            case 4:
                return new id004_FirstRegistration();
            case 5:
                return new id005_Courses();
            case 6:
                return new id006_Training();
            case 7:
                return new id007_SupportBusinessProject();
            case 8:
                return new id008_Service();
            case 9:
                return new id009_ShowEvent();
            case 10:
                return new id010_Survey();
            case 11:
                return new id011_ShowAdminInfo();
            case 12:
                return new id012_EditMenu();
            case 13:
                return new id013_AddSurvey();
            case 14:
                return new id014_EditSurvey();
            case 15:
                return new id015_EditAdmin();
            case 16:
                return new id016_ReportSuggestion();
            case 17:
                return new id017_ReportSurvey();
            case 18:
                return new id018_ReportProfile();
            case 19:
                return new id019_EditEvent();
            case 20:
                return new id020_ReportService();
            case 21:
                return new id021_Registration();
            case 22:
                return new id022_Consultation();
            case 23:
                return new id023_Complaint();
            case 24:
                return new id024_ReportComplaint();
            case 25:
                return new id025_ShowUsers();
            case 26:
                return new id026_EditGroup();
            case 27:
                return new id027_AddHandling();
            case 28:
                return new id028_Kpi();
            case 29:
                return new id029_Reminder();

            case 31:
                return new id031_StructureShowInfo();
            case 32:
                return new id032_SpecialistInfo();
            case 33:
                return new id033_SpecShowInfo();
            case 34:
                return new id034_MapLocationSend();
            case 35:
                return new id035_SpecialistEdit();
            case 36:
                return new id036_Photo();
        }
        return null;
    }
}
