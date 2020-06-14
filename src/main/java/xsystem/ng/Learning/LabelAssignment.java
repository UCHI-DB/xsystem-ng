package xsystem.ng.Learning;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xsystem.ng.XStructure;

public class LabelAssignment {

    private final static Logger LOG = LoggerFactory.getLogger(LabelAssignment.class.getName());

    public static ArrayList<Pair<XStructure, String>> learnedXwithLabel;

    private static ArrayList<Pair<String, String>> randomStringsofDataTypes;

    private static ArrayList<Pair<String, String>> regexandType;

    public LabelAssignment() {

        LabelAssignment.randomStringsofDataTypes = randomStringwithType();

        LabelAssignment.regexandType = regexwithType();

        LabelAssignment.learnedXwithLabel = XandType();

    }
    private ArrayList<Pair<String, String>> randomStringwithType(){

        if(randomStringsofDataTypes != null)

            return randomStringsofDataTypes;

        String path = "src/main/resources/Regex/regexlst.csv";

        ArrayList<Pair<String, String>> res = new ArrayList<>();

        try {

            CSVParser parser = new CSVParserBuilder()
                                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                                .build();


            CSVReader reader = new CSVReaderBuilder(new FileReader(path))
                                .withCSVParser(parser)
                                .build();
            
            List<String[]> myEntries = reader.readAll();

            for(String[] s : myEntries){

                String str = s[3];

                if(str == null){
                    str = "";
                }
                else if(str.length() == 0){
                    str = "";
                }

                res.add(Pair.with(s[1], str));

            }

        } catch (Exception e) {

            LOG.info(e.toString());

        }
        
        return res;

    }

    private ArrayList<Pair<String, String>> regexwithType(){

        if(regexandType != null)

            return regexandType;
        
        String path = "src/main/resources/Regex/javaregex.csv";

        ArrayList<Pair<String, String>> res = new ArrayList<>();

        try {

            CSVParser parser = new CSVParserBuilder()
                                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                                .build();

            CSVReader reader = new CSVReaderBuilder(new FileReader(path))
                                .withCSVParser(parser)
                                .build();

            List<String[]> myEntries = reader.readAll();

            for(String[] s : myEntries){
                res.add(Pair.with(s[0], s[1]));
            }

        } catch (Exception e) {

            LOG.info(e.toString());
            //TODO: handle exception

        }

        return res;

    }
    
    private String xStructtotype(XStructure x){

        String res = "Not Known";

        for(Pair<String, String> regType : regexandType){

            boolean matches = Pattern.matches(regType.getValue1(), x.generateStrings(1).get(0).replace("$", ""));

            if(matches){

                res = regType.getValue0();

                break;

            }

        }

        return res;

    }
    
    private XStructure mostMatched(String str){

        new LearnXStruct();

        ArrayList<Double> scores = new ArrayList<>();

        if(str.isEmpty())

            str = "";
        
        for(XStructure x : LearnXStruct.learned){

            scores.add(x.computeOutlierScore(str));

        }

        double maxVal = Collections.max(scores);

        int maxIdx = scores.indexOf(maxVal);

        return LearnXStruct.learned.get(maxIdx);

    }

    private ArrayList<Pair<XStructure, String>> XandType(){

        new LearnXStruct();

        if(learnedXwithLabel != null){

            return learnedXwithLabel;

        }

        ArrayList<Pair<XStructure, String>> res = new ArrayList<>();

        for(XStructure x : LearnXStruct.learned){

            String type = xStructtotype(x);

            Pair<XStructure, String> pair = Pair.with(x, type);

            LOG.info(x.toString() + " matches data of type " + type);

            res.add(pair);

        }

        for(Pair<String, String> pair : randomStringsofDataTypes){

            XStructure matched = mostMatched(pair.getValue1().replace("\n", " "));

            Pair<XStructure, String> resPair = Pair.with(matched, pair.getValue0());

            LOG.info(matched.toString() + " matches data of type " + pair.getValue0());

            res.add(resPair);

        }
        
        return res;

    }

}