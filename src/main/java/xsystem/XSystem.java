package xsystem;

import org.javatuples.Pair;
import xsystem.layers.XStructure;

import java.util.ArrayList;
import java.util.regex.Pattern;

public interface XSystem {

    public XStructure build(ArrayList<String> data);

    public ArrayList<String> generate(XStructure pattern, int n);

    public double similarity(XStructure pattern, String value);

    public double similarity(XStructure pattern1, XStructure pattern2);

    public boolean match(XStructure pattern1, Pattern regex);

    public void learnXStructs(String inputFolder, String outputJSONfile);

    public ArrayList<Pair<XStructure, String>> readXStructswthType(String inputJSONfolder);

    public void labelAssignwthRegex(String sampleRegexFolderPath, String learnedXStructsJSONfolderpath, String outFile);

    public String labelAssignmentwthXStruct(ArrayList<String> strList, String referenceFilePath);

    public double computeOutlierScore(XStructure pattern, String str);

    public XStructure mergetwoXStructs(XStructure first, XStructure second);

    public XStructure mergeMultipleXStructs(ArrayList<XStructure> xstructLst);

}
