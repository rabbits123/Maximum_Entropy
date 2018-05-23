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
import uit.sentiment.maxent.MaximumEntropy;
import uit.sentiment.test.Measures;

/**
 *
 * @author Phu
 */
public class Test {

    private static final String path = "src/resources/data/corpus_topic.txt";
    private static int numFolds = 5;
    private static int numClasses = 4;
    private static int numSentences = 16000;
    private static ReadTextFile reader = new ReadTextFile();

    public Test(int numFolds, int numClasses, int numSentences) {
        this.numFolds = numFolds;
        this.numClasses = numClasses;
        this.numSentences = numSentences;
    }
    

    public Test() {

    }

    // trả về confusion matrix
    public static int[][] computeType1(int start, int end) {
        MaximumEntropy maxent = new MaximumEntropy();
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
        int predict;
        int real;
        int i = 1;
        for (String str : testingSentences) {
            predict = Integer.parseInt(maxent.predictSentence(str));
            real = result.get(str);
            confMatrix[real][predict] += 1;
        }

        return confMatrix;
    }

    public static int[][] computeType2(int start, int end) {
        MaximumEntropy maxent = new MaximumEntropy();
        int[][] confMatrix = new int[numClasses][numClasses];

        // load các câu train
        List<String> trainingSentences = new ArrayList<>();
        trainingSentences = reader.readTextFile(1, start, path);

        // load các câu test
        List<String> testingSentences = new ArrayList<>();
        testingSentences = reader.getSentences(start, end, path);

        // load kết quả
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        result = reader.getResult(start, end, path);

        // train data bằng dl đưa vào
        maxent.trainingData2(trainingSentences);

        // dự đoán
        int predict;
        int real;
        int i = 1;
        for (String str : testingSentences) {
            predict = Integer.parseInt(maxent.predictSentence(str));
            real = result.get(str);
            confMatrix[real][predict] += 1;
        }

        return confMatrix;
    }

    public static int[][] computeType3(int start, int end) {
        MaximumEntropy maxent = new MaximumEntropy();
        int[][] confMatrix = new int[numClasses][numClasses];

        // load các câu train
        List<String> trainingSentences = new ArrayList<>();
        trainingSentences = reader.readTextFile(1, start, end, numSentences, path);

        // load các câu test
        List<String> testingSentences = new ArrayList<>();
        testingSentences = reader.getSentences(start, end, path);

        // load kết quả
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        result = reader.getResult(start, end, path);

        // train data bằng dl đưa vào
        maxent.trainingData2(trainingSentences);

        // dự đoán
        int predict;
        int real;
        int i = 1;
        for (String str : testingSentences) {
            predict = Integer.parseInt(maxent.predictSentence(str));
            real = result.get(str);
            confMatrix[real][predict] += 1;
        }
        return confMatrix;
    }

    public int totalArray(int[] arr) {
        int total = 0;
        for (int i = 0; i < arr.length; i++) {
            total += arr[i];
        }
        return total;
    }

    public static Measures compute(int[][] confMatrix) {
        Measures mea = new Measures();
        int totalRightAnswer = 0;
        int totalAnswer = 0;
        int[] rows = new int[confMatrix.length];
        int[] cols = new int[confMatrix[0].length];
        int tempRows = 0, tempCols = 0;
        for (int i = 0; i < confMatrix.length; i++) {
            for (int j = 0; j < confMatrix[i].length; j++) {
                totalAnswer += confMatrix[i][j];
                tempRows += confMatrix[i][j];
                tempCols += confMatrix[j][i];
            }
            rows[i] = tempRows;
            tempRows = 0;
            cols[i] = tempCols;
            tempCols = 0;
        }
        List<Double> listPre = new ArrayList<>();
        List<Double> listRec = new ArrayList<>();
        List<Double> listF1 = new ArrayList<>();
        
        int []labelList = new int[confMatrix.length];
        
        for(int i=0; i < confMatrix.length; i++){
            for(int j =0 ; j < confMatrix[i].length;j++){
                labelList[i] += confMatrix[i][j];
            }
        }
        
        double tempPre = 0;
        double tempRec = 0;
        
        for (int i = 0; i < confMatrix.length; i++) {
            totalRightAnswer += confMatrix[i][i];
            double rec = (double) confMatrix[i][i] / (cols[i]);
            double pre = (double) confMatrix[i][i] / (rows[i]);
            double f1 = (2 * (pre * rec)) / (pre + rec);
            
            tempPre += labelList[i] *  pre;
            tempRec += labelList[i] *  rec;
            
            listPre.add(pre);
            listRec.add(rec);
            listF1.add(f1);
        }
        mea.setAccuracy((double) totalRightAnswer / totalAnswer);
        double microAvgPre = tempPre/totalAnswer;
        double microAvgRec = tempRec/totalAnswer;
        double microAvgF1 = (2 * (microAvgPre * microAvgRec)) / (microAvgPre + microAvgRec);
        
        mea.setMicroAverageF1(microAvgF1);
        mea.setMicroAverageRec(microAvgRec);
        mea.setMicroAveragePre(microAvgPre);
        mea.setPrecision(listPre);
        mea.setRecall(listRec);
        mea.setF1_score(listF1);
        return mea;
    }

