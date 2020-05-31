package xsystem.ng.Enums;

import java.util.ArrayList;
import java.util.HashMap;

//Class representing class of character in string

public interface CharClass {

    public String rep();
    public XClass toXClass();
    public ArrayList<Character> domain();
    public Boolean isClass();
    public HashMap<Character, Double> baseHist();
    
}
