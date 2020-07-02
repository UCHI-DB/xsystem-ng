package xsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import xsystem.layers.Branch;
import xsystem.support.Config;
import xsystem.support.Utils;
import xsystem.support.Wrapper;

/**Represents The XStructure!!
 * @author Ipsita Mohanty
 * @version 0.0.1
 * @since 0.0.1
*/
public class XStructure {

    private final HashMap<Branch, Double> branches;

    private final ArrayList<String> lines;

    private final double branchingThreshold;

    private final ArrayList<Double> history;

    /** The Tokens Generator */
    public ArrayList<String> tokensGenerator;

    /** The Hinges Generator */
    public ArrayList<String> hingesGenerator;

    /** The MinHash String Generator */
    public ArrayList<String> minHashStringGenerator;

    /**Default XStructure constructor*/
    @JsonCreator
    public XStructure(){
        this.branches = new HashMap<>();
        this.lines = new ArrayList<>();
        this.branchingThreshold = Config.branchingSeed;
        this.history = new ArrayList<>();
        this.tokensGenerator = tokensGenerator(branches);
        this.hingesGenerator = hingesGenerator(branches);
        this.minHashStringGenerator = minHashStringGenerator(tokensGenerator, hingesGenerator);

    }

    /**
     * Creates a XStructure with given parameters
     * @param _lines lines learned
     * @param _branches branches contained in XStructure
     * @param _bThresh Branching Threshold
     * @param _history history
     */
    public XStructure(ArrayList<String> _lines, HashMap<Branch, Double> _branches, double _bThresh, ArrayList<Double> _history){
        this.branches = _branches;
        this.lines = (Config.tts) ? _lines : new ArrayList<String>();
        this.branchingThreshold = _bThresh;
        this.history = (Config.tts) ? _history : new ArrayList<Double>();
        this.tokensGenerator = tokensGenerator(_branches);
        this.hingesGenerator = hingesGenerator(_branches);
        this.minHashStringGenerator = minHashStringGenerator(tokensGenerator(_branches), hingesGenerator(_branches));
    }

    /**
     * Creates a XStructure given all parameters
     * @param lines lines learned
     * @param branches branches contained in XStructure
     * @param branchingThreshold Branching Threshold
     * @param history history
     * @param tokensGenerator Tokens Generator
     * @param hingesGenerator Hinges Generator
     * @param minHashStringGenerator MinHash String Generator
     */
    @JsonCreator
    public XStructure(@JsonProperty("lines") ArrayList<String> lines, @JsonProperty("branches") HashMap<Branch, Double> branches, 
    @JsonProperty("branchingThreshold") double branchingThreshold, @JsonProperty("history") ArrayList<Double> history,
    @JsonProperty("tokensGenerator") ArrayList<String> tokensGenerator, @JsonProperty("hingesGenerator") ArrayList<String> hingesGenerator,
    @JsonProperty("minHashStringGenerator") ArrayList<String> minHashStringGenerator) {
        super();
        this.branches = branches;
        this.lines = lines;
        this.branchingThreshold = branchingThreshold;
        this.history = history;
        this.tokensGenerator = tokensGenerator;
        this.hingesGenerator = hingesGenerator;
        this.minHashStringGenerator = minHashStringGenerator;
    }

    /**
     * Gets the Tokens Generator
     * @return Tokens Generator
     */
    public ArrayList<String> getTokensGenerator() {
		return tokensGenerator;
	}

    /**
     * Sets the Tokens Generator parameter
     * @param tokensGenerator Tokens Generator
     */
	public void setTokensGenerator(ArrayList<String> tokensGenerator) {
		this.tokensGenerator = tokensGenerator;
	}

    /**
     * Gets the Hinges Generator
     * @return Hinges Generator
     */
	public ArrayList<String> getHingesGenerator() {
		return hingesGenerator;
	}

    /**
     * Sets the Hinges Generator parameter
     * @param hingesGenerator Hinges Generator
     */
	public void setHingesGenerator(ArrayList<String> hingesGenerator) {
		this.hingesGenerator = hingesGenerator;
	}

