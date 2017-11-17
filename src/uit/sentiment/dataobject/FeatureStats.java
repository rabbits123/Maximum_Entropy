package uit.sentiment.dataobject;

import java.util.HashMap;
import java.util.Map;

public class FeatureStats {
    //Tổng số dòng dữ liệu
    public int n = 0;
    // với feature x thuộc phân lớp Y có tần số Z
    public Map<String, Map<String, Integer>> featureCategoryJointCount;
    //phân lớp X có tần số y
    public Map<String, Integer> categoryCounts;
    
    public FeatureStats() {
        n = 0;
        featureCategoryJointCount = new HashMap<>();
        categoryCounts = new HashMap<>();
    }
}
