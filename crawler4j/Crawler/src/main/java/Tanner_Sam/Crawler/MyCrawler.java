package Tanner_Sam.Crawler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import org.iq80.leveldb.*;
import static org.fusesource.leveldbjni.JniDBFactory.*;

public class MyCrawler extends WebCrawler {

//    CrawlStats myCrawlStats = new CrawlStats();
    private List<String> StopWords = Arrays.asList(
        "a","about","above","after","again","against","all","am","an","and","any","are","aren't","as","at","be","because","been","before","being","below","between","both","but","by","can't","cannot","could","couldn't","did","didn't","do","does","doesn't","doing","don't","down","during","each","few","for","from","further",
        "had","hadn't","has","hasn't","have","haven't","having","he","he'd","he'll","he's","her","here","here's","hers","herself","him","himself","his","how","how's","i","i'd","i'll","i'm","i've","if","in","into","is","isn't","it","it's","its","itself","let's","me","more","most","mustn't","my","myself",
        "no","nor","not","of","off","on","once","only","or","other","ought","our","ours","ourselves","out","over","own","same","shan't","she","she'd","she'll","she's","should","shouldn't","so","some","such","than","that","that's","the","their","theirs","them","themselves","then","there","there's","these","they","they'd","they'll","they're","they've","this","those","through","to","too",
        "under","until","up","very","was","wasn't","we","we'd","we'll","we're","we've","were","weren't","what","what's","when","when's","where","where's","which","while","who","who's","whom","why","why's","with","won't","would","wouldn't","you","you'd","you'll","you're","you've","your","yours","yourself","yourselves"
    );

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpg|jpeg|png|tif|tiff|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    private final static Pattern MOREFILTERS = Pattern.compile(".*(\\.(txt|htm|ly|html|xml|mus|midi|ppt))$");
    

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "https://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
    /*
     @Override
     public boolean shouldVisit(Page referringPage, WebURL url) {
         String href = url.getURL().toLowerCase();
         return !FILTERS.matcher(href).matches()
                && href.startsWith("http://djp3.westmont.edu/gutenberg/")
                && !href.contains("?");
     }
    */
    
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
    	String href = url.getURL().toLowerCase(Locale.US);
    	boolean answer = !FILTERS.matcher(href).matches() && href.startsWith("http://djp3.westmont.edu/gutenberg");
    	boolean check = answer && href.startsWith("http://djp3.westmont.edu/gutenberg/stacks") 
    			&& !MOREFILTERS.matcher(href).matches();
    	if(check) {
    		try {
    			BufferedWriter writer = new BufferedWriter(new FileWriter("/share/bigspace/CS128-1-F19/crawly/raichu.txt", true));
    			writer.write("Found link to " + url + " on page " + referringPage.getWebURL().getURL().toLowerCase(Locale.US));
    			writer.newLine();
    			writer.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	return answer;
    }

     /**
      * This function is called when a page is fetched and ready
      * to be processed by your program.
      */
     @Override
     public void visit(Page page) {
    	 /*
         String url = page.getWebURL().getURL();
         System.out.println("URL: " + url);
         Controller.uniquePages++;

         if (page.getParseData() instanceof HtmlParseData) {
             HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
             String text = htmlParseData.getText();


             //parse text
             text = text.toLowerCase();
             text = text.replaceAll("[\\p{Punct}\\d\\n\\r]", " "); //replace special characters with spaces
             String[] textAL = text.split("\\s+"); //split the text on spaces
             
             HashMap<String, Integer> localDictionary = new HashMap<String, Integer>();
             
             //count words on page & find BB
             for(int i=0;i<textAL.length;i++){
                //skip stop words
                if(StopWords.contains(textAL[i])){
                    continue;
                }
               
                //store term count pairs in a local dictionary, to be added to db later
                if(localDictionary.containsKey(textAL[i])){
                	localDictionary.put(textAL[i], localDictionary.get(textAL[i])+1);
                }else{
                	localDictionary.put(textAL[i], 1);
                }
                
                switch (textAL[i]) {
	                case "foolishness":
	                	Controller.foolishnessPageCount++;
	                	break;
	                case "deity":
	                	Controller.deityPageCount++;
	                	break;
	                case "assassination": 
	                	Controller.assassinationPageCount++;
	                	break;
                }
                
                //if 'banksoopy brickle' is on this page, record the url
                if(textAL[i].equalsIgnoreCase("banksoopy") && textAL[i+1].equalsIgnoreCase("brickle")){
                	Controller.bbPages.add(url);
                }
                
             }
             //create (term, doc, count) tuples from local dictionary and put in db
             WriteBatch dbBatch = Controller.db.createWriteBatch();
             WriteBatch simpleBatch = Controller.simpleDB.createWriteBatch();
             try {
            	 for (HashMap.Entry<String, Integer> entry : localDictionary.entrySet()) {
            		 dbBatch.put(bytes(entry.getKey()+":"+url), bytes(entry.getValue().toString()));// key:"term:https://url.com", value:"1"
            		 if(Controller.simpleDB.get(bytes(entry.getKey())) != bytes(":)")) {
            			 simpleBatch.put(bytes(entry.getKey()), bytes(":)"));
            			 Controller.totalWordsFound++;
            		 }
            	 }
            	 Controller.db.write(dbBatch);
            	 Controller.simpleDB.write(simpleBatch);
             } finally {
            	 try {
					dbBatch.close();
					simpleBatch.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
             }
         }
         */
    }

}

