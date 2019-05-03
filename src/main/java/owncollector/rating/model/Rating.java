package owncollector.rating.model;

public class Rating {

    private final int mark;

    public Rating(int mark) {
        this.mark = mark;
    }

    public int getMark() {
        return mark;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "mark=" + mark +
                '}';
    }
}
