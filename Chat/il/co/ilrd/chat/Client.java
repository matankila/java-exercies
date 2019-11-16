package il.co.ilrd.chat;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    private SocketChannel client;
    private ByteBuffer readByteBuffer = ByteBuffer.allocate(256);
    private volatile boolean running = true;

    /*************************************** API methods *************************************************/
    public void send(String msg) {
        Objects.requireNonNull(msg, "send, cannot get null message");
        String tokenizedMsg = client.socket().getLocalPort() + ": " + msg.trim();
        try {
            client.write(ByteBuffer.wrap(tokenizedMsg.getBytes()));
        }
        catch (IOException e) {
            e.getCause();
        }
    }

    public void register(String ip, int port) throws IOException {
        client = SocketChannel.open(new InetSocketAddress(ip, port));
        readRequests();
    }

    public void closeConnection() {
        running = false;
        readByteBuffer = null;

        try {
            client.close();
        }
        catch (IOException e) {
            e.getCause();
        }
    }

    /*************************************** Non API methods *************************************************/
    private void readRequests() {
        Thread t = new Thread(()-> {
            while (running) {
                try {
                    if (-1 == client.read(readByteBuffer)) {
                        System.out.println("server out");
                        closeConnection();

                        return;
                    }

                    byte[] bytes;
                    if (readByteBuffer.hasArray()) {
                        bytes = readByteBuffer.array();
                    }
                    else {
                        bytes = new byte[readByteBuffer.remaining()];
                        readByteBuffer.get(bytes);
                    }

                    System.out.println(new String(bytes));
                    readByteBuffer = ByteBuffer.allocate(256);
                }
                catch (IOException e) {
                    e.getCause();
                }
            }
        });

        t.start();
    }

    public static void main(String[] args) {
        Client c = new Client();
        try {
            c.register("127.0.0.1", 55555);
        }
        catch (IOException e) {
            e.getCause();
        }

        while (true) {
            String str = new Scanner(System.in).nextLine();
            switch (str) {
                case "exit": {
                    c.closeConnection();
                    return;
                }
                default: {
                    c.send(str);
                    break;
                }
            }
        }
    }
}