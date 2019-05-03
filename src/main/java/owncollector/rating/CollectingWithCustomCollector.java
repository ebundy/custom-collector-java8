package owncollector.rating;


import owncollector.rating.collector.RatingCollector;
import owncollector.rating.model.Rating;
import owncollector.rating.model.SummarizedRating;

import java.util.List;

import static owncollector.rating.model.CommonFixture.create10millionsOfRatings;

public class CollectingWithCustomCollector {

    public static void main(String[] args) {

        List<Rating> ratings = create10millionsOfRatings();
        long start = System.currentTimeMillis();
        SummarizedRating summarizedRating =
                ratings.stream()
                       .collect(new RatingCollector());

        System.out.println("time = " + (System.currentTimeMillis() - start));

        System.out.println(summarizedRating);
    }

}
