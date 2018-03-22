/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.sentiment.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.sentiment.test.Test;

/**
 *
 * @author Phu
 */
public class ReadTextFile {

    public static List<String> readTextFile(String path) {
        List<String> text = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String line =" ";
            while (line != null) {
                line = br.readLine();
                text.add(line);
                System.out.println(line);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return text;
    }

    public static List<String> readTextFile(int start, int end, String path) {
        List<String> text = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            int i = 1;
            String line = " ";
            while (line != null) {
                line = br.readLine();
                if (i >= start && i <= end) {
                    text.add(line);  
                }
                if (i == end) {
                    break;
                }
                i++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return text;
    }

    public static List<String> readTextFile(int startTrain, int startTest, int endTest, int endTrain, String path) {
        List<String> text = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            int i = 1;
            String line = " ";
            while (line != null) {
                line = br.readLine();
                if (i >= startTrain && i <= startTest || i >= endTest && i <= endTrain) {
                    text.add(line);
                }
                if (i > endTrain) {
                    break;
                }
                i++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return text;

    }

    // Hàm đoc kết quả từ file text Map<String, int>
    public static LinkedHashMap<String, Integer> getResult(int start, int end, String file) {
        LinkedHashMap<String, Integer> hash = new LinkedHashMap<String, Integer>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));
            String line = " ";
            int i = 1;
            String[] parseText;
            while (line != null) {
                line = br.readLine();
                if (i >= start && i <= end) {
                    parseText = line.split("\t");
                    hash.put(parseText[1], Integer.parseInt(parseText[0]));
                }
                if (i > end) {
                    break;
                }
                i++;
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadTextFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadTextFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(ReadTextFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return hash;
    }

    // lấy câu từ file text và trả về list<String> ==> các câu để test
    public static List<String> getSentences(int start, int end, String file) {
        List<String> sentences = new ArrayList<String>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));
            String line = " ";
            int i = 1;
            String sentence = " ";
            String[] parseText;
            while (line != null) {
                line = br.readLine();
                if (i >= start && i <= end) {
                    parseText = line.split("\t");
                    sentences.add(parseText[1]);
                }
                if (i > end) {
                    break;
                }
                i++;
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadTextFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadTextFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(ReadTextFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sentences;
    }
    public static void main(String[] args) {
        //List<String> str = readTextFile(10, 15, 20, 25, "src/resources/data/corpus_full.txt");

        // Map.Entry returns a collection-view wof the map, whose elements are of this class
        // Returns a Set that contains the entries in the map.
        readTextFile(1,5,10,20,"src/resources/data/corpus_full.txt");
        
    }
}
