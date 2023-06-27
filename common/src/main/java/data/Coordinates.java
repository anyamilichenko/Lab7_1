package data;

import java.io.Serializable;
import java.util.Objects;

/**
 * X-Y координаты
 */

public class Coordinates implements Serializable {
    private double x;

    private double y; //Значение поля должно быть больше -513, Поле не может быть null

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return X координата
     */
    public double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates{"
                + "x=" + x
                + ", y=" + y
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinates that = (Coordinates) o;
        return x == that.x && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
