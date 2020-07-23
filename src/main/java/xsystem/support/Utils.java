package xsystem.support;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math3.stat.inference.ChiSquareTest;

import xsystem.enums.CharClass;
import xsystem.enums.LCC;
import xsystem.enums.NUM;
import xsystem.enums.SPC;
import xsystem.enums.UCC;

/**Represents the Utils Class
 * @author Ipsita Mohanty
 * @version 0.0.1
 * @since 0.0.1
*/
public class Utils {

    public final static HashMap<Character, Double> asciiMap;
    static{
        HashMap<Character, Double> asciMap = new HashMap<>();
        for(int i=0; i<=256; i++) asciMap.put((char)i, 0.0);
        asciiMap = asciMap;
    }

    /**
     * Returns Character Class of given character
     * @param c given character
     * @return Character Class
     */
    public static CharClass getCharacterClass(char c) {
        new Config();
        if(new String(Config.upperCaseChar).contains(Character.toString(c)))
            return new UCC();
        else if(new String(Config.lowerCaseChar).contains(Character.toString(c)))
            return new LCC();
        else if(new String(Config.numbers).contains(Character.toString(c)))
            return new NUM();
        else return new SPC(Character.toString(c));
    }

    /**
     * Performs Chi-Square test expecting a uniform distribution
     * @param observed the observed values
     * @return boolean value if significant or not
     */
    public static Boolean significant(ArrayList<Double> observed) {
        if(observed.size()<2) return false;
        else{
            ChiSquareTest chi = new ChiSquareTest();
            double[] exp = new double[observed.size()];
            long[] obs = new long[observed.size()];
            for(int i = 0; i< observed.size(); i++){
                obs[i] = observed.get(i).longValue();
                exp[i] = 1.0;
            }
            return chi.chiSquareTest(exp, obs, 0.01);
        }
    }

     /**
      * Round-robin iterator for merging streams
      * @param streams streams
      * @return merged stream list
      */
    public static ArrayList<Wrapper> mergeStreams(ArrayList<ArrayList<Wrapper>> streams){
        ArrayList<Wrapper> res = new ArrayList<>();
        Boolean updated = true;
        while (updated){
            updated = false;
            for( ArrayList<Wrapper> lst : streams){
                if(lst.size()>0){
                    res.add(lst.get(0));
                    lst.remove(0);
                    updated = true;
                }
            }
        }
        return res;
    }
}
