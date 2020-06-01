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

		HashMap<Character, Double> _charHist = new HashMap<Character, Double>();
		_charHist.put(c, 1.0);

		HashMap<CharClass, ArrayList<Character>> _cclassHist = new HashMap<CharClass, ArrayList<Character>>();
		_cclassHist.put(utils.getCharacterClass(c), new ArrayList<>(Arrays.asList(c)));

		int _total = 1;

		XClass _representation = repfn(_charHist, _cclassHist, _total);

		ArrayList<String> _symbolStringGenerator = _representation.lshDomain();

		this.charHist = _charHist;
		this.cclassHist = _cclassHist;
		this.total = _total;
		this.representation = _representation;
		this.symbolStringGenerator = _symbolStringGenerator;

	}

	public Symbol(HashMap<Character, Double> _charHist, HashMap<CharClass, ArrayList<Character> > _cclassHist, int _total) {

		super();

		this.charHist = _charHist;
		this.cclassHist = _cclassHist;
		this.total = _total;
		this.representation = repfn(_charHist, _cclassHist, _total);
		this.symbolStringGenerator = this.representation.lshDomain();

	}

	public Symbol addChar(char c){

		Utils utils = new Utils();

		HashMap<Character, Double> _charHist = charHist;
		HashMap<CharClass, ArrayList<Character> > _cclassHist = cclassHist;
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
		int _total = total+1;

		return new Symbol(_charHist, _cclassHist, _total);
	}

	private XClass repfn(HashMap<Character, Double> _charHist, HashMap<CharClass, ArrayList<Character> > _cclassHist, int _total){

		Config config = new Config();
		Utils utils = new Utils();

		HashMap<CharClass, Double> symbolPcts = new HashMap<>();
		_cclassHist.forEach((k, v) -> {
			double val = (double)v.size()/_total;
			symbolPcts.put(k, val);
		});

		CharClass maxSym = Collections.max(symbolPcts.entrySet(), Map.Entry.comparingByValue()).getKey();

		if(symbolPcts.get(maxSym) > 0.9){

			if(! maxSym.isClass()) return maxSym.toXClass();

			else{

				HashMap<Character, Double> trucatedHist = new HashMap<>();

				_charHist.forEach((k,v) -> {
					if(utils.getCharacterClass(k) == maxSym) trucatedHist.put(k, v);
				});

				HashMap<Character, Double> totalHist = new HashMap<>();
				totalHist.putAll(trucatedHist);
				totalHist.putAll(maxSym.baseHist());

				// Null hypothesis accepted
				if(!utils.significant(new ArrayList<Double>(totalHist.values()))) return maxSym.toXClass();

				// Only one character
				else if(trucatedHist.size() == 1) return new X_SPEC(String.valueOf(trucatedHist.keySet().iterator().next()));

				else{

					Map<Character, Double> sorted = trucatedHist.entrySet().stream().sorted(comparingByValue()).collect( toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
					AtomicReference<Double> atomicSum = new AtomicReference<>(0.0);
					// Cumilative Sum
					sorted.entrySet().forEach(e -> e.setValue(atomicSum.accumulateAndGet(e.getValue(), (x, y) -> x + y))); 
					double cutoff = (1-config.capturePct)*(new ArrayList<Double>(trucatedHist.values()).stream().mapToDouble(a -> a).sum());
					ArrayList<String> charToUse = new ArrayList<>();

					sorted.forEach((k,v) -> {
						if(v>cutoff) charToUse.add(String.valueOf(k));
					});

					return new X_OR(charToUse);

				}
			}
		}

		//Multiple Characters

		else{

			HashMap<Character, Double> resMap = new HashMap<>();
			resMap.putAll(utils.asciiMap());
			resMap.putAll(_charHist);

			if(!utils.significant(new ArrayList<Double>(resMap.values())))
				return new X_ANY();
			else
				return maxSym.toXClass();
		}
	}

	public double scoreChar (char c){
		Utils utils = new Utils();
		if(!(cclassHist.containsKey(utils.getCharacterClass(c)))) return 1.0;
		else if( !(charHist.containsKey(c)) && !(representation.isClass()) ) return 0.5;
		else return 0.0;
	}

	public String toString() {return representation.rep();}

}