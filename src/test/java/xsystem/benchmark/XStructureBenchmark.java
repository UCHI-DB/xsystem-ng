package xsystem.benchmark;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.BasicConfigurator;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import xsystem.CSVGenerator;
import xsystem.layers.XStructure;
import xsystem.learning.LearningModel;
import xsystem.learning.XStructType;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class XStructureBenchmark {
    static int numTables = 2;
    static int numRows = 10;
    static int numAttributes = 6;
    static int maxStringLength = 10;
    final static String folder = "src/test/resources/csv";
    final static String outFolder = "src/test/resources/output/benchmark/";
    ArrayList<XStructType> lstXStructType;

    @Setup
    public void prepare() {
        BasicConfigurator.configure();
        new CSVGenerator();
        CSVGenerator.writeTables(numTables, numRows, numAttributes, maxStringLength);
        File file = new File(outFolder);
        file.mkdir();
        String outfile = outFolder + "learned.json";
        LearningModel model = new LearningModel();
        model.learnStructs(folder, outfile);
        lstXStructType = model.readXStructsfromJSON(outFolder);
    }

    @Benchmark
    public void learnXStructtoJSONBenchmark(){
        BasicConfigurator.configure();
        String outfile = outFolder + "benchmarkLearn.json";
        LearningModel model = new LearningModel();
        model.learnStructs(folder, outfile);
    }

    @Benchmark
    public void readXstuctfromJSONBenchmark(Blackhole blackhole){
        BasicConfigurator.configure();
        LearningModel model = new LearningModel();
        ArrayList<XStructType> learned = model.readXStructsfromJSON(outFolder);
        blackhole.consume(learned);
    }

    @Benchmark
    public void addLinesBenchmark(Blackhole blackhole){
        ArrayList<String> lines = new ArrayList<>();
        for(int i=0; i<numRows; i++){
            String line = RandomStringUtils.randomAlphabetic(5, maxStringLength);
            lines.add(line);
        }
        int random = ThreadLocalRandom.current().nextInt(0, lstXStructType.size());
        XStructure x = lstXStructType.get(random).xStructure;
        XStructure learned = x.addNewLines(lines);
        blackhole.consume(learned);
    }

    @Benchmark
    public void generateStringsBenchMark(Blackhole blackhole){
        int random = ThreadLocalRandom.current().nextInt(0, lstXStructType.size());
        XStructure x = lstXStructType.get(random).xStructure;
        int randomInt = ThreadLocalRandom.current().nextInt(5, 10+1);
        ArrayList<String> randomStrings = x.generateStrings(randomInt);
        blackhole.consume(randomStrings);
    }

    @Benchmark
    public void subsetScoreBenchmark(Blackhole blackhole){
        int r1 = ThreadLocalRandom.current().nextInt(0, lstXStructType.size());
        int r2 = ThreadLocalRandom.current().nextInt(0, lstXStructType.size());
        XStructure x1 = lstXStructType.get(r1).xStructure;
        XStructure x2 = lstXStructType.get(r2).xStructure;
        double score = x1.subsetScore(x2);
        blackhole.consume(score);
    }

    @Benchmark
    public void outlierScoreBenchmark(Blackhole blackhole){
        int random = ThreadLocalRandom.current().nextInt(0, lstXStructType.size());
        XStructure x = lstXStructType.get(random).xStructure;
        String randomStr = RandomStringUtils.randomAlphabetic(5, maxStringLength);
        double outlierScore = x.computeOutlierScore(randomStr);
        blackhole.consume(outlierScore);
    }

    @Benchmark
    public void mergeXStructBenchmark(Blackhole blackhole){
        int r1 = ThreadLocalRandom.current().nextInt(0, lstXStructType.size());
        int r2 = ThreadLocalRandom.current().nextInt(0, lstXStructType.size());
        XStructure x1 = lstXStructType.get(r1).xStructure;
        XStructure x2 = lstXStructType.get(r2).xStructure;
        XStructure merged = x1.mergeWith(x2);
        blackhole.consume(merged);
    }

    @Benchmark
    public void compareTwoXStructBenchmark(Blackhole blackhole){
        int r1 = ThreadLocalRandom.current().nextInt(0, lstXStructType.size());
        int r2 = ThreadLocalRandom.current().nextInt(0, lstXStructType.size());
        XStructure x1 = lstXStructType.get(r1).xStructure;
        XStructure x2 = lstXStructType.get(r2).xStructure;
        double score = x1.compareTwo(x1, x2);
        blackhole.consume(score);
    }

    @Benchmark
    public void mergeMultipleXStructBenchmark(Blackhole blackhole){
        int r1 = ThreadLocalRandom.current().nextInt(0, lstXStructType.size());
        int r2 = ThreadLocalRandom.current().nextInt(0, lstXStructType.size());
        XStructure x1 = lstXStructType.get(r1).xStructure;
        ArrayList<XStructure> tobeMerged = new ArrayList<>();
        for(int i=0; i<r2; i++)
            tobeMerged.add(lstXStructType.get(i).xStructure);
        XStructure merged = x1.mergeMultiple(tobeMerged);
        blackhole.consume(merged);
    }

    @TearDown
    public void shutDown(){
        File dirInput = new File(folder);
        File[] directoryListinginput = dirInput.listFiles();
        if (directoryListinginput != null) {
            for (File child : directoryListinginput) {
                child.delete();
            }
            dirInput.delete();
        }
        File dir2 = new File(outFolder);
        File[] directoryListingOut = dir2.listFiles();
        if (directoryListingOut != null) {
            for (File child : directoryListingOut) {
                child.delete();
            }
            dir2.delete();
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                        .include(XStructureBenchmark.class.getSimpleName())
                        .forks(1)
                        .warmupIterations(1)
                        .measurementIterations(1)
                        .resultFormat(ResultFormatType.JSON)
                        .result("src/test/resources/benchmarking/XStructureBenchmark.json")
                        .build();

        new Runner(opt).run();
    }
}
