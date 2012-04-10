import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//app retrieves excel file contents and meake some calculations,average og some values based on company names..
//it takes time to process 100.000 rows!
public class ReadFiles {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		// An excel file name. You can create a file name with a full

		// path information.

		String filename = "C:\\Kitap2.xlsx";
		// Create an ArrayList to store the data read from excel sheet.

		List<TradeInfo> sheetData = new ArrayList<TradeInfo>();
		FileInputStream fis = null;

		try {

			//

			// Create a FileInputStream that will be use to read the

			// excel file.

			//

			fis = new FileInputStream(filename);

			//

			// Create an excel workbook from the file system.

			//

			// HSSFWorkbook workbook = new HSSFWorkbook(fis);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);

			// Get the first sheet on the workbook.
			XSSFSheet sheet = workbook.getSheetAt(0);

			// When we have a sheet object in hand we can iterator on

			// each sheet's rows and on each row's cells. We store the

			// data read on an ArrayList so that we can printed the

			// content of the excel to the console.

			Iterator rows = sheet.rowIterator();

			while (rows.hasNext()) {

				XSSFRow row = (XSSFRow) rows.next();

				TradeInfo info = null;
				try {
					String name = row.getCell(0).getStringCellValue();
					double revenueTY = row.getCell(1).getNumericCellValue();
					double revenueLY = row.getCell(3).getNumericCellValue();

					info = new TradeInfo();
					info.setName(name);
					info.setMarketRevenueTY(revenueTY);
					info.setMarketRevenueLY(revenueLY);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// ignore
					System.out.println("ignored exception for headers");

				}
				sheetData.add(info);
			}
			sheetData.removeAll(Collections.singletonList(null));

			
				Collection<String> companyNames = getCompanyNames(sheetData);

				for (String name : companyNames) {

					List<TradeInfo> companyInfoList = getCompanyInfoListByName(
							sheetData, name);

					Double totalRevenueTY = 0.0;
					Double totalRevenueLY = 0.0;

					for (TradeInfo tradeInfo : companyInfoList) {

						totalRevenueTY += tradeInfo.getMarketRevenueTY();
						totalRevenueLY += tradeInfo.getMarketRevenueLY();

					}

					double averageRevenueTY = totalRevenueTY
							/ companyInfoList.size();
					double averageRevenueLY = totalRevenueLY
							/ companyInfoList.size();

					/*
					 System.out.println("company name: " + name
							+ " average values are: " + averageRevenueTY + " "
							+ averageRevenueLY);
							*/
					 for (TradeInfo tradeInfo : companyInfoList) {
						 tradeInfo.setTotalMarketRevenueTY(totalRevenueTY);
						 tradeInfo.setTotalMarketRevenueLY(totalRevenueLY);
						 tradeInfo.setAverageMarketRevenueTY(averageRevenueTY);
						 tradeInfo.setAverageMarketRevenueLY(averageRevenueLY);
					}

				}
				XSSFWorkbook newWB          = new XSSFWorkbook();
				XSSFSheet newSheet = newWB.createSheet();
				CellStyle cellStyle = newWB.createCellStyle();

				
				createHeader(newSheet,cellStyle);
				System.out.println("HEADER CREATED");
				
				
				//for (int j = 39999; j <80000; j++) {
					for (int j = 1; j <40000; j++) {
					XSSFRow row     = newSheet.createRow(j); 
					TradeInfo tradeInformation=sheetData.get(j-1);
					
					XSSFCell cell   = row.createCell((short)0); 
					cell.setCellValue(tradeInformation.getName()); 
					row.createCell((short)1).setCellValue(tradeInformation.getMarketRevenueTY()); 
					row.createCell((short)2).setCellValue(tradeInformation.getAverageMarketRevenueTY());
					row.createCell((short)3).setCellValue(tradeInformation.getMarketRevenueLY());
					row.createCell((short)4).setCellValue(tradeInformation.getAverageMarketRevenueLY());
					row.createCell((short)5).setCellValue(tradeInformation.getTotalMarketRevenueTY());
					row.createCell((short)6).setCellValue(tradeInformation.getTotalMarketRevenueLY());

					
				}
				/*
				 * long i=1;
				for (TradeInfo tradeInformation : sheetData) {
					
					XSSFRow row     = newSheet.createRow(i); 
					i++;
					
					XSSFCell cell   = row.createCell((short)0); 
					cell.setCellValue(tradeInformation.getName()); 
					row.createCell((short)2).setCellValue(tradeInformation.getMarketRevenueTY()); 
					row.createCell((short)3).setCellValue(tradeInformation.getAverageMarketRevenueTY());
					row.createCell((short)4).setCellValue(tradeInformation.getMarketRevenueLY());
					row.createCell((short)5).setCellValue(tradeInformation.getAverageMarketRevenueLY());

				}
				*/
				/*
				while (rows2.hasNext()) {
				
				XSSFRow row = (XSSFRow) rows2.next();
				
				String compName = row.getCell(0).getStringCellValue();
				if(!compName.startsWith("Head"))
				{

				List<TradeInfo> companyList=getCompanyInfoListByName(sheetData, compName);
				double averageTY=companyList.get(0).getAverageMarketRevenueTY();
				double averageLY=companyList.get(0).getAverageMarketRevenueLY();
				
				
				try {
					Cell cellAveargeTY = row.createCell(2);
					cellAveargeTY.setCellType(Cell.CELL_TYPE_NUMERIC);
					cellAveargeTY.setCellValue(averageTY);
					
					Cell cellAveargeLY = row.createCell(4);
					cellAveargeLY.setCellType(Cell.CELL_TYPE_NUMERIC);
					cellAveargeLY.setCellValue(averageLY);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("null pointer a sebebiyet veren caompany name: "+compName);
				}
				
				}
				}
				*/

			System.out.println("writing output file");
			FileOutputStream fileOut = new FileOutputStream("C:\\Kitap3.xlsx");
			newWB.write(fileOut);
	        fileOut.close();
	        System.out.println("COMPLETED");


		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			if (fis != null) {

				fis.close();

			}

		}

