/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maxent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Phu
 */
public class Maxent {

    public static void exportFile(String path) {
        File src = new File(path);
        XSSFWorkbook wb = null;
        BufferedWriter data = null;
        try {
            FileInputStream fis = new FileInputStream(src);
            wb = new XSSFWorkbook(fis);

            data = new BufferedWriter(new FileWriter("src/resources/corpus_sentiment.txt"));

            XSSFSheet sheet1 = wb.getSheetAt(0);

            int rowCount = sheet1.getLastRowNum();
            for (int i = 0; i < rowCount; i++) {
                String s = sheet1.getRow(i).getCell(0).getStringCellValue() + " ";
                int num = Integer.valueOf(sheet1.getRow(i).getCell(1).getStringCellValue());
                if ((s.length() != 0)) {
                    data.write(num + "\t" + s + "\n");
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
                data.flush();
                data.close();
            } catch (IOException ex) {
                Logger.getLogger(Maxent.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public static void main(String[] args) {
        exportFile("data.xlsx");
    }

}
