
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tasnim
 */
public class NaiveBayes {
    ArrayList<Document> docs;
    ArrayList<String> topics;
    double smoothingFactor;
    double[] logPriorProbs;
    double[][] logCondProbs;
    

    public NaiveBayes(double smoothingFactor, ArrayList<String> topics) {
        this.smoothingFactor = smoothingFactor;
        this.topics = topics;
        logPriorProbs = new double[topics.size()];
    }
    
    
    public void train(ArrayList<Document> docs){
        this.docs = docs;
        
        
        //build 1 document for each topic        
        HashMap<String, Document> classDocs = new HashMap<>();
        for(String topic: topics){
            classDocs.put(topic, new Document(new HashMap<String, Integer>(), topic));
        }
            
        //merge        
        for (Iterator<Document> iterator = docs.iterator(); iterator.hasNext();) {
            Document doc = iterator.next();            
            Document d = classDocs.get((String)doc.topicName);
            d.merge(doc);
        }
        
        for(String ss : classDocs.keySet()){
            System.out.println(ss);
            System.out.println(classDocs.get((String)ss).wordMap);
            System.out.println();
            System.out.println();
        }
        
        logCondProbs = new double[topics.size()][docs.size()];
    }
}
