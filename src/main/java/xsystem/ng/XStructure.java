package xsystem.ng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import xsystem.ng.Layers.Branch;
import xsystem.ng.Support.Config;
import xsystem.ng.Support.Utils;
import xsystem.ng.Support.Wrapper;

public class XStructure {

    //The XStructure!!

    private final HashMap<Branch, Double> branches;

    private final ArrayList<String> lines;

    private final double branchingThreshold;

    private final ArrayList<Double> history;

    public ArrayList<String> tokensGenerator;

    public ArrayList<String> hingesGenerator;

    public ArrayList<String> minHashStringGenerator;

    public XStructure(){

        this.branches = new HashMap<>();
        this.lines = new ArrayList<>();
        this.branchingThreshold = Config.branchingSeed;
        this.history = new ArrayList<>();
        this.tokensGenerator = tknsGen(branches);
        this.hingesGenerator = hngGen(branches);
        this.minHashStringGenerator = minHashGen(tokensGenerator, hingesGenerator);

    }

    public XStructure(ArrayList<String> _lines, HashMap<Branch, Double> _branches, double _bThresh, ArrayList<Double> _history){

        this.branches = _branches;
        this.lines = (Config.tts) ? _lines : new ArrayList<String>();
        this.branchingThreshold = _bThresh;
        this.history = (Config.tts) ? _history : new ArrayList<Double>();
        this.tokensGenerator = tknsGen(_branches);
        this.hingesGenerator = hngGen(_branches);
        this.minHashStringGenerator = minHashGen(tknsGen(_branches), hngGen(_branches));

    }

    //Compute the tokensGenerator
    private ArrayList<String> tknsGen (HashMap<Branch, Double> brnches){

        ArrayList<ArrayList<Wrapper>> lst = new ArrayList<>();

        brnches.forEach( (b, v) -> {

            ArrayList<Wrapper> wrList = new ArrayList<>();
            ArrayList<String> stList = b.branchStringGenerator;

            for(String str : stList){
                wrList.add(new Wrapper(str));
            }

            lst.add(wrList);
        });

        ArrayList<Wrapper> res = Utils.mergeStreams(lst);
        ArrayList<String> result = new ArrayList<>();

        for(Wrapper w : res){
            result.add(w.getString());
        }

        return result;

    }

    //Compute the hingesGenerator
    private ArrayList<String> hngGen (HashMap<Branch, Double> brnches){
        
        ArrayList<ArrayList<Wrapper>> lst = new ArrayList<>();

        brnches.forEach( (b, v) -> {

            ArrayList<Wrapper> wrList = new ArrayList<>();
            ArrayList<String> stList = b.tokenizerStringGenerator;

            for(String str : stList){
                wrList.add(new Wrapper(str));
            }

            lst.add(wrList);
        });

        ArrayList<Wrapper> res = Utils.mergeStreams(lst);
        ArrayList<String> result = new ArrayList<>();

        for(Wrapper w : res){
            result.add(w.getString());
        }

        return result;

    }

    //Compute the minHashStringGenerator
    private ArrayList<String> minHashGen (ArrayList<String> strLst1, ArrayList<String> strLst2){

        ArrayList<ArrayList<Wrapper>> lst = new ArrayList<>();

        ArrayList<Wrapper> subLst1 = new ArrayList<>();
        ArrayList<Wrapper> subLst2 = new ArrayList<>();

        for(String str : strLst1){
            subLst1.add(new Wrapper(str));
        }

        for(String str : strLst2){
            subLst2.add(new Wrapper(str));
        }

        for(String str : strLst1){
            subLst2.add(new Wrapper(str));
        }

        lst.add(subLst1);
        lst.add(subLst2);

        ArrayList<Wrapper> res = Utils.mergeStreams(lst);
        ArrayList<String> result = new ArrayList<>();

        for(Wrapper w : res){
            result.add(w.getString());
        }

        return result;

    }

