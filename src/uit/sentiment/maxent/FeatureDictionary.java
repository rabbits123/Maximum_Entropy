package uit.sentiment.maxent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uit.sentiment.io.ReadFileText;
import uit.sentiment.utils.IConstant;
import vn.hus.nlp.tokenizer.VietTokenizer;

public class FeatureDictionary {
    static Map<String, Map<String, Float>> supportDic = loadDictionary();

    public FeatureDictionary() {
        
    }
    
    private static Map<String, Map<String, Float>> loadDictionary(){
        Map<String, Map<String, Float>> dic = new HashMap<>();
        dic.put(IConstant.CONST_DIC, ReadFileText.loadDictionary(uit.sentiment.nb.feature.FeatureDictionary.class.getResourceAsStream(IConstant.dictionary)));
        dic.put(IConstant.CONST_DIC_INTENSIFIER, ReadFileText.loadDictionary(uit.sentiment.nb.feature.FeatureDictionary.class.getResourceAsStream(IConstant.IntensifierDictionary)));
        dic.put(IConstant.CONST_DIC_NEGATIVE, ReadFileText.loadDictionary(uit.sentiment.nb.feature.FeatureDictionary.class.getResourceAsStream(IConstant.NegativeDictionary)));
        return dic;
    }
    
    public static List<String> createFeatureDictionary(String sentence, String featPrefix, VietTokenizer tokenizer){
        if(supportDic == null){
            supportDic = loadDictionary();
        }
        List<String> featureNames = new ArrayList<>();
        String[] sentences = tokenizer.tokenize(sentence);
        for (String sentences1 : sentences) {
            for (String words : sentences1.split(" ")) {
                if(supportDic.get(IConstant.CONST_DIC).containsKey(words) ||
                        supportDic.get(IConstant.CONST_DIC_INTENSIFIER).containsKey(words) ||
                        supportDic.get(IConstant.CONST_DIC_NEGATIVE).containsKey(words)){
                    if(featPrefix != null){
                        featureNames.add(featPrefix + "-" + words);
                    }else{
                        featureNames.add(IConstantMaxent.FEAT_PREFIX_DICTIONARY + "-" + words);
                    }
                }
            }
        }
        return featureNames;
    }
}
