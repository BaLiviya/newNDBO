package baliviya.com.github.NDBO.services;

import baliviya.com.github.NDBO.dao.DaoFactory;
import baliviya.com.github.NDBO.dao.impl.CountHandlingPlanDao;
import baliviya.com.github.NDBO.dao.impl.PropertiesDao;
import baliviya.com.github.NDBO.dao.impl.RegistrationHandlingDao;
import baliviya.com.github.NDBO.dao.impl.UserDao;
import baliviya.com.github.NDBO.entity.custom.HandlingReport;
import baliviya.com.github.NDBO.entity.custom.RegistrationHandling;
import baliviya.com.github.NDBO.entity.standart.User;
import baliviya.com.github.NDBO.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.*;

@Slf4j
public class ServiceReportService {

    private DaoFactory              factory                 = DaoFactory.getInstance();
    private RegistrationHandlingDao registrationHandlingDao = factory.getRegistrationHandlingDao();
    private CountHandlingPlanDao    countHandlingPlanDao    = factory.getCountHandlingPlanDao();
    private PropertiesDao           propertiesDao           = factory.getPropertiesDao();
    private UserDao                 userDao                 = factory.getUserDao();
    private XSSFWorkbook            workbook                = new XSSFWorkbook();
    private XSSFCellStyle           style                   = workbook.createCellStyle();
    private XSSFWorkbook            originWorkbook;
    private Sheet                   firstOriginSheet;
    private Sheet                   firstSheet;
    private Sheet                   secondOriginSheet;
    private Sheet                   secondSheet;
    private Sheet                   thirdOriginSheet;
    private Sheet                   thirdSheet;

    private Sheet                   fourthOriginSheet;
    private Sheet                   fourthSheet;
    private Sheet                   fifthOriginSheet;
    private Sheet                   fifthSheet;
    private Date                    dateBegin;
    private Date                    dateEnd;


    public  void sendServiceReport  (long chatId, DefaultAbsSender bot, Date dateBegin, Date dateEnd, int messagePrevReport) throws TelegramApiException {
        this.dateBegin = dateBegin;
        this.dateEnd   = dateEnd;
        try {
            sendReport(chatId, bot, messagePrevReport);
        } catch (Exception e) {
            log.error("Can't create/send report", e);
            try {
                bot.execute(new SendMessage(chatId, "Ошибка при создании отчета"));
            } catch (TelegramApiException ex) {
                log.error("Can't send message", ex);
            }
        }
    }

    private void sendReport         (long chatId, DefaultAbsSender bot, int messagePrevReport) throws IOException, TelegramApiException {
        try {
            originWorkbook = new XSSFWorkbook(new FileInputStream(new File(propertiesDao.getPropertiesValue(4))));
        } catch (Exception e) {
            log.error("Can't read file, error: ", e);
        }

        createFirstTitle();
        addFirstPageInfo();

        createSecondTitle();
        addSecondPageInfo();

        createThirdTitle();
        addThirdPageInfo();

//        createFifthTitle();
//        addFifthPageInfo();

        addInfo();
        sendFile(chatId, bot, dateBegin, dateEnd);
    }


    private void createFirstTitle() {
        firstOriginSheet    = originWorkbook.getSheetAt(0);
        firstSheet          = workbook.createSheet(firstOriginSheet.getSheetName());
        for (int indexRow = 0; indexRow < firstOriginSheet.getLastRowNum() + 1; indexRow++) {
            Row originRow = firstOriginSheet.getRow(indexRow);
            Row row       = firstSheet.createRow(indexRow);
            if (indexRow == 2) row.setHeight((short) 500);
            for (int indexCell = 0; indexCell < 4; indexCell++) {
                setFirstTitleValue(row, indexCell, getTitleValue(originRow, indexCell), indexRow);
            }
        }
    }

