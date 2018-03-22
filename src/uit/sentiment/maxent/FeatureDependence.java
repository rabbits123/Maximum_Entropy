package uit.sentiment.maxent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.sentiment.dataobject.TreeDependence;
import uit.sentiment.dataobject.TreeNodeDependence;
import uit.sentiment.io.ReadFileText;
import uit.sentiment.preprocessing.Preprocessing;
import uit.sentiment.utils.IConstant;

public class FeatureDependence {
    
    
    // danh sách các câu dependence đã được tạo sẵn
    private static List<TreeDependence> treeDependences = ReadFileText.loadTreeDependence(FeatureDependence.class.getResourceAsStream(IConstantMaxent.STR_PATH_DEPENDENCE));
    
    // danh sach các quan hệ dependence 
    private static List<String> relationDependence = ReadFileText.loadRelationTreeDependence(FeatureDependence.class.getResourceAsStream(IConstantMaxent.STR_PATH_DEPENDENCE_RELATION));
    
    // danh sach các quan hệ dependence kết hợp với tagger
    private static Map<String, List<String>> relationDependenceTagger = ReadFileText.loadRelationAndTaggerTreeDependence(FeatureDependence.class.getResourceAsStream(IConstantMaxent.STR_PATH_DEPENDENCE_RELATION_TAGGER));
    
    private static HashMap<String, String> hashmap = createHashMap();
    
    // tiền xử lý
    private static Preprocessing pre = new Preprocessing();
    /**
     * create feature dependence cho 1 câu
     * @param sentence câu cần xét
     * @param featPrefix kí tự đại diện cho feature. vd : "#ddc" => "#ddc-feature" 
     * @return featureNames nơi lưu kết quả thực hiện 
     */
    public static List<String> createFeatureDependenceBase(String sentence, String featPrefix){
        if(sentence.equals(IConstant.NEG) || sentence.equals(IConstant.NEU) || sentence.equals(IConstant.POS)){
            return null;
        }
        if(treeDependences == null){
            treeDependences = ReadFileText.loadTreeDependence(FeatureDependence.class.getResourceAsStream(IConstantMaxent.STR_PATH_DEPENDENCE));
        }
        List<String> featureNames = new ArrayList<>();
        boolean isExist = false;
        
        for (TreeDependence dependence : treeDependences) {
            if(hashmap.get(dependence.getSentence().trim()).equals(sentence.trim())){
                
                isExist = true;
                Map<Integer, List<Integer>> trees = new HashMap<>();// luu lại các node con của 1 node
                // tìm con của các node và lưu vào tree
                searchDependenceForHead(dependence.getTokens(), 0, trees);
                trees.remove(0);
                // xử lý tạo feature 
                for (Map.Entry<Integer, List<Integer>> entry : trees.entrySet()) {
                    // bỏ qua các node là các node lá
                    if(entry.getValue().size() == 0){
                        continue;
                    }
                    // tạo các feature cho các node không phải node lá
                    for (Integer idTokens : entry.getValue()) {
                        if(featPrefix != null){
                            featureNames.add(featPrefix + "-" + getFeature(entry.getKey(), idTokens, dependence.getTokens(), false));
                        }else{
                            featureNames.add(IConstantMaxent.FEAT_PREFIX_DEPENDENCE_BASE + "-" + getFeature(entry.getKey(), idTokens, dependence.getTokens(), false));
                        }
                    }
                }
                break;
            }
        }
        if(!isExist){
            //System.out.println(sentence);
        }
        return featureNames;
    }
    