		// showExelData(sheetData);

	}

	private static List<TradeInfo> getCompanyInfoListByName(
			List<TradeInfo> sheetData, String name) {
		// TODO Auto-generated method stub
		List<TradeInfo> list = new ArrayList<TradeInfo>();
		for (TradeInfo tradeInfo : sheetData) {

			if (name!=null && name.equals(tradeInfo.getName()))
				list.add(tradeInfo);
		}
		return list;
	}

	private static void showExelData(List<TradeInfo> sheetData) {

		for (TradeInfo tradeInfo : sheetData) {
			System.out.println(tradeInfo.toString());
		}

	}

	private static Collection<String> getCompanyNames(List<TradeInfo> sheetData) {
		Set<String> distinctNmesList = new HashSet<String>();
		for (TradeInfo tradeInfo : sheetData) {
//			if(tradeInfo.getName()==null)
//				System.out.println("this guys name is null: "+tradeInfo.toString());

			distinctNmesList.add(tradeInfo.getName());
		}
		System.out.println("there are " + distinctNmesList.size()
				+ " different companies");

		return distinctNmesList;

	}
	
	private static void createHeader(XSSFSheet sheet, CellStyle cellStyle) {
		XSSFRow row     = sheet.createRow((short)0); 
		cellStyle.setFillBackgroundColor(new HSSFColor.YELLOW().getIndex());

		
		XSSFCell cell   = row.createCell((short)0); 
		cell.setCellValue("Head Office"); 
		cell.setCellStyle(cellStyle);
		
		XSSFCell cell2   = row.createCell((short)1); 
		cell2.setCellValue("Market Revenue TY");
		cell2.setCellStyle(cellStyle);
		
		XSSFCell cell4   = row.createCell((short)3); 
		cell4.setCellValue("Market Revenue LY");
		cell4.setCellStyle(cellStyle);
		
		cellStyle.setFillBackgroundColor(new HSSFColor.RED().getIndex());
		
		XSSFCell cell3   = row.createCell((short)2); 
		cell3.setCellValue("Average Market Revenue TY");
		cell3.setCellStyle(cellStyle);

		XSSFCell cell5   = row.createCell((short)4); 
		cell5.setCellValue("Average Market Revenue LY");
		cell5.setCellStyle(cellStyle);
		
		XSSFCell cell6   = row.createCell((short)5); 
		cell6.setCellValue("TOTAL Market Revenue TY");
		cell6.setCellStyle(cellStyle);

		XSSFCell cell7   = row.createCell((short)6); 
		cell7.setCellValue("TOTAL Market Revenue LY");
		cell7.setCellStyle(cellStyle);
		
//		row.createCell((short)1).setCellValue("Market Revenue TY");
//		row.createCell((short)2).setCellValue("Average Market Revenue TY"); 
//		row.createCell((short)3).setCellValue("Market Revenue LY");
//		row.createCell((short)4).setCellValue("Average Market Revenue LY");
	}
}
