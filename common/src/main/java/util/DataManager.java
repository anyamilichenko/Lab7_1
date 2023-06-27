package util;

import data.Dragon;
import data.User;

import java.util.LinkedList;

/**
 * This interface does everything but giving the collection itself, so no SOLID principles should be
 * violated :)
 */
public interface DataManager {

    void addUser(User user);

    void addDragon(Dragon dragon);

    boolean validateUser(String username, String password);

    boolean checkIfUsernameUnique(String username);

//    boolean checkIfMin(Dragon dragon);

    void clearOwnedData(String username);

    void setCollection(LinkedList<Dragon> dragons);

    //    String filterLessThanSemesterEnumToString(Semester inpEnum);
//
    String getCollectionInfo();
    //InfoCommand.InfoCommandResult getInfoAboutCollections();

    void removeLast(String username);
    int getCollectionSize();

    //Dragon getMinByIdDragon();

    String ascendingDataToString();

    void removeDragonById(long id);

    String showSortedByName();

    void updateDragonById(long id, Dragon dragon);

    int countGreater(Integer arg);

    void removeGreaterIfOwned(Dragon dragon, String username);

    boolean validateOwner(String username, long dragonId);

    void initialiseData();
    LinkedList<Dragon> getMainData();

    void removeAllByWeight(Double arg, String username);


}