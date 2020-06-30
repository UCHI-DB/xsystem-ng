package xsystem.unit;

import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xsystem.learning.LearningModel;
import xsystem.learning.XStructType;

public class XStructReadingTest {

    private final static Logger LOG = LoggerFactory.getLogger(LearningModel.class.getName());

    @Test
    public void readXStructFromJSONTest(){
        BasicConfigurator.configure();

        LearningModel model = new LearningModel();

        String path = "src/main/resources/Learned/LearnedXStructs.json";
        ArrayList<XStructType> resfromFile = model.readXStructsfromJSON(path);

        for(XStructType x : resfromFile)
            LOG.info("XStructure " + x.xStructure + " of type " + x.type + " has been read correctly!");
        
        String folderPath = "src/main/resources/Learned";
        ArrayList<XStructType> resfromFolder = model.readXStructsfromJSON(folderPath);

        for(XStructType x : resfromFolder)
            LOG.info("XStructure " + x.xStructure + " of type " + x.type + " has been read correctly!");
    }
}