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
import xsystem.learning.LearningModel;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class LearningModelBenchmark {
    static String regexFilePath = "src/test/resources/regexData/regexandType.csv";
    static String learnedXstructfile = "src/test/resources/output/learned";
    static String outputFile = "src/test/resources/output/benchmark/regexMatched.json";
    ArrayList<String> input;

    @Setup
    public void prepare(){
        int random = ThreadLocalRandom.current().nextInt(5,11);
        ArrayList<String> strList = new ArrayList<>();
        for(int i=0; i<random; i++)
            strList.add(RandomStringUtils.randomAlphabetic(5, 10));
        input = strList;
        File file = new File("src/test/resources/output/benchmark");
        file.mkdir();
    }

    @Benchmark
    public void labelAssignmentwithRegexBenchmark(){
        BasicConfigurator.configure();
        LearningModel model = new LearningModel();
        model.labelAssignmentWithRegex(regexFilePath, learnedXstructfile, outputFile);
    }

    @Benchmark
    public void labelAssignmentBenchmark(Blackhole blackhole){
        BasicConfigurator.configure();
        LearningModel model = new LearningModel();
        String label = model.labelAssignment(input, LearningModel.defaultXLabelRef);
        blackhole.consume(label);
    }

    @TearDown
    public void shutdown(){
        File dir = new File("src/test/resources/output/benchmark");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                child.delete();
            }
            dir.delete();
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LearningModelBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(1)
                .resultFormat(ResultFormatType.JSON)
                .result("src/test/resources/benchmarking/LearningModelBenchmark.json")
                .build();

        new Runner(opt).run();
    }
}
