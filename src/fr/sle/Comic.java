package fr.sle;

import java.util.Objects;

/**
 * @author slemoine
 */
public class Comic {

    private String title;

    private Integer price;

    public Comic(String title, Integer price) {
        Objects.requireNonNull(title);
        Objects.requireNonNull(price);
        this.title = title;
        this.price = price;
    }

    public Integer getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "ComicInputLine{" +
                "title='" + title + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comic line = (Comic) o;
        return Objects.equals(title, line.title) &&
                Objects.equals(price, line.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, price);
    }

}
