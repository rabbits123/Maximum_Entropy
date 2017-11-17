package uit.sentiment.maxent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import uit.sentiment.nb.feature.FeatureDictionary;


public class FeaturesDictionary {
    public static List<String> createFeatureDictionary(String sentence, String featPrefix){

        List<String> featureNames = new ArrayList<>();
        FeatureDictionary fd = new FeatureDictionary();
        List<String> vecDic = Arrays.asList(fd.vecDictionary(sentence));
        for (int i = 0; i < vecDic.size(); i++) {
            String feature = "";
            if(featPrefix == null) {
                switch(i){
                    case 0:
                        feature = IConstantMaxent.FEAT_PREFIX_DICTIONARY + IConstantMaxent.FEAT_PREFIX_DICTIONARY_ADJECTIVE_SCORE + vecDic.get(i);
                        break;
                    case 1:
                        feature = IConstantMaxent.FEAT_PREFIX_DICTIONARY + IConstantMaxent.FEAT_PREFIX_DICTIONARY_ADVERB_SCORE + vecDic.get(i);
                        break;
                    case 2:
                        feature = IConstantMaxent.FEAT_PREFIX_DICTIONARY + IConstantMaxent.FEAT_PREFIX_DICTIONARY_NOUN_SCORE + vecDic.get(i);
                        break;
                    case 3:
                        feature = IConstantMaxent.FEAT_PREFIX_DICTIONARY + IConstantMaxent.FEAT_PREFIX_DICTIONARY_VERB_SCORE + vecDic.get(i);
                        break;
                    case 4:
                        feature = IConstantMaxent.FEAT_PREFIX_DICTIONARY + IConstantMaxent.FEAT_PREFIX_DICTIONARY_SENTENCE_SCORE + vecDic.get(i);
                        break;
                    case 5:
                        feature = IConstantMaxent.FEAT_PREFIX_DICTIONARY + IConstantMaxent.FEAT_PREFIX_DICTIONARY_INTENSIFIER_SCORE + vecDic.get(i);
                        break;
                    case 6:
                        feature = IConstantMaxent.FEAT_PREFIX_DICTIONARY + IConstantMaxent.FEAT_PREFIX_DICTIONARY_NEGATIVE_SCORE + vecDic.get(i);
                        break;
                    case 7:
                        feature = IConstantMaxent.FEAT_PREFIX_DICTIONARY + IConstantMaxent.FEAT_PREFIX_DICTIONARY_DEFECTS_SCORE + vecDic.get(i);
                        break;
                    case 8:
                        feature = IConstantMaxent.FEAT_PREFIX_DICTIONARY + IConstantMaxent.FEAT_PREFIX_DICTIONARY_POSITIVETRENDS_SCORE + vecDic.get(i);
                        break;
                    case 9:
                        feature = IConstantMaxent.FEAT_PREFIX_DICTIONARY + IConstantMaxent.FEAT_PREFIX_DICTIONARY_WORDOPPOSITE_SCORE + vecDic.get(i);
                        break;
                }
            } else {
                switch(i){
                    case 0:
                        feature = featPrefix + IConstantMaxent.FEAT_PREFIX_DICTIONARY_ADJECTIVE_SCORE + vecDic.get(i);
                        break;
                    case 1:
                        feature = featPrefix + IConstantMaxent.FEAT_PREFIX_DICTIONARY_ADVERB_SCORE + vecDic.get(i);
                        break;
                    case 2:
                        feature = featPrefix + IConstantMaxent.FEAT_PREFIX_DICTIONARY_NOUN_SCORE + vecDic.get(i);
                        break;
                    case 3:
                        feature = featPrefix + IConstantMaxent.FEAT_PREFIX_DICTIONARY_VERB_SCORE + vecDic.get(i);
                        break;
                    case 4:
                        feature = featPrefix + IConstantMaxent.FEAT_PREFIX_DICTIONARY_SENTENCE_SCORE + vecDic.get(i);
                        break;
                    case 5:
                        feature = featPrefix + IConstantMaxent.FEAT_PREFIX_DICTIONARY_INTENSIFIER_SCORE + vecDic.get(i);
                        break;
                    case 6:
                        feature = featPrefix + IConstantMaxent.FEAT_PREFIX_DICTIONARY_NEGATIVE_SCORE + vecDic.get(i);
                        break;
                    case 7:
                        feature = featPrefix + IConstantMaxent.FEAT_PREFIX_DICTIONARY_DEFECTS_SCORE + vecDic.get(i);
                        break;
                    case 8:
                        feature = featPrefix + IConstantMaxent.FEAT_PREFIX_DICTIONARY_POSITIVETRENDS_SCORE + vecDic.get(i);
                        break;
                    case 9:
                        feature = featPrefix + IConstantMaxent.FEAT_PREFIX_DICTIONARY_WORDOPPOSITE_SCORE + vecDic.get(i);
                        break;
                }
            }
            if(feature != null) {
                featureNames.add(feature);
            }

        }// end xu ly feature tu dien
        return featureNames;
    }
}
