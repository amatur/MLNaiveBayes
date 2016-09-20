package classifier;

import java.util.ArrayList;

/**
 *
 * @author Hera
 */
public class Main {
    
    public static void main(String[] args) {
        
        TopicFileParser topicReader = new TopicFileParser("topics.txt");
        ArrayList<String> topics = (ArrayList<String>)topicReader.getDataList();
        
        ArrayList trainigSet = new ArrayList<>();
        ArrayList testSet = new ArrayList<>();
        
        for (String topic : topics) {
            FileParser trainingFileReader = new XMLParser("./Training/"+topic+".xml", topic);
            FileParser testFileReader = new XMLParser("./Test/"+topic+".xml", topic);
            trainigSet.addAll(trainingFileReader.getDataList());
            testSet.addAll(testFileReader.getDataList());
        }
        
        KNNTextClassifier c = new KNNTextClassifier(trainigSet, testSet, 1, topics);
        c.train();
        c.test();
        
    }
    
}
