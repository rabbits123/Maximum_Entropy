package uit.sentiment.maxent;

public class IConstantMaxent {
    
    /**
     * config = /resources/cheese2007.prop
     */
    public static String config = "/resources/cheese2007.prop";
    
    /**
     * tagger = /resources/Ngram-POS.txt
     */
    public static String tagger = "/resources/Ngram-POS.txt";
    
    /**
     * STR_PATH_DEPENDENCE = /resources/Tree_parser_outfile.txt
     */
    public static String STR_PATH_DEPENDENCE = "/resources/Tree_parser_outfile.txt";
    
    /**
     * STR_PATH_DEPENDENCE_RELATION = /resources/Tree_parser_relation.txt
     */
    public static String STR_PATH_DEPENDENCE_RELATION = "/resources/Tree_parser_relation.txt";
    
    /**
     * STR_PATH_DEPENDENCE_RELATION_TAGGER = /resources/Tree_parser_relation_tagger.txt
     */
    public static String STR_PATH_DEPENDENCE_RELATION_TAGGER = "/resources/Tree_parser_relation_tagger.txt";
            
    /**
     * CLASSMAXENT = CLASS
     * stanford feature P(c). c{pos,neg,neu}
     */
    public static  String CLASSMAXENT = "CLASS";
    
    /**
     * numFold = 10
     */
    public static int numFold = 10;
    
    /**
     * NGramMaxent = 4
     */
    public static int NGramMaxent = 4;
    
    /**
     * sentenceInput = Kết quả phân lớp
     */
    public static String sentenceInput = "Kết quả phân lớp";
    
    /**
     * FEAT_PREFIX_DEPENDENCE_BASE = #ddc
     */
    public static String FEAT_PREFIX_DEPENDENCE_BASE = "#ddc";
    
    /**
     * FEAT_PREFIX_DEPENDENCE_RELATION = #ddcr
     */
    public static String FEAT_PREFIX_DEPENDENCE_RELATION = "#ddcr";
    
    /**
     * FEAT_PREFIX_DEPENDENCE_TAGGER = #ddcrt
     */
    public static String FEAT_PREFIX_DEPENDENCE_TAGGER = "#ddcrt";
    
    /**
     * FEAT_PREFIX_DICTIONARY = #dic
     */
    public static String FEAT_PREFIX_DICTIONARY = "#dic";
    
    /**
     * DEPENDENCE_RELATION = dependence
     */
    public static String DEPENDENCE_RELATION = "dependence";
    
    /**
     * DEPENDENCE_RELATION_TAGGER = tagger
     */
    public static String DEPENDENCE_RELATION_TAGGER = "tagger";
    
    /**
     * FEAT_PREFIX_DICTIONARY_NOUN_SCORE = -noun-
     */
    public static String FEAT_PREFIX_DICTIONARY_NOUN_SCORE = "-noun-";
    
    /**
     * FEAT_PREFIX_DICTIONARY_VERB_SCORE = -verb-
     */
    public static String FEAT_PREFIX_DICTIONARY_VERB_SCORE = "-verb-";
    
    /**
     * FEAT_PREFIX_DICTIONARY_ADJECTIVE_SCORE = -adjective-
     */
    public static String FEAT_PREFIX_DICTIONARY_ADJECTIVE_SCORE = "-adjective-";
    
    /**
     * FEAT_PREFIX_DICTIONARY_ADVERB_SCORE = -adverb-
     */
    public static String FEAT_PREFIX_DICTIONARY_ADVERB_SCORE = "-adverb-";
    
    /**
     * FEAT_PREFIX_DEPENDENCE_BASE = #ddc
     */
    public static String FEAT_PREFIX_DICTIONARY_SENTENCE_SCORE = "-sentence-";
    
    /**
     * FEAT_PREFIX_DICTIONARY_INTENSIFIER_SCORE = -intensifier-
     */
    public static String FEAT_PREFIX_DICTIONARY_INTENSIFIER_SCORE = "-intensifier-";
    
    /**
     * FEAT_PREFIX_DICTIONARY_NEGATIVE_SCORE = -Negative-
     */
    public static String FEAT_PREFIX_DICTIONARY_NEGATIVE_SCORE = "-negative-";
    
    /**
     * FEAT_PREFIX_DICTIONARY_DEFECTS_SCORE = -defects-
     */
    public static String FEAT_PREFIX_DICTIONARY_DEFECTS_SCORE = "-defects-";
    
    /**
     * FEAT_PREFIX_DICTIONARY_POSITIVETRENDS_SCORE = -positivetrends-
     */
    public static String FEAT_PREFIX_DICTIONARY_POSITIVETRENDS_SCORE = "-positivetrends-";
    
    /**
     * FEAT_PREFIX_DICTIONARY_WORDOPPOSITE_SCORE = -wordopposite-
     */
    public static String FEAT_PREFIX_DICTIONARY_WORDOPPOSITE_SCORE = "-wordopposite-";
    
}
