package xsystem;

import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.ThreadLocalRandom;

import com.opencsv.CSVWriter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVGenerator {
    
    private final static Logger LOG = LoggerFactory.getLogger(CSVGenerator.class.getName());

    static String csvDir = "src/test/resources/csv/";

    final public static String DIR = FilenameUtils.separatorsToSystem(csvDir);

    static RandomStringGenerator strGenerator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();

    static ThreadLocalRandom intGenerator = ThreadLocalRandom.current();

    private static String generateDate(){
		String randomYear = Integer.toString(intGenerator.nextInt(1970, 2015 + 1));
		String randomMonth = Integer.toString(intGenerator.nextInt(1, 12 + 1));
		String randomDay = Integer.toString(intGenerator.nextInt(1, 28 + 1));
		if(randomMonth.length()==1){
			randomMonth = '0' + randomMonth;
		}
		if(randomDay.length()==1){
			randomDay = '0' + randomDay;
		}
		String dateTime = randomYear+'/'+randomMonth+'/'+randomDay;
		return dateTime;
    }
    
    public static void writeTables (int numTables, int numRows, int numAttrs, int maxStringLength) {
		File dirObj = new File(DIR);
		if (!dirObj.exists()) {
			dirObj.mkdirs();
		}
		for (int i=1; i<=numTables; i++) {
			writeTable(i, numRows, numAttrs, maxStringLength);
		}
    }
    
    private static void writeTable (int tableID, int numRows, int numAttrs, int maxStringLength) {
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(DIR + "table" + tableID + ".csv"));
			writer.writeNext(getHeader(numAttrs));
			for (int i=1; i<=numRows; i++) {
				String[] row = getRow(i, numAttrs, maxStringLength);
				writer.writeNext(row);
			}
		     writer.close();
		} catch (Exception e) {
			LOG.info("[Error] " + e);
		}
		strGenerator.generate(1,20);
		RandomUtils.nextBoolean();
    }
    
    private static String[] getHeader (int numAttrs) {
		String[] row = new String[numAttrs];
		for (int i=0; i<numAttrs; i++) {
			switch (i % 4) {
			case 1:
				row[i] = "Date" + i;
				break;
			case 2:
				row[i] = "Coordinates" + i;
                break;
            case 3:
				row[i] = "Email" + i;
				break;
			default:
				row[i] = "Phone Number" + i;
			}
		}
		return row;	
    }
    
    private static String[] getRow (int id, int numAttrs, int maxStringLength) {
		String[] row = new String[numAttrs];
		for (int i=0; i<numAttrs; i++) {
			switch (i % 4) {
			case 1:
				row[i] = generateDate();
				break;
            case 2:
                int randomInt1 = intGenerator.nextInt(-99999999, 99999999);
                double d1 = randomInt1/1000000.0;
                int randomInt2 = ThreadLocalRandom.current().nextInt(-99999999, 99999999);
                double d2 = randomInt2/1000000.0;
                row[i] = Double.toString(d1) + " " + Double.toString(d2);
				break;
			case 3:
                row[i] = strGenerator.generate(1, maxStringLength) + "@" + strGenerator.generate(5) + "." + strGenerator.generate(3);
				break;
			default:
                String r1 = Integer.toString(intGenerator.nextInt(100, 999));
                String r2 = Integer.toString(intGenerator.nextInt(100, 999));
                String r3 = Integer.toString(intGenerator.nextInt(1000, 9999));
                row[i] = r1 + "-" + r2 + "-" + r3;
			}
		}
		return row;
	}
}