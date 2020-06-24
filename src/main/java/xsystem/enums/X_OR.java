package xsystem.enums;

import java.util.ArrayList;
import java.util.Random;

public class X_OR implements XClass {
    ArrayList<String> cs;

    public X_OR(ArrayList<String> cs) {
		this.cs = cs;
	}

	public String representation(){
        return "(" + String.join("|", cs) + ")";
    }

    public ArrayList<Character> domain(){
        ArrayList<Character> charlst = new ArrayList<>();
        for(String str : cs){
            char[] chars = str.toCharArray();
            charlst.add(chars[0]);
        }
        return charlst;
    }

    public Boolean isClass(){
        return false;
    }

    public String randomRep(){
        ArrayList<Character> chars = domain();
        int random = new Random().nextInt(chars.size());
        String randomChar = Character.toString(chars.get(random));
        return randomChar;
    }

    public ArrayList<String> lshDomain(){
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<Character> chars = domain();
        for(char c : chars){
            list.add(String.valueOf(c));
        }
        return list;
    }

    public String toString(){
        return representation();
    }
}