package xsystem.layers;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import xsystem.support.Config;
import xsystem.support.Utils;
import xsystem.support.Wrapper;

public class Branch {

//Branch Layer of the XStructure

	public ArrayList<String> lines;

	public ArrayList<Token> tokenStructs;

	public ArrayList<String> tokenizers;

	public ArrayList<String> branchStringGenerator;

	public ArrayList<String> tokenizerStringGenerator;

	public Branch(ArrayList<String> _l, ArrayList<String> _tknzs, ArrayList<Token> _tks){
		this.lines = ((Config.tts) ? _l : new ArrayList<>());
		this.tokenStructs = _tks;
		this.tokenizers = _tknzs;
		this.branchStringGenerator = branchStringGenerator(this.tokenStructs);
		this.tokenizerStringGenerator = tokenizerStringGenerator(this.tokenizers);

		checkRep();
	}

	public Branch(String str){
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

	//Assertion function to keep a check
	public void checkRep(){
		assert tokenStructs.size() == tokenizers.size() : "Not Valid";
		assert tokenizers.get(tokenizers.size()-1).equals("$");
	}

	// Tokenizes a string, returning hinges (called once for now, every iteration later)
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

	// Makes a set of initialized token structures (called once)
	public ArrayList<Token> makeTokenStructs (String str){
		ArrayList<Token> result = new ArrayList<>();
		String[] subStrs = str.split("[" + Pattern.quote ( new String(Config.splChars) ) + "]", -1);

		for(int i = 0 ; i<subStrs.length; i++)
			result.add(new Token());

		return result;
	}

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

	// Tokenize a string based on tokenizers
	public ArrayList<String> tokenizeString (String str){
		String tokStr = tokenizers.stream().map(Object::toString).collect(Collectors.joining(""));
		String[] splitted = str.split("[" + Pattern.quote ( tokStr ) + "]", -1);
		ArrayList<String> result = new ArrayList<>();

		for(String s : splitted) 
			result.add(s);
		
		return result;
	}

	// Learn a new string
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

	// Generate random strings from this branch
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

	// Check if this branch contains another branch
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

	// Reset the done_adding counter for all of the tokens
	public Branch reopened(){
		ArrayList<Token> reTok = new ArrayList<>();
		for(Token t : tokenStructs) 
			reTok.add(t.reopened());
	
		return new Branch(lines,tokenizers, reTok);
	}

	// Merges two branch structures
	public Branch merged(Branch outer, Branch inner){
		Branch merged = outer.reopened();

		for(String str : inner.lines)
			merged = merged.learnString(str);

		return merged;
	}

	public String toString(){
		String res = "";
	
		for(int i=0; i<tokenStructs.size() && i<tokenizers.size(); i++){
			res += tokenStructs.get(i).toString()+tokenizers.get(i);
		}
	
		return res;
	}

}