    private void addFirstPageInfo() {
        int rowIndex = 4;
        int workSpaceCount       = countHandlingPlanDao.getCountById(29).getCountPeople();
        int businessProjectCount = countHandlingPlanDao.getCountById(30).getCountPeople();
        int workCount            = countHandlingPlanDao.getCountById(31).getCountPeople();
        int businessManCount     = countHandlingPlanDao.getCountById(32).getCountPeople();
        int allCount             = workSpaceCount + businessProjectCount + workCount + businessManCount;
        setFirstTitleValue(firstSheet.getRow(rowIndex++),3, String.valueOf(String.valueOf(workSpaceCount)),      4);
        setFirstTitleValue(firstSheet.getRow(rowIndex++),3, String.valueOf(String.valueOf(businessProjectCount)),5);
        setFirstTitleValue(firstSheet.getRow(rowIndex++),3, String.valueOf(String.valueOf(workCount)),           6);
        setFirstTitleValue(firstSheet.getRow(rowIndex++),3, String.valueOf(String.valueOf(businessManCount)),    7);
        setFirstTitleValue(firstSheet.getRow(rowIndex),  3, String.valueOf(String.valueOf(allCount)),            8);
    }

    private void setFirstTitleValue(Row row, int numberCell, String cellValue, int rowIndex) {
        Cell cell = row.createCell(numberCell);
        if (        rowIndex == 0 && numberCell == 0) {
            cell.setCellStyle(setBoldStyleWithoutTable(14));
        } else if ( rowIndex == 2 && numberCell == 0 || rowIndex == 2 && numberCell == 1 || rowIndex == 2 && numberCell == 2) {
            cell.setCellStyle(setStyleBold(12 , true));
        } else if ( rowIndex == 3 && numberCell == 2 || rowIndex == 3 && numberCell == 3) {
            cell.setCellStyle(setBoldStyleColorBackground(IndexedColors.YELLOW.getIndex(), true));
        } else if ( rowIndex == 4 && numberCell == 2 || rowIndex == 4 && numberCell == 3 ||
                rowIndex == 5 && numberCell == 2 || rowIndex == 5 && numberCell == 3 ||
                rowIndex == 6 && numberCell == 2 || rowIndex == 6 && numberCell == 3 ||
                rowIndex == 7 && numberCell == 2 || rowIndex == 7 && numberCell == 3 ||
                rowIndex == 8 && numberCell == 2 || rowIndex == 8 && numberCell == 3) {
            cell.setCellStyle(setBoldStyleColorBackground(IndexedColors.YELLOW.getIndex(), false));
        } else if ( rowIndex == 3 && numberCell == 0 || rowIndex == 3 && numberCell == 1 ||
                rowIndex == 4 && numberCell == 0 || rowIndex == 4 && numberCell == 1 ||
                rowIndex == 5 && numberCell == 0 || rowIndex == 5 && numberCell == 1 ||
                rowIndex == 6 && numberCell == 0 || rowIndex == 6 && numberCell == 1 ||
                rowIndex == 7 && numberCell == 0 || rowIndex == 7 && numberCell == 1 ||
                rowIndex == 8 && numberCell == 1) {
            cell.setCellStyle(setStyleBold(12, true));
        } else {

        }
        cell.setCellValue(cellValue);
    }


    private void createSecondTitle() {
        secondOriginSheet   = originWorkbook.getSheetAt(1);
        secondSheet         = workbook.createSheet(secondOriginSheet.getSheetName());
        for (int indexRow = 0; indexRow < secondOriginSheet.getLastRowNum() + 1; indexRow++) {
            Row originRow = secondOriginSheet.getRow(indexRow);
            Row row       = secondSheet.createRow(indexRow);
            if (indexRow == 2 || indexRow == 3 || indexRow == 4 || indexRow == 16 || indexRow == 17 || indexRow == 20 || indexRow == 21 || indexRow == 25) row.setHeight((short) 550);
            for (int indexCell = 0; indexCell < 4; indexCell++) {
                setSecondTitleValue(row, indexCell, getTitleValue(originRow, indexCell), indexRow);
            }
        }
    }

