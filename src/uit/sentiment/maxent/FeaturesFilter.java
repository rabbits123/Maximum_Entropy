package uit.sentiment.maxent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uit.sentiment.io.ReadFileText;
import vn.hus.nlp.tagger.VietnameseMaxentTagger;
import vn.hus.nlp.tokenizer.VietTokenizer;

public class FeaturesFilter {
    private static Map<Integer,List<String>> pos = extractTagger(ReadFileText.readData(FeaturesFilter.class.getResourceAsStream(IConstantMaxent.tagger)));
    private static Map<String, List<String>> lTagger = extract(ReadFileText.readData(FeaturesFilter.class.getResourceAsStream(IConstantMaxent.tagger)));
    
    
    public static String filter1(String feature, VietTokenizer tokenizer, VietnameseMaxentTagger vtagger){
        if(pos == null|| pos.size()<=0){
            pos = extractTagger(ReadFileText.readData(FeaturesFilter.class.getResourceAsStream(IConstantMaxent.tagger)));
        }
        // lấy prefix của feature
        String featPrefix = feature.substring(0,feature.indexOf("-") + 1);
        // lấy feature
        String tmp = feature.substring(feature.indexOf("-") + 1,feature.length());
        
        if(tokenizer.tokenize(tmp)[0].split(" ").length == 2){
            String[] tokens = tokenizer.tokenize(tmp)[0].split(" ");
            String label1 = vtagger.tagText(tokens[0]).split("/")[1];
            if(lTagger.containsKey(label1)){
                String label2 = vtagger.tagText(tokens[1]).split("/")[1];
                if(lTagger.get(label1).lastIndexOf(label2) != -1){
                    // feature hợp lệ
                    return featPrefix+vtagger.tagText(tmp);
                }
            }else {
                return null;
            }
        }
        return feature;
    }
    
    private static Map<String,List<String>> extract(String[] posTaggers){
        Map<String,List<String>> resultPos = new HashMap<>();
        for (String posTagger : posTaggers) {
            String[] token = posTagger.split("_");
            if(!resultPos.containsKey(token[0])){
                resultPos.put(token[0], new ArrayList<>());
            }
            if(resultPos.get(token[0]).lastIndexOf(token[1]) == -1){
                resultPos.get(token[0]).add(token[1]);
            }
        }
        return resultPos;
    }
    
    
    public static boolean filter(String feature, VietTokenizer tokenizer, VietnameseMaxentTagger vtagger){
        if(pos == null|| pos.size()<=0){
            pos = extractTagger(ReadFileText.readData(FeaturesFilter.class.getResourceAsStream(IConstantMaxent.tagger)));
        }
        boolean isValid = true;
        String tmp = feature.substring(feature.indexOf("-") + 1,feature.length());
        if(tokenizer.tokenize(tmp)[0].split(" ").length == 2){
            isValid = false;
            String[] tokens = tokenizer.tokenize(tmp)[0].split(" ");
            for (Map.Entry<Integer, List<String>> entry : pos.entrySet()) {
                List<String> ltagger = entry.getValue();
                if(vtagger.tagText(tokens[0]).endsWith(ltagger.get(0)) && vtagger.tagText(tokens[1]).endsWith(ltagger.get(1))){
                    return true;
                }
            }
            
        }
        return isValid;
    }
        
    private static Map<Integer,List<String>> extractTagger(String[] posTaggers){
        Map<Integer,List<String>> resultPos = new HashMap<>();
        int i = 0;
        for (String posTagger : posTaggers) {
            String[] token = posTagger.split("_");
            resultPos.put(i, Arrays.asList(token));
            i++;
        }
        return resultPos;
    }
    
    public static void main(String[] args) {
        Map<String, List<String>> tmp = new HashMap<>();
        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        a.add("3");
        a.add("4");
        a.add("5");
        a.add("6");
        tmp.put("tien", a);
        System.out.println(tmp.get("tien").lastIndexOf("5"));
        System.out.println(tmp.containsValue("3"));
    }
}