    /**
     * Gets the MinHash String Generator
     * @return MinHash String Generator
     */
	public ArrayList<String> getMinHashStringGenerator() {
		return minHashStringGenerator;
	}

    /**
     * Sets the MinHash String Generator parameter
     * @param minHashStringGenerator MinHash String Generator
     */
	public void setMinHashStringGenerator(ArrayList<String> minHashStringGenerator) {
		this.minHashStringGenerator = minHashStringGenerator;
	}

    /**
     * Gets the Branches contained in the XStruct
     * @return branch list with scores
     */
	public HashMap<Branch, Double> getBranches() {
		return branches;
	}

    /**
     * Gets the lines represented by a XStruct
     * @return list of lines
     */
	public ArrayList<String> getLines() {
		return lines;
	}

    /**
     * Gets the Branching Threshhold
     * @return Branching Threshhold
     */
	public double getBranchingThreshold() {
		return branchingThreshold;
	}

    /**
     * Gets the history of lines learned
     * @return history
     */
	public ArrayList<Double> getHistory() {
		return history;
	}

	//Compute the tokensGenerator
    private ArrayList<String> tokensGenerator (HashMap<Branch, Double> brnches){
        ArrayList<ArrayList<Wrapper>> lst = new ArrayList<>();

        brnches.forEach( (b, v) -> {
            ArrayList<Wrapper> wrList = new ArrayList<>();
            ArrayList<String> stList = b.branchStringGenerator;

            for(String str : stList)
                wrList.add(new Wrapper(str));

            lst.add(wrList);
        });

        ArrayList<Wrapper> res = Utils.mergeStreams(lst);
        ArrayList<String> result = new ArrayList<>();

        for(Wrapper w : res)
            result.add(w.getString());

        return result;
    }

    //Compute the hingesGenerator
    private ArrayList<String> hingesGenerator (HashMap<Branch, Double> brnches){
        ArrayList<ArrayList<Wrapper>> lst = new ArrayList<>();

        brnches.forEach( (b, v) -> {
            ArrayList<Wrapper> wrList = new ArrayList<>();
            ArrayList<String> stList = b.tokenizerStringGenerator;
            for(String str : stList)
                wrList.add(new Wrapper(str));

            lst.add(wrList);
        });

        ArrayList<Wrapper> res = Utils.mergeStreams(lst);
        ArrayList<String> result = new ArrayList<>();

        for(Wrapper w : res)
            result.add(w.getString());

        return result;
    }

    //Compute the minHashStringGenerator
    private ArrayList<String> minHashStringGenerator (ArrayList<String> strLst1, ArrayList<String> strLst2){
        ArrayList<ArrayList<Wrapper>> lst = new ArrayList<>();
        ArrayList<Wrapper> subLst1 = new ArrayList<>();
        ArrayList<Wrapper> subLst2 = new ArrayList<>();

        for(String str : strLst1)
            subLst1.add(new Wrapper(str));

        for(String str : strLst2)
            subLst2.add(new Wrapper(str));

        for(String str : strLst1)
            subLst2.add(new Wrapper(str));

        lst.add(subLst1);
        lst.add(subLst2);

        ArrayList<Wrapper> res = Utils.mergeStreams(lst);
        ArrayList<String> result = new ArrayList<>();

        for(Wrapper w : res)
            result.add(w.getString());

        return result;
    }

    /**
     * Adds/Learns a line into the XStruct
     * @param line the line to be learned
     * @return XStructure and boolean representing if max sample size is reached or not
     */
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
        if(branches.size() == _branches.size())
            _history.add(param.getValue2());
        else
            _history.add(0.0);

        XStructure xstruct = new XStructure(_lines, _branches, _bthresh, _history);

        DescriptiveStatistics stats = new DescriptiveStatistics();

        for(double d : history)
            stats.addValue(d);

        double stdev = stats.getStandardDeviation();
        Boolean bool = (history.size()+1)%Config.inc > 0  && Config.neededSampleSize(stdev) < history.size();

