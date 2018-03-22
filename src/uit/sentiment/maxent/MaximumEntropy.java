package uit.sentiment.maxent;

import edu.stanford.nlp.classify.Classifier;
import edu.stanford.nlp.classify.ColumnDataClassifier;
import edu.stanford.nlp.classify.Dataset;
import edu.stanford.nlp.classify.GeneralDataset;
import edu.stanford.nlp.classify.LinearClassifier;
import edu.stanford.nlp.classify.LinearClassifierFactory;
import static edu.stanford.nlp.dcoref.CoNLL2011DocumentReader.logger;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.BasicDatum;
import edu.stanford.nlp.ling.Datum;
import edu.stanford.nlp.process.WordShapeClassifier;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.util.Generics;
import edu.stanford.nlp.util.Index;
import edu.stanford.nlp.util.Pair;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.hus.nlp.tokenizer.VietTokenizer;
import uit.sentiment.io.ReadTextFile;

public class MaximumEntropy implements Serializable {

    transient ColumnDataClassifier cdc;
    public static LinearClassifier<String, String> classifier;
    transient LinearClassifierFactory<String, String> factory = new LinearClassifierFactory<>();
    transient VietTokenizer tokenizer = new VietTokenizer();
    //VietnameseMaxentTagger vtagger = new VietnameseMaxentTagger();;

    boolean isDependenceBase = true;
    boolean isDependenceRelation = false;
    boolean isDependenceRelationTagger = false;

    boolean isTagger = false;

    boolean isDictionary = false;

