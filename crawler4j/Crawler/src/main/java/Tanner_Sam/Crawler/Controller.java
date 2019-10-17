package Tanner_Sam.Crawler;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.io.*;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import org.iq80.leveldb.*;
import static org.fusesource.leveldbjni.JniDBFactory.*;


public class Controller {
	
	/*
	public static DB db;
	public static DB simpleDB;
    public static int foolishnessPageCount = 0;
    public static int deityPageCount = 0;
    public static int assassinationPageCount = 0;
    public static int totalWordsFound = 0;
    public static int uniquePages = 0;
    public static Set<String> bbPages;
    */

    public static void main(String[] args) throws Exception {
    	long startTime = System.currentTimeMillis();
    	
//    	bbPages = new HashSet<String>();
    	
        String crawlStorageFolder = "/share/bigspace/CS128-1-F19/crawly";
        int numberOfCrawlers = 10;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setUserAgentString("Westmont IR Tanner Leslie Sam Reep: Team raichu");
        config.setPolitenessDelay(100);
        
        // Instantiate the controller for this crawl.
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        // For each crawl, you need to add some seed urls. These are the first
        // URLs that are fetched and then the crawler starts following links
        // which are found in these pages
        controller.addSeed("http://djp3.westmont.edu/gutenberg/index.php");

        // The factory which creates instances of crawlers.
        CrawlController.WebCrawlerFactory<MyCrawler> crawlFactory = MyCrawler::new;

//        Options options = new Options();
//        options.createIfMissing(true);
//        db = factory.open(new File("/share/bigspace/CS128-1-F19/crawly/CrawlDB"), options);
//        simpleDB = factory.open(new File("/share/bigspace/CS128-1-F19/crawly/WordsDB"), options);
        
        try {

	        // Start the crawl. This is a blocking operation, meaning that your code
	        // will reach the line after this only when crawling is finished.
	        controller.start(crawlFactory, numberOfCrawlers);
        
        } finally {
        	/*
	        // ---Post Crawl Processing---//
        	//Gets results and prints them to console and writes them to a file for backup
        	
        	//calculate how long the crawl took
	        long endTime = System.currentTimeMillis();
	        long runTime = endTime - startTime;
	        long seconds = runTime/1000;
	        long minutes = seconds/60;
	        long hours = minutes/60;
	        seconds = seconds%60;
	        minutes = minutes%60;
	
	        BufferedWriter writer = new BufferedWriter(new FileWriter("/share/bigspace/CS128-1-F19/crawly/results.txt"));	        
	        
	        writer.write("Total Runtime: " + hours + " hours " + minutes + " minutes " + seconds + " seconds\n");
	        writer.write("Total Unique Pages Found: " + uniquePages + "\n");
	        writer.write("Total Unique Words Found: " + totalWordsFound + "\n");
	        writer.write("Foolishness Page Count: " + foolishnessPageCount + "\n");
	        writer.write("Deity Page Count: " + deityPageCount + "\n");
	        writer.write("Assassination Page Count: " + assassinationPageCount + "\n");
	        writer.write("Banksoopy Brickle Pages:\n");
	        
	        System.out.println("Total Runtime: " + hours + " hours " + minutes + " minutes " + seconds + " seconds");
	        System.out.println("Total Unique Pages Found: " + uniquePages);
	        System.out.println("Total Unique Words Found: " + totalWordsFound);
	        System.out.println("Foolishness Page Count: " + foolishnessPageCount);
	        System.out.println("Deity Page Count: " + deityPageCount);
	        System.out.println("Assassination Page Count: " + assassinationPageCount);
	        System.out.println("Banksoopy Brickle Pages:");
	        for(String page : bbPages) {
	        	writer.write(page + "\n");
	        	System.out.println(page);
	        }
	        writer.close();
	        db.close();
	        simpleDB.close();
	        */
        }
    }
}