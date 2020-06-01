package xsystem.ng.Layers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Map.Entry.*;
import static java.util.stream.Collectors.*; 

import xsystem.ng.Enums.CharClass;
import xsystem.ng.Enums.XClass;
import xsystem.ng.Enums.X_ANY;
import xsystem.ng.Enums.X_OR;
import xsystem.ng.Enums.X_SPEC;
import xsystem.ng.Support.Config;
import xsystem.ng.Support.Utils;

public class Symbol {

//Symbol Layer of the XStructure

	HashMap<Character, Double> charHist;
	HashMap<CharClass, ArrayList<Character> > cclassHist;
	int total;
	XClass representation;
	ArrayList<String> symbolStringGenerator;

	public Symbol(char c) {
		Utils utils = new Utils();
		Config config = new Config();
		
		charHist = new HashMap<Character, Double>();
		charHist.put(c, 1.0);
		
		cclassHist = new HashMap<CharClass, ArrayList<Character>>();
		cclassHist.put(utils.getCharacterClass(c), new ArrayList<>(Arrays.asList(c)));
		
		total = 1;

		HashMap<CharClass, Double> symbolPcts = new HashMap<>();
		cclassHist.forEach((k, v) -> {
			double val = (double)v.size()/total;
			symbolPcts.put(k, val);
		});
		CharClass maxSym = Collections.max(symbolPcts.entrySet(), Map.Entry.comparingByValue()).getKey();
		if(symbolPcts.get(maxSym) > 0.9){
			if(! maxSym.isClass()) representation = maxSym.toXClass();
			else{
				HashMap<Character, Double> trucatedHist = new HashMap<>();
				charHist.forEach((k,v) -> {
					if(utils.getCharacterClass(k) == maxSym) trucatedHist.put(k, v);
				});
				HashMap<Character, Double> totalHist = new HashMap<>();
				totalHist.putAll(trucatedHist);
				totalHist.putAll(maxSym.baseHist());
				if(!utils.significant(new ArrayList<Double>(totalHist.values()))) representation = maxSym.toXClass(); // Null hypothesis accepted
				else if(trucatedHist.size() == 1) representation = new X_SPEC(String.valueOf(trucatedHist.keySet().iterator().next())); // Only one character
				else{
					Map<Character, Double> sorted = trucatedHist.entrySet().stream().sorted(comparingByValue()).collect( toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
					AtomicReference<Double> atomicSum = new AtomicReference<>(0.0);
					sorted.entrySet().forEach(e -> e.setValue(atomicSum.accumulateAndGet(e.getValue(), (x, y) -> x + y))); // Cumilative Sum
					double cutoff = (1-config.capturePct)*(new ArrayList<Double>(trucatedHist.values()).stream().mapToDouble(a -> a).sum());
					ArrayList<String> charToUse = new ArrayList<>();
					sorted.forEach((k,v) -> {
						if(v>cutoff) charToUse.add(String.valueOf(k));
					});
					representation = new X_OR(charToUse);
				}
			}
		}
		else{ //Multiple Characters
			HashMap<Character, Double> resMap = new HashMap<>();
			resMap.putAll(utils.asciiMap());
			resMap.putAll(charHist);
			if(!utils.significant(new ArrayList<Double>(resMap.values())))
				representation = new X_ANY();
			else
				representation = maxSym.toXClass();
		}

		symbolStringGenerator = representation.lshDomain();

	}

	public Symbol(HashMap<Character, Double> _charHist, HashMap<CharClass, ArrayList<Character> > _cclassHist, int _total) {
		super();
		this.charHist = _charHist;
		this.cclassHist = _cclassHist;
		this.total = _total;

		Utils utils = new Utils();
		Config config = new Config();
		XClass _representation;

		HashMap<CharClass, Double> symbolPcts = new HashMap<>();
		_cclassHist.forEach((k, v) -> {
			double val = (double)v.size()/_total;
			symbolPcts.put(k, val);
		});
		CharClass maxSym = Collections.max(symbolPcts.entrySet(), Map.Entry.comparingByValue()).getKey();
		if(symbolPcts.get(maxSym) > 0.9){
			if(! maxSym.isClass()) _representation = maxSym.toXClass();
			else{
				HashMap<Character, Double> trucatedHist = new HashMap<>();
				_charHist.forEach((k,v) -> {
					if(utils.getCharacterClass(k) == maxSym) trucatedHist.put(k, v);
				});
				HashMap<Character, Double> totalHist = new HashMap<>();
				totalHist.putAll(trucatedHist);
				totalHist.putAll(maxSym.baseHist());
				if(!utils.significant(new ArrayList<Double>(totalHist.values()))) _representation = maxSym.toXClass(); // Null hypothesis accepted
				else if(trucatedHist.size() == 1) _representation = new X_SPEC(String.valueOf(trucatedHist.keySet().iterator().next())); // Only one character
				else{
					Map<Character, Double> sorted = trucatedHist.entrySet().stream().sorted(comparingByValue()).collect( toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
					AtomicReference<Double> atomicSum = new AtomicReference<>(0.0);
					sorted.entrySet().forEach(e -> e.setValue(atomicSum.accumulateAndGet(e.getValue(), (x, y) -> x + y))); // Cumilative Sum
					double cutoff = (1-config.capturePct)*(new ArrayList<Double>(trucatedHist.values()).stream().mapToDouble(a -> a).sum());
					ArrayList<String> charToUse = new ArrayList<>();
					sorted.forEach((k,v) -> {
						if(v>cutoff) charToUse.add(String.valueOf(k));
					});
					_representation = new X_OR(charToUse);
				}
			}
		}
		else{ //Multiple Characters
			HashMap<Character, Double> resMap = new HashMap<>();
			resMap.putAll(utils.asciiMap());
			resMap.putAll(charHist);
			if(!utils.significant(new ArrayList<Double>(resMap.values())))
				_representation = new X_ANY();
			else
				_representation = maxSym.toXClass();
		}
		this.representation = _representation;

		ArrayList<String> _symbolStringGenerator = _representation.lshDomain();
		this.symbolStringGenerator = _symbolStringGenerator;
	}

	public HashMap<Character, Double> getCharHist() {
		return charHist;
	}

	public Symbol addChar(char c){
		Utils utils = new Utils();
		HashMap<Character, Double> _charHist = this.charHist;
		HashMap<CharClass, ArrayList<Character> > _cclassHist = this.cclassHist;
		CharClass charClass = utils.getCharacterClass(c);
		if(_charHist.containsKey(c)){
			_charHist.replace(c, _charHist.get(c)+1);
			ArrayList<Character> newValue = _cclassHist.get(charClass);
			newValue.add(c);
			_cclassHist.replace(charClass, newValue);
		}
		else{
			_charHist.put(c, 1.0);
			ArrayList<Character> lst = new ArrayList<>();
			lst.add(c);
			_cclassHist.put(charClass, lst);
		}
		int _total = this.total+1;
		return new Symbol(_charHist, _cclassHist, _total);
	}

	public double scoreChar (char c){
		Utils utils = new Utils();
		if(!(cclassHist.containsKey(utils.getCharacterClass(c)))) return 1.0;
		else if( !(charHist.containsKey(c)) && !(representation.isClass()) ) return 0.5;
		else return 0.0;
	}

	public String toString() {
		return representation.rep();
	}

}
