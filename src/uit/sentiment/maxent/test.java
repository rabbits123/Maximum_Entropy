/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.sentiment.maxent;

/**
 *
 * @author Phu
 */
public class test {
    public static void main(String[] args) {
        MaximunEntropy maxent = new MaximunEntropy();
        String []arr = {"1","2","3","4"};
        maxent.trainingData(arr);
        System.out.println(maxent.predictSentence("1"));

    }
    
}
