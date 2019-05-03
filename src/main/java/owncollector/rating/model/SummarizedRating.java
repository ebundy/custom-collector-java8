package owncollector.rating.model;

public class SummarizedRating {

    private float prctGood;
    private float prctAverage;
    private float prctBad;

    public SummarizedRating(float prctGood, float prctAverage, float prctBad) {
        this.prctGood = prctGood;
        this.prctAverage = prctAverage;
        this.prctBad = prctBad;
    }

    public float getPrctGood() {
        return prctGood;
    }

    public float getPrctAverage() {
        return prctAverage;
    }

    public float getPrctBad() {
        return prctBad;
    }

    @Override
    public String toString() {
        return "SummarizedRating{" +
                "prctGood=" + prctGood +
                ", prctAverage=" + prctAverage +
                ", prctBad=" + prctBad +
                '}';
    }
}
