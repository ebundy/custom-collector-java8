package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import owncollector.rating.collector.ConcurrentRatingCollector;
import owncollector.rating.collector.RatingCollector;
import owncollector.rating.model.Rating;
import owncollector.rating.model.RatingLevel;
import owncollector.rating.model.SummarizedRating;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class CollectorBenchmark {

    private List<Rating> ratings;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder().include(CollectorBenchmark.class.getSimpleName())
                                          .warmupIterations(5)
                                          .measurementIterations(5)
                                          .forks(1)
                                          .build();
        new Runner(opt).run();
    }

    @Setup(Level.Iteration)
    public void doSetup() {
        ratings = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100_000; i++) {
            ratings.add(new Rating(random.nextInt(11)));
        }
    }


    @Benchmark
    public void _1_sequential_stream_with_builtin_collector() {
        ratings.stream()
               .collect(collectingAndThen(groupingBy(r -> getRatingLevel(r), Collectors.counting()),
                                          m -> getSummarizedRating(ratings, m)
                        )
               );
    }

    @Benchmark
    public void _2_parallel_stream_with_builtin_collector() {
        ratings.parallelStream()
               .collect(collectingAndThen(groupingBy(r -> getRatingLevel(r), Collectors.counting()),
                                          m -> getSummarizedRating(ratings, m)
                        )
               );
    }

    @Benchmark
    public void _3_sequential_stream_with_custom_collector() {
        ratings.stream()
               .collect(new RatingCollector());
    }

    @Benchmark
    public void _4_parallel_stream_with_custom_collector() {
        ratings.parallelStream()
               .collect(new ConcurrentRatingCollector());
    }


    private static RatingLevel getRatingLevel(Rating r) {
        int mark = r.getMark();
        if (mark >= 4 && mark <= 6) {
            return RatingLevel.AVERAGE;
        } else if (mark < 4) {
            return RatingLevel.BAD;
        }
        return RatingLevel.GOOD;
    }


    private static SummarizedRating getSummarizedRating(List<Rating> ratings, Map<RatingLevel, Long> m) {
        float prctGood = m.getOrDefault(RatingLevel.GOOD,
                                        0L) / (float) ratings.size();
        float prctAverage = m.getOrDefault(RatingLevel.AVERAGE,
                                           0L) / (float) ratings.size();
        float prctBad = m.getOrDefault(RatingLevel.BAD,
                                       0L) / (float) ratings.size();
        return new SummarizedRating(prctGood, prctAverage, prctBad);
    }


}