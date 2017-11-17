package uit.sentiment.utils;

public class IConstant {
    
    public static String curentPath = System.getProperty("user.dir");
    
    public static String dataPath = "/resources/data/data_pre.xlsx";
    
    public static String DATA_PATH_FACEBOOK = "/resources/data/FB_data_noemotion.xlsx";
//    public static String dataPath = "/resources/data/TS_1015C.xlsx";
    
    public static String NEG = "neg";
    public static String NEU = "neu";
    public static String POS = "pos";
    
    public static String NoLabel = "noLabel";
    
    public static int fold=10;
    
    //preprocess data
    public static boolean isStopWord = false;
    public static boolean isSCharacter = false;
    public static boolean isCRepeat = false;
    public static boolean isEmoticon = true;
    
    public static String sentenceNeutral = "/resources/dont_process.txt";
    public static String stopWord = "/resources/stopword.txt";
    public static String tagger = "/resources/tagger.txt";

    public static String dictionary = "/resources/dictionary/dictionary.txt";
    public static String IntensifierDictionary = "/resources/dictionary/IntensifierDictionary.txt";
    public static String NegativeDictionary = "/resources/dictionary/negative.txt";
    
    public static String CONST_DIC = "CONST_DIC";
    public static String CONST_DIC_INTENSIFIER = "CONST_DIC_INTENSIFIER";
    public static String CONST_DIC_NEGATIVE = "CONST_DIC_NEGATIVE";
    
    
    public static int MAXSTRINGLENGTH1 = 200;
    public static int MAXSTRINGLENGTH2 = 250;
}
