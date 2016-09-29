import classifier.KNNTextClassifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

/* collected
 Best SF: 0.39599999999999946 Best Acc: 98.11122770199371
 Best SF: 0.39599999999999946Best Acc: 98.11122770199371
 Best SF: 0.31600000000000017 Best Acc: 98.11122770199371
 */

public class Main {

    /* File Paths */
    static int NUM_TRAINING_ROW = 5000;
    static int ROW_EACH_ITER = 500;
    static int NUM_ITERATIONS = 10;
    static int NUM_TEST_DOCS = 50;

    /* Naive Bayes Parameters */
    static double BEST_SF = 0.31600000000000017;

    /* File Paths */
    static String blacklistFile = "blacklist.txt";
    static String topicFileName = "./Data/topics.txt";
    static String trainFilePath = "./Data/Training/";
    static String testFilePath = "./Data/Test/";

    public static ArrayList<classifier.Document> docConverter(ArrayList<Document> docsNaiveBayes) {
        ArrayList<classifier.Document> docsKNN = new ArrayList<>();
        for (Document d : docsNaiveBayes) {
            classifier.Document knnDoc = new classifier.Document(d.topicName);
            for (Entry e : d.wordMap.entrySet()) {
                knnDoc.addWord((String) e.getKey(), (Integer) e.getValue());
            }
            docsKNN.add(knnDoc);
        }
        return docsKNN;
    }

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
            new XMLRead(testFilePath.concat(topic).concat(".xml"), testDocs, blacklist, topic).processFile(NUM_TEST_DOCS);
        }

        //double bestSmoothingFactor = NaiveBayes.findBestSmoothingFactor(trainDocs, testDocs, topics);

        Sampst sampstNB = new Sampst();
        Sampst sampstKNN = new Sampst();

        for (int i = 0; i < NUM_ITERATIONS; i++) {

            ArrayList<Document> trainDocsSubset = new ArrayList<>();
            for (int j = i * ROW_EACH_ITER; j < i * ROW_EACH_ITER + ROW_EACH_ITER; j++) {
                for (int k = 0; k < topics.size(); k++) {
                    trainDocsSubset.add(trainDocs.get(k * NUM_TRAINING_ROW + j));
                }

            }

            NaiveBayes NB = new NaiveBayes(BEST_SF, topics, trainDocsSubset);
            KNNTextClassifier KNN = new KNNTextClassifier(docConverter(trainDocsSubset), docConverter(testDocs), 7, KNNTextClassifier.COSINE);
            KNN.train();


            double accKNN = KNN.test();
            double accNB = NB.test(testDocs);
            sampstNB.add(accNB);
            sampstKNN.add(accKNN);
            System.out.printf("NB: %.4f%s \t KNN: %.4f%s\n", accNB, "%", accKNN, "%");
        }

        System.out.println("T-score " + Sampst.tScore(sampstNB, sampstKNN));
        System.out.println(Sampst.accept005(Sampst.tScore(sampstNB, sampstKNN)));
        System.out.println(Sampst.accept01(Sampst.tScore(sampstNB, sampstKNN)));
        System.out.println(Sampst.accept05(Sampst.tScore(sampstNB, sampstKNN)));

    }

}
