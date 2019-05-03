package owncollector.rating;


import owncollector.rating.model.CommonFixture;
import owncollector.rating.model.Rating;
import owncollector.rating.model.SummarizedRating;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;

public class CollectingWithBuiltInCollectorButWithoutExtractedFunction {

    private enum RatingLevel {
        GOOD, AVERAGE, BAD
    }

    public static void main(String[] args) {

        List<Rating> ratings = CommonFixture.create10millionsOfRatings();

        long start = System.currentTimeMillis();
        SummarizedRating summarizedRating =
                ratings.stream()
                       .collect(collectingAndThen(groupingBy(r -> {
                                                      int mark = r.getMark();
                                                      if (mark >= 4 && mark <= 6) {
                                                          return RatingLevel.AVERAGE;
                                                      } else if (mark < 4) {
                                                          return RatingLevel.BAD;
                                                      }
                                                      return RatingLevel.GOOD;
                                                  }, Collectors.counting()),
                                                  m -> {
                                                      float prctGood = m.getOrDefault(RatingLevel.GOOD,
                                                                                      0L) / (float) ratings.size();
                                                      float prctAverage = m.getOrDefault(RatingLevel.AVERAGE,
                                                                                         0L) / (float) ratings.size();
                                                      float prctBad = m.getOrDefault(RatingLevel.BAD,
                                                                                     0L) / (float) ratings.size();
                                                      return new SummarizedRating(prctGood, prctAverage, prctBad);
                                                  })
                       );

        System.out.println("time = " + (System.currentTimeMillis() - start));

        System.out.println(summarizedRating);
    }


}
