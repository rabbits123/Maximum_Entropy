package uit.sentiment.dataobject;

import java.util.HashMap;
import java.util.Map;

public class Document {
    public String category="";
    public Map<String, Integer> nGram;
    

    public Document() {
        nGram = new HashMap<String, Integer>();
    }
}