        return Pair.with(xstruct.trim(), bool);
    }

    /**
     * Adds/Learns a list of lines into the XStruct
     * @param lines line list to be learned
     * @return XStructure after learning the given lines
     */
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
                if(scannedLines.get(i).getValue1())
                    return scannedLines.get(i).getValue0();
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

            if(minPair.getValue0()<branchingThreshold)
                return Triplet.with(branches, minPair.getValue1(), minPair.getValue0());

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
        if(branches.size()<=Config.maxBranches)
            return this;

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
            else
                distanceMatrix.putAll(_dm);

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
    
                if(key.getValue0() == b1)
                    it.remove();
                else if(key.getValue0() == b2)
                    it.remove();
                else if(key.getValue1() == b1) 
                    it.remove();
                else if(key.getValue1() == b2)
                    it.remove();
            }
            
            if(numTimes == 1)
                return x;
            
            else{
                HashMap<Pair<Branch,Branch>, Double> copy = new HashMap<Pair<Branch,Branch>, Double>(distanceMatrix);
            
                return x.trim(numTimes-1, copy);
            }
        }
    }

    /**
     * Generates n number of random strings from a XStructure
     * @param n nuber of random strings you want to generate
     * @return list of random strings
     */
    public ArrayList<String> generateStrings(int n) {
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i<n; i++){
            double randomIndex = Math.random();
            double tSum = branches.values().stream().mapToDouble(f -> f.doubleValue()).sum();
            double cumSum = 0.0;

            for(Branch b : branches.keySet()){
                cumSum += branches.get(b);
                if(cumSum/tSum > randomIndex)
                    result.add(b.generateRandomStrings(1).get(0));
            }
        }
        return result;
    }

    /** Converts XStructure to String
	 * @return string representation of a XStructure
	*/
    public String toString(){
        ArrayList<String> reslst = new ArrayList<>();

        for(Branch b : branches.keySet()){
            String res = "(" + b.toString() + "," + branches.get(b).toString() + ")";
            reslst.add(res);
        }

        return String.join("|", reslst);
    }

    /**
     * Compare one XStruct to other
     * @param other the other XStruct
     * @return the computed subset score
     */
    public double subsetScore(XStructure other) {
        Set<Branch> brLst = other.branches.keySet();
        double tscore = 0.0;
        double sum = 0.0;

        for(Branch b1 : this.branches.keySet()){
            ArrayList<Double> scores = new ArrayList<>();

            for(Branch b2 : brLst)
                scores.add(b2.superScore(b1));

            tscore += Collections.min(scores)*this.branches.get(b1);
            sum += this.branches.get(b1);

        }
        return tscore/sum;
    }

    /**
     * Computes the outlier score for a given string
     * @param str input string
     * @return outlier score
     */
    public double computeOutlierScore(String str){
        double sum = 0.0;

        for(Branch b : branches.keySet())
            sum += branches.get(b);

        double outlierScore = 0.0;
        for(Branch b : branches.keySet())
            outlierScore += b.scoreString(str) * (branches.get(b)/sum);

        return outlierScore;
    }

    /**
     * Merge two XStructures
     * @param other the other XStructures
     * @return XStruct after merging
     */
    public XStructure mergeWith(XStructure other){
        HashMap<Branch, Double> _branches = new HashMap<>();
        _branches.putAll(branches);
        _branches.putAll(other.branches);

        XStructure x = new XStructure(new ArrayList<String>(), _branches, 0.0, new ArrayList<Double>());

        return x.trim(_branches.size()-Config.maxBranches);
    }

    public double compareTwo(XStructure x, XStructure y){
        return (x.subsetScore(y) + y.subsetScore(x))/2;
    }

    /**
     * Merge a list of XStructs together
     * @param xs list of XStructs to be merged
     * @return result XStruct after merging
     */
    public XStructure mergeMultiple(ArrayList<XStructure> xs){
        XStructure merged = new XStructure();

        for(XStructure x : xs)
            merged = merged.mergeWith(x);

        return merged;
    }

}
