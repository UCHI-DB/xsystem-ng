package xsystem.unit;

import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.junit.After;
import org.junit.Test;

import xsystem.CSVGenerator;
import xsystem.learning.LearningModel;

public class LearnXStructTest {
    static int numTables = 5;
	static int numRows = 10;
	static int numAttributes = 6;
    static int maxStringLength = 10;
    final static String folder = "src/test/resources/csv";
    final static String outFolder = "src/test/resources/output/";
    
    @Test
    public void testLearnXstructs(){
        BasicConfigurator.configure();

        new CSVGenerator();
        CSVGenerator.writeTables(numTables, numRows, numAttributes, maxStringLength);

        String outFile = outFolder + "learned.json";

        LearningModel model = new LearningModel();
        
        model.learnStructs(folder, outFile);
    }

    //Delete the generated CSV files and folder
    @After
    public void cleanup() {
		File dir = new File(folder);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				child.delete();
			}
			dir.delete();
		}
	}
}