/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.sentiment.maxent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uit.sentiment.preprocessing.Preprocessing;

/**
 *
 * @author Phu
 */
public class FeedbackParser {
    private static List<Feedback> feebackList = new ArrayList<>();
    private Preprocessing pre;
    private MaximumEntropy topic = new MaximumEntropy();
    private MaximumEntropy sentiment = new MaximumEntropy();
    public void getData(String path) {
        sentiment.classifier = sentiment.loadModel("src\\resources\\MaxentSentiment.model");
        topic.classifier = topic.loadModel("src\\resources\\MaxentTopic.model");
        
        pre = new Preprocessing();
        
        File src = new File(path);
        XSSFWorkbook wb = null;
        try {
            FileInputStream fis = new FileInputStream(src);
            wb = new XSSFWorkbook(fis);
            XSSFSheet sheet1 = wb.getSheetAt(0);
            int end = sheet1.getLastRowNum();
            for (int i = 1; i < end; i++) {
                String maKhoa = sheet1.getRow(i).getCell(2).getStringCellValue();
                String tenGV = sheet1.getRow(i).getCell(4).getStringCellValue();
                String maMonHoc = sheet1.getRow(i).getCell(5).getStringCellValue();
                String tenMonHoc = sheet1.getRow(i).getCell(6).getStringCellValue();

                String sentence1 = "";
                String sentence2 = "";

                try {
                    sentence1 = sheet1.getRow(i).getCell(9).getStringCellValue();
                    sentence2 = sheet1.getRow(i).getCell(10).getStringCellValue();
                } catch (java.lang.NullPointerException e) {
                    sentence1 = "";
                    sentence2 = "";
                } catch (java.lang.IllegalStateException e) {
                    sentence1 = "";
                    sentence2 = "";
                }
                if(sentence1.trim().length() != 0){
                    Feedback fb = new Feedback(sentence1, maKhoa, tenGV, maMonHoc, tenMonHoc,
                            sentiment.predictSentence(pre.preprocessSentence(sentence1)), 
                            topic.predictSentence(pre.preprocessSentence(sentence1)));
                    feebackList.add(fb);
                }
                if(sentence2.trim().length() != 0){
                    Feedback fb = new Feedback(sentence2, maKhoa, tenGV, maMonHoc, tenMonHoc,
                            sentiment.predictSentence(pre.preprocessSentence(sentence2)), 
                            topic.predictSentence(pre.preprocessSentence(sentence2)));
                    feebackList.add(fb);
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
        }
    }
}
