package xsystem.ng.Layers;

import java.util.ArrayList;

public class Token {
	ArrayList<Symbol> symbols;
	Boolean doneAdding;
	ArrayList<Double> history;
	double stdDev;
	ArrayList<Double> archive;
	
	public Token(ArrayList<Symbol> symbols, Boolean doneAdding, ArrayList<Double> history, double stdDev,
			ArrayList<Double> archive) {
		this.symbols = symbols;
		this.doneAdding = doneAdding;
		this.history = history;
		this.stdDev = stdDev;
		this.archive = archive;
	}

	
}