    public Pair<XStructure, Boolean> addLine(String line){

        Triplet<HashMap<Branch, Double>, Branch, Double> param = findRightBranch(line);

        Branch b = param.getValue1();

        ArrayList<String> _lines = lines;
        _lines.add(line);

        HashMap<Branch, Double> _branches = param.getValue0();
        double score = _branches.get(b) + 1.0;
        _branches.remove(b);
        _branches.put(b.learnString(line), score);

        double _bthresh = branchingThreshold;

        ArrayList<Double> _history = history;
        if(branches.size() == _branches.size()){
            _history.add(param.getValue2());
        }
        else{
            _history.add(0.0);
        }

        XStructure xstruct = new XStructure(_lines, _branches, _bthresh, _history);

        DescriptiveStatistics stats = new DescriptiveStatistics();

        for(double d : history) stats.addValue(d);

        double stdev = stats.getStandardDeviation();

        Boolean bool = (history.size()+1)%Config.inc > 0  && Config.neededSampleSize(stdev) < history.size();

        return Pair.with(xstruct.trim(), bool);

    }

    public XStructure addNewLines(ArrayList<String> lines){
        
        if(Config.tts){
            
            ArrayList<Pair<XStructure, Boolean>> scannedLines = new ArrayList<>();
            
            XStructure x = this;
            
            for(String line : lines){
                
                if(line.length() != 0){
                    
                    Pair<XStructure, Boolean> pair = x.addLine(line);
                    x = pair.getValue0();
                    scannedLines.add(pair);
                }
            }
            
            for(int i = scannedLines.size() - 1; i>=0; i--){
                if(scannedLines.get(i).getValue1()) return scannedLines.get(i).getValue0();
            }
            
            return scannedLines.get(scannedLines.size()-1).getValue0();

        }

        else{
            XStructure x = this;
            Pair<XStructure, Boolean> added = Pair.with(x, false);

            for( String line : lines){
                if(line.length()>0){
                    added = x.addLine(line);
                    x = added.getValue0();
                }
            }

            return added.getValue0();

        }

    }

    private Pair<HashMap<Branch, Double>, Branch> newBranch(String str){

        Branch b = new Branch(str);

        HashMap<Branch, Double> res = new HashMap<>();
        res.putAll(branches);
        res.put(b, (double) 0);
    
        return Pair.with(res, b);

    }

    // Given a string, find which branch of the representation should learn it
    private Triplet<HashMap<Branch, Double>, Branch, Double> findRightBranch(String str){

        if(branches.size() == 0){

            Pair<HashMap<Branch, Double>, Branch> newBr = newBranch(str);

            return Triplet.with(newBr.getValue0(), newBr.getValue1(), 0.0);

        }

        else{

            Map<Branch, Double> scores = new HashMap<>();

            branches.forEach((key, val)->{
                scores.put(key, key.scoreString(str));
            });

            Entry<Branch, Double> min = Collections.min(scores.entrySet(), new Comparator<Entry<Branch, Double>>() {
                public int compare(Entry<Branch, Double> entry1, Entry<Branch, Double> entry2) {
                    return entry1.getValue().compareTo(entry2.getValue());
                }
            });            

            Pair<Double, Branch> minPair = Pair.with(min.getValue(), min.getKey());

            if(minPair.getValue0()<branchingThreshold){

                return Triplet.with(branches, minPair.getValue1(), minPair.getValue0());

            }

            else{

                Pair<HashMap<Branch, Double>, Branch> newBr = newBranch(str);

                return Triplet.with(newBr.getValue0(), newBr.getValue1(), minPair.getValue0());

            }

        }

    }

    // Merge back
    private XStructure trim(){

        int numTimes = 1;

        HashMap<Pair<Branch,Branch>, Double> _dm = null;

        return trim(numTimes, _dm);

    }

    private XStructure trim(int numTimes){

        HashMap<Pair<Branch,Branch>, Double> _dm = null;

        return trim(numTimes, _dm);

    }

