package harvest.util;

import harvest.model.Quantity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadExcelFile {

    public List<Quantity> readHarvestFile(File file) {
        List<Quantity> list = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                Quantity quantity = new Quantity();
                Cell cellId = row.getCell(0);
                Cell cellName = row.getCell(1);
                Cell cellQuantity = row.getCell(row.getPhysicalNumberOfCells() - 1);
                if (evaluator.evaluateFormulaCell(cellQuantity) == CellType.NUMERIC) {
                    quantity.setAllQuantity(cellQuantity.getNumericCellValue());
                }
                quantity.getEmployee().setEmployeeId((int) cellId.getNumericCellValue());
                quantity.getEmployee().setEmployeeFullName(cellName.getStringCellValue());
                if (quantity.getAllQuantity() > 0) {
                    list.add(quantity);
                }
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("return excel list");
        return list;
    }

//    public List<Harvest> readHarvestFile(File file) {
//        List<Harvest> harvestList = new ArrayList<>();
//        try {
//            XSSFWorkbook workbook = new XSSFWorkbook(file);
//            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
//            XSSFSheet sheet = workbook.getSheetAt(0);
//            for (Row row : sheet) {
//                if (row.getRowNum() == 0) {
//                    continue;
//                }
//                Harvest harvest = new Harvest();
//                Cell cellId = row.getCell(0);
//                Cell cellName = row.getCell(1);
//                Cell cellQuantity = row.getCell(row.getPhysicalNumberOfCells() - 1);
//                if (evaluator.evaluateFormulaCell(cellQuantity) == CellType.NUMERIC) {
//                    harvest.setAllQuantity(cellQuantity.getNumericCellValue());
//                }
//                harvest.setEmployeeID((int) cellId.getNumericCellValue());
//                harvest.setEmployeeName(cellName.getStringCellValue());
//                if (harvest.getAllQuantity() > 0) {
//                    harvestList.add(harvest);
//                }
//            }
//            workbook.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return harvestList;
//    }
}
