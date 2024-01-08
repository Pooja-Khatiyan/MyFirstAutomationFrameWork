package com.qa.opencart.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtil {
//it is just to read the data,so we create a static method here, so that we can call by it's name
//heree we are not creating any driver as it's purpose is just to read the data from excel sheet
//we have to supply from where to read the data: ithis will return 2 D object having row & column

	private static final String TEST_DATA_SHEET_PATH = ".\\src\\test\\resources\\testdata\\OpenCartTestData.xlsx";
	private static Workbook book;// workbook coming from apache poi api : workbook reference created at global level
    private static Sheet sheet; //we have to import from :org.apache.poi.ss.usermodel:incase multiple options
	
	public static Object[][] getTestData(String sheetName) {

		System.out.println("reading test data from sheet:" + sheetName);
		Object data[][] = null;

//it will help to connect the stream with Excel Sheet
		try {
			FileInputStream ip = new FileInputStream(TEST_DATA_SHEET_PATH);
//here we have to use class: WorkbookFactory :: coming from apache poi api
			book = WorkbookFactory.create(ip);// return type of create is workbook
        sheet = book.getSheet(sheetName);//getSheet return sheet: we reached inside the particular sheet
			data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];//array is initalized: empty sheet is created
//through for loop we will start filling the sheet :2 loop :1-row ;1 - column
			for(int i=0; i<sheet.getLastRowNum(); i++) {
				for(int j=0; j<sheet.getRow(0).getLastCellNum(); j++) {
					data[i][j]= sheet.getRow(i+1).getCell(j).toString();
					
//we have to start from 2nd row as in 1st we have titles: i+1 is used,data present in excel sheet is excel String we have to convert it into java String.
//for i=0 loop will run 5 times then it increment the value by 1 and again run 5 times (j: 0 to 4 : inner loop run then increment outer loop by 1)
			
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

}
