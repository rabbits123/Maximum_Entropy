
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Phu
 */
public class ParseDependencyRelation {

    public static void exportFile(int start, int end, String fileName) {
        File src = new File(fileName);
        XSSFWorkbook wb = null;
        BufferedWriter dependencyTXT = null;
        
        try {
            FileInputStream fis = new FileInputStream(src);
            wb = new XSSFWorkbook(fis);
            
           dependencyTXT  = new BufferedWriter(new FileWriter("dependency.txt"));
     
            XSSFSheet sheet1 = wb.getSheetAt(0);

            int rowCount = sheet1.getLastRowNum();
            for (int i = start; i <rowCount; i++) {
                String str = sheet1.getRow(i).getCell(0).getStringCellValue();
                
                List<String>s = null;
                
                dependencyTXT.write(i+"\n"+str+"\n");
                
                for (String temp : s){
                    dependencyTXT.write(temp+"\n");
                }
                
                
                
                 
            }
            
        } catch (FileNotFoundException ex) {
            System.out.println("Không tìm thấy file");
        } catch (IOException ex) {
            System.out.println("Lỗi");
        } finally {
            try {
                wb.close();
            } catch (IOException ex) {
                System.out.println("Lỗi");
            }
            try {
                dependencyTXT.flush();
                
            } catch (IOException ex) {
                Logger.getLogger(ParseDependencyRelation.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }
        
    }

}
