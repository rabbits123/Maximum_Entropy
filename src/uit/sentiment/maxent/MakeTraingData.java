/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.sentiment.maxent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static uit.sentiment.preprocessing.Preprocessing.checkAccent;
import static uit.sentiment.preprocessing.Preprocessing.loadStopwords;
import static uit.sentiment.preprocessing.Preprocessing.preProcess;
import static uit.sentiment.preprocessing.Preprocessing.removeStopWord;

/**
 *
 * @author Phu
 */
public class MakeTraingData {

    private static List<String> listStopwords;

    public static String preprocessSentence(String str) {
        if (checkAccent(str)) {
            //str = preProcess(str, "\\d|\\s+");
            // remove stopword
            str = removeStopWord(str);
            str = preProcess(str, "\\.|,|\\?|!|\"|'|#|;|:|\\(|\\)|\\[|\\]|-|wzjwz|%|franction|:v|:3|:p");
            str = str.toLowerCase().replaceAll("\\d+", "#num");
            return str;
        }
        return null;
    }

    public MakeTraingData() {
        listStopwords = loadStopwords();
    }

    public static boolean checkAccent(String str) {
        if (str != null) {
            str = str.toLowerCase();

            char[] chr = {'à', 'á', 'ả', 'ã', 'ạ', 'è', 'é', 'ẻ', 'ẽ', 'ẹ', 'â', 'ă', 'ấ', 'ầ', 'ẩ', 'ẫ', 'ậ', 'ằ', 'ắ', 'ẳ', 'ẵ', 'ậ', 'ê', 'ề', 'ế', 'ể', 'ễ', 'ệ', 'ò', 'ó', 'ỏ', 'õ', 'ọ', 'ồ', 'ô', 'ố', 'ổ', 'ỗ', 'ộ', 'ơ', 'ờ', 'ớ', 'ở', 'ỡ', 'ợ', 'ù', 'ú', 'ủ', 'ũ', 'ụ', 'ư', 'ừ', 'ứ', 'ử', 'ữ', 'ự', 'ỳ', 'ý', 'ỷ', 'ỹ', 'ỵ', 'đ', 'í', 'ì', 'ỉ', 'ĩ', 'ị'};

            for (int i = 0; i < str.length(); i++) {
                for (int j = 0; j < chr.length; j++) {
                    if (str.charAt(i) == chr[j]) {
                        return true;
                    }
                }
            }
            System.out.println(str);
        }
        return false;
    }

    public static String removeStopWord(String str) {
        StringBuilder builder = new StringBuilder();
        String[] arr = (str.toLowerCase().split("\\s"));
        List<String> listWord = new ArrayList<String>();
        for (String s : arr) {
            listWord.add(s);
        }
        listWord.removeAll(listStopwords);
        for (String build : listWord) {
            builder.append(build + " ");
        }
        return builder.toString().trim();
    }

    private static void parse() {
        File src = new File("E:\\Maxent\\src\\resources\\data\\corpus_full.xlsx");

        BufferedWriter bw1 = null;
        BufferedWriter bw2 = null;
        try {
            FileWriter fw1 = new FileWriter(new File("E:\\Maxent\\src\\resources\\data\\topic.txt"));
            bw1 = new BufferedWriter(fw1);

            FileWriter fw2 = new FileWriter(new File("E:\\Maxent\\src\\resources\\data\\sentiment.txt"));
            bw2 = new BufferedWriter(fw2);

            XSSFWorkbook wb = null;
            try {
                FileInputStream fis = new FileInputStream(src);
                wb = new XSSFWorkbook(fis);
                XSSFSheet sheet1 = wb.getSheetAt(0);

                for (int i = 0; i < sheet1.getLastRowNum(); i++) {

                    String sentence = sheet1.getRow(i).getCell(1).getStringCellValue();
                    sentence = preprocessSentence(sentence);
                    if (checkAccent(sentence) && sentence.length() != 0) {

                        String topLabel;
                        String sentLabel;
                        try {
                            topLabel = sheet1.getRow(i).getCell(4).getNumericCellValue() + "";
                            sentLabel = sheet1.getRow(i).getCell(5).getNumericCellValue() + "";
                        } catch (java.lang.IllegalStateException e) {
                            topLabel = sheet1.getRow(i).getCell(4).getStringCellValue();
                            sentLabel = sheet1.getRow(i).getCell(5).getStringCellValue();
                        }
                        bw1.write(sentLabel + "\t" + sentence + "\n");
                        bw2.write(topLabel + "\t" + sentence + "\n");
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

        } catch (IOException ex) {
            Logger.getLogger(MakeTraingData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw1.flush();
                bw2.flush();
                bw1.close();
                bw2.close();
            } catch (IOException ex) {
                Logger.getLogger(MakeTraingData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void main(String[] args) {

        MakeTraingData dt = new MakeTraingData();
        dt.parse();
    }
}
