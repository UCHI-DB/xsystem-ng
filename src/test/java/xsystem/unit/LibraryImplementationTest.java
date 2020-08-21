package xsystem.unit;

import org.apache.commons.text.RandomStringGenerator;
import org.apache.log4j.BasicConfigurator;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xsystem.CSVGenerator;
import xsystem.XSystemImplementation;
import xsystem.layers.XStructure;
import xsystem.learning.LearningModel;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class LibraryImplementationTest {
    static int numTables = 5;
    static int numRows = 10;
    static int numAttributes = 6;
    static int maxStringLength = 10;
    final static String folder = "src/test/resources/csv";
    final static String outFolder = "src/test/resources/output/learned/";
    private final static Logger LOG = LoggerFactory.getLogger(LibraryImplementationTest.class.getName());

    @Test
    public void learnXStructsTestImpl(){
        BasicConfigurator.configure();

        new CSVGenerator();
        CSVGenerator.writeTables(numTables, numRows, numAttributes, maxStringLength);

        XSystemImplementation xsyst = new XSystemImplementation();
        xsyst.learnXStructs(folder, outFolder+"implLearned.json");
    }

    @Test
    public void readXStructswthTypeTestImpl(){
        BasicConfigurator.configure();
        XSystemImplementation xsyst = new XSystemImplementation();
        xsyst.readXStructswthType(outFolder+"implLearned.json");
    }

    @Test
    public void buildTestImpl(){
        BasicConfigurator.configure();
        int random = ThreadLocalRandom.current().nextInt(20, 100+1);
        ArrayList<String> lines = new ArrayList<>();
        RandomStringGenerator strGenerator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        for(int i=0; i<random; i++)
            lines.add(strGenerator.generate(5, 20));
        XSystemImplementation xsyst = new XSystemImplementation();
        XStructure xst = xsyst.build(lines);
        LOG.info("XStructure " + xst);
    }

    @Test
    public void generateTestImpl(){
        BasicConfigurator.configure();
        int random = ThreadLocalRandom.current().nextInt(20, 100+1);
        ArrayList<String> lines = new ArrayList<>();
        RandomStringGenerator strGenerator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        for(int i=0; i<random; i++)
            lines.add(strGenerator.generate(5, 20));
        XSystemImplementation xsyst = new XSystemImplementation();
        XStructure xst = xsyst.build(lines);
        LOG.info("Generated lines - " + xsyst.generate(xst, ThreadLocalRandom.current().nextInt(5, 10)));
    }

    @Test
    public void similarityStrTestImpl(){
        BasicConfigurator.configure();
        int random = ThreadLocalRandom.current().nextInt(20, 100+1);
        ArrayList<String> lines = new ArrayList<>();
        RandomStringGenerator strGenerator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        for(int i=0; i<random; i++)
            lines.add(strGenerator.generate(5, 20));
        XSystemImplementation xsyst = new XSystemImplementation();
        XStructure xst = xsyst.build(lines);
        LOG.info("Computed Score - " + String.valueOf(xsyst.similarity(xst, strGenerator.generate(10, 15))));
    }

    @Test
    public void similarityXStrctTestImpl(){
        BasicConfigurator.configure();
        RandomStringGenerator strGenerator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();

        int random1 = ThreadLocalRandom.current().nextInt(20, 100+1);
        ArrayList<String> lines1 = new ArrayList<>();

        int random2 = ThreadLocalRandom.current().nextInt(20, 100+1);
        ArrayList<String> lines2 = new ArrayList<>();

        for(int i=0; i<random1; i++)
            lines1.add(strGenerator.generate(5, 20));

        for(int i=0; i<random2; i++)
            lines2.add(strGenerator.generate(5, 20));

        XSystemImplementation xsyst = new XSystemImplementation();
        XStructure xst1 = xsyst.build(lines1);
        XStructure xst2 = xsyst.build(lines2);

        LOG.info("Computed Score - " + String.valueOf(xsyst.similarity(xst1, xst2)));
    }

    @Test
    public void matchingTestImpl(){
        BasicConfigurator.configure();
        int random = ThreadLocalRandom.current().nextInt(20, 100+1);
        ArrayList<String> lines = new ArrayList<>();
        RandomStringGenerator strGenerator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        for(int i=0; i<random; i++)
            lines.add(strGenerator.generate(5, 20));
        XSystemImplementation xsyst = new XSystemImplementation();
        XStructure xst = xsyst.build(lines);

        Pattern regex = Pattern.compile("\"^[a-zA-Z0-9]*$\"");

        LOG.info("On matching the random regex and xstruct, the output is " + xsyst.match(xst, regex));
    }

    @Test
    public void outlierScoreTestImpl(){
        BasicConfigurator.configure();
        int random = ThreadLocalRandom.current().nextInt(20, 100+1);
        ArrayList<String> lines = new ArrayList<>();
        RandomStringGenerator strGenerator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        for(int i=0; i<random; i++)
            lines.add(strGenerator.generate(5, 20));
        XSystemImplementation xsyst = new XSystemImplementation();
        XStructure xst = xsyst.build(lines);
        LOG.info("Computed Outlier Score - " + String.valueOf(xsyst.computeOutlierScore(xst, strGenerator.generate(10, 15))));
    }

    @Test
    public void mergeTwoXStTestImpl(){
        BasicConfigurator.configure();
        RandomStringGenerator strGenerator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();

        int random1 = ThreadLocalRandom.current().nextInt(20, 100+1);
        ArrayList<String> lines1 = new ArrayList<>();

        int random2 = ThreadLocalRandom.current().nextInt(20, 100+1);
        ArrayList<String> lines2 = new ArrayList<>();

        for(int i=0; i<random1; i++)
            lines1.add(strGenerator.generate(5, 20));

        for(int i=0; i<random2; i++)
            lines2.add(strGenerator.generate(5, 20));

        XSystemImplementation xsyst = new XSystemImplementation();
        XStructure xst1 = xsyst.build(lines1);
        XStructure xst2 = xsyst.build(lines2);

        XStructure merged = xsyst.mergetwoXStructs(xst1, xst2);

        LOG.info("On merging XStruct 1 {" + xst1 + "} with XStruct 2 {" + xst2 + "} we get {" + merged + "}");
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
