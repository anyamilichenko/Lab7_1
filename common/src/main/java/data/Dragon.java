package data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Objects;

public class Dragon implements Comparable<Dragon>, Serializable {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creation_date; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long dragons_age; //Значение поля должно быть больше 0
    private Integer wingspan; //Значение поля должно быть больше 0, Поле не может быть null
    private Double weight; //Значение поля должно быть больше 0, Поле не может быть null
    private DragonCharacter dragon_character; //Поле не может быть null
    private DragonCave cave; //Поле может быть null

    private LinkedList<Dragon> mainData = new LinkedList<>();
    //private LocalDateTime lastInitTime;
    private String authorName;



    public Dragon(String name, Coordinates coordinates, long dragons_age, DragonCharacter dragon_character, DragonCave cave, int wingspan, double weight, String authorName) {
        this.name = name;
        this.coordinates = coordinates;
        this.dragons_age = dragons_age;
        this.dragon_character = dragon_character;
        this.cave = cave;
        this.wingspan = wingspan;
        this.weight = weight;
        this.authorName = authorName;

    }

    /**
     * @return ID дракона
     */

    public Long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }



    public String getName(){
        return this.name;
    }


    public Coordinates getCoordinates(){
        return coordinates;
    }
    public LocalDateTime getCreationDate() {
        return creation_date;
    }



//    public java.time.LocalDateTime getCreationDate(){
//        return creationDate;
//    }


    public long getAge(){
        return (int)dragons_age;
    }



    public Integer getWingspan(){
        return wingspan;
    }



    public Double getWeight(){
        return weight;
    }





    public DragonCharacter getCharacter(){
        return dragon_character;
    }



    public DragonCave getCave(){
        return cave;
    }

    public String getAuthorName() {
        return authorName;
    }

    //@Override
    //public int compareTo(Dragon d){
    //    return age.compareTo(d.getAge());
    //}

    @Override
    public String toString(){
        String info = "";
        info += "\n\u001B[37m"+"\u001B[36m"+"Dragon - " + id + "\u001B[36m"+"\u001B[37m";
        info += "\n Name: " + name;
        info += "\n Coordinates: " + coordinates;
        info += "\n Age: " + dragons_age;
        info += "\n Wingspan: " + wingspan;
        info += "\n Weight: " + weight;
        info += "\n Personality: " + dragon_character;
        info += "\n Dragon cave: " + cave;
        info += "\n Author name: " + authorName;
        return info;
    }


    @Override
    public boolean equals(Object f) {
        if (this == f) return true;
        if (f == null || getClass() != f.getClass()) return false;
        Dragon that = (Dragon) f;
        return name.equals(that.getName()) && coordinates.equals(that.getCoordinates()) && creation_date.equals(that.getCreationDate()) &&
                (dragons_age == that.getAge()) && (wingspan == that.getWingspan()) &&
                (weight == that.getWeight()) && (dragon_character == that.getCharacter()) &&
                (cave == that.getCave());
    }
    //    @Override
//    public boolean equals(Object f) {
//        if (this == f) return true;
//        if (f == null || getClass() != f.getClass()) return false;
//        Dragon that = (Dragon) f;
//        return name.equals(that.getName(newInstance.getName())) && coordinates.equals(that.getCoordinates()) && creationDate.equals(that.getCreationDate()) &&
//                (age == that.getAge()) && (wingspan == that.getWingspan()) &&
//                (weight == that.getWeight()) && (character == that.getCharacter()) &&
//                (cave == that.getCave());
//    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creation_date, dragons_age, wingspan, weight, dragon_character, cave);
    }

    @Override
    public int compareTo(Dragon dragon) {
        return getName().compareTo(dragon.getName());
    }
//    @Override
//    public int compareTo(Dragon dragon) {
//        return getName(newInstance.getName()).compareTo(dragon.getName(newInstance.getName()));
//    }



//    public Long newId() {
//        if (mainData.isEmpty()) {
//            return 1L;
//        }
//        Long lastId = Long.MIN_VALUE;
//        for (Iterator<Dragon> iter = mainData.iterator(); iter.hasNext();) {
//            Dragon d = iter.next();
//            lastId = Math.max(lastId, d.getID());
//        }
//        return lastId + 1;
//    }

//    @Override
//    public boolean validate(){
//        if (id == null || id <=0) return false;
//        if (name == null || name.isEmpty()) return false;
//        if (coordinates == null) return false;
//        if (creationDate == null) return false;
//        if (age >= 0) return false;
//        if (wingspan >=0 || wingspan == null) return false;
//        if (weight >=0 || weight == null) return false;
//        if (character == null) return false;
//        return true;
//    }





}



