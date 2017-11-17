package uit.sentiment.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.sentiment.dataobject.TreeDependence;
import uit.sentiment.dataobject.TreeNodeDependence;
import uit.sentiment.maxent.IConstantMaxent;

public class ReadFileText {
    // load file stopword.txt,tagger.txt,don'tprocess.txt
    public static String loadData(InputStream is){
        StringBuilder builder = new StringBuilder();
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(is,"UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String line = br.readLine();
            while(line!=null){
                builder.append(line).append("_");
                line = br.readLine();
            }
            isr.close();
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        }

        return builder.toString();
    }
    
    public static HashMap<String,Float> loadDictionary(InputStream is){

        HashMap<String,Float> dic = new HashMap<>();
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(is,"UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String line = br.readLine();
            while(line!=null){
                String[]temp = line.split("~");
                float score = 0;
                try{
                    score = Float.parseFloat(temp[1].trim());
                    if(!dic.containsKey(temp[0])){
                        dic.put(temp[0].trim(),score);
                    }
                }catch(NumberFormatException ex){
                    score = Float.parseFloat(temp[0].trim());
                    if(!dic.containsKey(temp[1])){
                        dic.put(temp[0].trim(),score);
                    }
                }
                line = br.readLine();
            }
            isr.close();
            br.close();
            is.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dic;
    }
    
    //load data for maxent
    public static String[] readData(InputStream is){
        List<String> sentences = new ArrayList<>();
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            while(line!=null){
                sentences.add(line);
                line = br.readLine();
            }
            isr.close();
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sentences.toArray(new String[sentences.size()]);
    }

    public static List<TreeDependence> loadTreeDependence(InputStream is){
        List<TreeDependence> dependenceTrees = new ArrayList<>();
        TreeDependence dependence = new TreeDependence();
        List<TreeNodeDependence> lTokens = new ArrayList<>();
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(is,"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            while (line != null) {
                if (line.trim().length() > 0) {
                    String[] pharses = line.split("\\|");
                    Integer index = 0;
                    index = Integer.parseInt(pharses[0].trim());
                    if (pharses.length == 2) {
                        // xử lý câu
                        dependence.setId(index);
                        String sentence = pharses[1].trim();
                        if(pharses[1].trim().endsWith(".")){
                            sentence = pharses[1].trim().substring(0,pharses[1].trim().length()-1).trim();
                        }
                        dependence.setSentence(sentence);
                    }else if (pharses.length == 5) {
                        //sử lý các node của câu
                        TreeNodeDependence node = new TreeNodeDependence();
                        node.setIndex(index);
                        node.setWord(pharses[1]);
                        node.setTagger(pharses[2]);
                        node.setIndexHead(Integer.parseInt(pharses[3]));
                        node.setRelation(pharses[4]);
                        lTokens.add(node);
                    }
                } else {
                    // kết thúc câu chuyển sang câu kế tiếp
                    if(lTokens.size()>0){
                        List<TreeNodeDependence> tmp = new ArrayList<>();
                        tmp.addAll(lTokens);
                        dependence.setTokens(tmp);
                        lTokens.clear();
                        TreeDependence tmpDependence = dependence.clone();
                        dependenceTrees.add(tmpDependence);
                        dependence.reset();
                    }
                }
                line = br.readLine();
                if(line==null){
                    if(lTokens.size()>0){
                        List<TreeNodeDependence> tmp = new ArrayList<>();
                        tmp.addAll(lTokens);
                        dependence.setTokens(tmp);
                        lTokens.clear();
                        TreeDependence tmpDependence = dependence.clone();
                        dependenceTrees.add(tmpDependence);
                        dependence.reset();
                    }
                }
            }
            isr.close();
            br.close();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, "Lỗi đọc file");
        } catch (IOException ex) {   
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, "lỗi đọc dữ liệu");
        }catch(NumberFormatException ex){
            ex.printStackTrace();
        }
        return dependenceTrees;
    }

    public static List<String> loadRelationTreeDependence(InputStream is){
        List<String> relation = new ArrayList<>();
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(is,"UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String line = br.readLine();
            while(line!=null){
                if(line.trim().length() > 0){
                    relation.add(line.trim());
                }
                line = br.readLine();
            }
            isr.close();
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        }

        return relation;
    }
    
    public static Map<String, List<String>> loadRelationAndTaggerTreeDependence(InputStream is){
        List<String> relationDependence = new ArrayList<>();
        List<String> relationTagger = new ArrayList<>();
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(is,"UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String line = br.readLine();
            while(line!=null){
                if(line.trim().length() > 0){
                    if(line.trim().split("-").length >= 2){
                        String[] tokens = line.trim().split("-");
                        if(tokens[0].equals(IConstantMaxent.DEPENDENCE_RELATION)){
                            relationDependence.add(tokens[1].trim());
                        } else if(tokens[0].equals(IConstantMaxent.DEPENDENCE_RELATION_TAGGER)){
                            relationTagger.add(tokens[1].trim());
                        }
                    }
                }
                line = br.readLine();
            }
            isr.close();
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadFileText.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Map<String, List<String>> relationDependenceTagger = new HashMap<>();
        relationDependenceTagger.put(IConstantMaxent.DEPENDENCE_RELATION, relationDependence);
        relationDependenceTagger.put(IConstantMaxent.DEPENDENCE_RELATION_TAGGER, relationTagger);
        
        return relationDependenceTagger;
    }
    
}
