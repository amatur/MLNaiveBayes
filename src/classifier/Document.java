package classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Hera
 */
public class Document {
    
    HashMap wordList = null;
    String topic;
    
    public Document(String topic) {
        wordList = new HashMap();
        this.topic = topic;
    }
    
    public void addWord(String word, int frequency) {
        wordList.put(word, frequency);
    }
    
    int getFrequency(String word) {
        return (int)wordList.get(word);
    }
    
    void increaseFrequency(String word) {
        word = word.trim();
        if (word.length() == 0)
            return;
        Integer count = (Integer)wordList.get(word);
        if (count == null)
            count = 0;
        count++;
        
        wordList.remove(word);
        wordList.put(word, count);
    }
    
    void removeWords(ArrayList<String> words) {
        for (String word : words) {
            this.wordList.remove(word);
        }
    }
    
    void print() {
        System.out.println("Topic: " + this.topic);
        for (Object o : wordList.keySet()) {
            String word = (String)o;
            System.out.println("Word: " + word + " Freq: " + wordList.get(word));
        }
    }
    
    int hammingDistance(Document d) {
        int distance = 0;
        for (Object o : d.wordList.keySet()) {
            String str = (String)o;
            if (!this.wordList.containsKey(str)) {
                distance++;
            }
        }
        for (Object o : this.wordList.keySet()) {
            String str = (String)o;
            if (!d.wordList.containsKey(str)) {
                distance++;
            }
        }
        return distance;
    }
    
    int euclideanDistance(Document d) {
        int distance = 0;
        HashSet<String> set = new HashSet<String>();
        for (Object obj : d.wordList.keySet())
            set.add((String)obj);
        for (Object obj : this.wordList.keySet())
            set.add((String)obj);
        for (Object obj : set) {
            int value1, value2;
            Object o1 = d.wordList.get((String)obj);
            if (o1 != null) {
                value1 = (Integer)o1;
            } else {
                value1 = 0;
            }
            Object o2 = this.wordList.get((String)obj);
            if (o2 != null) {
                value2 = (Integer)o2;
            } else {
                value2 = 0;
            }
            distance += (value1 - value2) * (value1 - value2);
        }
        return distance;
    }
    
    double cosineDistance(Document d) {
        return (double)this.dotProduct(d) / (this.vectorLength() * d.vectorLength());
    }
    
    private int dotProduct(Document d) {
        int dp = 0;
        HashSet<String> set = new HashSet<String>();
        for (Object obj : d.wordList.keySet())
            set.add((String)obj);
        for (Object obj : this.wordList.keySet())
            set.add((String)obj);
        for (Object obj : set) {
            String str = (String)obj;
            Object o1 = d.wordList.get(str);
            Object o2 = this.wordList.get(str);
            if (o1 != null && o2 != null) {
                dp += (Integer)(o1) * (Integer)(o2);
            }
        }
        return dp;
    }
    
    private double vectorLength() {
        int total = 0;
        for (Object o : this.wordList.keySet()) {
            String str = (String)o;
            int val = (Integer)this.wordList.get(str);
            total += val * val;
        }
        return Math.sqrt(total);
    }
    
    public static void main(String[] args) {
        Document d = new Document("Test");
        d.increaseFrequency("i");
        d.increaseFrequency("i");
        d.increaseFrequency("i");
        d.increaseFrequency("am");
        d.increaseFrequency("a");
        d.increaseFrequency("boy");
        d.print();
    }
    
}
