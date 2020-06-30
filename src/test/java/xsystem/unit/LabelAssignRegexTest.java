package xsystem.unit;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;

import xsystem.learning.LearningModel;

public class LabelAssignRegexTest {

    @Test
    public void labelAssignmentwithRegexTest(){
        BasicConfigurator.configure();

        LearningModel model = new LearningModel();

        String regexFilePath = "src/test/resources/regexData/regexandType.csv";
        String learnedXstructfile = "src/test/resources/output/learned";
        String outputFile = "src/test/resources/output/validation/regexMatched.json";

        model.labelAssignmentWithRegex(regexFilePath, learnedXstructfile, outputFile);
    }
}