import classifier.*;
import classifier.KNNTextClassifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class Main {

    static int NUM_TRAINING_ROW = 5000;
    static int ROW_EACH_ITER = 500;
    static int NUM_ITERATIONS = 10;

    static String blacklistFile = "blacklist.txt";
    static String topicFileName = "./Data/topics.txt";
    static String trainFilePath = "./Data/Training/";
    static String testFilePath = "./Data/Test/";

    public static void main(String[] args) {

        ArrayList<String> topics = new ArrayList<>();
        ArrayList<Document> trainDocs = new ArrayList<>();
        ArrayList<Document> testDocs = new ArrayList<>();
        Set<String> blacklist = new HashSet<>();

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        System.out.print(dateFormat.format(new Date()) + " ");
        System.out.println("File parsing in progress ... please wait.");
        new BlacklistRead(blacklistFile, blacklist).processFile();
        new TopicRead(topicFileName, topics).processFile();

        for (String topic : topics) {
            System.out.println(topic);
            new XMLRead(trainFilePath.concat(topic).concat(".xml"), trainDocs, blacklist, topic).processFile(NUM_TRAINING_ROW + 2);
            new XMLRead(testFilePath.concat(topic).concat(".xml"), testDocs, blacklist, topic).processFile(50);
        }

        NaiveBayes NB = new NaiveBayes(0.1, topics);
        

        /**
         * iterations *
         */
        //init
        double[] alphas = new double[50];
        alphas[0] = 1;
        double sf = 1;
        for (int i = 1; i < NUM_ITERATIONS; i++) {
            if (i >= 1 && i < 10) {
                sf = sf - 0.05;
            }
            if (i >= 10 && i < 20) {
                sf = sf - 0.01;

            }
            if (i >= 20 && i < 30) {
                sf = sf - 0.005;

            }
            if (i >= 30 && i < 50) {
                sf = sf - 0.001;
            }
            alphas[i] = sf;
        }

       
        
        Sampst sampstNB = new Sampst();
         Sampst sampstKNN = new Sampst();
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            
            ArrayList<Document> trainDocsSubset = new ArrayList<>();
            for (int j = i*ROW_EACH_ITER; j < i*ROW_EACH_ITER + ROW_EACH_ITER ; j++) {
                for (int k = 0; k < topics.size(); k++) {
                     trainDocsSubset.add(trainDocs.get(k*NUM_TRAINING_ROW + j));
                }
               
            }
            NB.train(trainDocsSubset);
            Classifier c = new KNNTextClassifier(docConverter(trainDocsSubset), docConverter(testDocs), 7, KNNTextClassifier.HAMMING);
            c.train();
                        
            NB.setSmoothingFactor(alphas[i]);
            
            System.out.printf("%f\t", alphas[i]);

            //start testing
            int correct = 0;
            int wrong = 0;

            for (Document testDoc : testDocs) {
                if (NB.test(testDoc) == testDoc.topicName) {
                    correct++;
                } else {
                    wrong++;
                }
            }
            double accuracy = (correct * 1.0) / (correct + wrong) * 100;
            sampstNB.add(accuracy);
            System.out.printf("%.4f%s\n", accuracy, "%");
            sampstKNN.add(c.test()*100);
            System.out.printf("KNN: %.4f%s\n", c.test()*100, "%");
            
           // System.err.println();
        
        }
        
        System.out.println("T-score " + Sampst.tScore(sampstNB, sampstKNN));
        System.out.println(Sampst.accept005(Sampst.tScore(sampstNB, sampstKNN)));
        System.out.println(Sampst.accept01(Sampst.tScore(sampstNB, sampstKNN)));
        System.out.println(Sampst.accept05(Sampst.tScore(sampstNB, sampstKNN)));
    }

    
    
    public static ArrayList<classifier.Document> docConverter(ArrayList<Document> docsNaiveBayes){
         ArrayList<classifier.Document> docsKNN = new ArrayList<>();
         for(Document d : docsNaiveBayes){
             classifier.Document knnDoc = new classifier.Document(d.topicName);             
             for(Entry e : d.wordMap.entrySet()){
                 knnDoc.addWord((String)e.getKey(), (Integer)e.getValue());
             }
             docsKNN.add(knnDoc);             
         }
         return docsKNN;
    }
    
}
