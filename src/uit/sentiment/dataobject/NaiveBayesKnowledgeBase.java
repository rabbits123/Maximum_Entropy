package uit.sentiment.dataobject;

import java.util.HashMap;
import java.util.Map;

public class NaiveBayesKnowledgeBase {
    /**
     * number of training observations
     */
    public int SentenceCount=0;
    
    /**
     * number of categories
     */
    public int ClassifierCount=0;
    /**
     * priors for P(c)
     */
    public Map<String, Double> Priors = new HashMap<>();
    /**
     * number of features(word)
     */
    public int FeatureCountNGram=0;
    /**
     * likelihood for P(x|c)
     */
    public Map<String, Map<String, Double>> LikelihoodsNGram = new HashMap<>();
    
    public NaiveBayesKnowledgeBase() {
    }
}
