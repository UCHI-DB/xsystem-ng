package xsystem.layers;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import xsystem.support.Config;
import xsystem.support.Utils;
import xsystem.support.Wrapper;

/**Represents the Branch Layer of the XStructure.
 * @author Ipsita Mohanty
 * @version 0.0.1
 * @since 0.0.1
*/
public class Branch {

	/**List of lines the branch has learned*/
	public ArrayList<String> lines;

	/**The list of Tokens in the Branch.*/
	public ArrayList<Token> tokenStructs;

	/**List of tokenizers in the Branch*/
	public ArrayList<String> tokenizers;

	/**Branch String generator*/
	public ArrayList<String> branchStringGenerator;

	/**Tokenizer String generator*/
	public ArrayList<String> tokenizerStringGenerator;

	/**
	 * Creates a Branch with given parameters
	 * @param _l List of lines
	 * @param _tknzs List of tokenizers
	 * @param _tks List of Tokens
	 */
	public Branch(ArrayList<String> _l, ArrayList<String> _tknzs, ArrayList<Token> _tks){
		this.lines = ((Config.tts) ? _l : new ArrayList<>());
		this.tokenStructs = _tks;
		this.tokenizers = _tknzs;
		this.branchStringGenerator = branchStringGenerator(this.tokenStructs);
		this.tokenizerStringGenerator = tokenizerStringGenerator(this.tokenizers);

		checkRep();
	}

	/**
	 * Creates a Branch given a String
	 * @param str the given String
	 */
	public Branch(String str) {
		ArrayList<String> _lines = new ArrayList<>();
		_lines.add(str);

		ArrayList<String> _tokenizers = findTokenizers(str);
		ArrayList<Token> _tokenStructs = makeTokenStructs(str);

		this.lines = ((Config.tts) ? _lines : new ArrayList<>());
		this.tokenStructs = _tokenStructs;
		this.tokenizers = _tokenizers;
		this.branchStringGenerator = branchStringGenerator(_tokenStructs);
		this.tokenizerStringGenerator = tokenizerStringGenerator(_tokenizers);

		checkRep();
	}

	/**
	 * Creates a Branch given all the parameters
	 * @param lines List of lines
	 * @param tokenStructs List of Tokens
	 * @param tokenizers List of tokenizers
	 * @param branchStringGenerator Branch String generator
	 * @param tokenizerStringGenerator Tokenizer String generator
	 */
	@JsonCreator
	public Branch(@JsonProperty("lines") ArrayList<String> lines, 
	@JsonProperty("tokenStructs") ArrayList<Token> tokenStructs, 
	@JsonProperty("tokenizers") ArrayList<String> tokenizers, 
	@JsonProperty("branchStringGenerator") ArrayList<String> branchStringGenerator,
	@JsonProperty("tokenizerStringGenerator") ArrayList<String> tokenizerStringGenerator)
	{
        super();
        this.lines = lines;
		this.tokenStructs = tokenStructs;
		this.tokenizers = tokenizers;
		this.branchStringGenerator = branchStringGenerator;
		this.tokenizerStringGenerator = tokenizerStringGenerator;
    }
	
	/**
	 * Gets the list of lines
	 * @return list of lines
	 */
	public ArrayList<String> getLines() {
		return lines;
	}

	/**
	 * Sets the lines variable
	 * @param lines lines list
	 */
	public void setLines(ArrayList<String> lines) {
		this.lines = lines;
	}

	/**
	 * Gets the Tokens in the Branch
	 * @return Tokens contained
	 */
	public ArrayList<Token> getTokenStructs() {
		return tokenStructs;
	}

	/**
	 * Sets the Tokens in Branch
	 * @param tokenStructs Token List
	 */
	public void setTokenStructs(ArrayList<Token> tokenStructs) {
		this.tokenStructs = tokenStructs;
	}

	/**
	 * Gets the Tokenizers in the Branch
	 * @return List of Tokenizers
	 */
	public ArrayList<String> getTokenizers() {
		return tokenizers;
	}