    private void addSecondPageInfo() {
        setSecondTitleValue(secondSheet.getRow( 6), 3, String.valueOf(factory.getRecipientDao().getCountByTime(dateBegin, dateEnd)),               6);

//        setSecondTitleValue(secondSheet.getRow( 7), 3, String.valueOf(factory.getServiceDao().getAll().size()),            7);
//        setSecondTitleValue(secondSheet.getRow( 9), 3, String.valueOf(factory.getServiceDao().getAll(2).size()),9);
//        setSecondTitleValue(secondSheet.getRow(10), 3, String.valueOf(factory.getServiceDao().getAll(1).size()),10);
//        setSecondTitleValue(secondSheet.getRow(11), 3, String.valueOf(factory.getServiceDao().getAll(4).size()),11);
//        setSecondTitleValue(secondSheet.getRow(12), 3, String.valueOf(factory.getServiceDao().getAll(3).size()),12);
//        setSecondTitleValue(secondSheet.getRow(13), 3, String.valueOf(factory.getServiceDao().getAll(5).size()),13);


        setSecondTitleValue(secondSheet.getRow(15), 3, String.valueOf(registrationHandlingDao.getCountConsultationById(dateBegin, dateEnd, 1)), 15);
        setSecondTitleValue(secondSheet.getRow(16), 3, String.valueOf(registrationHandlingDao.getCountConsultationById(dateBegin, dateEnd, 2)), 16);
        setSecondTitleValue(secondSheet.getRow(17), 3, String.valueOf(registrationHandlingDao.getCountConsultationById(dateBegin, dateEnd, 3)), 17);
        setSecondTitleValue(secondSheet.getRow(18), 3, String.valueOf(registrationHandlingDao.getCountConsultationById(dateBegin, dateEnd, 4)), 18);
        setSecondTitleValue(secondSheet.getRow(19), 3, String.valueOf(registrationHandlingDao.getCountConsultationById(dateBegin, dateEnd, 5)), 19);
        setSecondTitleValue(secondSheet.getRow(20), 3, String.valueOf(registrationHandlingDao.getCountConsultationById(dateBegin, dateEnd, 6)), 20);
        setSecondTitleValue(secondSheet.getRow(21), 3, String.valueOf(registrationHandlingDao.getCountConsultationById(dateBegin, dateEnd, 7)), 21);

        setSecondTitleValue(secondSheet.getRow(23), 3, String.valueOf(registrationHandlingDao.getCountConsultationById(dateBegin, dateEnd, 8)), 23);
        setSecondTitleValue(secondSheet.getRow(24), 3, String.valueOf(registrationHandlingDao.getCountConsultationById(dateBegin, dateEnd, 9)), 24);
        setSecondTitleValue(secondSheet.getRow(25), 3, String.valueOf(registrationHandlingDao.getCountConsultationById(dateBegin, dateEnd, 10)),25);
        setSecondTitleValue(secondSheet.getRow(26), 3, String.valueOf(registrationHandlingDao.getCountConsultationById(dateBegin, dateEnd, 11)),26);
    }

    private void setSecondTitleValue(Row row, int numberCell, String cellValue, int rowIndex) {
        Cell cell = row.createCell(numberCell);
        if         (rowIndex ==  0 && numberCell == 1) {
            cell.setCellStyle(setBoldStyleWithoutTable(14));
        } else if  (rowIndex ==  2 && numberCell == 1 || rowIndex ==  3 && numberCell == 0 || rowIndex == 3 && numberCell == 1 ||
                    rowIndex ==  5 && numberCell == 0 || rowIndex ==  5 && numberCell == 1 ||
                    rowIndex ==  8 && numberCell == 0 || rowIndex ==  8 && numberCell == 1 ||
                    rowIndex == 27 && numberCell == 1) {
            cell.setCellStyle(setStyleBold(12, true));
        } else if  (rowIndex >=  2 && numberCell == 2 || rowIndex >=  2 && numberCell == 3) {
            cell.setCellStyle(setBoldStyleColorBackground(IndexedColors.YELLOW.getIndex(), true));
        } else if  (rowIndex ==  4 && numberCell == 0 || rowIndex ==  4 && numberCell == 1 ||
                    rowIndex == 14 && numberCell == 0 || rowIndex == 14 && numberCell == 1 ||
                    rowIndex == 22 && numberCell == 0 || rowIndex == 22 && numberCell == 1 ||
                    rowIndex == 26 && numberCell == 0 || rowIndex == 26 && numberCell == 1) {
            cell.setCellStyle(setBoldStyleColorBackground(IndexedColors.GREY_40_PERCENT.getIndex(), true));
        } else {
            cell.setCellStyle(setStyleBold(12, false));
        }
        cell.setCellValue(cellValue);
    }


