/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.sentiment.maxent;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Phu
 */
public class test {
    public static void main(String[] args) {
        MaximumEntropy maxent = new MaximumEntropy();
        
        List<String> test = new ArrayList<>();
        test.add("1\ta");
        test.add("2\tb");
        test.add("3\tc");
        test.add("4\td");
        test.add("5\te");
        String []arr = (String[]) test.toArray(new String[0]);
        maxent.trainingData(arr);
        System.out.println(maxent.predictSentence("1"));

    }
    
}
