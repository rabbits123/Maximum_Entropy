package uit.sentiment.dataobject;

public class TreeNodeDependence {
    Integer index = new Integer(0);
    String word = "";
    String tagger = "";
    Integer indexHead = new Integer(0);
    String relation = "";

    
    public TreeNodeDependence() {
    }
    
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTagger() {
        return tagger;
    }

    public void setTagger(String tagger) {
        this.tagger = tagger;
    }

    public Integer getIndexHead() {
        return indexHead;
    }

    public void setIndexHead(Integer indexHead) {
        this.indexHead = indexHead;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
    public String print(){
        return ("\nindex " + this.getIndex() + "\t word : " +  getWord() + "\t tagger : " + getTagger() + "\t indexHeade " + getIndexHead() + "\t relation : " + this.getRelation());
    }
    
}
