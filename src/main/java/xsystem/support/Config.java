package xsystem.support;

import java.util.ArrayList;

/**Represents the Config Class.
 * @author Ipsita Mohanty
 * @version 0.0.1
 * @since 0.0.1
*/
public class Config {

    /**Maximum Branches in a XStructure */
    public final static int maxBranches = 7;

    /** Branching Seed*/
    public final static double branchingSeed = 0.1;

    /** List of Special Characters*/
    public final static char[] splChars;
    static {
        ArrayList<Character> r = new ArrayList<Character>();
        for(int i = 0; i<=47; i++){
            char c = (char)i;
            r.add(c);
        }
        for(int i = 58; i<=64; i++){
            char c = (char)i;
            r.add(c);
        }
        for(int i = 91; i<=96; i++){
            char c = (char)i;
            r.add(c);
        }
        for(int i = 123; i<=255; i++){
            char c = (char)i;
            r.add(c);
        }
        char[] res = new char[r.size()];
        for(int i = 0; i<r.size(); i++){
            res[i] = r.get(i).charValue();
        }
        splChars = res;
    }

    /** List of UpperCase Characters*/
    public final static char[] upperCaseChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /** List of LowerCase Characters*/
    public final static char[] lowerCaseChar = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    /** List of Numeric Characters*/
    public final static char[] numbers = "0123456789".toCharArray();

    /** Increment*/
    public final static int inc = 100;

    /** tts*/
    public final static Boolean tts = true;

    /** capturePct*/
    public final static Double capturePct = 0.8;

    /**
     * Computes the needed Sample Size
     * @param std standard deviation
     * @return the needed sample size
     */
    public static double neededSampleSize(double std){
        return Math.pow(1.96*std/0.5, 2.0);
    }

}
