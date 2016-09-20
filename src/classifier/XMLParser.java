package classifier;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hera
 */
public class XMLParser extends FileParser {
    
    String topicName;
    
    XMLParser(String fileName, String topic) {
        super(fileName);
        this.topicName = topic;
    }
    
    @Override 
    public ArrayList getDataList() {
        
        ArrayList<Document> list = new ArrayList<Document>();
        
        List<String> lines;
        try {
            lines = Files.readAllLines(super.file.toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        int count = 0;
        for (String line : lines) {
            String[] words = line.split("Body=\"&lt;p&gt;");
            if (words.length == 2) {
                list.add(makeDocument(words[1].split("&lt;/p&gt;&#xA;\"")[0], this.topicName));
                count++;
                if(count == 10)
                    break;
            }
        }

        return list;
    }
    
    Document makeDocument(String line, String topicName) {
        line = line.replaceAll("\\.|\\?", "");
        line = line.toLowerCase();
        Document d = new Document(topicName);
        String[] words = line.split(" ");
        for (String word : words) {
            d.increaseFrequency(word);
        }
        return d;
    }
    
}