    public MaximumEntropy() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(IConstantMaxent.config));
            properties.setProperty("1.maxNGramLeng", "" + IConstantMaxent.NGramMaxent);
            this.cdc = new ColumnDataClassifier(properties);

        } catch (IOException ex) {
            Logger.getLogger(MaximumEntropy.class.getName()).log(Level.SEVERE, null, ex);
        }
        factory.useConjugateGradientAscent();
        // Turn on per-iteration convergence updates
        factory.setVerbose(true);
        //Small amount of smoothing
        factory.setSigma(15.0);
    }

    public void trainingData(String[] data) {
        Pair<GeneralDataset<String, String>, List<String[]>> dataInfo = readDataset(this.cdc, data, true);
        classifier = factory.trainClassifier(dataInfo.first());
    }

    // list<String> truyền vào training data
    public void trainingData2(List<String> data) {
        Pair<GeneralDataset<String, String>, List<String[]>> dataInfo = readDataset2(this.cdc, data, true);
        classifier = factory.trainClassifier(dataInfo.first());
    }

    public Pair<GeneralDataset<String, String>, List<String[]>> readDataset2(ColumnDataClassifier cdc, List<String> data, boolean inTestPhase) {
        GeneralDataset<String, String> dataset = new Dataset<>();
        List<String[]> lineInfos = new ArrayList<>();
        int lineNo = 0;
        int minColumns = Integer.MAX_VALUE;
        int maxColumns = 0;
        for (String line : data) {
            if (line != null) {
                lineNo++;
                String[] strings = line.split("\t");
                if (strings.length < 2) {
                    throw new RuntimeException("Line format error at line " + lineNo + ": " + line);
                }
                if (strings.length < minColumns) {
                    minColumns = strings.length;
                }
                if (strings.length > maxColumns) {
                    maxColumns = strings.length;
                }
                if (inTestPhase) {
                    lineInfos.add(strings);
                }
                if (strings.length < cdc.flags.length) {
                    throw new RuntimeException("Error: Line has too few tab-separated columns (" + maxColumns
                            + ") for " + cdc.flags.length + " columns required by specified properties: " + line);
                }
                dataset.add(makeDatumFromStrings(cdc, strings));
            }
        }

        return new Pair<>(dataset, lineInfos);
    }

    public String predictSentence(String sentence) {
        if (this.classifier == null) {
            throw new IllegalArgumentException("Make sure you train first a classifier before you use it.");
        }
        Datum<String, String> d = makeDatumFromStrings(this.cdc, new String[]{"neu", sentence});
        String result = this.classifier.classOf(d);
        return result;
    }

    public Pair<GeneralDataset<String, String>, List<String[]>> readDataset(ColumnDataClassifier cdc, String[] data, boolean inTestPhase) {
        GeneralDataset<String, String> dataset = new Dataset<>();
        List<String[]> lineInfos = new ArrayList<>();
        int lineNo = 0;
        int minColumns = Integer.MAX_VALUE;
        int maxColumns = 0;
        for (String line : data) {
            lineNo++;
            String[] strings = line.split("\t");
            if (strings.length < 2) {
                throw new RuntimeException("Line format error at line " + lineNo + ": " + line);
            }
            if (strings.length < minColumns) {
                minColumns = strings.length;
            }
            if (strings.length > maxColumns) {
                maxColumns = strings.length;
            }
            if (inTestPhase) {
                lineInfos.add(strings);
            }
            if (strings.length < cdc.flags.length) {
                throw new RuntimeException("Error: Line has too few tab-separated columns (" + maxColumns
                        + ") for " + cdc.flags.length + " columns required by specified properties: " + line);
            }
            dataset.add(makeDatumFromStrings(cdc, strings));
        }

        return new Pair<>(dataset, lineInfos);
    }

    public Datum<String, String> makeDatumFromStrings(ColumnDataClassifier cdc, String[] strings) {
        return makeDatum(cdc, strings);
    }

    public Datum<String, String> makeDatum(ColumnDataClassifier cdc, String[] strs) {
        String goldAnswer = cdc.globalFlags.goldAnswerColumn < strs.length ? strs[cdc.globalFlags.goldAnswerColumn] : "";;
        List<String> theFeatures = new ArrayList<>();
        Collection<String> globalFeatures = Generics.newHashSet();
        globalFeatures.add("CLASS");

        addAllInterningAndPrefixing(cdc, theFeatures, globalFeatures, "");
        for (int i = 0; i < cdc.flags.length; i++) {
            Collection<String> featuresC = Generics.newHashSet();//important that this is a hash set to prevent same feature from being added multiple times
            makeDatum(cdc, strs[i], cdc.flags[i], featuresC, goldAnswer);
            addAllInterningAndPrefixing(cdc, theFeatures, featuresC, i + "-");
        }

        return new BasicDatum<>(theFeatures, goldAnswer);
    }

    private void addAllInterningAndPrefixing(ColumnDataClassifier cdc, Collection<String> accumulator, Collection<String> addend, String prefix) {
        assert prefix != null;
        for (String protoFeat : addend) {
            if (!prefix.isEmpty()) {
                protoFeat = prefix + protoFeat;
            }
            if (cdc.globalFlags.intern) {
                protoFeat = protoFeat.intern();
            }
            accumulator.add(protoFeat);
        }
    }

    private void makeDatum(ColumnDataClassifier cdc, String cWord, ColumnDataClassifier.Flags flags, Object featuresC, String goldAns) {

        //logger.info("Making features for " + cWord + " flags " + flags);
        if (flags == null) {
            // no features for this column
            return;
        }
        if (flags.filename) {
            cWord = IOUtils.slurpFileNoExceptions(cWord);
        }
        if (flags.lowercase) {
            cWord = cWord.toLowerCase(Locale.ENGLISH);
        }
        /**
         * 22/03/2017 sử dụng toàn bộ câu như 1 feature với S- là tiền tố dc sử
         * dụng cho feature này
         */
        if (flags.useString) {
            ColumnDataClassifier.addFeature(featuresC, "S-" + cWord, ColumnDataClassifier.DEFAULT_VALUE);
        }
        /**
         * 22/03/2017 sử dụng độ dài của câu như 1 feature từ thuộc vào file
         * config là sẽ có mấy cấp độ(<10,10< và < 20,>30)
         *
         */
        if (flags.binnedLengths != null) {
            int len = cWord.length();
            String featureName = null;
            for (int i = 0; i <= flags.binnedLengths.length; i++) {
                if (i == flags.binnedLengths.length || len <= flags.binnedLengths[i]) {
                    featureName = "Len-" + ((i == 0) ? 0 : (flags.binnedLengths[i - 1] + 1)) + '-' + ((i == flags.binnedLengths.length) ? "Inf" : Integer.toString(flags.binnedLengths[i]));
                    if (flags.binnedLengthsCounter != null) {
                        flags.binnedLengthsCounter.incrementCount(featureName, goldAns);
                    }
                    break;
                }
            }
            ColumnDataClassifier.addFeature(featuresC, featureName, ColumnDataClassifier.DEFAULT_VALUE);
        }
        /**
         * 22/03/2017 nếu dữ liệu đưa vào là số thì ta sử dụng feature này tương
         * tự feature length, feature này tính giá trị của các token xem nó nằm
         * trong khoảng nào(cword = 5.6, 0<5.6<10)
         */
        if (flags.binnedValues != null) {
            double val = flags.binnedValuesNaN;
            try {
                val = Double.parseDouble(cWord);
            } catch (NumberFormatException nfe) {
                // do nothing -- keeps value of flags.binnedValuesNaN
            }
            String featureName = null;
            for (int i = 0; i <= flags.binnedValues.length; i++) {
                if (i == flags.binnedValues.length || val <= flags.binnedValues[i]) {
                    featureName = "Val-(" + ((i == 0) ? "-Inf" : Double.toString(flags.binnedValues[i - 1])) + ',' + ((i == flags.binnedValues.length) ? "Inf" : Double.toString(flags.binnedValues[i])) + ']';
                    if (flags.binnedValuesCounter != null) {
                        flags.binnedValuesCounter.incrementCount(featureName, goldAns);
                    }
                    break;
                }
            }
            ColumnDataClassifier.addFeature(featuresC, featureName, ColumnDataClassifier.DEFAULT_VALUE);
        }
        /**
         * 22/03/2017 sử dụng tần số xuất hiện của các ký tự như 1 feature(bảng
         * chữ cái tiếng anh có 26 ký tự)-> cải tiến thành từ mỗi ký tự sẽ có 3
         * trường hợp : - không xuất hiện : char-(ký tự)-0 - xuất hiện 1 lần :
         * char-(ký tự)-1-1 - xuất hiện nhiều hơn 1 lần : char-(ký tự)-2-Inf
         */
        if (flags.countChars != null) {
            int[] cnts = new int[flags.countChars.length];
            for (int i = 0; i < cnts.length; i++) {
                cnts[i] = 0;
            }
            /**
             * 22/03/2017 xác định tần số xuất hiện của các ký tự
             */
            for (int i = 0, len = cWord.length(); i < len; i++) {
                char ch = cWord.charAt(i);
                for (int j = 0; j < cnts.length; j++) {
                    if (ch == flags.countChars[j]) {
                        cnts[j]++;
                    }
                }
            }// end
            for (int j = 0; j < cnts.length; j++) {
                String featureName = null;
                for (int i = 0; i <= flags.countCharsBins.length; i++) {
                    if (i == flags.countCharsBins.length || cnts[j] <= flags.countCharsBins[i]) {
                        featureName = "Char-" + flags.countChars[j] + '-' + ((i == 0) ? 0 : (flags.countCharsBins[i - 1] + 1)) + '-' + ((i == flags.countCharsBins.length) ? "Inf" : Integer.toString(flags.countCharsBins[i]));
                        break;
                    }
                }
                ColumnDataClassifier.addFeature(featuresC, featureName, ColumnDataClassifier.DEFAULT_VALUE);
            }
        }
        if (flags.splitWordsPattern != null || flags.splitWordsTokenizerPattern != null
                || flags.splitWordsWithPTBTokenizer) {
            String[] bits;
            if (flags.splitWordsTokenizerPattern != null) {
                bits = ColumnDataClassifier.regexpTokenize(flags.splitWordsTokenizerPattern, flags.splitWordsIgnorePattern, cWord);
            } else if (flags.splitWordsPattern != null) {
                bits = ColumnDataClassifier.splitTokenize(flags.splitWordsPattern, flags.splitWordsIgnorePattern, cWord);
            } else { //PTB tokenizer
                bits = cdc.ptbTokenize(cWord);
            }

            if (flags.splitWordCount) {
                ColumnDataClassifier.addFeature(featuresC, "SWNUM", bits.length);
            }
            if (flags.logSplitWordCount) {
                ColumnDataClassifier.addFeature(featuresC, "LSWNUM", Math.log(bits.length));
            }
            if (flags.binnedSplitWordCounts != null) {
                String featureName = null;
                for (int i = 0; i <= flags.binnedSplitWordCounts.length; i++) {
                    if (i == flags.binnedSplitWordCounts.length || bits.length <= flags.binnedSplitWordCounts[i]) {
                        featureName = "SWNUMBIN-" + ((i == 0) ? 0 : (flags.binnedSplitWordCounts[i - 1] + 1)) + '-' + ((i == flags.binnedSplitWordCounts.length) ? "Inf" : Integer.toString(flags.binnedSplitWordCounts[i]));
                        break;
                    }
                }
                ColumnDataClassifier.addFeature(featuresC, featureName, ColumnDataClassifier.DEFAULT_VALUE);
            }
            // add features over splitWords
            for (int i = 0; i < bits.length; i++) {
                if (flags.useSplitWords) {
                    ColumnDataClassifier.addFeature(featuresC, "SW-" + bits[i], ColumnDataClassifier.DEFAULT_VALUE);
                }
                if (flags.useLowercaseSplitWords) {
                    ColumnDataClassifier.addFeature(featuresC, "LSW-" + bits[i].toLowerCase(), ColumnDataClassifier.DEFAULT_VALUE);
                }
                if (flags.useSplitWordPairs) {
                    if (i + 1 < bits.length) {
                        ColumnDataClassifier.addFeature(featuresC, "SWP-" + bits[i] + '-' + bits[i + 1], ColumnDataClassifier.DEFAULT_VALUE);
                    }
                }
                if (flags.useLowercaseSplitWordPairs) {
                    if (i + 1 < bits.length) {
                        ColumnDataClassifier.addFeature(featuresC, "LSWP-" + bits[i].toLowerCase() + '-' + bits[i + 1].toLowerCase(), ColumnDataClassifier.DEFAULT_VALUE);
                    }
                }
                if (flags.useAllSplitWordPairs) {
                    for (int j = i + 1; j < bits.length; j++) {
                        // sort lexicographically
                        if (bits[i].compareTo(bits[j]) < 0) {
                            ColumnDataClassifier.addFeature(featuresC, "ASWP-" + bits[i] + '-' + bits[j], ColumnDataClassifier.DEFAULT_VALUE);
                        } else {
                            ColumnDataClassifier.addFeature(featuresC, "ASWP-" + bits[j] + '-' + bits[i], ColumnDataClassifier.DEFAULT_VALUE);
                        }
                    }
                }
                if (flags.useAllSplitWordTriples) {
                    for (int j = i + 1; j < bits.length; j++) {
                        for (int k = j + 1; k < bits.length; k++) {
                            // sort lexicographically
                            String[] triple = new String[3];
                            triple[0] = bits[i];
                            triple[1] = bits[j];
                            triple[2] = bits[k];
                            Arrays.sort(triple);
                            ColumnDataClassifier.addFeature(featuresC, "ASWT-" + triple[0] + '-' + triple[1] + '-' + triple[2], ColumnDataClassifier.DEFAULT_VALUE);
                        }
                    }
                }
                if (flags.useSplitWordNGrams) {
                    StringBuilder sb = new StringBuilder("SW#");
                    for (int j = i; j < i + flags.minWordNGramLeng - 1 && j < bits.length; j++) {
                        sb.append('-');
                        sb.append(bits[j]);
                    }
                    int maxIndex = (flags.maxWordNGramLeng > 0) ? Math.min(bits.length, i + flags.maxWordNGramLeng) : bits.length;
                    for (int j = i + flags.minWordNGramLeng - 1; j < maxIndex; j++) {
                        if (flags.wordNGramBoundaryRegexp != null) {
                            if (flags.wordNGramBoundaryPattern.matcher(bits[j]).matches()) {
                                break;
                            }
                        }
                        sb.append('-');
                        sb.append(bits[j]);
                        ColumnDataClassifier.addFeature(featuresC, sb.toString(), ColumnDataClassifier.DEFAULT_VALUE);
                    }
                }
                // this is equivalent to having boundary tokens in splitWordPairs -- they get a special feature
                if (flags.useSplitFirstLastWords) {
                    if (i == 0) {
                        ColumnDataClassifier.addFeature(featuresC, "SFW-" + bits[i], ColumnDataClassifier.DEFAULT_VALUE);
                    } else if (i == bits.length - 1) {
                        ColumnDataClassifier.addFeature(featuresC, "SLW-" + bits[i], ColumnDataClassifier.DEFAULT_VALUE);
                    }
                }
                if (flags.useLowercaseSplitFirstLastWords) {
                    if (i == 0) {
                        ColumnDataClassifier.addFeature(featuresC, "LSFW-" + bits[i].toLowerCase(), ColumnDataClassifier.DEFAULT_VALUE);
                    } else if (i == bits.length - 1) {
                        ColumnDataClassifier.addFeature(featuresC, "SLW-" + bits[i].toLowerCase(), ColumnDataClassifier.DEFAULT_VALUE);
                    }
                }
                if (flags.useSplitNGrams || flags.useSplitPrefixSuffixNGrams) {
                    Collection<String> featureNames = cdc.makeNGramFeatures(bits[i], flags, true, "S#");
                    for (String featureName : featureNames) {
                        ColumnDataClassifier.addFeature(featuresC, featureName, ColumnDataClassifier.DEFAULT_VALUE);
                    }
                }
                if (flags.splitWordShape > edu.stanford.nlp.process.WordShapeClassifier.NOWORDSHAPE) {
                    String shape = edu.stanford.nlp.process.WordShapeClassifier.wordShape(bits[i], flags.splitWordShape);
                    // logger.info("Shaper is " + flags.splitWordShape + " word len " + bits[i].length() + " shape is " + shape);
                    ColumnDataClassifier.addFeature(featuresC, "SSHAPE-" + shape, ColumnDataClassifier.DEFAULT_VALUE);
                }
            } // for bits
            if (flags.wordVectors != null) {
                double[] averages = null;
                for (String bit : bits) {
                    float[] wv = flags.wordVectors.get(bit);
                    if (wv != null) {
                        if (averages == null) {
                            averages = new double[wv.length];
                            for (int j = 0; j < wv.length; j++) {
                                averages[j] += wv[j];
                            }
                        }
                    }
                }
                if (averages != null) {
                    for (int j = 0; j < averages.length; j++) {
                        averages[j] /= bits.length;
                        ColumnDataClassifier.addFeature(featuresC, "SWV-" + j, averages[j]);
                    }
                    // } else {
                    //   logger.info("No word vectors found for words in |" + cWord + '|');
                }
            } // end if wordVectors
        } // end if uses some split words features
        //end 

        /**
         * 22/03/2017 xác định shape của word : 0: other 1: LOWERCASE >2:
         * WT-XXXXXXXXXXXXXXXXXX
         */
        if (flags.wordShape > WordShapeClassifier.NOWORDSHAPE) {
            String shape = edu.stanford.nlp.process.WordShapeClassifier.wordShape(cWord, flags.wordShape);
            ColumnDataClassifier.addFeature(featuresC, "SHAPE-" + shape, ColumnDataClassifier.DEFAULT_VALUE);
        }
        /**
         * 22/03/2017 sử dụng Ngram làm feature với n được xác định từ CLASS
         * ICONSTANTMAXENT sử dụng kết hợp các ngram(3-gram:
         * 1-gram+2gram+3gram;4-gram:....) ngoài ra còn sử dụng n-gram trước và
         * ngram kế tiếp: n-gram trước được xác định bằng tiền tố B n-gram kế
         * tiếp được xác định bằng tiền tố E feature :
         * #-feature,#B-feature,#E-feature
         */
        if (flags.useNGrams || flags.usePrefixSuffixNGrams) {
            Collection<String> featureNames = cdc.makeNGramFeatures(cWord, flags, false, "#");
            for (String featureName : featureNames) {
                // bổ sung thêm feature pos tagger
//                if (isTagger) {
//                    String newFeature = FeaturesFilter.filter1(featureName, tokenizer, vtagger);
//                    if (newFeature != null) {
//                        ColumnDataClassifier.addFeature(featuresC, newFeature, ColumnDataClassifier.DEFAULT_VALUE);
//                    }
//                }
                // thêm feature đã được tạo (trước, sau và chính nó)
                ColumnDataClassifier.addFeature(featuresC, featureName, ColumnDataClassifier.DEFAULT_VALUE);
            }
        }//end

        /**
         * 26/05/2017 sử dụng dependence làm feature : head+dependence feature :
         * #ddc-feature
         */
        if (isDependenceBase) {
            //System.out.println("right" + cWord);
            Collection<String> featureNames = FeatureDependence.createFeatureDependenceBase(cWord, null);
            if (featureNames != null) {
                for (String featureName : featureNames) {
                    ColumnDataClassifier.addFeature(featuresC, featureName, ColumnDataClassifier.DEFAULT_VALUE);
                    //System.out.println("Feature : " + featureName);
                }
            }
        }

        /**
         * 26/05/2017 sử dụng dependence cùng các quan hệ của nó làm feature
         * feature : #ddcr-feature
         */
        if (isDependenceRelation) {
            Collection<String> featureNames = FeatureDependence.createFeatureDependenceRelation(cWord, null);
            if (featureNames != null) {
                for (String featureName : featureNames) {
                    ColumnDataClassifier.addFeature(featuresC, featureName, ColumnDataClassifier.DEFAULT_VALUE);
                }
            }
        }

        /**
         * 26/05/2017 sử dụng dependence cùng nhãn từ loại làm feature và chỉ
         * tập chung vào 1 số nhãn nhất định feature : #ddcT-feature
         */
        if (isDependenceRelationTagger) {
            Collection<String> featureNames = FeatureDependence.createFeatureDependenceRelationTagger(cWord, null);
            if (featureNames != null) {
                for (String featureName : featureNames) {
                    ColumnDataClassifier.addFeature(featuresC, featureName, ColumnDataClassifier.DEFAULT_VALUE);
                }
            }
        }


        if (flags.isRealValued || flags.logTransform || flags.logitTransform || flags.sqrtTransform) {
            ColumnDataClassifier.addFeatureValue(cWord, flags, featuresC);
        }
        //logger.info("Made featuresC " + featuresC);
    }  //end makeDatum

    //=========================================//
    //get information 
    public Classifier<String, String> getClassifier() {
        return classifier;
    }

    public Index<String> getFeatureIndex() {
        return classifier.featureIndex();
    }

    public Index<String> getLabelIndex() {
        return classifier.labelIndex();
    }

    public double[][] getWeight() {
        return classifier.weights();
    }

    public ColumnDataClassifier getCdc() {
        return cdc;
    }

    public Counter<String> scoresOf(Datum<String, String> d) {
        Counter<String> scores = this.classifier.scoresOf(d);
        return scores;
    }

    public Counter<String> logProbabilityOf(Datum<String, String> d) {
        return classifier.logProbabilityOf(d);
    }

    public Counter<String> probabilityOf(Datum<String, String> d) {
        return classifier.probabilityOf(d);
    }

    public void dump() {
        classifier.dump();
    }

    public void justificationOf(Datum<String, String> d) {
        classifier.justificationOf(d);
    }

    public boolean isIsDependenceBase() {
        return isDependenceBase;
    }

    public void setIsDependenceBase(boolean isDependenceBase) {
        this.isDependenceBase = isDependenceBase;
    }

    public boolean isIsDependenceRelation() {
        return isDependenceRelation;
    }

    public void setIsDependenceRelation(boolean isDependenceRelation) {
        this.isDependenceRelation = isDependenceRelation;
    }

    public boolean isIsDependenceRelationTagger() {
        return isDependenceRelationTagger;
    }

    public void setIsDependenceRelationTagger(boolean isDependenceRelationTagger) {
        this.isDependenceRelationTagger = isDependenceRelationTagger;
    }

    public boolean isIsTagger() {
        return isTagger;
    }

    public void setIsTagger(boolean isTagger) {
        this.isTagger = isTagger;
    }

    public boolean isIsDictionary() {
        return isDictionary;
    }

    public void setIsDictionary(boolean isDictionary) {
        this.isDictionary = isDictionary;
    }

    public void setClassifier(LinearClassifier classifier) {
        this.classifier = classifier;
    }
    
    public static LinearClassifier loadModel(String modelName){
        LinearClassifier<String, String> cl = null;
        try {
             cl = IOUtils.readObjectFromFile(new File(modelName));
        } catch (IOException ex) {
            Logger.getLogger(MaximumEntropy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MaximumEntropy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cl;
    }
    
    private static void saveModel(String modelName){
        try {
            IOUtils.writeObjectToFile(classifier, modelName);
        } catch (IOException ex) {
            Logger.getLogger(MaximumEntropy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static void main(String[] args) {
        MaximumEntropy ma = new MaximumEntropy();

        ReadTextFile reader = new ReadTextFile();

        List<String> trainingSentences = new ArrayList<>();

        trainingSentences = reader.readTextFile(0, 16149, "src/resources/data/corpus_sentiment.txt");
        
        ma.trainingData2(trainingSentences);
        ma.saveModel("src/resources/MaxentSentiment.model");
        
        
//        try {
//            IOUtils.writeObjectToFile(classifier, "test.model");
//        } catch (IOException ex) {
//            Logger.getLogger(MaximumEntropy.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

}
