package classifier;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Hera
 */
public class KNNTextClassifier extends Classifier {
    
    int k;
    ArrayList<String> topics;

    public KNNTextClassifier(ArrayList<Document> trainingList, ArrayList<Document> testList, int k, ArrayList<String> topics) {
        super(trainingList, testList);
        this.k = k;
        this.topics = topics;
    }
    
    @Override
    void train() {
        // nothing to do for k-NN
    }
    
    @Override double test() {
        
        int success = 0, failure = 0;
        for (Object obj : testList) {
            
            Document d = (Document)obj;
            
            ArrayList<DocDistanceTuple> listToSort = new ArrayList<DocDistanceTuple>();
            for (Object obj1 : trainingList) {
                Document d1 = (Document)obj1;
                listToSort.add(new DocDistanceTuple(d1, d1.hammingDistance(d)));
            } 
            
            Collections.sort(listToSort);

            int[] count = new int[this.topics.size()];
            for (int i = 0; i < k; i++) {
                Document nn = listToSort.get(i).d;
                for (int j = 0; j < this.topics.size(); j++) {
                    if (this.topics.get(j).equals(nn.topic)) {
                        count[j]++;
                        break;
                    }
                }
            }
            
            int maxCount = -1, index = -1;
            for (int j = 0; j < count.length; j++) {
                if (count[j] > maxCount) {
                    maxCount = count[j];
                    index = j;
                }
            }
            
            String classifiedTopic = this.topics.get(index);
            
            System.out.println("Actual: " + d.topic + " Classified: " + classifiedTopic);
            
            if (d.topic.equals(classifiedTopic)) {
                System.out.println("Corrrectly classified");
                success++;
            } else {
                System.out.println("Inorrrectly classified");
                failure++;
            }
            
        }
        
        System.out.println("Success: " + success + " Failure: " + failure);
        return 1.0 * success / (success + failure);
    }
    
}



class DocDistanceTuple implements Comparable {
    Document d;
    double distance;
    
    DocDistanceTuple(Document d, double distance) {
        this.d = d;
        this.distance = distance;
    }
    
    @Override
    public int compareTo(Object o) {
        DocDistanceTuple ddt = (DocDistanceTuple)o;
        if (this.distance == ddt.distance)
            return 0;
        else
            return (int) ( (this.distance-ddt.distance)*1000 );
    }
    
}