package uit.sentiment.dataobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeDependence implements Serializable{
    
    
    Integer id = null;
    String sentence = "";
    List<TreeNodeDependence> tokens = new ArrayList<>();

    public String print(){
            String temp =  "Câu : " + this.getSentence() + "\t id :" + getId() + "\t " ;
            for (int i =0; i< tokens.size();i++){
                String x = tokens.get(i).print();
                temp +=x;
            }
            
            return temp;
    }
    
    public TreeDependence() {
    }

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public List<TreeNodeDependence> getTokens() {
        return tokens;
    }

    public void setTokens(List<TreeNodeDependence> tokens) {
        this.tokens = tokens;
    }
    
    public void reset(){
        id = null;
        sentence = "";
        tokens = new ArrayList<>();
    }
    
    public TreeDependence clone(){
        TreeDependence dependence = new TreeDependence();
        dependence.setId(id);
        dependence.setSentence(sentence);
        dependence.setTokens(tokens);
        return dependence;
    }
    
    
}
