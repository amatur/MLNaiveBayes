package classifier;

import java.util.ArrayList;

/**
 *
 * @author Hera
 */
public abstract class Classifier {

    ArrayList trainingList;
    ArrayList testList;
    
    Classifier(ArrayList trainingSet, ArrayList testSet) {
        this.testList = testSet;
        this.trainingList = trainingSet;
    }
    
    abstract void train();
    
    abstract double test();
    
}
