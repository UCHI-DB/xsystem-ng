package xsystem;

import org.javatuples.Pair;
import xsystem.layers.XStructure;
import xsystem.learning.LearningModel;
import xsystem.learning.XStructType;
import xsystem.support.Config;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class XSystemImplementation implements XSystem {

    @Override
    public XStructure build(ArrayList<String> data){
        XStructure res = (new XStructure()).addNewLines(data);
        return res;
    }

    @Override
    public ArrayList<String> generate(XStructure pattern, int n){
        return pattern.generateStrings(n);
    }

    @Override
    public double similarity(XStructure pattern, String value){
        if(value.isEmpty())
            return 0;
        int req = Config.maxBranches;
        ArrayList<String> lines = new ArrayList<>();
        while(req-- >= 0)
            lines.add(value);
        XStructure x = (new XStructure()).addNewLines(lines);
        return pattern.compareTwo(pattern, x);
    }

    @Override
    public double similarity(XStructure pattern1, XStructure pattern2){
        return pattern1.compareTwo(pattern1, pattern2);
    }

    @Override
    public boolean match(XStructure pattern1, Pattern regex){
        String s = pattern1.generateStrings(1).get(0);
        return regex.matcher(s).matches();
    }

    @Override
    public void learnXStructs(String inputFolder, String outputJSONfile) {
        LearningModel model = new LearningModel();
        model.learnStructs(inputFolder, outputJSONfile);
    }

    @Override
    public ArrayList<Pair<XStructure, String>> readXStructswthType(String inputJSONfolder) {
        LearningModel model = new LearningModel();
        ArrayList<XStructType> xtype = model.readXStructsfromJSON(inputJSONfolder);
        ArrayList<Pair<XStructure, String>> res = new ArrayList<>();
        for ( XStructType x : xtype)
            res.add(new Pair<>(x.xStructure, x.type));
        return res;
    }

    @Override
    public void labelAssignwthRegex(String sampleRegexFolderPath, String learnedXStructsJSONfolderpath, String outFile) {
        LearningModel model = new LearningModel();
        model.labelAssignmentWithRegex(sampleRegexFolderPath, learnedXStructsJSONfolderpath, outFile);
    }

    @Override
    public String labelAssignmentwthXStruct(ArrayList<String> strList, String referenceFilePath) {
        LearningModel model = new LearningModel();
        String res = model.labelAssignment(strList, referenceFilePath);
        return res;
    }

    @Override
    public double computeOutlierScore(XStructure pattern, String str) {
        return pattern.computeOutlierScore(str);
    }

    @Override
    public XStructure mergetwoXStructs(XStructure first, XStructure second) {
        return first.mergeWith(second);
    }

    @Override
    public XStructure mergeMultipleXStructs(ArrayList<XStructure> XStructLst) {
        if(XStructLst.isEmpty())
            return null;
        XStructure head = XStructLst.get(0);
        XStructLst.remove(0);
        return head.mergeMultiple(XStructLst);
    }
}