	/**
	 * Sets the tokenizers in the branch
	 * @param tokenizers List of Tokenizers
	 */
	public void setTokenizers(ArrayList<String> tokenizers) {
		this.tokenizers = tokenizers;
	}

	/**
	 * Gets the BranchString generator
	 * @return BranchString generator
	 */
	public ArrayList<String> getBranchStringGenerator() {
		return branchStringGenerator;
	}

	/**
	 * Sets the BranchString generator
	 * @param branchStringGenerator BranchString generator
	 */
	public void setBranchStringGenerator(ArrayList<String> branchStringGenerator) {
		this.branchStringGenerator = branchStringGenerator;
	}

	/**
	 * Gets the TokenizerString Genrator
	 * @return TokenizerString Genrator
	 */
	public ArrayList<String> getTokenizerStringGenerator() {
		return tokenizerStringGenerator;
	}

	/**
	 * Sets the TokenizerString Genrator
	 * @param tokenizerStringGenerator TokenizerString Genrator
	 */
	public void setTokenizerStringGenerator(ArrayList<String> tokenizerStringGenerator) {
		this.tokenizerStringGenerator = tokenizerStringGenerator;
	}

	//BranchStringGenerator function
	private ArrayList<String> branchStringGenerator (ArrayList<Token> tokens){
		int len = tokens.size();
		ArrayList<ArrayList<Wrapper>> lst = new ArrayList<>();
	
		for(int i=0; i<len; i++){
			ArrayList<Wrapper> strList = new ArrayList<>();
			ArrayList<String> strlr = tokens.get(i).tokenStringGenerator;
			for(String str : strlr){
				String res = str + "-" + String.valueOf(i);
				strList.add(new Wrapper(res));
			}
			lst.add(strList);
		}

		ArrayList<Wrapper> res = Utils.mergeStreams(lst);
		ArrayList<String> result = new ArrayList<>();
	
		for(Wrapper w : res)
			result.add(w.getString());
	
		return result;
	}

	//TokenizerStringGenerator function
	private ArrayList<String> tokenizerStringGenerator (ArrayList<String> tokenizers){
		ArrayList<String> result = new ArrayList<>();

		for(int i=0; i<tokenizers.size();i++)
			result.add(tokenizers.get(i) + "-H" + String.valueOf(i));

		return result;

	}

	/** Assertion function to keep a check */ 
	public void checkRep(){
		assert tokenStructs.size() == tokenizers.size() : "Not Valid";
		assert tokenizers.get(tokenizers.size()-1).equals("$");
	}

	/**
	 * Tokenizes a string, returning hinges (called once for now, every iteration later)
	 * @param str input String
	 * @return The hinges in the input String
	 */
	public ArrayList<String> findTokenizers (String str){
		ArrayList<String> result = new ArrayList<>();
		new Config();

		for(char c : str.toCharArray()){
			if(new String(Config.splChars).contains(String.valueOf(c))) 
				result.add(String.valueOf(c));
		}
		result.add("$");

		return result;
	}

	/**
	 * Makes a set of initialized token structures (called once)
	 * @param str input String
	 * @return List of Token Structures
	 */
	public ArrayList<Token> makeTokenStructs (String str){
		ArrayList<Token> result = new ArrayList<>();
		String[] subStrs = str.split("[" + Pattern.quote ( new String(Config.splChars) ) + "]", -1);

		for(int i = 0 ; i<subStrs.length; i++)
			result.add(new Token().learnToken(subStrs[i]));

		return result;
	}

