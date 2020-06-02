package xsystem.ng.Layers;

import java.util.ArrayList;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import xsystem.ng.Enums.XClass;
import xsystem.ng.Support.Config;
import xsystem.ng.Support.Utils;
import xsystem.ng.Support.Wrapper;

public class Token {

	public ArrayList<Symbol> symbols;

	public Boolean doneAdding;

	public ArrayList<Double> history;

	public double stdDev;

	public ArrayList<Double> archive;

	private final boolean tryToStop = new Config().tts;

	public ArrayList<String> tokenStringGenerator;

	public double scoreSoFar;

	public ArrayList<XClass> representation;

	public Token(){

		symbols = new ArrayList<>();
		doneAdding = false;
		history = new ArrayList<>();
		stdDev = 100.0;
		archive = new ArrayList<>();
		tokenStringGenerator = tokenStr(symbols);
		scoreSoFar = ((double)archive.stream().mapToDouble(a -> a).sum())/(archive.size());
		representation = repfunc(symbols);

	}
	
	public Token(ArrayList<Symbol> symbols, Boolean doneAdding, ArrayList<Double> history, double stdDev, ArrayList<Double> archive) {
		
		this.symbols = symbols;

		if(tryToStop) this.doneAdding = doneAdding;
		else this.doneAdding = false;

		if(tryToStop) this.history = history;
		else this.history = new ArrayList<>();

		this.stdDev = stdDev;

		if(tryToStop) this.archive = archive;
		else this.archive = new ArrayList<>();

		this.tokenStringGenerator = tokenStr(symbols);

		this.scoreSoFar = ((double)archive.stream().mapToDouble(a -> a).sum())/(archive.size());

		this.representation = repfunc(symbols);
	}

	private ArrayList<String> tokenStr(ArrayList<Symbol> symbols){

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

		Utils utils = new Utils();

		ArrayList<Wrapper> res = utils.mergeStreams(lst);
		ArrayList<String> result = new ArrayList<>();

		for(Wrapper w : res){
			result.add(w.getString());
		}

		return result;

	}

	private ArrayList<XClass> repfunc(ArrayList<Symbol> symbolList){

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

		if(doneAdding) return this;

		else{

			Config config = new Config();

			double tokenScore = 0;
			for(int i=0; i<token.length() && i<symbols.size();i++){
				tokenScore += symbols.get(i).scoreChar(token.charAt(i));
			}

			ArrayList<Symbol> _symbolStructs = new ArrayList<>();

			for(int i=0; i<token.length(); i++){
				if(i>=symbols.size()) _symbolStructs.add( new Symbol(token.charAt(i)) );
				else _symbolStructs.add(symbols.get(i).addChar(token.charAt(i)));
			}

			ArrayList<Double> _history = history;
			_history.add(tokenScore);

			double _stdDev;
			DescriptiveStatistics stats = new DescriptiveStatistics();
			for(double d : _history) stats.addValue(d);

			if(_history.size() % config.inc == 0) _stdDev = stats.getStandardDeviation();
			else _stdDev = stdDev;

			Boolean _doneAdding = config.neededSampleSize(_stdDev) < _history.size();

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

		if(score == 0) return 0;
		else if(score > 0) return score;
		else return 1.0;

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