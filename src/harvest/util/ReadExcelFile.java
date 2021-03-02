package harvest.util;

import harvest.model.Harvest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadExcelFile {

    public List<Harvest> readHarvestWork(File file) {
        List<Harvest> harvestList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()){
                Row row = rowIterator.next();
                if (row.getRowNum() == 0){
                    continue;
                }
                Harvest harvest = new Harvest();
                Cell cellId = row.getCell(0);
                Cell cellName = row.getCell(1);
                Cell cellQuantity = row.getCell(2);
                harvest.setEmployeeID((int)cellId.getNumericCellValue());
                harvest.setEmployeeName(cellName.getStringCellValue());
                harvest.setAllQuantity(cellQuantity.getNumericCellValue());
                if (harvest.getAllQuantity() > 0){
                    harvestList.add(harvest);
                }
            }
            workbook.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return harvestList;
    }

    private double formatCell(XSSFWorkbook workbook, Cell cell){
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        double num = 0.0;
        if (cell.getCellType() == CellType.FORMULA) {
            switch (evaluator.evaluateFormulaCell(cell)) {
                case BOOLEAN:
                    System.out.println(cell.getBooleanCellValue());
                    break;
                case NUMERIC:
                    num = cell.getNumericCellValue();
                    System.out.println(cell.getNumericCellValue());
                    break;
                case STRING:
                    System.out.println(cell.getStringCellValue());
                    break;
            }
        }
        return num;
    }

    public void readSheet(File file){
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            DataFormatter dataFormatter = new DataFormatter();
            Iterator<Sheet> sheetIterator = workbook.iterator();
            while (sheetIterator.hasNext()){
                Sheet sheet = sheetIterator.next();
                System.out.println("Sheet name: " + sheet.getSheetName());
                System.out.println("==================================");
                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()){
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.iterator();
                    while (cellIterator.hasNext()){
                        Cell cell = cellIterator.next();
                        String value = dataFormatter.formatCellValue(cell);
                        System.out.println(value);
                    }
                    System.out.println();
                }
            }
            workbook.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readFormula(File file){
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter dataFormatter = new DataFormatter();
            XSSFSheet sheet = workbook.getSheetAt(0);
                System.out.println("Sheet name: " + sheet.getSheetName());
                System.out.println("==================================");
                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()){
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.iterator();
                    while (cellIterator.hasNext()){
                        Cell cell = cellIterator.next();
                        String value = dataFormatter.formatCellValue(cell);
                        System.out.println(value);
                        if (cell.getCellType() == CellType.FORMULA) {
                            switch (evaluator.evaluateFormulaCell(cell)) {
                                case BOOLEAN:
                                    System.out.println(cell.getBooleanCellValue());
                                    break;
                                case NUMERIC:
                                    System.out.println(cell.getNumericCellValue());
                                    break;
                                case STRING:
                                    System.out.println(cell.getStringCellValue());
                                    break;
                            }
                        }
                    }
                    System.out.println();
                }
            workbook.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //Fetch the Last Cached Value
    public void readCachedValue(File file){
        try {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            CellAddress cellAddress = new CellAddress("C2");
            Row row = sheet.getRow(cellAddress.getRow());
            Cell cell = row.getCell(cellAddress.getColumn());

            if (cell.getCellType() == CellType.FORMULA) {
                switch (cell.getCachedFormulaResultType()) {
                    case BOOLEAN:
                        System.out.println(cell.getBooleanCellValue());
                        break;
                    case NUMERIC:
                        System.out.println(cell.getNumericCellValue());
                        break;
                    case STRING:
                        System.out.println(cell.getRichStringCellValue());
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
