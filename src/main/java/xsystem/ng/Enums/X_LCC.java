package xsystem.ng.Enums;

import java.util.ArrayList;
import java.util.Random;

import xsystem.ng.Support.Config;

public class X_LCC implements XClass {

	public String rep(){
        return "\\w";
    }

    public ArrayList<Character> domain(){
        char[] lowerCaseChar = new Config().lowerCaseChar;
        ArrayList<Character> charlst = new ArrayList<Character>();
        for(char ch: lowerCaseChar){
            charlst.add(ch);
        }
        return charlst;
    }

    public Boolean isClass(){
        return true;
    }

    public String randomRep(){
        ArrayList<Character> chars = domain();
        int random = new Random().nextInt(chars.size());
        String randomChar = Character.toString(chars.get(random));
        return randomChar;
    }

    public ArrayList<String> lshDomain(){
        ArrayList<String> list = new ArrayList<String>();
        list.add(rep());
        return list;
    }
    public String toString(){
        return rep();
    }
    
}