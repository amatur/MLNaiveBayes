
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
        double TRAIN_PERCENT = .8;
        
        
        ArrayList<String> topics = new ArrayList<>();
        ArrayList<Document> docs = new ArrayList<>();
        Set<String> blacklist = new HashSet<>();

        new BlacklistRead(blacklistFile, blacklist).processFile();
        new TopicRead(topicFileName, topics).processFile();

        for (String topic : topics) {
            new XMLRead(trainFilePath.concat(topic).concat(".xml"), docs, blacklist, topic).processFile(400);
        }

        for (Iterator<Document> iterator = docs.iterator(); iterator.hasNext();) {
            Document next = iterator.next();
           // System.out.println(next.wordMap);
        }

        
        int numDocs = docs.size();
        int testStartIndex = (int) Math.floor(docs.size() * TRAIN_PERCENT);
        ArrayList<Document> trainingList = new ArrayList<>();
        ArrayList<Document> testList = new ArrayList<>();
        Collections.shuffle(docs);
        for (int i = 0; i < testStartIndex; i++) {
            trainingList.add(docs.get(i));
        }
        for (int i = testStartIndex; i < numDocs; i++) {
            testList.add(docs.get(i));
        }

        NaiveBayes NB = new NaiveBayes(1, topics);
        NB.train(trainingList);
        //NB.test(testList);

    }

}