    private XStructure trim(int numTimes, HashMap<Pair<Branch,Branch>, Double> _dm){

        if(branches.size()<=Config.maxBranches){

            return this;

        }

        else{

            Map<Pair<Branch,Branch>, Double> distanceMatrix = new HashMap<>();

            if(_dm == null){
                for(Branch b1 : branches.keySet()){

                    for(Branch b2 : branches.keySet()){

                        Pair<Branch,Branch> bPair = Pair.with(b1, b2);
                        distanceMatrix.put(bPair, b1.superScore(b2));

                    }

                }

            }

            else distanceMatrix.putAll(_dm);

            Entry<Pair<Branch,Branch>, Double> minPair = Collections.min(distanceMatrix.entrySet(), new Comparator<Entry<Pair<Branch,Branch>, Double>>() {
                public int compare(Entry<Pair<Branch,Branch>, Double> entry1, Entry<Pair<Branch,Branch>, Double> entry2) {
                    return entry1.getValue().compareTo(entry2.getValue());
                }
            });

            HashMap<Branch, Double> _branches = branches;

            Branch b1 = minPair.getKey().getValue0();

            Branch b2 = minPair.getKey().getValue1();
            
            _branches.put(b1.merged(b1, b2), branches.get(b1)+branches.get(b2));
            
            _branches.remove(b1);
            
            _branches.remove(b2);
            
            double _bThresh = Math.max(minPair.getValue(), branchingThreshold+0.01);
            
            XStructure x = new XStructure(lines, _branches, _bThresh, new ArrayList<Double>());
            
            Iterator<Map.Entry<Pair<Branch,Branch>, Double>> it = distanceMatrix.entrySet().iterator();
            
            while (it.hasNext()) {
            
                Map.Entry<Pair<Branch,Branch>, Double> pair = it.next();
            
                Pair<Branch,Branch> key = (Pair<Branch,Branch>)pair.getKey();
            
                if(key.getValue0() == b1) it.remove();
            
                else if(key.getValue0() == b2) it.remove();
            
                else if(key.getValue1() == b1) it.remove();
            
                else if(key.getValue1() == b2) it.remove();
            
            }
            
            if(numTimes == 1){
            
                return x;
            
            }
            
            else{
            
                HashMap<Pair<Branch,Branch>, Double> copy = new HashMap<Pair<Branch,Branch>, Double>(distanceMatrix);
            
                return x.trim(numTimes-1, copy);
            
            }
        
        }

    }

    public ArrayList<String> generateStrings(int n) {

        ArrayList<String> result = new ArrayList<>();

        for(int i = 0; i<n; i++){

            double randomIndex = Math.random();

            double tSum = branches.values().stream().mapToDouble(f -> f.doubleValue()).sum();

            double cumSum = 0.0;

            for(Branch b : branches.keySet()){

                cumSum += branches.get(b);

                if(cumSum/tSum > randomIndex){

                    result.add(b.generateRandomStrings(1).get(0));

                }

            }

        }

        return result;

    }

    public String toString(){

        ArrayList<String> reslst = new ArrayList<>();

        for(Branch b : branches.keySet()){

            String res = "(" + b.toString() + "," + branches.get(b).toString() + ")";

            reslst.add(res);

        }

        return String.join("|", reslst);

    }

    // Comparison

    public double subsetScore(XStructure other) {

        Set<Branch> brLst = other.branches.keySet();

        double tscore = 0.0;

        double sum = 0.0;

        for(Branch b1 : branches.keySet()){

            ArrayList<Double> scores = new ArrayList<>();

            for(Branch b2 : brLst){
                scores.add(b2.superScore(b1));
            }

            tscore += Collections.min(scores)*branches.get(b1);

            sum += branches.get(b1);

        }

        return tscore/sum;

    }

    // Outlier score for a given string
    public double computeOutlierScore(String str){

        double sum = 0.0;

        for(Branch b : branches.keySet()) sum += branches.get(b);

        double outlierScore = 0.0;

        for(Branch b : branches.keySet()){
            outlierScore += b.scoreString(str) * (branches.get(b)/sum);
        }

        return outlierScore;

    }

    public XStructure mergeWith(XStructure other){

        HashMap<Branch, Double> _branches = new HashMap<>();
        _branches.putAll(branches);
        _branches.putAll(other.branches);

        XStructure x = new XStructure(new ArrayList<String>(), _branches, 0.0, new ArrayList<Double>());

        return x.trim(this.branches.size()-Config.maxBranches);

    }

    public double compareTwo(XStructure x, XStructure y){

        return (x.subsetScore(y) + y.subsetScore(x))/2;

    }

    public XStructure mergeMultiple(ArrayList<XStructure> xs){

        XStructure merged = new XStructure();

        for(XStructure x : xs){
            merged = merged.mergeWith(x);
        }

        return merged;

    }

}
