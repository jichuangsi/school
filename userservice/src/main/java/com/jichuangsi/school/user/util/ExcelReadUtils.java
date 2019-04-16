package com.jichuangsi.school.user.util;

import com.jichuangsi.school.user.constant.ResultCode;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelReadUtils {

    public static final List<String> getOneRow(int rowNum, MultipartFile file) throws Exception {
        List<String> results = new ArrayList<String>();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            /*int numberOfSheets = workbook.getNumberOfSheets();*/
            HSSFSheet sheet = workbook.getSheetAt(0);
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            //...
            HSSFRow row = sheet.getRow(rowNum);
            int physicalNumberOfCells = row.getPhysicalNumberOfCells();
            for (int i = 0; i < physicalNumberOfCells; i++) {
                results.add(getCellString(row.getCell(i)));
            }
        } catch (Exception e) {
            throw new Exception(ResultCode.EXCEL_IMPORT_MSG);
        }
        return results;
    }

    private static String getCellString(HSSFCell cell){
        String result = "";
        if (null != cell) {
            if (cell.getCellType() == CellType.STRING) {
                result = cell.getStringCellValue();
            }else if (cell.getCellType() == CellType.NUMERIC){
                Date date = cell.getDateCellValue();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                result = sdf.format(date);
            }
        }
        return result;
    }
}