    /**
     * create feature dependence cho 1 câu và chỉ tập trung vào một số quan hệ nhất định
     * @param sentence câu cần xét
     * @param featPrefix kí tự đại diện cho feature. vd : "#ddcr" => "#ddcr-feature" 
     * @return featureNames nơi lưu kết quả thực hiện 
     */
    public static List<String> createFeatureDependenceRelation(String sentence, String featPrefix){
        if(sentence.equals(IConstant.NEG) || sentence.equals(IConstant.NEU) || sentence.equals(IConstant.POS)){
            return null;
        }
        if(treeDependences == null){
            treeDependences = ReadFileText.loadTreeDependence(FeatureDependence.class.getResourceAsStream(IConstantMaxent.STR_PATH_DEPENDENCE));
        }
        if(relationDependence == null){
            relationDependence = ReadFileText.loadRelationTreeDependence(FeatureDependence.class.getResourceAsStream(IConstantMaxent.STR_PATH_DEPENDENCE_RELATION));
        }
        List<String> featureNames = new ArrayList<>();
        boolean isExist = false;
        for (TreeDependence dependence : treeDependences) {
            if(hashmap.get(dependence.getSentence().trim()).equals(sentence.trim())){
                isExist = true;
                Map<Integer, List<Integer>> trees = new HashMap<>();// luu lại các node con của 1 node
                // tìm con của các node và lưu vào tree
                searchDependenceForHead(dependence.getTokens(), 0, trees);
                trees.remove(0);
                // xử lý tạo feature 
                for (Map.Entry<Integer, List<Integer>> entry : trees.entrySet()) {
                    // bỏ qua các node là các node lá
                    if(entry.getValue().size() == 0){
                        continue;
                    }
                    // tạo các feature cho các node không phải node lá
                    for (Integer idTokens : entry.getValue()) {
                        // kiểm tra node có quan hệ gì với node cha
                        if(!checkRelationNodeTreeDependence(idTokens, dependence.getTokens(), relationDependence)){
                            continue;
                        }
                        if(featPrefix != null){
                            featureNames.add(featPrefix + "-" + getFeature(entry.getKey(), idTokens, dependence.getTokens(), false));
                        }else{
                            featureNames.add(IConstantMaxent.FEAT_PREFIX_DEPENDENCE_RELATION + "-" + getFeature(entry.getKey(), idTokens, dependence.getTokens(), false));
                        }
                    }
                }
                break;
            }
        }
        if(!isExist){
            //System.out.println(sentence);
        }
        return featureNames;
    }
    
    /**
     * create feature dependence cho 1 câu và chỉ tập trung vào một số quan hệ nhất định
     * kết hợp với nhãn từ loại(chỉ một số nhãn nhất định)
     * @param sentence câu cần xét
     * @param featPrefix kí tự đại diện cho feature. vd : "#ddcrt" => "#ddcrt-feature" 
     * @return featureNames nơi lưu kết quả thực hiện 
     */
    public static List<String> createFeatureDependenceRelationTagger(String sentence, String featPrefix){
        if(sentence.equals(IConstant.NEG) || sentence.equals(IConstant.NEU) || sentence.equals(IConstant.POS)){
            return null;
        }
        if(treeDependences == null){
            treeDependences = ReadFileText.loadTreeDependence(FeatureDependence.class.getResourceAsStream(IConstantMaxent.STR_PATH_DEPENDENCE));
        }
        if(relationDependenceTagger == null){
            relationDependenceTagger = ReadFileText.loadRelationAndTaggerTreeDependence(FeatureDependence.class.getResourceAsStream(IConstantMaxent.STR_PATH_DEPENDENCE_RELATION_TAGGER));
        }
        List<String> featureNames = new ArrayList<>();
        boolean isExist = false;
        for (TreeDependence dependence : treeDependences) {
            if(hashmap.get(dependence.getSentence().trim()).equals(sentence.trim())){
                isExist = true;
                Map<Integer, List<Integer>> trees = new HashMap<>();// luu lại các node con của 1 node
                // tìm con của các node và lưu vào tree
                searchDependenceForHead(dependence.getTokens(), 0, trees);
                trees.remove(0);
                // xử lý tạo feature 
                for (Map.Entry<Integer, List<Integer>> entry : trees.entrySet()) {
                    // bỏ qua các node là các node lá
                    if(entry.getValue().size() == 0){
                        continue;
                    }
                    // tạo các feature cho các node không phải node lá
                    for (Integer idTokens : entry.getValue()) {
                        // kiểm tra node có quan hệ gì với node cha
                        if(!checkRelationNodeTreeDependence(idTokens, dependence.getTokens(), relationDependenceTagger.get(IConstantMaxent.DEPENDENCE_RELATION))){
                            continue;
                        }
                        if(!checkTaggerNodeTreeDependence(entry.getKey(), idTokens, dependence.getTokens(), relationDependenceTagger.get(IConstantMaxent.DEPENDENCE_RELATION_TAGGER))){
                            continue;
                        }
                        if(featPrefix != null){
                            featureNames.add(featPrefix + "-" + getFeature(entry.getKey(), idTokens, dependence.getTokens(), true));
                        }else{
                            featureNames.add(IConstantMaxent.FEAT_PREFIX_DEPENDENCE_RELATION + "-" + getFeature(entry.getKey(), idTokens, dependence.getTokens(), true));
                        }
                    }
                }
                break;
            }
        }
        if(!isExist){
            //System.out.println(sentence);
        }
        return featureNames;
    }
    