    public static List<Double> avg(List<Double> l1, List<Double> l2, List<Double> l3, List<Double> l4, List<Double> l5) {
        List<Double> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            list.add((l1.get(i) + l2.get(i) + l3.get(i) + l4.get(i) + l5.get(i)) / 5);
        }
        return list;
    }

    public static Measures computeAverage(Measures m1, Measures m2, Measures m3, Measures m4, Measures m5) {
        Measures avg = new Measures();
        avg.setAccuracy(((m1.getAccuracy()) + (m2.getAccuracy()) + (m3.getAccuracy()) + (m4.getAccuracy()) + (m5.getAccuracy())) / 5);
        for (int i = 0; i < 5; i++) {
            avg.setPrecision(avg(m1.getPrecision(), m2.getPrecision(), m3.getPrecision(), m4.getPrecision(), m5.getPrecision()));
            avg.setRecall(avg(m1.getRecall(), m2.getRecall(), m3.getRecall(), m4.getRecall(), m5.getRecall()));
            avg.setF1_score(avg(m1.getF1_score(), m2.getF1_score(), m3.getF1_score(), m4.getF1_score(), m5.getF1_score()));
        }
        return avg;
    }

    public static Measures average() {
        Measures mTotal = null, m1, m2, m3, m4, m5;
        int noPerFold = numSentences / numFolds;
        m1 = compute(computeType1(0, noPerFold));
        System.out.println("================ Fold 1 ================ ");
        m2 = compute(computeType2(numSentences - noPerFold, noPerFold));
        System.out.println("================ Fold 2 ================ ");
        m3 = compute(computeType3(noPerFold, noPerFold * 2));
        System.out.println("================ Fold 3 ================ ");
        m4 = compute(computeType3(noPerFold * 2, noPerFold * 3));
        System.out.println("================ Fold 4 ================ ");
        m5 = compute(computeType3(noPerFold * 3, noPerFold * 4));
        System.out.println("================ Fold 5 ================ ");

        mTotal = computeAverage(m1, m2, m3, m4, m5);

        mTotal.display();

        return mTotal;
    }

    public static void main(String[] args) {
        
        int noPerFold = numSentences / numFolds;
        
        System.out.println(" ========= fold 1 =========");
        Measures m1 = compute(computeType1(1, noPerFold));
        System.out.println(" ========= fold 2 =========");
        Measures m3 = compute(computeType3(noPerFold + 1, noPerFold * 2));
        System.out.println(" ========= fold 3 =========");
        Measures m4 = compute(computeType3(noPerFold * 2 + 1 , noPerFold * 3));
        System.out.println(" ========= fold 4 =========");
        Measures m5 = compute(computeType3(noPerFold * 3 + 1, noPerFold * 4));
        System.out.println(" ========= fold 5 =========");
        Measures m2 = compute(computeType2(noPerFold * 4 + 1, noPerFold * 5));
       
        Measures avg = new Measures();
        
        avg.setAccuracy(((m1.getAccuracy()) + (m2.getAccuracy()) + (m3.getAccuracy()) + (m4.getAccuracy()) + (m5.getAccuracy())) / 5);
        
        avg.setMicroAverageF1((m1.getMicroAverageF1() + m2.getMicroAverageF1() + m3.getMicroAverageF1() + m4.getMicroAverageF1() + m5.getMicroAverageF1()) / 5);
        avg.setMicroAverageRec((m1.getMicroAverageRec() + m2.getMicroAverageRec() + m3.getMicroAverageRec() + m4.getMicroAverageRec() + m5.getMicroAverageRec()) / 5);
        avg.setMicroAveragePre((m1.getMicroAveragePre() + m2.getMicroAveragePre() + m3.getMicroAveragePre() + m4.getMicroAveragePre() + m5.getMicroAveragePre()) / 5);        
        avg.setAccuracy(((m1.getAccuracy()) + (m2.getAccuracy()) + (m3.getAccuracy()) + (m4.getAccuracy()) + (m5.getAccuracy())) / 5);

        avg.setPrecision(avg(m1.getPrecision(), m2.getPrecision(), m3.getPrecision(), m4.getPrecision(), m5.getPrecision()));
        avg.setRecall(avg(m1.getRecall(), m2.getRecall(), m3.getRecall(), m4.getRecall(), m5.getRecall()));
        avg.setF1_score(avg(m1.getF1_score(), m2.getF1_score(), m3.getF1_score(), m4.getF1_score(), m5.getF1_score()));

        Measures mavg = computeAverage(m1,m2, m3, m4, m5);
        m1.display();

    }
}
