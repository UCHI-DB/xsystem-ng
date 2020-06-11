package xsystem.ng.Learning;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVReader;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xsystem.ng.XStructure;

public class ParseCSV {

    private final static Logger LOG = LoggerFactory.getLogger(ParseCSV.class.getName());

    public static HashMap<String, ArrayList<Pair<String, XStructure>>> logs;

    public static ArrayList<XStructure> learned;

    public ParseCSV(){

        HashMap<String, ArrayList<Pair<String, XStructure>>> _logs = learnStructs();

        ParseCSV.logs = _logs;
        
        ParseCSV.learned = learned(_logs);
    }

    private HashMap<String, ArrayList<Pair<String, XStructure>>> learnStructs() {

        if(logs != null)
            return logs;

        String path = "src/main/resources/LearningData";

        final File folder = new File(path);

        HashMap<String, ArrayList<Pair<String, XStructure>>> logs = new HashMap<>();

        for(File file : folder.listFiles()){

            LOG.info("Started Learning From File " + file.getName());

            ArrayList<Pair<String, XStructure>> colXstruct = new ArrayList<>();

            try {

                CSVReader reader = new CSVReader(new FileReader(file.getPath()));

                List<String[]> myEntries = reader.readAll();
                
                ArrayList<ArrayList<String>> columns = new ArrayList<>();

                for(String s : myEntries.get(0)){

                    ArrayList<String> column = new ArrayList<>();

                    column.add(s);

                    columns.add(column);

                }

                for(int i=1; i<myEntries.size(); i++){

                    String[] row = myEntries.get(i);

                    if(row.length != columns.size()) LOG.info("Error in CSV file");

                    for(int j=0; j<row.length ; j++){

                        columns.get(j).add(row[j]);

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
                }

                logs.put(file.getName(), colXstruct);

            } 

            catch (Exception e) {
                //TODO: handle exception
                LOG.info("[Error] " + e);
            }
        }

        return logs;
    }

    private ArrayList<XStructure> learned(HashMap<String, ArrayList<Pair<String, XStructure>>> logs){

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