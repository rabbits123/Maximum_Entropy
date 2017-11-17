/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.sentiment.custom;

import java.util.List;

/**
 *
 * @author Phu
 */
public class DepdendencyNode {
    private int id; 
    private String word;
    private String posTag;
    private String headID;
    private String depdendencyRelation;

    public DepdendencyNode(int id, String word, String posTag, String headID, String depdendencyRelation) {
        this.id = id;
        this.word = word;
        this.posTag = posTag;
        this.headID = headID;
        this.depdendencyRelation = depdendencyRelation;
    }

    public DepdendencyNode(String sentence) {
        //Câu đưa vào 1|tôi|P|2|sub
        String []arr = sentence.split("\\|");
        this.id = Integer.parseInt(arr[0]);
        this.word = arr[1];
        this.posTag = arr[2];
        this.headID = arr[3];
        this.depdendencyRelation = arr[4];    
    }
//    public List<String> getWordFromRelation(String relation){
//        List<String> strList = null;
//        
//        
//        return strList;
//    }
    public DepdendencyNode() {
    }

    
    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getPosTag() {
        return posTag;
    }

    public String getHeadID() {
        return headID;
    }

    public String getDepdendencyRelation() {
        return depdendencyRelation;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setPosTag(String posTag) {
        this.posTag = posTag;
    }

    public void setHeadID(String headID) {
        this.headID = headID;
    }

    public void setDepdendencyRelation(String depdendencyRelation) {
        this.depdendencyRelation = depdendencyRelation;
    }
    
    public String toString(){
        return "ID: " + this.getId() + ", Word: " + this.getWord() + ", POS Tag: " + this.getPosTag() + ", HeadID: " + this.getHeadID() + ", Dependency Relation : " + this.getDepdendencyRelation();
    }
    
    public static void main(String[] args) {
    }
}
