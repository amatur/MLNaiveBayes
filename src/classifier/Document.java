package classifier;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Hera
 */
public class Document {
    
    HashMap wordList = null;
    String topic;
    
    Document(String topic) {
        wordList = new HashMap();
        this.topic = topic;
    }
    
    void addWord(String word, int frequency) {
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
            if (!this.wordList.containsKey(o)) {
                distance++;
            }
        }
        for (Object o : this.wordList.keySet()) {
            if (!d.wordList.containsKey(o)) {
                distance++;
            }
        }
        return distance;
    }
    
    int euclideanDistance(Document d) {
        int distance = 0;
        HashSet<Object> set = new HashSet<Object>();
        for (Object obj : d.wordList.keySet())
            set.add(obj);
        for (Object obj : this.wordList.keySet())
            set.add(obj);
        for (Object obj : set) {
            int value1, value2;
            Object o1 = d.wordList.get(obj);
            if (o1 != null) {
                value1 = (Integer)o1;
            } else {
                value1 = 0;
            }
            Object o2 = this.wordList.get(obj);
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
        HashSet<Object> set = new HashSet<Object>();
        for (Object obj : d.wordList.keySet())
            set.add(obj);
        for (Object obj : this.wordList.keySet())
            set.add(obj);
        for (Object obj : set) {
            Object o1 = d.wordList.get(obj);
            Object o2 = this.wordList.get(obj);
            if (o1 != null && o2 != null) {
                dp += (Integer)(o1) * (Integer)(o2);
            }
        }
        return dp;
    }
    
    private double vectorLength() {
        int total = 0;
        for (Object o : this.wordList.keySet()) {
            int val = (Integer)this.wordList.get(o);
            total += val * val;
        }
        return Math.sqrt(total);
    }
    
}
