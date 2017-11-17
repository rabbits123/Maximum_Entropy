package uit.sentiment.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileExcel {
    public FileExcel() {
    }
    
    public static HashMap<Integer, ArrayList<List<String>>> readXLSFile(InputStream is){

        try{
            HSSFWorkbook workbook = new HSSFWorkbook(is);
            HashMap<Integer, ArrayList<List<String>>> data = new HashMap<>();
            
            Iterator<Sheet> sheets = workbook.sheetIterator();
            int i = 0;
            while(sheets.hasNext()){
                Sheet sheet = sheets.next();
                Iterator<Row> rows = sheet.iterator();
                ArrayList<List<String>> lsheet = new ArrayList<>();
                while(rows.hasNext()){
                    Row row = rows.next();
                    Iterator<Cell> cells = (Iterator<Cell>) row.cellIterator();
                    List<String> lRow = new ArrayList<>();
                    while(cells.hasNext()){
                        Cell cell = cells.next();
                        
                        switch(cell.getCellType()){
                            case Cell.CELL_TYPE_STRING:
                                lRow.add(cell.getStringCellValue().getBytes(Charset.forName("UTF-8")).toString());
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                lRow.add(""+cell.getNumericCellValue());
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                lRow.add(""+cell.getNumericCellValue());
                                break;
                            default :
                                break;
                        }
                    }
                    lsheet.add(lRow);
                }
                data.put(i, lsheet);
                i++;
            }
            is.close();
            return data;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    
    }
    
    public static HashMap<Integer, ArrayList<List<String>>> readXLSXFile(InputStream is){

        try{
            XSSFWorkbook workbook = new XSSFWorkbook(is);
//            XSSFFont wbFont;
//            wbFont = workbook.createFont();
//            wbFont.setCharSet(XSSFFont.ANSI_CHARSET);
//            XSSFCellStyle cellStyle =workbook.createCellStyle();
//            cellStyle.setFont(wbFont);
            HashMap<Integer, ArrayList<List<String>>> data = new HashMap<>();
            
            Iterator<Sheet> sheets = workbook.sheetIterator();
            int i = 0;
            while(sheets.hasNext()){
                Sheet sheet = sheets.next();
                Iterator<Row> rows = sheet.iterator();
                ArrayList<List<String>> lsheet = new ArrayList<>();
                while(rows.hasNext()){
                    Row row = rows.next();
                    Iterator<Cell> cells = (Iterator<Cell>) row.cellIterator();
                    List<String> lRow = new ArrayList<>();
                    while(cells.hasNext()){
                        Cell cell = cells.next();
                        switch(cell.getCellType()){
                            case Cell.CELL_TYPE_STRING:
                                if(cell.getStringCellValue().length()!=0){
                                    lRow.add(new String(cell.getStringCellValue().getBytes("utf-8"), "UTF-8"));
                                }
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                lRow.add(""+cell.getNumericCellValue());
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                lRow.add(""+cell.getNumericCellValue());
                                break;
                            default :
                                break;
                        }
                    }
                    lsheet.add(lRow);
                }
                data.put(i, lsheet);
                i++;
            }
            is.close();
            return data;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void writeXLSFile(String path, List<List<String>> data){

        FileOutputStream fos = null;
        try{
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet newSheet = workbook.createSheet();
            int rownum = 0;
            
            for (List<String> list : data) {
                HSSFRow row = newSheet.createRow(rownum++);
                int cellnum = 0;
                for (String value : list) {
                    Cell cell = row.createCell(cellnum++);
                    cell.setCellValue((String) value);
                }
            }
            
            fos = new FileOutputStream(path,false);
            workbook.write(fos);
            System.out.println("Writing on XLSX file Finished ...");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileExcel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(FileExcel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void writeXLSXFile(String path, List<List<String>> data){
        FileOutputStream fos = null;
        try{
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet newSheet = workbook.createSheet();
            int rownum = newSheet.getLastRowNum(); 
            
            for (List<String> list : data) {
                XSSFRow row = newSheet.createRow(rownum++);
                int cellnum = 0;
                for (String value : list) {
                    Cell cell = row.createCell(cellnum++);
                    cell.setCellValue((String) value);
                }
            }
            
            fos = new FileOutputStream(path);
            workbook.write(fos);
            System.out.println("Writing on XLSX file Finished ...");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileExcel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(FileExcel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
