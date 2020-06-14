package xsystem.ng.Learning;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import org.apache.log4j.BasicConfigurator;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xsystem.ng.XStructure;

public class LearnXStruct {

    private final static Logger LOG = LoggerFactory.getLogger(LearnXStruct.class.getName());

    public static HashMap<String, ArrayList<Pair<String, XStructure>>> logs;

    public static ArrayList<XStructure> learned;

    public LearnXStruct(){

        HashMap<String, ArrayList<Pair<String, XStructure>>> _logs = learnStructs();

        LearnXStruct.logs = _logs;

        LearnXStruct.learned = learned(_logs);
        
    }

    private HashMap<String, ArrayList<Pair<String, XStructure>>> learnStructs() {

        if(logs != null)
        return logs;

        BasicConfigurator.configure();

        String path = "src/main/resources/LearningData";

        String outputFile = "src/main/resources/Learned/LearnedXStructs.csv";

        final File folder = new File(path);

        final File outFile = new File(outputFile);

        HashMap<String, ArrayList<Pair<String, XStructure>>> logs = new HashMap<>();

        try {

            FileWriter outputfile = new FileWriter(outFile);

            CSVWriter writer = new CSVWriter(outputfile);

            for (File file : folder.listFiles()) {

                LOG.info("Started Learning From File " + file.getName());

                String[] header = { file.getName() };
                writer.writeNext(header);

                ArrayList<Pair<String, XStructure>> colXstruct = new ArrayList<>();

                CSVParser parser = new CSVParserBuilder()
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .build();

                CSVReader reader = new CSVReaderBuilder(new FileReader(file.getPath()))
                .withCSVParser(parser)
                .build();

                List<String[]> myEntries = reader.readAll();
                
                ArrayList<ArrayList<String>> columns = new ArrayList<>();

                for(String s : myEntries.get(0)){

                    ArrayList<String> column = new ArrayList<>();

                    column.add(s);

                    columns.add(column);

                }

                for(int i=0; i<myEntries.size(); i++){
                    
                    if(i==0);

                    else{
                        String[] row = myEntries.get(i);

                        if(row.length != columns.size()) LOG.info("Error in CSV file");

                        for(int j=0; j<row.length ; j++){
                            if(row[j] == null)
                                columns.get(j).add("");
                            else
                                columns.get(j).add(row[j]);
                        }
                    }

                }

                for(ArrayList<String> col : columns){

                    XStructure x = new XStructure();

                    String colName = col.get(0);

                    col.remove(0);

                    XStructure learned = x.addNewLines(col);

                    Pair<String, XStructure> pair = Pair.with(colName, learned);

                    colXstruct.add(pair);

                    LOG.info(colName + " has XStruct " + learned);

                    String[] entry = {colName, learned.toString()};
                    writer.writeNext(entry);
                }

                logs.put(file.getName(), colXstruct);

            }

            writer.close();
        }

        catch (Exception e) {
            //TODO: handle exception
            LOG.info("[Error] " + e);
        }

        return logs;

    }

    private ArrayList<XStructure> learned(HashMap<String, ArrayList<Pair<String, XStructure>>> logs){

        if(learned != null)
            return learned;
        
        ArrayList<XStructure> res = new ArrayList<>();

        for(String s : logs.keySet()){

            ArrayList<Pair<String, XStructure>> colXstructpair = logs.get(s);

            for(Pair<String, XStructure> colX : colXstructpair){
                res.add(colX.getValue1());
            }

        }

        return res;

    }

}