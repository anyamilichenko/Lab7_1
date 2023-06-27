package data;

import java.io.Serializable;
import java.util.Objects;

public class DragonCave implements Serializable {
    private Double depth; //Поле может быть null
    private float numberOfTreasures; //Значение поля должно быть больше 0


    public DragonCave(Double depth, float numberOfTreasures) {
        this.depth = depth;
        this.numberOfTreasures = numberOfTreasures;
    }
    @Override
    public String toString() {
        return "Depth: " + depth +
                ", number of treasures: " + numberOfTreasures;
    }
    public Double getDepth(){
        return depth;
    }

    public float getNumberOfTreasures(){
        return numberOfTreasures;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DragonCave cave = (DragonCave) o;
        return Objects.equals(depth, cave.depth) && Objects.equals(numberOfTreasures, cave.numberOfTreasures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depth, numberOfTreasures);
    }
}