    /**
     * xet quan hệ giữa node con với node cha có thuộc các quan hệ cho hay không 
     * @param idTokens id node cần kiểm tra
     * @param treeNodeDependences danh sach node 
     * @param relation danh sach cac quan hệ đươc chọn
     * 
     */
    private static boolean checkRelationNodeTreeDependence(Integer idTokens, List<TreeNodeDependence> treeNodeDependences, List<String> relation){
        for (TreeNodeDependence treeNodeDependence : treeNodeDependences) {
            if(!treeNodeDependence.getIndex().equals(idTokens)){
                continue;
            }
            if(relation.lastIndexOf(treeNodeDependence.getRelation()) != -1){
                return true;
            }
            return false;
        }
        return false;
    }
    
    /**
     * xét nhãn từ loại của 1 node
     * @param idTokens id node cần kiểm tra
     * @param treeNodeDependences danh sach node 
     * @param relation danh sach các nhãn hợp lệ
     * 
     */
    private static boolean checkTaggerNodeTreeDependence(Integer idHead, Integer idDependence, List<TreeNodeDependence> treeNodeDependences, List<String> tagger){
        for (TreeNodeDependence treeNodeDependence : treeNodeDependences) {
            if(treeNodeDependence.getIndex().equals(idHead) && tagger.lastIndexOf(treeNodeDependence.getTagger().trim()) != -1){
                return true;
            }
            if(treeNodeDependence.getIndex().equals(idDependence) && tagger.lastIndexOf(treeNodeDependence.getTagger().trim()) != -1){
                return true;
            }
        }
        return false;
    }
    
    /**
     * tìm tất cả các node con của 1 node dựa vào đệ quy
     * @param ltoken danh sach các node của tree
     * @param id node cha
     * @param tree nơi lưu kết quả thực hiện
     * 
     */
    private static void searchDependenceForHead(List<TreeNodeDependence> ltoken, int id, Map<Integer,List<Integer>> tree){
        List<Integer> dependence = new ArrayList<>();
        for (TreeNodeDependence treeNodeDependence : ltoken) {
            if (treeNodeDependence.getIndexHead() == id) {
                dependence.add(treeNodeDependence.getIndex());
                searchDependenceForHead(ltoken, treeNodeDependence.getIndex(), tree);
            }
        }
        tree.put(id, dependence);
    }
    
    /**
     * create feature from head and dependence of sentence
     * @param idHead id của head
     * @param iddependence id của dependence
     * @param lTreesNode danh sach các node của tree dêpndence
     */
    private static String getFeature(Integer idHead, Integer iddependence, List<TreeNodeDependence> lTreesNode, boolean isHasTagger){
        String head = "";
        String dependence = "";
        int count = 0;
        for (TreeNodeDependence treeNodeDependence : lTreesNode) {
            if(count == 2){
                break;
            }
            if(treeNodeDependence.getIndex().equals(idHead)){
                head = treeNodeDependence.getWord();
                if(isHasTagger){
                    head += "_" + treeNodeDependence.getTagger();
                }
                ++count;
            }
            if(treeNodeDependence.getIndex().equals(iddependence)){
                dependence = treeNodeDependence.getWord();
                if(isHasTagger){
                    dependence += "_" + treeNodeDependence.getTagger();
                }
                ++count;
            }
        }
        if(isHasTagger){
            return head + " " + dependence;
        }else{
            return head + "_" + dependence;
        }
    }
    
    private static HashMap<String, String> createHashMap(){
       HashMap<String, String> hashMap = new HashMap<String, String>();
       
       BufferedReader br = null;
       BufferedReader br2 = null;
       
        try {
            br = new BufferedReader(new FileReader("rawSentences.txt"));
            br2 = new BufferedReader(new FileReader("processedSentence.txt"));
            String key = null;
            String value = null;
            while(((key = br.readLine()) != null) && ((value = br2.readLine()) != null)){
                hashMap.put(key, value);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FeatureDependence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FeatureDependence.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       return hashMap;
    }

    public static void main(String[] args) {
//        List<String> list = createFeatureDependenceBase("1 chỉ thực hành chỉ còn ý nghĩa tượng trưng", null);
//        for(String s : list){
//            System.out.println(s);
//        }


        createHashMap();
    }
}
