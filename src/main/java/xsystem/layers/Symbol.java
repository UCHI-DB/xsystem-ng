package xsystem.layers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static java.util.Map.Entry.*;
import static java.util.stream.Collectors.*;

import xsystem.support.Config;
import xsystem.support.Utils;
import xsystem.enums.CharClass;
import xsystem.enums.XClass;
import xsystem.enums.X_ANY;
import xsystem.enums.X_OR;
import xsystem.enums.X_SPEC;

public class Symbol {

//Symbol Layer of the XStructure

	public HashMap<Character, Double> charHist;

	public HashMap<CharClass, ArrayList<Character> > cclassHist;

	public int total;

	public XClass representation;

	public ArrayList<String> symbolStringGenerator;

	public Symbol(char c) {
		HashMap<Character, Double> _charHist = new HashMap<Character, Double>();
		_charHist.put(c, 1.0);

		HashMap<CharClass, ArrayList<Character>> _cclassHist = new HashMap<CharClass, ArrayList<Character>>();
		_cclassHist.put(Utils.getCharacterClass(c), new ArrayList<>(Arrays.asList(c)));

		int _total = 1;
		XClass _representation = representationFunction(_charHist, _cclassHist, _total);
		ArrayList<String> _symbolStringGenerator = _representation.lshDomain();

		this.charHist = _charHist;
		this.cclassHist = _cclassHist;
		this.total = _total;
		this.representation = _representation;
		this.symbolStringGenerator = _symbolStringGenerator;
	}

	public Symbol(HashMap<Character, Double> _charHist, HashMap<CharClass, ArrayList<Character> > _cclassHist, int _total) {
		this.charHist = _charHist;
		this.cclassHist = _cclassHist;
		this.total = _total;
		this.representation = representationFunction(_charHist, _cclassHist, _total);
		this.symbolStringGenerator = this.representation.lshDomain();
	}

	@JsonCreator
	public Symbol(@JsonProperty("charHist") HashMap<Character, Double> charHist, 
	@JsonProperty("cclassHist") HashMap<CharClass, ArrayList<Character> > cclassHist, 
	@JsonProperty("total") int total, @JsonProperty("representation") XClass representation,
	@JsonProperty("symbolStringGenerator") ArrayList<String> symbolStringGenerator)
	{
        super();
        this.charHist = charHist;
		this.cclassHist = cclassHist;
		this.total = total;
		this.representation = representation;
		this.symbolStringGenerator = symbolStringGenerator;
	}
	
	//Getters and Setters

	public HashMap<Character, Double> getCharHist() {
		return charHist;
	}

	public void setCharHist(HashMap<Character, Double> charHist) {
		this.charHist = charHist;
	}

	public HashMap<CharClass, ArrayList<Character>> getCclassHist() {
		return cclassHist;
	}

	public void setCclassHist(HashMap<CharClass, ArrayList<Character>> cclassHist) {
		this.cclassHist = cclassHist;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public XClass getRepresentation() {
		return representation;
	}

	public void setRepresentation(XClass representation) {
		this.representation = representation;
	}

	public ArrayList<String> getSymbolStringGenerator() {
		return symbolStringGenerator;
	}

	public void setSymbolStringGenerator(ArrayList<String> symbolStringGenerator) {
		this.symbolStringGenerator = symbolStringGenerator;
	}

	public Symbol addChar(char c){
		HashMap<Character, Double> _charHist = new HashMap<>();
		_charHist.putAll(charHist);

		if(_charHist.containsKey(c)){
			double val = charHist.get(c);
			_charHist.put(c, val+1.0);
		}
		else
			_charHist.put(c, 1.0);
		
		HashMap<CharClass, ArrayList<Character> > _cclassHist = new HashMap<>();
		_cclassHist.putAll(cclassHist);

		CharClass charClass = Utils.getCharacterClass(c);

		if(_cclassHist.containsKey(charClass)){
			ArrayList<Character> lst = new ArrayList<>();
			lst.addAll(_cclassHist.get(charClass));
			lst.add(c);
			_cclassHist.put(charClass, lst);
		}
		else{
			ArrayList<Character> lst = new ArrayList<>();
			lst.add(c);
			_cclassHist.put(charClass, lst);
		}

		int _total = total+1;

		return new Symbol(_charHist, _cclassHist, _total);
	}

	//Getting the representation of a Symbol
	private XClass representationFunction(HashMap<Character, Double> _charHist, HashMap<CharClass, ArrayList<Character> > _cclassHist, int _total){
		HashMap<CharClass, Double> symbolPcts = new HashMap<>();

		_cclassHist.forEach((k, v) -> {
			double val = (double)v.size()/_total;
			symbolPcts.put(k, val);
		});

		CharClass maxSym = Collections.max(symbolPcts.entrySet(), Map.Entry.comparingByValue()).getKey();

		if(symbolPcts.get(maxSym) > 0.9){
			if(! maxSym.isClass())
				return maxSym.toXClass();
			else{
				HashMap<Character, Double> trucatedHist = new HashMap<>();

				_charHist.forEach((k,v) -> {
					if(Utils.getCharacterClass(k).representation() == maxSym.representation())
						trucatedHist.put(k, v);
				});

				HashMap<Character, Double> totalHist = new HashMap<>();
				totalHist.putAll(maxSym.baseHist());
				totalHist.putAll(trucatedHist);

				// Null hypothesis accepted
				if(!Utils.significant(new ArrayList<Double>(totalHist.values())))
					return maxSym.toXClass();

				// Only one character
				else if(trucatedHist.size() == 1)
					return new X_SPEC(String.valueOf(trucatedHist.keySet().iterator().next()));

				//Multiple Characters
				else{
					Map<Character, Double> sorted = trucatedHist.entrySet().stream().sorted(comparingByValue()).collect( toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
					AtomicReference<Double> atomicSum = new AtomicReference<>(0.0);

					// Cumilative Sum
					sorted.entrySet().forEach(e -> e.setValue(atomicSum.accumulateAndGet(e.getValue(), (x, y) -> x + y))); 
					double cutoff = (1- Config.capturePct)*(new ArrayList<Double>(trucatedHist.values()).stream().mapToDouble(a -> a).sum());
					ArrayList<String> charToUse = new ArrayList<>();

					sorted.forEach((k,v) -> {
						if(v>cutoff)
							charToUse.add(String.valueOf(k));
					});
					return new X_OR(charToUse);
				}
			}
		}

		else{
			HashMap<Character, Double> resMap = new HashMap<>();
			resMap.putAll(Utils.asciiMap());
			resMap.putAll(_charHist);

			if(!Utils.significant(new ArrayList<Double>(resMap.values())))
				return new X_ANY();
			else
				return maxSym.toXClass();
		}
	}

	public double scoreChar (char c){
		if(!(cclassHist.containsKey(Utils.getCharacterClass(c))))
			return 1.0;
		else if( !(charHist.containsKey(c)) && !(representation.isClass()) )
			return 0.5;
		else
			return 0.0;
		
	}

	public String toString() {
		return representation.representation();
	}

}