	/**
	 * Compute the score of input String from a Branch
	 * @param str input String
	 * @return computed score
	 */
	public double scoreString(String str){
		ArrayList<String> tokens = new ArrayList<>();
		ArrayList<String> tokeniseStr = tokenizeString(str);
		for(int i=0; i<tokenStructs.size();i++){
			if(i<tokeniseStr.size()) 
				tokens.add(tokeniseStr.get(i));
			else 
				tokens.add("");
		}

		double sum = 0;
		for(int i=0; i<tokenStructs.size() && i<tokens.size();i++)
			sum += tokenStructs.get(i).scoreToken(tokens.get(i));

		return sum/(str.length() + 0.01);
	}

	/**
	 * Tokenize a string based on tokenizers
	 * @param str input String
	 * @return tokenized String
	 */
	public ArrayList<String> tokenizeString (String str){
		String tokStr = tokenizers.stream().map(Object::toString).collect(Collectors.joining(""));
		String[] splitted = str.split("[" + Pattern.quote ( tokStr ) + "]", -1);
		ArrayList<String> result = new ArrayList<>();

		for(String s : splitted) 
			result.add(s);
		
		return result;
	}

	/**
	 * Learn a new string
	 * @param str String to be learned
	 * @return Branch after learning the given String
	 */
	public Branch learnString(String str){
		checkRep();

		ArrayList<String> strTokens = new ArrayList<>();
		ArrayList<String> tokeniseStr = tokenizeString(str);

		for(int i = 0; i<tokenStructs.size(); i++){
			if(i<tokeniseStr.size()) 
				strTokens.add(tokeniseStr.get(i));
			else
				strTokens.add("");
		}
		
		ArrayList<Token> newTokens = new ArrayList<>();
		for(int i = 0; i<tokenStructs.size(); i++)
			newTokens.add(tokenStructs.get(i).learnToken(strTokens.get(i)));
		
		ArrayList<String> newLines = new ArrayList<>();
		newLines.addAll(lines);
		newLines.add(str);

		return new Branch(newLines, tokenizers, newTokens);
	}

	/**
	 * Generate n random strings from this branch
	 * @param n no. of strings to be genrated
	 * @return list of generated strings
	 */
	public ArrayList<String> generateRandomStrings (int n){
		ArrayList<String> res = new ArrayList<>();
		
		for(int j =0; j<n; j++){
			String st = "";
			for(int i=0; i<tokenStructs.size() && i<tokenizers.size();i++)
				st += tokenStructs.get(i).randomToken()+tokenizers.get(i);
		
			res.add(st);
		}
		
		return res;
	}

	/**
	 * Check if this branch contains another branch
	 * @param other the other Branch
	 * @return the comuted score based on similarity
	 */
	public double superScore(Branch other){
		double stddev = Double.MAX_VALUE;
		DescriptiveStatistics allScores = new DescriptiveStatistics();

		while(allScores.getN()<Config.neededSampleSize(stddev)){
			ArrayList<String> str = other.generateRandomStrings(Config.inc);
			for(String s : str)
				allScores.addValue(scoreString(s));

			stddev = allScores.getStandardDeviation();
		}
		
		return allScores.getSum()/(allScores.getN()+0.1) + 0.1;
	}

	/**
	 * Reset the doneAdding counter for all of the Tokens
	 * @return a new Branch will reseted doneAdding Tokens
	 */
	public Branch reopened(){
		ArrayList<Token> reTok = new ArrayList<>();
		for(Token t : tokenStructs) 
			reTok.add(t.reopened());
	
		return new Branch(lines,tokenizers, reTok);
	}

	/**
	 * Merges two branch structures
	 * @param outer the outer Branch
	 * @param inner the inner Branch
	 * @return the merged Branch
	 */
	public Branch merged(Branch outer, Branch inner){
		Branch merged = outer.reopened();

		for(String str : inner.lines)
			merged = merged.learnString(str);

		return merged;
	}

	/** Converts Branch to String
	 * @return string representation of a Branch
	*/
	public String toString(){
		String res = "";
		for(int i=0; i<tokenStructs.size() && i<tokenizers.size(); i++){
			res += tokenStructs.get(i).toString()+tokenizers.get(i);
		}
	
		return res;
	}

}