    private void createThirdTitle() {
        thirdOriginSheet   = originWorkbook.getSheetAt(2);
        thirdSheet         = workbook.createSheet(thirdOriginSheet.getSheetName());
        for (int indexRow = 0; indexRow < thirdOriginSheet.getLastRowNum() + 1; indexRow++) {
            Row originRow = thirdOriginSheet.getRow(indexRow);
            Row row       = thirdSheet.createRow(indexRow);
            if (indexRow == 1) {
                row.setHeight((short) 1000);
            }
            for (int indexCell = 0; indexCell < 4; indexCell++) {
                setThirdTitleValue(row, indexCell, getTitleValue(originRow, indexCell), indexRow);
            }
        }
    }

    private void addThirdPageInfo() {
        int rowIndex   = 5;
        int numberCell = 3;
        List<RegistrationHandling> allCoursesByTime   = registrationHandlingDao.getAllCoursesByTime  (dateBegin, dateEnd);
        List<RegistrationHandling> allTrainingsByTime = registrationHandlingDao.getAllTrainingsByTime(dateBegin, dateEnd);
        List<RegistrationHandling> allBusinessByTime  = registrationHandlingDao.getAllBusinessByTime (dateBegin, dateEnd);
        if (allCoursesByTime != null && allCoursesByTime.size() != 0) {
            for (int index = 1; index < 21; index++) {
                int count = 0;
                for (RegistrationHandling handling : allCoursesByTime) {
                    if (handling.getIdHandling() == index) {
                        count++;
                    }
                }
                setThirdTitleValue(thirdSheet.getRow(rowIndex), numberCell, String.valueOf(count), rowIndex);
                if (rowIndex == 7 || rowIndex == 11 || rowIndex == 14 || rowIndex == 25 || rowIndex == 28) rowIndex++;
                if (rowIndex == 18) rowIndex += 2;
                rowIndex++;
            }
        }
        if (allTrainingsByTime != null && allTrainingsByTime.size() != 0) {
            rowIndex = 33;
            for (int index = 1; index < 5; index++) {
                int count = 0;
                for (RegistrationHandling handling : allTrainingsByTime) {
                    if (handling.getIdHandling() == index) {
                        count++;
                    }
                }
                setThirdTitleValue(thirdSheet.getRow(rowIndex), numberCell, String.valueOf(count), rowIndex);
                rowIndex++;
            }
        }
        if (allBusinessByTime != null && allBusinessByTime.size() != 0) {
            rowIndex = 38;
            for (int index = 1; index < 4; index++) {
                int count = 0;
                for (RegistrationHandling handling : allBusinessByTime) {
                    if (handling.getIdHandling() == index) {
                        count++;
                    }
                }
                setThirdTitleValue(thirdSheet.getRow(rowIndex), numberCell, String.valueOf(count), rowIndex);
                rowIndex++;
            }
        }
        setThirdTitleValue(thirdSheet.getRow(41), numberCell, String.valueOf(allBusinessByTime.size() + allCoursesByTime.size() + allTrainingsByTime.size()), 41);
    }

