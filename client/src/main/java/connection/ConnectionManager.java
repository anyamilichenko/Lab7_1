package connection;

import dataTransmission.CommandFromClient;
import dataTransmission.CommandResult;
import exceptions.DataCantBeSentException;
import util.OutputManager;
import util.Pair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.UnresolvedAddressException;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("FieldCanBeLocal")
public final class ConnectionManager {
    private final int clientPort;
    private final int serverPort;
    private final String clientIp;
    private final String serverIp;

    private final OutputManager outputManager;
    private final int waitingTime = 2000;
    private final int countOfBytesForSize = 4;
    private final int timeoutToSend = 10;

    public ConnectionManager(int clientPort, int serverPort, String clientIp, String serverIp, OutputManager outputManager) {
        this.clientPort = clientPort;
        this.serverPort = serverPort;
        this.clientIp = clientIp;
        this.serverIp = serverIp;
        this.outputManager = outputManager;
    }

    public CommandResult sendCommand(CommandFromClient commandFromClient) throws DataCantBeSentException, UnresolvedAddressException {
        try (DatagramChannel datagramChannel = DatagramChannel.open()) {
            datagramChannel.configureBlocking(false); // нужно, чтобы в случае, если от сервера не придет никакого ответа не блокироваться навсегда
            System.out.println(commandFromClient.getCommand().toString());
            send(datagramChannel, commandFromClient);
            return receiveCommandResult(datagramChannel);
        } catch (BindException e) {
            return new CommandResult("Could not send data on the Inet address, bind exception. Please re-start client with another arguments", false);
        } catch (IOException e) {
            return new CommandResult("Something went wrong executing the command, the message is: " + e.getMessage(), false);
        }

    }

    private void send(DatagramChannel datagramChannel, CommandFromClient commandFromClient) throws IOException, DataCantBeSentException, UnresolvedAddressException {
        datagramChannel.bind(new InetSocketAddress(clientIp, clientPort));
        SocketAddress serverSocketAddress = new InetSocketAddress(serverIp, serverPort);
        Pair<byte[], byte[]> pair = serialize(commandFromClient);
        byte[] sendDataBytes = pair.getFirst();
        byte[] sendDataAmountBytes = pair.getSecond();
        try {
            ByteBuffer sendDataAmountWrapper = ByteBuffer.wrap(sendDataAmountBytes);
            int limit = timeoutToSend;
            while (datagramChannel.send(sendDataAmountWrapper, serverSocketAddress) < sendDataAmountBytes.length) {
                limit -= 1;
                outputManager.println("Could not send a package, re-trying");
                if (limit == 0) {
                    throw new DataCantBeSentException();
                }
            } // сначала отправляем число-количество байтов в основном массиве байтов
            ByteBuffer sendBuffer = ByteBuffer.wrap(sendDataBytes);
            while (datagramChannel.send(sendBuffer, serverSocketAddress) < sendDataBytes.length) {
                limit -= 1;
                outputManager.println("Could not send a package, re-trying");
                if (limit == 0) {
                    throw new DataCantBeSentException();
                }
            }
        } catch (IOException e) {
            outputManager.println("Could not resolve the Inet address you wrote. Please check it and maybe restart the client");
        }


    }

    private CommandResult receiveCommandResult(DatagramChannel datagramChannel) throws IOException {
        byte[] amountOfBytesHeader = new byte[countOfBytesForSize];
        ByteBuffer amountOfBytesHeaderWrapper = ByteBuffer.wrap(amountOfBytesHeader);
        try {
            receiveToBuffer(datagramChannel, amountOfBytesHeaderWrapper, waitingTime);
            byte[] dataBytes = new byte[bytesToInt(amountOfBytesHeader)];


            ByteBuffer dataBytesWrapper = ByteBuffer.wrap(dataBytes);

            receiveToBuffer(datagramChannel, dataBytesWrapper, 10);

            return (CommandResult) deserialize(dataBytes);

        } catch (TimeoutException e) {
            e.printStackTrace();
            return new CommandResult("Could not receive any answer from server", false);
        } catch (ClassNotFoundException e) {
            return new CommandResult("Received incorrect answer from server", false);
        }
    }

    private void receiveToBuffer(DatagramChannel datagramChannel, ByteBuffer receiverBuffer, int timeoutMills) throws TimeoutException, IOException {
        int timeout = timeoutMills;
        SocketAddress checkingAddress = null;

        while (checkingAddress == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace(); // never thrown
            }
            checkingAddress = datagramChannel.receive(receiverBuffer);
            if (timeout == 0) {
                throw new TimeoutException("Timeout exceeded. Could not receive any data.");
            }
            timeout--;
        }
    }

    /**
     * @return first - data itself, second - amount of bytes in data
     */
    private Pair<byte[], byte[]> serialize(Object obj) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(obj);
        byte[] sizeBytes = ByteBuffer.allocate(countOfBytesForSize).putInt(byteArrayOutputStream.size()).array();

        return new Pair<>(byteArrayOutputStream.toByteArray(), sizeBytes); // в первых 4 байтах будет храниться число-количество данных отправления
    }

    public static int bytesToInt(byte[] bytes) {
        final int vosem = 8;
        final int ff = 0xFF;

        int value = 0;
        for (byte b : bytes) {
            value = (value << vosem) + (b & ff);
        }
        return value;
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
