package connection;

import dataTransmission.CommandFromClient;

import org.apache.logging.log4j.core.Logger;

import util.Pair;
import util.State;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

public class ClientDataReceiver {
    private static final int HEADER_LENGTH = 4;
    private static final int TIMEOUT_MILLS = 10;
    private final Queue<Pair<CommandFromClient, SocketAddress>> queueToBeExecuted;
    private final Logger logger;

    public ClientDataReceiver(Logger logger, Queue<Pair<CommandFromClient, SocketAddress>> queueToBeExecuted) {
        this.logger = logger;
        this.queueToBeExecuted = queueToBeExecuted;
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

    public void startReceivingData(DatagramChannel datagramChannel, State<Boolean> isWorking, ExecutorService threadPool) throws IOException, InterruptedException {
        while (isWorking.getValue()) {
            ByteBuffer amountOfBytesInMainDataBuffer = ByteBuffer.wrap(new byte[HEADER_LENGTH]);
            receiveActiveWaiting(datagramChannel, amountOfBytesInMainDataBuffer, isWorking);
            if (isWorking.getValue()) {
                ByteBuffer dataByteBuffer = ByteBuffer.wrap(new byte[bytesToInt(amountOfBytesInMainDataBuffer.array())]);
                SocketAddress clientSocketAddress = null;
                try {
                    clientSocketAddress = receiveWithTimeout(datagramChannel, dataByteBuffer, TIMEOUT_MILLS);
                } catch (TimeoutException e) {
                    logger.error("Could not receive correct information from client");
                }
                CommandFromClient receivedCommand = null;
                try {
                    receivedCommand = ((CommandFromClient) deserialize(dataByteBuffer.array()));
                    Pair<CommandFromClient, SocketAddress> pairToBeExecuted = new Pair<>(receivedCommand, clientSocketAddress);
                    queueToBeExecuted.add(pairToBeExecuted);

                    logger.info("Received a full request from a client, added it to an executing queue:\n" + pairToBeExecuted);
                } catch (ClassNotFoundException e) {
                    logger.error("Found incorrect data from client. Ignoring it");
                }

            }
        }
    }

    private SocketAddress receiveWithTimeout(
            DatagramChannel datagramChannel,
            ByteBuffer
                    byteBuffer,
            int timeoutMills
    ) throws IOException, InterruptedException, TimeoutException {
        int amountToWait = timeoutMills;
        SocketAddress receivedSocketAddress = null;
        while (amountToWait > 0) {
            receivedSocketAddress = datagramChannel.receive(byteBuffer);
            if (Objects.nonNull(receivedSocketAddress)) {
                logger.info("Received a new client request 2/2");
                return receivedSocketAddress;
            } else {
                Thread.sleep(1);
                amountToWait--;
            }
        }
        throw new TimeoutException();
    }

    private SocketAddress receiveActiveWaiting(
            DatagramChannel datagramChannel,
            ByteBuffer byteBuffer,
            State<Boolean> isWorking
    ) throws IOException {
        while (isWorking.getValue()) {
            SocketAddress receivedSocketAddress = datagramChannel.receive(byteBuffer);
            if (Objects.nonNull(receivedSocketAddress)) {
                logger.info("Received a new client request 1/2");
                return receivedSocketAddress;
            }
        }
        return null;
    }
}