    private void setThirdTitleValue(Row row, int numberCell, String cellValue, int rowIndex) {
        Cell cell = row.createCell(numberCell);
        if (rowIndex != 0 && rowIndex != 1 && numberCell == 0 || rowIndex != 0 && rowIndex != 1 && numberCell == 1)   cell.setCellStyle(setStyleBold(12, false));
        if (rowIndex != 0 && rowIndex != 1 && numberCell == 2 || rowIndex != 0 && rowIndex != 1 && numberCell == 3 )  cell.setCellStyle(setBoldStyleColorBackground(IndexedColors.YELLOW.getIndex(),true));
        if (rowIndex == 1 && numberCell == 0) cell.setCellStyle(setBoldStyleWithoutTable(12));
        if (rowIndex == 2 && numberCell == 0 || rowIndex == 2 && numberCell == 1 ||
                rowIndex ==  4 && numberCell == 0 || rowIndex ==  4 && numberCell == 1 ||
                rowIndex ==  8 && numberCell == 0 || rowIndex ==  8 && numberCell == 1 ||
                rowIndex == 12 && numberCell == 0 || rowIndex == 12 && numberCell == 1 ||
                rowIndex == 15 && numberCell == 0 || rowIndex == 15 && numberCell == 1 ||
                rowIndex == 20 && numberCell == 0 || rowIndex == 20 && numberCell == 1 ||
                rowIndex == 26 && numberCell == 0 || rowIndex == 26 && numberCell == 1 ||
                rowIndex == 29 && numberCell == 0 || rowIndex == 29 && numberCell == 1) {
            cell.setCellStyle(setStyleBold(12, true));
        }
        if (rowIndex == 3 && numberCell == 0 || rowIndex == 3 && numberCell == 1 || rowIndex == 19 && numberCell == 0 || rowIndex == 19 && numberCell == 1 ) cell.setCellStyle(setBoldStyleColorBackground(IndexedColors.GREY_40_PERCENT.getIndex(), true));
//        if (rowIndex == 2 && numberCell == 0 || rowIndex == 2  && numberCell == 1 || rowIndex ==  2 && numberCell == 2) cell.setCellStyle(setStyleBold(12, true));
//        if (rowIndex == 33 && numberCell == 0 || rowIndex == 33 && numberCell == 1 ||
//                rowIndex == 37 && numberCell == 0 || rowIndex == 37 && numberCell == 1 ||
//                rowIndex == 4 && numberCell == 0 || rowIndex == 4  && numberCell == 1) {
//            cell.setCellStyle(setBoldStyleColorBackground(IndexedColors.GREY_40_PERCENT.getIndex(), true));
//            //rowIndex == 20 && numberCell == 0 || rowIndex == 20 && numberCell == 1 ||
//        }
//        if (rowIndex == 4 && numberCell == 0 || rowIndex == 4 && numberCell == 1 || rowIndex ==  8 && numberCell == 0 || rowIndex ==  8 && numberCell == 1 ||
//                rowIndex == 12 && numberCell == 0 || rowIndex == 12 && numberCell == 1 || rowIndex == 15 && numberCell == 0 || rowIndex == 15 && numberCell == 1 ||
//                rowIndex == 20 && numberCell == 0 || rowIndex == 20 && numberCell == 1 || rowIndex == 26 && numberCell == 0 || rowIndex == 26 && numberCell == 1 ||
//                rowIndex == 29 && numberCell == 0 || rowIndex == 29 && numberCell == 1 || rowIndex == 41 && numberCell == 1) {
//            cell.setCellStyle(setStyleBold(12, true));
//        }
        cell.setCellValue(cellValue);
    }


//    private void createFifthTitle() {
//        fifthOriginSheet = originWorkbook.getSheetAt(4);
//        fifthSheet       = workbook.createSheet(fifthOriginSheet.getSheetName());
//        for (int indexRow = 0; indexRow < 2; indexRow++) {
//            Row originRow = fifthOriginSheet.getRow(indexRow);
//            Row row       = fifthSheet.createRow(indexRow);
//            if (indexRow == 1) row.setHeight((short) 1500);
//            for (int indexCell = 0; indexCell < 39; indexCell++) {
//                setFifthTitleValue(row, indexCell, getTitleValue(originRow, indexCell), indexRow);
//            }
//        }
//    }

//    private void addFifthPageInfo() {
//        List<RegistrationHandling> allConsultationByTime = registrationHandlingDao.getAllConsultationByTime(dateBegin, dateEnd);
//        List<RegistrationHandling> allTrainingsByTime    = registrationHandlingDao.getAllTrainingsByTime(dateBegin, dateEnd);
//        List<RegistrationHandling> allCoursesByTime      = registrationHandlingDao.getAllCoursesByTime(dateBegin, dateEnd);
//        Map<Long, HandlingReport>  allRegistrations      = new HashMap<>();
//        HandlingReport             handlingReport;
//        int rowIndex  = 2;
//        int cellIndex = 0;
//
//        if (allConsultationByTime != null && allConsultationByTime.size() != 0) {
//            for (RegistrationHandling registrationHandling : allConsultationByTime) {
//                if (allRegistrations.containsKey(registrationHandling.getChatId())) {
//                    handlingReport = allRegistrations.get(registrationHandling.getChatId());
//                } else {
//                    handlingReport  = new HandlingReport();
//                    User user       = userDao.getUserByChatId(registrationHandling.getChatId());
//                    handlingReport.setId(user.getId());
//                    handlingReport.setFullName(user.getFullName());
//                    handlingReport.setIin(user.getIin());
//                    handlingReport.setPhone(user.getPhone());
//                    handlingReport.setStatus(user.getStatus());
//                }
//                switch (registrationHandling.getIdHandling()) {
//                    case 1:
//                        handlingReport.setProblemCount(handlingReport.getProblemCount() + 1);
//                        break;
//                    case 2:
//                        handlingReport.setKindergartenCount(handlingReport.getKindergartenCount() + 1);
//                        break;
//                    case 3:
//                        handlingReport.setEducationForKidsCount(handlingReport.getEducationForKidsCount() + 1);
//                        break;
//                    case 4:
//                        handlingReport.setFoodsCount(handlingReport.getFoodsCount() + 1);
//                        break;
//                    case 5:
//                        handlingReport.setHomeCount(handlingReport.getHomeCount() + 1);
//                        break;
//                    case 6:
//                        handlingReport.setSocialBenefitsCount(handlingReport.getSocialBenefitsCount() + 1);
//                        break;
//                    case 7:
//                        handlingReport.setSupportDocCount(handlingReport.getSupportDocCount() + 1);
//                        break;
//                    case 8:
//                        handlingReport.setMedicalCount(handlingReport.getMedicalCount() + 1);
//                        break;
//                    case 9:
//                        handlingReport.setWellnessCount(handlingReport.getWellnessCount() + 1);
//                        break;
//                    case 10:
//                        handlingReport.setLifestyleCount(handlingReport.getLifestyleCount() + 1);
//                        break;
//                    case 11:
//                        handlingReport.setJobsCount(handlingReport.getJobsCount() + 1);
//                        break;
//                }
//                allRegistrations.put(registrationHandling.getChatId(), handlingReport);
//            }
//        }
//        if (allTrainingsByTime != null && allTrainingsByTime.size() != 0) {
//            for (RegistrationHandling registrationHandling : allTrainingsByTime) {
//                if (allRegistrations.containsKey(registrationHandling.getChatId())) {
//                    handlingReport = allRegistrations.get(registrationHandling.getChatId());
//                } else {
//                    handlingReport  = new HandlingReport();
//                    User user       = userDao.getUserByChatId(registrationHandling.getChatId());
//                    handlingReport.setId(user.getId());
//                    handlingReport.setFullName(user.getFullName());
//                    handlingReport.setIin(user.getIin());
//                    handlingReport.setPhone(user.getPhone());
//                    handlingReport.setStatus(user.getStatus());
//                }
//                switch (registrationHandling.getIdHandling()) {
//                    case 1:
//                        handlingReport.setGrowUpYourselfCount(handlingReport.getGrowUpYourselfCount() + 1);
//                        break;
//                    case 2:
//                        handlingReport.setFamilyCount(handlingReport.getFamilyCount() + 1);
//                        break;
//                    case 3:
//                        handlingReport.setBusinessCount(handlingReport.getBusinessCount() + 1);
//                        break;
//                    case 4:
//                        handlingReport.setParentingCount(handlingReport.getParentingCount() + 1);
//                        break;
//                }
//            }
//        }
//        if (allCoursesByTime != null && allCoursesByTime.size() != 0) {
//            for (RegistrationHandling registrationHandling : allCoursesByTime) {
//
//            }
//        }
//
//        for (Map.Entry<Long, HandlingReport> entry : allRegistrations.entrySet()) {
//            fifthSheet.createRow(rowIndex).createCell(cellIndex++).setCellValue(entry.getValue().getId());
//            fifthSheet.getRow(rowIndex)   .createCell(cellIndex++).setCellValue(entry.getValue().getFullName());
//            fifthSheet.getRow(rowIndex)   .createCell(cellIndex++).setCellValue(entry.getValue().getIin());
//            fifthSheet.getRow(rowIndex)   .createCell(cellIndex++).setCellValue(entry.getValue().getPhone());
//            fifthSheet.getRow(rowIndex)   .createCell(cellIndex++).setCellValue(entry.getValue().getStatus());
//            fifthSheet.getRow(rowIndex)   .createCell( 9)      .setCellValue(entry.getValue().getProblemCount());
//            fifthSheet.getRow(rowIndex)   .createCell(11)      .setCellValue(entry.getValue().getSupportDocCount());
//            fifthSheet.getRow(rowIndex)   .createCell(13)      .setCellValue(entry.getValue().getHomeCount());
//            fifthSheet.getRow(rowIndex)   .createCell(15)      .setCellValue(entry.getValue().getSocialBenefitsCount());
//            fifthSheet.getRow(rowIndex)   .createCell(17)      .setCellValue(entry.getValue().getEducationForKidsCount());
//            fifthSheet.getRow(rowIndex)   .createCell(19)      .setCellValue(entry.getValue().getFoodsCount());
//            fifthSheet.getRow(rowIndex)   .createCell(21)      .setCellValue(entry.getValue().getKindergartenCount());
//            fifthSheet.getRow(rowIndex)   .createCell(23)      .setCellValue(entry.getValue().getWellnessCount());
//            fifthSheet.getRow(rowIndex)   .createCell(25)      .setCellValue(entry.getValue().getGrowUpYourselfCount());
//            fifthSheet.getRow(rowIndex)   .createCell(27)      .setCellValue(entry.getValue().getParentingCount());
//            fifthSheet.getRow(rowIndex)   .createCell(29)      .setCellValue(entry.getValue().getBusinessCount());
//            fifthSheet.getRow(rowIndex)   .createCell(31)      .setCellValue(entry.getValue().getFamilyCount());
//        }
//    }



