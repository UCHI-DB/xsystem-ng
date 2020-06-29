package xsystem.unit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import org.apache.log4j.BasicConfigurator;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xsystem.CSVGenerator;
import xsystem.learning.LearningModel;

public class LabelAssignmentTest {
    static int numTables = 5;
	static int numRows = 10;
	static int numAttributes = 6;
    static int maxStringLength = 10;
    final static String folder = "src/test/resources/csv";
    private final static Logger LOG = LoggerFactory.getLogger(LearningModel.class.getName());

    @Test
    public void defaultLabelAssignment(){
        BasicConfigurator.configure();

        new CSVGenerator();
        CSVGenerator.writeTables(numTables, numRows, numAttributes, maxStringLength);

        File inputFolder = new File(folder);

        try {

            CSVWriter writer = new CSVWriter(new FileWriter("src/test/resources/output/validation/defaultLabels.csv"));

            for(File inputFile : inputFolder.listFiles()){
                String head = "DataLabels from file " + inputFile.getName();
                String[] header = {head};
                writer.writeNext(header);

                CSVParser parser = new CSVParserBuilder()
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .build();

                CSVReader reader = new CSVReaderBuilder(new FileReader(inputFile.getPath()))
                .withCSVParser(parser)
                .build();

                List<String[]> myEntries = reader.readAll();

                ArrayList<String> colName = new ArrayList<>();
                ArrayList<ArrayList<String>> colData = new ArrayList<>();

                LearningModel model = new LearningModel();
                
                for(String str : myEntries.get(0)){
                    colName.add(str);
                    colData.add(new ArrayList<>());
                }

                myEntries.remove(0);

                for(String[] strList : myEntries){
                    for(int i=0; i<colName.size(); i++){
                        colData.get(i).add(strList[i]);
                    }
                }
                ArrayList<String> labels = new ArrayList<>();

                for(ArrayList<String> input : colData){
                    String label = model.labelAssignment(input, LearningModel.defaultXLabelRef);
                    labels.add(label);
                }

                for(int i=0; i<labels.size() && i<colName.size(); i++){
                    String[] entry = {colName.get(i), labels.get(i)};
                    writer.writeNext(entry);
                    LOG.info(colName.get(i) + " is assigned label " + labels.get(i));
                }
            }

            writer.close();
        } catch (Exception e) {
            LOG.info("[Error] " + e);
        }
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