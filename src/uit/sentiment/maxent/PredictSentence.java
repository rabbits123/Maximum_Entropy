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
import uit.sentiment.io.ReadTextFile;
import uit.sentiment.preprocessing.Preprocessing;
import static uit.sentiment.preprocessing.Preprocessing.preprocessSentence;

public class PredictSentence {

    private static Preprocessing pre = new Preprocessing();
    private static ReadTextFile reader = new ReadTextFile();

    public PredictSentence() {
    }

    public static void main(String[] args) {

        MaximumEntropy topic = new MaximumEntropy();

        //sentiment
        MaximumEntropy sentiment = new MaximumEntropy();
        sentiment.classifier = sentiment.loadModel("src\\resources\\MaxentSentiment.model");

        topic.classifier = topic.loadModel("src\\resources\\MaxentTopic.model");
        sentiment.classifier = sentiment.loadModel("E:\\Maxent\\src\\resources\\MaxentSentiment.model");

        List<String> sent = new ArrayList<>();

        File src = new File("src\\resources\\data\\corpus_full.xlsx");
        XSSFWorkbook wb = null;
        try {
            FileInputStream fis = new FileInputStream(src);
            wb = new XSSFWorkbook(fis);
            XSSFSheet sheet1 = wb.getSheetAt(0);

            for (int i = 0; i < sheet1.getLastRowNum(); i++) {
                String sentence = sheet1.getRow(i).getCell(1).getStringCellValue();
                System.out.println(sentiment.predictSentence(preprocessSentence(sentence))
                        + "\t" + topic.predictSentence(preprocessSentence(sentence)) + "\t" + sentence);
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