    private void            setFifthTitleValue(Row row, int numberCell, String cellValue, int rowIndex) {
        Cell cell = row.createCell(numberCell);
        cell.setCellValue(cellValue);
        if (rowIndex == 1 && numberCell == 0 || rowIndex == 1 && numberCell == 1 || rowIndex == 1 && numberCell == 2 || rowIndex == 1 && numberCell == 3 || rowIndex == 1 && numberCell == 4) {
            cell.setCellStyle(setStyleBold(14, true));
        }
        if (rowIndex == 0 && numberCell ==  5     || rowIndex == 1 && numberCell ==  5 || rowIndex == 1 && numberCell ==  7 || rowIndex == 1 && numberCell == 9 ) cell.setCellStyle(setBoldStyleColorBackground(IndexedColors.ORANGE.getIndex(), true));
        if (rowIndex == 0 && numberCell == 11     || rowIndex == 1 && numberCell == 11 || rowIndex == 1 && numberCell == 13 ||
                rowIndex == 1 && numberCell == 15 || rowIndex == 1 && numberCell == 17 || rowIndex == 1 && numberCell == 19 ||
                rowIndex == 1 && numberCell == 21) {
            cell.setCellStyle(setBoldStyleColorBackground(IndexedColors.YELLOW.getIndex(), true));
        }
        if (rowIndex == 0 && numberCell == 23 || rowIndex == 1 && numberCell == 23 || rowIndex == 1 && numberCell == 24) cell.setCellStyle(setBoldStyleColorBackground(IndexedColors.AQUA.getIndex(), true));
        if (rowIndex == 0 && numberCell == 25 || rowIndex == 1 && numberCell == 25 || rowIndex == 1 && numberCell == 26 || rowIndex == 1 && numberCell == 27 || rowIndex == 1 && numberCell == 28 ||
                rowIndex == 1 && numberCell == 29 || rowIndex == 1 && numberCell == 30 || rowIndex == 1 && numberCell == 31 || rowIndex == 1 && numberCell == 32 ||
                rowIndex == 1 && numberCell == 33 || rowIndex == 1 && numberCell == 34 || rowIndex == 1 && numberCell == 35 || rowIndex == 1 && numberCell == 36) {
            cell.setCellStyle(setBoldStyleColorBackground(IndexedColors.LIGHT_ORANGE.getIndex(), true));
        }
    }

