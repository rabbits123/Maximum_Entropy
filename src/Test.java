
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.sentiment.maxent.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Phu
 */
public class Test {
    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("cheeseDisease.train"));
            String line;
            while((line = br.readLine()) != null){
                line = br.readLine();
                System.out.print(line);
                stringList.add(line);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        MaximumEntropy maxent = new MaximumEntropy();
        maxent.trainingData2(stringList);
        System.out.println(maxent.predictSentence("Leptospirosis"));
    }
}
