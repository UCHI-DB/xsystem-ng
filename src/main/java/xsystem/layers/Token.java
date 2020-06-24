package xsystem.layers;

import java.util.ArrayList;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import xsystem.support.Config;
import xsystem.support.Utils;
import xsystem.support.Wrapper;
import xsystem.enums.XClass;

public class Token {

//Token Layer of the XStructure

	public ArrayList<Symbol> symbols;

	public Boolean doneAdding;

	public ArrayList<Double> history;

	public double stdDev;

	public ArrayList<Double> archive;

	private final boolean tryToStop = Config.tts;

	public ArrayList<String> tokenStringGenerator;

	public double scoreSoFar;

	public ArrayList<XClass> representation;

	public Token(){
		symbols = new ArrayList<>();
		doneAdding = false;
		history = new ArrayList<>();
		stdDev = 100.0;
		archive = new ArrayList<>();
		tokenStringGenerator = tokenStringGenerator(symbols);
		scoreSoFar = ((double)archive.stream().mapToDouble(a -> a).sum())/(archive.size());
		representation = representationFunc(symbols);
	}
	
	public Token(ArrayList<Symbol> symbols, Boolean doneAdding, ArrayList<Double> history, double stdDev, ArrayList<Double> archive) {
		this.symbols = symbols;
		this.stdDev = stdDev;
		this.tokenStringGenerator = tokenStringGenerator(symbols);
		this.scoreSoFar = ((double)archive.stream().mapToDouble(a -> a).sum())/(archive.size());
		this.representation = representationFunc(symbols);

		if(tryToStop){
			this.doneAdding = doneAdding;
			this.history = history;
			this.archive = archive;
		}
		else {
			this.doneAdding = false;
			this.history = new ArrayList<>();
			this.archive = new ArrayList<>();
		}
	}

	private ArrayList<String> tokenStringGenerator(ArrayList<Symbol> symbols){
		int len = symbols.size();
		ArrayList<ArrayList<Wrapper>> lst = new ArrayList<>();
		for(int i=0; i<len; i++){
			ArrayList<Wrapper> strList = new ArrayList<>();
			ArrayList<String> strlr = symbols.get(i).symbolStringGenerator;
			for(String str : strlr){
				String res = str + "-" + String.valueOf(i);
				strList.add(new Wrapper(res));
			}
			lst.add(strList);
		}

		ArrayList<Wrapper> res = Utils.mergeStreams(lst);
		ArrayList<String> result = new ArrayList<>();

		for(Wrapper w : res){
			result.add(w.getString());
		}

		return result;
	}

	private ArrayList<XClass> representationFunc(ArrayList<Symbol> symbolList){
		ArrayList<XClass> res = new ArrayList<>();

		if(symbolList.isEmpty())
			return res;
		else{
			for(Symbol symbol : symbolList){
				res.add(symbol.representation);
			}
			return res;
		}
	}

	public Token learnToken(String token){
		if(doneAdding)
			return this;
		else{
			double tokenScore = 0;
			
			for(int i=0; i<token.length() && i<symbols.size();i++){
				tokenScore += symbols.get(i).scoreChar(token.charAt(i));
			}

			ArrayList<Symbol> _symbolStructs = new ArrayList<>();

			for(int i=0; i<token.length(); i++){
				if(i>=symbols.size())
					_symbolStructs.add( new Symbol(token.charAt(i)) );
				else 
					_symbolStructs.add(symbols.get(i).addChar(token.charAt(i)));
			}

			ArrayList<Double> _history = history;
			_history.add(tokenScore);

			double _stdDev;
			DescriptiveStatistics stats = new DescriptiveStatistics();
			for(double d : _history) 
				stats.addValue(d);

			if(_history.size() % Config.inc == 0) 
				_stdDev = stats.getStandardDeviation();
			else 
				_stdDev = stdDev;

			Boolean _doneAdding = Config.neededSampleSize(_stdDev) < _history.size();

			ArrayList<Double> _archive = new ArrayList<>();
			if(_doneAdding){
				_archive.addAll(archive);
				_archive.addAll(_history);
			}
			else{
				_archive.addAll(archive);
			}

			return new Token(_symbolStructs, _doneAdding, _history, _stdDev, _archive);
		}
	}

	public Token reopened(){
		ArrayList<Double> _archive = new ArrayList<>();
		_archive.addAll(archive);
		_archive.addAll(history);

		return new Token(symbols, false, new ArrayList<>(), stdDev, _archive);
	}
	
	public double scoreToken(String token){
		double score = 0;

		for(int i = 0; i<token.length() && i<symbols.size(); i++){
			score += symbols.get(i).scoreChar(token.charAt(i));
		}
		return score;
	}

	public String randomToken(){
		String res = "";
		for(XClass x : representation){
			res += x.randomRep();
		}
		return res;
	}

	public String toString(){
		String res = "";
		for(XClass x : representation){
			res += x.toString();
		}
		return res;
	}
}
