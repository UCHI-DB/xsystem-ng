package xsystem.enums;

import java.util.ArrayList;
import java.util.Random;

import xsystem.support.Config;

public class X_NUM implements XClass {
	public String representation(){
        return "\\d";
    }

    public ArrayList<Character> domain(){
        char[] numbers = Config.numbers;
        ArrayList<Character> charlst = new ArrayList<Character>();
        for(char ch: numbers){
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
        list.add(representation());
        return list;
    }
    public String toString(){
        return representation();
    }
     
}