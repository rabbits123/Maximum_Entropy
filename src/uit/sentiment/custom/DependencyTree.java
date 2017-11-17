/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.sentiment.custom;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Phu
 */
public class DependencyTree {

    private int idSentence;
    private String sentence;
    private List<DepdendencyNode> listDepdendency = new ArrayList<>();

    public DependencyTree(int idSentence, String sentence, List<DepdendencyNode> listDepdendency) {
        this.idSentence = idSentence;
        this.sentence = sentence;
        this.listDepdendency = listDepdendency;
    }

    public int getIdSentence() {
        return idSentence;
    }

    public String getSentence() {
        return sentence;
    }

    public List<DepdendencyNode> getListDepdendency() {
        return listDepdendency;
    }

    public void setIdSentence(int idSentence) {
        this.idSentence = idSentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public void setListDepdendency(List<DepdendencyNode> listDepdendency) {
        this.listDepdendency = listDepdendency;
    }

    public int getSize() {
        return this.listDepdendency.size();
    }

    // lấy từ từ các quan hệ
    public List<String> getWordsFromRelation(List<String> relations) {
        List<String> words = new ArrayList<>();
        for (int i = 0; i < getSize(); i++) {
            if (relations.contains(listDepdendency.get(i).getDepdendencyRelation())) {
                // thêm từ đó vào tập dependency
                words.add(listDepdendency.get(i).getWord());
                // thêm từ phụ thuộc
                int head = Integer.parseInt(listDepdendency.get(i).getHeadID());
                words.add(listDepdendency.get(head).getWord());
            }
        }

        return words;
    }

    public static void main(String[] args) {

        DepdendencyNode a = new DepdendencyNode("1|1|M|2|det");
        DepdendencyNode b = new DepdendencyNode("2|tín_chỉ|N|3|sub");
        DepdendencyNode c = new DepdendencyNode("3|thực_hành|V|0|root");
        DepdendencyNode d = new DepdendencyNode("4|2|M|5|det");
        DepdendencyNode e = new DepdendencyNode("5|tín_chỉ|N|3|dob");
        DepdendencyNode f = new DepdendencyNode("6|học_phí|N|5|nmod");

        List<DepdendencyNode> listDepdendency = new ArrayList<>();
        listDepdendency.add(a);
        listDepdendency.add(b);
        listDepdendency.add(c);
        listDepdendency.add(d);
        listDepdendency.add(e);
        listDepdendency.add(f);

        int id = 4;
        String sent = "1 tín chỉ thực hành 2 tín chỉ học phí";

        DependencyTree dep = new DependencyTree(id, sent, listDepdendency);

        List<String> relation = new ArrayList<>();
        relation.add("root");
        List<String> list = dep.getWordsFromRelation(relation);
        for (String x : list) {
            System.out.println(list);
        }
    }
}
