import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Main {

    static String blacklistFile = "blacklist.txt";
    static String topicFileName = "./Data/topics.txt";
    static String trainFilePath = "./Data/Training/";
    static String testFilePath = "./Data/Test/";

    public static void main(String[] args) {
        
        ArrayList<String> topics = new ArrayList<>();
        ArrayList<Document> trainDocs = new ArrayList<>();        
        ArrayList<Document> testDocs = new ArrayList<>();
        Set<String> blacklist = new HashSet<>();

        new BlacklistRead(blacklistFile, blacklist).processFile();
        new TopicRead(topicFileName, topics).processFile();

        for (String topic : topics) {
            new XMLRead(trainFilePath.concat(topic).concat(".xml"), trainDocs, blacklist, topic).processFile(500);
            new XMLRead(testFilePath.concat(topic).concat(".xml"), testDocs, blacklist, topic).processFile(900);
        }

        NaiveBayes NB = new NaiveBayes(1, topics);
        NB.train(trainDocs);
        int correct = 0;
        int wrong = 0;
        
        for(Document testDoc : testDocs){            
            if(NB.test(testDoc) == testDoc.topicName){
                correct++;
            }else{
                wrong++;
            }
        }
        System.out.printf("Accuracy: %.4f%s ", (correct*1.0)/(correct+wrong)*100, "%" );

    }

}
