package xsystem.layers;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import xsystem.support.Config;
import xsystem.support.Utils;
import xsystem.support.Wrapper;
import xsystem.enums.XClass;

/**Represents the Token Layer of the XStructure.
 * @author Ipsita Mohanty
 * @version 0.0.1
 * @since 0.0.1
*/
public class Token {

	/**The list of Symbols in a Token.*/
	public ArrayList<Symbol> symbols;

	/**DoneAdding*/
	public Boolean doneAdding;

	/**History of strings learned.*/
	public ArrayList<Double> history;

	/**Standard Deviation*/
	public double stdDev;

	/**Archive*/
	public ArrayList<Double> archive;

	private final boolean tryToStop = Config.tts;

	/**Token String genarator*/
	public ArrayList<String> tokenStringGenerator;

	/**Score of token so far*/
	public double scoreSoFar;

	/**List of XClass representation of Token.*/
	public ArrayList<XClass> representation;

	/**Default Token constructor */
	public Token(){
		this.symbols = new ArrayList<>();
		this.doneAdding = false;
		this.history = new ArrayList<>();
		this.stdDev = 100.0;
		this.archive = new ArrayList<>();
		this.tokenStringGenerator = tokenStringGenerator(symbols);
		this.scoreSoFar = ((double)archive.stream().mapToDouble(a -> a).sum())/(archive.size());
		this.representation = representationFunc(symbols);
	}
	
	/**
	 * Creates a Token given the following parameters
	 * @param symbols list of Symbols
	 * @param doneAdding boolean doneAdding
	 * @param history history
	 * @param stdDev standard deviation
	 * @param archive archive
	 */
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

	/**
	 * Creates a Token given all parameters
	 * @param symbols list of Symbols
	 * @param doneAdding boolean doneAdding
	 * @param history history
	 * @param stdDev standard deviation
	 * @param archive archive
	 * @param tokenStringGenerator tokenStringGenerator
	 * @param scoreSoFar scoreSoFar
	 * @param representation XClass representation
	 */
	@JsonCreator
	public Token(@JsonProperty("symbols") ArrayList<Symbol> symbols, @JsonProperty("doneAdding") Boolean doneAdding, 
	@JsonProperty("history") ArrayList<Double> history,@JsonProperty("stdDev") double stdDev,
	@JsonProperty("archive") ArrayList<Double> archive, @JsonProperty("tokenStringGenerator") ArrayList<String> tokenStringGenerator,
	@JsonProperty("scoreSoFar") double scoreSoFar, @JsonProperty("representation") ArrayList<XClass> representation)
	{
        super();
        this.symbols = symbols;
		this.doneAdding = doneAdding;
		this.history = history;
		this.stdDev = stdDev;
		this.archive = archive;
		this.tokenStringGenerator = tokenStringGenerator;
		this.scoreSoFar = scoreSoFar;
		this.representation = representation;
    }
	
	/**
	 * Gets the list of Symbols in Token
	 * @return list of symbols
	 */
	public ArrayList<Symbol> getSymbols() {
		return symbols;
	}

	/**
	 * Sets Symbols in Token
	 * @param symbols Symbol List
	 */
	public void setSymbols(ArrayList<Symbol> symbols) {
		this.symbols = symbols;
	}

	/**
	 * Gets doneAdding
	 * @return doneAdding
	 */
	public Boolean getDoneAdding() {
		return doneAdding;
	}

	/**
	 * Sets the doneAddding instance variable
	 * @param doneAdding boolean doneAdding
	 */
	public void setDoneAdding(Boolean doneAdding) {
		this.doneAdding = doneAdding;
	}

	/**
	 * Gets the history
	 * @return history
	 */
	public ArrayList<Double> getHistory() {
		return history;
	}

	/**
	 * Sets the history
	 * @param history history
	 */
	public void setHistory(ArrayList<Double> history) {
		this.history = history;
	}

	/**
	 * Gets the Standard Deviation
	 * @return standard deviation
	 */
	public double getStdDev() {
		return stdDev;
	}

	/**
	 * Sets the Standard Deviation
	 * @param stdDev Standard Deviation
	 */
	public void setStdDev(double stdDev) {
		this.stdDev = stdDev;
	}

	/**
	 * Gets the Archive
	 * @return archive
	 */
	public ArrayList<Double> getArchive() {
		return archive;
	}

	/**
	 * Sets the Archive
	 * @param archive Archive
	 */
	public void setArchive(ArrayList<Double> archive) {
		this.archive = archive;
	}

	/**
	 * Gets the Token String generator
	 * @return TokenString generator
	 */
	public ArrayList<String> getTokenStringGenerator() {
		return tokenStringGenerator;
	}

	/**
	 * Sets the TokenString generator
	 * @param tokenStringGenerator TokenString Generator
	 */
	public void setTokenStringGenerator(ArrayList<String> tokenStringGenerator) {
		this.tokenStringGenerator = tokenStringGenerator;
	}

	/**
	 * Gets score so far
	 * @return scoreSoFar
	 */
	public double getScoreSoFar() {
		return scoreSoFar;
	}

	/**
	 * Sets score so far
	 * @param scoreSoFar scoreSoFar
	 */
	public void setScoreSoFar(double scoreSoFar) {
		this.scoreSoFar = scoreSoFar;
	}

	/**
	 * Gets representation
	 * @return representation of Token
	 */
	public ArrayList<XClass> getRepresentation() {
		return representation;
	}

	/**
	 * Sets the representation of a Token
	 * @param representation XClass representation
	 */
	public void setRepresentation(ArrayList<XClass> representation) {
		this.representation = representation;
	}

	/**
	 * @return tryToStop
	 */
	public boolean isTryToStop() {
		return tryToStop;
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

	/**
	 * Learns a given String into a Token
	 * @param token given String
	 * @return learned Token
	 */
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

	/**
	 * Reopen a learned Token
	 * @return the reopend Token
	 */
	public Token reopened(){
		ArrayList<Double> _archive = new ArrayList<>();
		_archive.addAll(archive);
		_archive.addAll(history);

		return new Token(symbols, false, new ArrayList<>(), stdDev, _archive);
	}
	
	/**
	 * Compute score of a String within a Token
	 * @param token given String whose score is to be calculated
	 * @return computed score
	 */
	public double scoreToken(String token){
		double score = 0;

		for(int i = 0; i<token.length() && i<symbols.size(); i++){
			score += symbols.get(i).scoreChar(token.charAt(i));
		}
		return score;
	}

	/**
	 * Gives a random Token
	 * @return String representation of a random Token
	 */
	public String randomToken(){
		String res = "";
		for(XClass x : representation){
			res += x.randomRep();
		}
		return res;
	}

	/** Converts Token to String
	 * @return string representation of a Token
	*/
	public String toString(){
		String res = "";
		for(XClass x : representation){
			res += x.toString();
		}
		return res;
	}
}
