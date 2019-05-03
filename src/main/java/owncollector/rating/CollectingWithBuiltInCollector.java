package owncollector.rating;


import owncollector.rating.model.Rating;
import owncollector.rating.model.RatingLevel;
import owncollector.rating.model.SummarizedRating;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static owncollector.rating.model.CommonFixture.create10millionsOfRatings;

public class CollectingWithBuiltInCollector {

    public static void main(String[] args) {

        List<Rating> ratings = create10millionsOfRatings();
        long start = System.currentTimeMillis();
        SummarizedRating summarizedRating =
                ratings.stream()
                       .parallel()
                       .collect(collectingAndThen(groupingBy(r -> getRatingLevel(r), Collectors.counting()),
                                                  m -> getSummarizedRating(ratings, m)
                                )
                       );

        System.out.println("time = " + (System.currentTimeMillis() - start));

        System.out.println(summarizedRating);
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
