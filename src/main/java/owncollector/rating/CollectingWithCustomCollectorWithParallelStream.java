package owncollector.rating;


import owncollector.rating.collector.ConcurrentRatingCollector;
import owncollector.rating.model.CommonFixture;
import owncollector.rating.model.Rating;
import owncollector.rating.model.SummarizedRating;

import java.util.List;

public class CollectingWithCustomCollectorWithParallelStream {

    public static void main(String[] args) {

        List<Rating> ratings = CommonFixture.create10millionsOfRatings();

        long start = System.currentTimeMillis();
        SummarizedRating summarizedRating =
                ratings.parallelStream()
                       .collect(new ConcurrentRatingCollector());

        System.out.println("time = " + (System.currentTimeMillis() - start));

        System.out.println(summarizedRating);
    }

}