    private void            addInfo() {
        int cellIndex = 0;

        firstSheet .addMergedRegion(new CellRangeAddress(0,0,0, 3));
        firstSheet .addMergedRegion(new CellRangeAddress(2,2,2, 3));

        secondSheet.addMergedRegion(new CellRangeAddress(0,0,0, 3));
        secondSheet.addMergedRegion(new CellRangeAddress(2,2,2, 3));

        thirdSheet .addMergedRegion(new CellRangeAddress(1,1,0, 3));
        thirdSheet .addMergedRegion(new CellRangeAddress(2,2,2, 3));
//        thirdSheet .addMergedRegion(new CellRangeAddress(3,3,2,3));

//        fifthSheet .addMergedRegion(new CellRangeAddress(0,0, 5,  10));
//        fifthSheet .addMergedRegion(new CellRangeAddress(0,0, 11, 22));
//        fifthSheet .addMergedRegion(new CellRangeAddress(0,0, 23, 24));
//        fifthSheet .addMergedRegion(new CellRangeAddress(0,0, 25, 36));
//
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1, 5, 6));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1, 7, 8));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1, 9, 10));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1,11, 12));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1,13, 14));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1,15, 16));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1,17, 18));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1,19, 20));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1,21, 22));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1,23, 24));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1,25, 26));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1,27, 28));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1,29, 30));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1,31, 32));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1,33, 34));
//        fifthSheet .addMergedRegion(new CellRangeAddress(1,1,35, 36));

        for (cellIndex = 0; cellIndex < 4; cellIndex++) {
            firstSheet .autoSizeColumn(cellIndex);
            secondSheet.autoSizeColumn(cellIndex);
            thirdSheet .autoSizeColumn(cellIndex);
        }
