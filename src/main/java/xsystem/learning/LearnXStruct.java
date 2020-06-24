package xsystem.learning;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import org.apache.log4j.BasicConfigurator;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xsystem.XStructure;

public class LearnXStruct {

    private final static Logger LOG = LoggerFactory.getLogger(LearnXStruct.class.getName());

    public LearnXStruct(){}

    public void learnStructs(String inputFolder, String outputJsonFile) {
        BasicConfigurator.configure();

        final File folder = new File(inputFolder);
        final File outFile = new File(outputJsonFile);

        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            List<Pair<XStructure, String>> result = new ArrayList<Pair<XStructure,String>>();

            for (File file : folder.listFiles()) {
                LOG.info("Started Learning From File " + file.getName());
                
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
                        if(row.length != columns.size())
                            LOG.info("Error in CSV file");

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
                    Pair<XStructure, String> pair = Pair.with(learned, colName);

                    LOG.info(colName + " has XStruct " + learned);
                    result.add(pair);
                }
            }
            writer.writeValue(outFile, result);
        }

        catch (Exception e) {
            //TODO: handle exception
            LOG.info("[Error] " + e);
        }
    }
}