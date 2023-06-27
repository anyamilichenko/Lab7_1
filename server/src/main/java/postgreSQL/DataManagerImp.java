package postgreSQL;

import data.Dragon;
import data.User;
import org.apache.logging.log4j.core.Logger;
import util.DataManager;
import util.Encryptor;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class DataManagerImp implements DataManager {
    private final Database database;
    private LinkedList<Dragon> mainData = new LinkedList<>();
    private LinkedList<User> users = new LinkedList<>();
    private final Logger logger;
    private final ReadWriteLock dragonCollectionLock = new ReentrantReadWriteLock(true);
    private final ReadWriteLock userLock = new ReentrantReadWriteLock(true);
    //private final ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final Date dateOfInitialization = new Date();


    public DataManagerImp(Database database, Logger logger) {
        this.database = database;
        this.logger = logger;


    }

    @Override
    public void addUser(User user) {
        Lock writeLock = userLock.writeLock();
        try {
            writeLock.lock();
            final User encryptedUser = new User(user.getId(), Encryptor.encryptThisString(user.getPassword()), user.getName());

            final long generatedId;

            try {
                generatedId = database.getUsersTable().add(encryptedUser);
                encryptedUser.setId(generatedId);
                users.add(encryptedUser);

                logger.info("Successfully registered a new user: " + encryptedUser);
            } catch (SQLException e) {
                logger.info("Problem with sql in void addUser");
            }

//            encryptedUser.setId(generatedId);
//            users.add(encryptedUser);
//
//            logger.info("Successfully registered a new user: " + encryptedUser);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void addDragon(Dragon dragon) {
        Lock writeLock = dragonCollectionLock.writeLock();
        try {
            writeLock.lock();
            final long generatedId = database.getDragonTable().add(dragon);
            dragon.setId(generatedId);
            mainData.add(dragon);
            logger.info("Successfully added a dragon: " + dragon);
        } catch (SQLException e) {
            logger.info("Problem with adding dragon in void addDragon");
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public boolean validateUser(String username, String password) {
        Lock readLock = userLock.readLock();
        try {
            readLock.lock();
            return users.stream().anyMatch(it -> it.getName().equals(username) && it.getPassword().equals(Encryptor.encryptThisString(password)));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean checkIfUsernameUnique(String username) {
        Lock readLock = userLock.readLock();
        try {
            readLock.lock();
            return users.stream().noneMatch(it -> it.getName().matches(username));
        } finally {
            readLock.unlock();
        }
    }

//    @Override
//    public boolean checkIfMin(Dragon dragon) {
//        Lock readLock = dragonCollectionLock.readLock();
//        try {
//            readLock.lock();
//            return mainData.isEmpty() || dragon.compareTo(mainData.peekFirst()) < 0;
//        } finally {
//            readLock.unlock();
//        }
//    }

    @Override
    public void clearOwnedData(String username) {
        Lock writeLock = userLock.writeLock();
        try {
            writeLock.lock();
            database.getDragonTable().clearOwnedData(username);
            mainData.removeIf(it -> it.getAuthorName().equals(username));
            logger.info("Successfully clear an owned data");
        } catch (SQLException e) {
            logger.info("Problem with clear an owned data");
        } finally {
            writeLock.unlock();
        }

    }

//    @Override
//    public String filterLessThanSemesterEnumToString(DragonCharacter inpEnum) {
//        Lock readLock = lock.readLock();
//        try {
//            readLock.lock();
//            StringJoiner output = new StringJoiner("\n\n");
//            mainData.stream().filter(it -> it.getCharacter().compareTo(inpEnum) < 0).forEach(it -> output.add(it.toString()));
//            return output.toString();
//        } finally {
//            readLock.unlock();
//        }
//    }

    @Override
    public int getCollectionSize() {
        return mainData.size();
    }


    @Override
    public LinkedList<Dragon> getMainData(){
        return mainData;
    }


    public void setCollection(LinkedList<Dragon> dragons) {
        this.mainData = dragons;
    }


    @Override
    public String getCollectionInfo() {
        return "Collection type: " + mainData.getClass().getName() + "\n"
                + "Date of initialization: " + dateOfInitialization + "\n"
                + "Collection size: " + mainData.size();
    }
//    @Override
//    public InfoCommand.InfoCommandResult getInfoAboutCollections() {
//        Lock readLock = lock.readLock();
//        try {
//            readLock.lock();
//            if (mainData.isEmpty()) {
//                return new InfoCommand.InfoCommandResult(
//                        0,
//                        0
//                );
//            }
//            return new InfoCommand.InfoCommandResult(
//                    mainData.size());
//        } finally {
//            readLock.unlock();
//        }
//    }


//    @Override
//    public Dragon getMinByIdDragon() {
//        Lock readLock = dragonCollectionLock.readLock();
//        try {
//            readLock.lock();
//            return mainData.stream().min(Comparator.comparingLong(Dragon::getId)).orElse(null);
//        } finally {
//            readLock.unlock();
//        }
//    }

    @Override
    public String ascendingDataToString() {
        Lock readLock = dragonCollectionLock.readLock();
        try {
            readLock.lock();
            return mainData.toString();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void removeDragonById(long id) {
        Lock writeLock = dragonCollectionLock.writeLock();
        try {
            writeLock.lock();
            database.getDragonTable().removeById(id);
            mainData.removeIf(it -> it.getId() == id);
            logger.info("Successfully remove a dragon with id: " + id);
        } catch (SQLException e) {
            logger.info("Problem with remove a dragon with id: " + id);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removeLast(String username) {
        Lock writeLock = dragonCollectionLock.writeLock();
        try {
            writeLock.lock();
            database.getDragonTable().removeLast(username);
            Dragon dragon = mainData.stream().sorted((o1,o2)-> o2.getId().compareTo(o1.getId())).collect(Collectors.toList()).stream().filter(el -> el.getAuthorName().equals(username)).findFirst().orElse(null);
            mainData.remove(dragon);
            logger.info("Successfully remove a last dragon element");
        } catch (SQLException e) {
            logger.info("Problem with remove a last dragon element");
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String showSortedByName() {
        Lock readLock = dragonCollectionLock.readLock();
        try {
            readLock.lock();
            return mainData
                    .stream()
                    .sorted(Comparator.comparing(Dragon::getName))
                    .collect(Collectors.toList()).toString();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void updateDragonById(long id, Dragon dragon) {
        Lock writeLock = dragonCollectionLock.writeLock();
        try {
            writeLock.lock();
            database.getDragonTable().updateById(id, dragon);
            mainData.removeIf(it -> it.getId() == id);
            mainData.add(dragon);
            logger.info("Successfully update dragon with id:" + id);
        } catch (SQLException e) {
            logger.info("Problem update dragon with id:" + id);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean validateOwner(String username, long dragonId) {
        Lock readLock = userLock.readLock();
        try {
            readLock.lock();
            return mainData.stream().anyMatch(it -> it.getId() == dragonId && it.getAuthorName().equals(username));
        } finally {
            readLock.unlock();
        }
    }


    @Override
    public void initialiseData() {
        try {
            this.mainData = database.getDragonTable().getDragonCollection();
            this.users = database.getUsersTable().getUserCollection();
            logger.info("Made a data manager with initialised collections:\n"
                    + mainData + "\n\n" + users);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countGreater(Integer arg) {

        Lock readLock = dragonCollectionLock.readLock();
        try {
            readLock.lock();
            int count = 0;
            for (Dragon currentDragon : mainData) {
                if (currentDragon.getAge() > arg) {
                    count++;
                }
            }
            return count;
        } finally {
            readLock.unlock();
        }
    }
    @Override
    public void removeGreaterIfOwned(Dragon dragon, String username) {
        Lock writeLock = dragonCollectionLock.writeLock();
        try {
            writeLock.lock();
            final Set<Long> idsToRemove = new HashSet<>();
            ListIterator<Dragon> iterator = mainData.listIterator();
            while (iterator.hasNext()) {
                Dragon currentDragon = iterator.next();
                if (currentDragon.getAuthorName().equals(username) && currentDragon.compareTo(dragon) > 0) {
                    idsToRemove.add(currentDragon.getId());
                    iterator.remove();
                    logger.info("Successfully remove");
                }
            }
            for (long id : idsToRemove) {
                database.getDragonTable().removeById(id);
            }
        } catch (SQLException e) {
            logger.info("Problem with remove elements");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }
    @Override
    public void removeAllByWeight(Double arg, String username) {
        Lock writeLock = dragonCollectionLock.writeLock();
        try {
            writeLock.lock();
            mainData.removeIf(dragon -> dragon.getAuthorName().equals(username) || dragon.getWeight() == arg);
            database.getDragonTable().removeByWeight(arg, username);
            return;
        } catch (SQLException e) {
            logger.info("Problem with removing elements by weight");
        } finally {
            writeLock.unlock();
        }
    }


}