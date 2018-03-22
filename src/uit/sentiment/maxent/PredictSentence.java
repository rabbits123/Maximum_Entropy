/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.sentiment.maxent;

import uit.sentiment.preprocessing.Preprocessing;
import static uit.sentiment.preprocessing.Preprocessing.preprocessSentence;

public class PredictSentence{
    
    private static Preprocessing pre = new Preprocessing();
    
    public PredictSentence(){
    }
    public static void main(String[] args) {
        
        //toptic
        MaximumEntropy topic = new MaximumEntropy();
        topic.classifier = topic.loadModel("src/resources/MaxentTopic.model");
        System.out.println(topic.predictSentence(pre.preprocessSentence("bài tập trực tuyến")));
        
        //sentiment
        MaximumEntropy sentiment = new MaximumEntropy();
        sentiment.classifier = sentiment.loadModel("src/resources/MaxentSentiment.model");
        System.out.println(sentiment.predictSentence(pre.preprocessSentence("bài_tập trực_tuyến")));
    }
}
