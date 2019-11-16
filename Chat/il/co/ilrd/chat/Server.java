package il.co.ilrd.chat;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Server {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(256);

    /***************************************** CTOR ************************************************/
    public Server(String ip, int port) throws IOException, Exception {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(ip, port));
            serverSocketChannel.configureBlocking(false);
            SelectionKey selectKy = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            serverInit();

        }
        catch (IOException e) {
            e.getCause();
        }
    }

    private void serverInit() throws IOException, Exception{
        while (true) {
            int noOfKeys = selector.select();
            Set<SelectionKey> keySet = selector.selectedKeys();
            Iterator<SelectionKey> iter = keySet.iterator();

            if (noOfKeys == 0) { continue; }

            while (iter.hasNext()) {
                SelectionKey currentKey = (SelectionKey) iter.next();
                if (currentKey.isAcceptable()) {
                    SocketChannel clientSocket = serverSocketChannel.accept();
                    clientSocket.configureBlocking(false);
                    clientSocket.register(selector, SelectionKey.OP_WRITE);
                }
                else if (currentKey.isWritable() || currentKey.isReadable()) {
                    ((SocketChannel) currentKey.channel()).read(buffer);
                    buffer.flip();
                    for (SelectionKey key : selector.keys()) {
                        if (key.isValid() && key.channel() instanceof SocketChannel && currentKey != key) {
                            SocketChannel sc = ((SocketChannel) (key.channel()));
                            if (!sc.isConnected()) {
                                System.out.println(sc.socket().getLocalPort() + "left chat");
                                key.cancel();
                            }
                            else { sc.write(buffer); }
                        }
                    }
                }

                buffer = ByteBuffer.allocate(256);
                iter.remove();
            }

            keySet.clear();
        }
    }

    public static void main(String[] args) {
        try {
            Server s = new Server("127.0.0.1", 55555);
        }
        catch (IOException e) {
            e.getCause();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
