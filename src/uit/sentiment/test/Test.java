/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.sentiment.test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import uit.sentiment.io.ReadTextFile;
import uit.sentiment.maxent.MaximunEntropy;

/**
 *
 * @author Phu
 */
public class Test {

    private static final String path = "src/resources/data/corpus_full.txt";
    private static final int numFolds = 4;
    private static final int numClasses = 4;
    private static int numSentences = 16000;
    private static ReadTextFile reader = new ReadTextFile();
    private static MaximunEntropy maxent = new MaximunEntropy();

    // trả về confusion matrix
    public static int[][] computeType1(int start, int end) {
        int[][] confMatrix = new int[numClasses][numClasses];

        // load các câu train
        List<String> trainingSentences = new ArrayList<>();
        trainingSentences = reader.readTextFile(end, numSentences, path);

        // load các câu test
        List<String> testingSentences = new ArrayList<>();
        testingSentences = reader.getSentences(start, end, path);

        // load kết quả
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        result = reader.getResult(start, end, path);

        // train data bằng dl đưa vào
        maxent.trainingData2(trainingSentences);

        // dự đoán
//        for(String str : testingSentences){
//            System.out.println(str + "\t" + maxent.predictSentence(str));
//        }
        System.out.println("\t" + maxent.predictSentence("bài_tập lớn mà thầy wzjwz74 đưa ra cũng khá là chung_chung , không yêu_cầu rõ_ràng , cụ_thể"));

        return confMatrix;

    }

    public static void main(String[] args) {
        computeType1(10, 20);
    }
}
