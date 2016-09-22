
import java.util.ArrayList;
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
        ArrayList<String> words = new ArrayList<>();
        Set<String> blacklist = new HashSet<>();
        
        new BlacklistRead(blacklistFile,blacklist).processFile();        
        new TopicRead(topicFileName,topics).processFile();
        
        for (String topic: topics){
             new XMLRead(trainFilePath.concat(topic).concat(".xml"), words, blacklist).processFile(400);
        }
        
        for (Iterator<String> iterator = words.iterator(); iterator.hasNext();) {
            String next = iterator.next();
             System.out.println(next);
        }
 
       
        
        ArrayList<Integer> training = new ArrayList<Integer>();
        
    }
    
}