//        for (cellIndex = 0; cellIndex < 5; cellIndex++) {
//            fifthSheet .autoSizeColumn(cellIndex);
//        }
    }

    private String          getTitleValue(Row row, int numberCell) {
        Cell cell = row.getCell(numberCell);
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    private XSSFCellStyle   setBoldStyleColorBackground(short colorIndex, boolean isBold) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font           = workbook.createFont();
        BorderStyle title       = BorderStyle.THIN;
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop   (title);
        cellStyle.setBorderBottom(title);
        cellStyle.setBorderRight (title);
        cellStyle.setBorderLeft  (title);
        cellStyle.setTopBorderColor     (IndexedColors.BLACK.getIndex());
        cellStyle.setRightBorderColor   (IndexedColors.BLACK.getIndex());
        cellStyle.setBottomBorderColor  (IndexedColors.BLACK.getIndex());
        cellStyle.setLeftBorderColor    (IndexedColors.BLACK.getIndex());
        cellStyle.setFillForegroundColor(colorIndex);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font     .setFontName("Times New Roman");
        font     .setFontHeight(12);
        if (isBold) font.setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private XSSFCellStyle   setStyleBold(int size, boolean bold) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font           = workbook.createFont();
        BorderStyle title       = BorderStyle.THIN;
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop   (title);
        cellStyle.setBorderBottom(title);
        cellStyle.setBorderRight (title);
        cellStyle.setBorderLeft  (title);
        cellStyle.setTopBorderColor   (IndexedColors.BLACK.getIndex());
        cellStyle.setRightBorderColor (IndexedColors.BLACK.getIndex());
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setLeftBorderColor  (IndexedColors.BLACK.getIndex());
        font     .setFontName("Times New Roman");
        font     .setFontHeight(size);
        if (bold) font     .setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private XSSFCellStyle   setBoldStyleWithoutTable(int fontSize) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font           = workbook.createFont();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        font     .setFontName("Times New Roman");
        font     .setFontHeight(fontSize);
        font     .setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private XSSFCellStyle   setStyleBoldVertical(int size) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle .setRotation((short)90);
        XSSFFont font           = workbook.createFont();
        BorderStyle title       = BorderStyle.THIN;
        cellStyle.setBorderTop   (title);
        cellStyle.setBorderBottom(title);
        cellStyle.setBorderRight (title);
        cellStyle.setBorderLeft  (title);
        cellStyle.setTopBorderColor   (IndexedColors.BLACK.getIndex());
        cellStyle.setRightBorderColor (IndexedColors.BLACK.getIndex());
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setLeftBorderColor  (IndexedColors.BLACK.getIndex());
        font     .setFontName("Times New Roman");
        font     .setFontHeight(size);
        font     .setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private void            sendFile(long chatId, DefaultAbsSender bot, Date dateBegin, Date dateEnd) throws IOException, TelegramApiException {
        String fileName = "Болванка за: " + DateUtil.getDayDate(dateBegin) + " - " + DateUtil.getDayDate(dateEnd) + ".xlsx";
        String path     = "C:\\test\\" + fileName;
        path           += new Date().getTime();
        try (FileOutputStream stream = new FileOutputStream(path)) {
            workbook.write(stream);
        } catch (IOException e) {
            log.error("Can't send file error: ", e);
        }
        sendFile(chatId, bot, fileName, path);
    }

    private void            sendFile(long chatId, DefaultAbsSender bot, String fileName, String path) throws TelegramApiException, IOException {
        File file = new File(path);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            bot.execute(new SendDocument().setChatId(chatId).setDocument(fileName, fileInputStream));
        }
        file.delete();
    }
}
