package support;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.BeforeGroups;

import java.io.*;
import java.util.Iterator;

/**
 * @project services-rest-assured
 * @user veronica.aruquipa
 * @date 6/1/2017 12:01 PM
 */

public class Utils {
    public static Object[][] getLocaleListFromExcelWB() {
        Object[][] localeList = null;
        try{
            String projectDir = System.getProperty("user.dir");
            FileInputStream file = new FileInputStream(new File(projectDir + Constants.testDataFile));

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Get first/desired sheet from workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;
            int i = 0;
            //Number of available locales in the xlsx file is get for define the array size.
            localeList = new String[sheet.getLastRowNum()+1][1];
            //Iterate through each rows one by one
            Iterator rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                row = (XSSFRow) rowIterator.next();
                //For each row, iterate through all the columns
                Iterator cellIterator = row.cellIterator();
                cell = (XSSFCell) cellIterator.next();
                //Extract the cell value
                localeList[i][0] = cell.getStringCellValue().trim();
                i++;
            }
            file.close();
            return localeList;
        } catch (Exception e){
            e.printStackTrace();
        }
        return localeList;
    }

    @BeforeGroups("car")
    public static void createTestResultsFile(){
        String projectDir = System.getProperty("user.dir");
        String workBookName = projectDir + Constants.testReportFile;//name of excel file
        XSSFWorkbook workBook = new XSSFWorkbook();

        XSSFSheet serverInfo = workBook.createSheet("Cars");
        XSSFRow rowServer = serverInfo.createRow(0);
        rowServer.createCell(0).setCellValue("Cars Available!");

        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(workBookName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //write this workbook to an Outputstream.
        try {
            workBook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
