package xsystem;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public interface XSystem {

    XStructure build(Iterator<String> data);

    Iterator<String> generate(XStructure pattern);

    List<String> generate(XStructure pattern, int limit);

    boolean match(XStructure pattern, String value);

    boolean match(XStructure pattern1, XStructure pattern2);

    boolean match(XStructure pattern1, Pattern regex);

}
