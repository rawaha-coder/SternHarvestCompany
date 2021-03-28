package harvest.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteExcelFile {

//    public void writeHarvesters(List<Harvest> harvestList, String pathName) {
//
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet("Harvesters work");
//
//        XSSFRow rowHead = sheet.createRow((short) 0);
//
//        Cell cellId = rowHead.createCell(0);
//        cellId.setCellStyle(headCellStyle(workbook));
//        cellId.setCellValue("ID");
//
//        Cell cellName = rowHead.createCell(1);
//        cellName.setCellStyle(headCellStyle(workbook));
//        cellName.setCellValue("Employee Name");
//
//        Cell cellQuantity = rowHead.createCell(2);
//        cellQuantity.setCellStyle(headCellStyle(workbook));
//        cellQuantity.setCellValue("Quantity Total");
//
//        for (int i = 0; i < harvestList.size(); i++) {
//            XSSFRow row = sheet.createRow(i + 1);
//            row.createCell(0).setCellValue(harvestList.get(i).getEmployeeID());
//            row.createCell(1).setCellValue(harvestList.get(i).getEmployeeName());
//            row.createCell(2).setCellType(CellType.NUMERIC);
//            row.getCell(2).setCellValue(0.0);
//        }
//
//        try {
//            //Write the workbook in file system
//            FileOutputStream out = new FileOutputStream(new File(pathName + ".xlsx"));
//            workbook.write(out);
//            out.close();
//            System.out.println("Employee written successfully on disk.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void writeHarvest(List<Harvest> harvestList, String name) {
//        //Blank workbook
//        XSSFWorkbook workbook = new XSSFWorkbook();
//
//        //Create a blank sheet
//        XSSFSheet sheet = workbook.createSheet("Harvesters work");
//
//        Map<String, Object[]> harvestTreeMap = new TreeMap<String, Object[]>();
//        harvestTreeMap.put("1", new Object[]{"ID", "NAME", "LASTNAME"});
//        int i = 1;
//        for (Harvest h : harvestList) {
//            harvestTreeMap.put(String.valueOf(i), new Object[]{h.getEmployeeID(), h.getEmployeeName(), h.getAllQuantity()});
//            i++;
//        }
//
//        Set<String> keySet = harvestTreeMap.keySet();
//        int rowNum = 0;
//        for (String key : keySet) {
//            Row row = sheet.createRow(rowNum++);
//            Object[] objArr = harvestTreeMap.get(key);
//            int cellNum = 0;
//            for (Object obj : objArr) {
//                Cell cell = row.createCell(cellNum++);
//                if (obj instanceof String)
//                    cell.setCellValue((String) obj);
//                else if (obj instanceof Integer)
//                    cell.setCellValue((Integer) obj);
//            }
//        }
//
//        try {
//            //Write the workbook in file system
//            FileOutputStream out = new FileOutputStream(new File(name + ".xlsx"));
//            workbook.write(out);
//            out.close();
//            System.out.println("Employee written successfully on disk.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private CellStyle headCellStyle(XSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle(); // Creating Style
        style.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(FillPatternType.FINE_DOTS);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 13);
        font.setFontName("Times New Roman");
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
