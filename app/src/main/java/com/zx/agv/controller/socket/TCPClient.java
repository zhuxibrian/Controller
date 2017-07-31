package com.zx.agv.controller.socket;


import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


/**
 * NIO TCP 客户端
 *
 */
public class TCPClient
{
    // 信道选择器
    private Selector selector;

    // 与服务器通信的信道
    SocketChannel socketChannel;

    // 要连接的服务器Ip地址
    private String hostIp;

    // 要连接的远程服务器在监听的端口
    private int hostListenningPort;

    private static TCPClient s_Tcp = null;

    public boolean isInitialized = false;

    public static synchronized TCPClient instance()
    {
        if (s_Tcp == null)
        {

            s_Tcp = new TCPClient(Const.SOCKET_SERVER,
                    Const.SOCKET_PORT);
        }
        return s_Tcp;
    }

    /**
     * 构造函数
     *
     * @param HostIp
     * @param HostListenningPort
     * @throws IOException
     */
    public TCPClient(String HostIp, int HostListenningPort)
    {
        this.hostIp = HostIp;
        this.hostListenningPort = HostListenningPort;

        try
        {
            initialize();
            this.isInitialized = true;
        } catch (IOException e)
        {
            this.isInitialized = false;
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e)
        {
            this.isInitialized = false;
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     *
     * @throws IOException
     */
    public void initialize() throws IOException
    {
        boolean done = false;

        try
        {
            // 打开监听信道并设置为非阻塞模式
            socketChannel = SocketChannel.open(new InetSocketAddress(hostIp,hostListenningPort));
            if (socketChannel != null)
            {
                socketChannel.socket().setTcpNoDelay(false);
                socketChannel.socket().setKeepAlive(true);
                // 设置 读socket的timeout时间
                socketChannel.socket().setSoTimeout(
                        Const.SOCKET_READ_TIMOUT);
                socketChannel.configureBlocking(false);

                // 打开并注册选择器到信道
                selector = Selector.open();
                if (selector != null)
                {
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    done = true;
                }
            }
        } finally
        {
            if (!done && selector != null)
            {
                selector.close();
            }
            if (!done)
            {
                socketChannel.close();
            }
        }
    }

    static void blockUntil(SelectionKey key, long timeout) throws IOException
    {

        int nkeys = 0;
        if (timeout > 0)
        {
            nkeys = key.selector().select(timeout);

        } else if (timeout == 0)
        {
            nkeys = key.selector().selectNow();
        }

        if (nkeys == 0)
        {
            throw new SocketTimeoutException();
        }
    }

    /**
     * 发送字符串到服务器
     *
     * @param message
     * @throws IOException
     */
    public void sendMsg(String message) throws IOException
    {
        ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("utf-8"));

        if (socketChannel == null)
        {
            throw new IOException();
        }
        socketChannel.write(writeBuffer);
    }

    /**
     * 发送数据
     *
     * @param bytes
     * @throws IOException
     */
    public void sendMsg(byte[] bytes) throws IOException
    {
        ByteBuffer writeBuffer = ByteBuffer.wrap(bytes);

        if (socketChannel == null)
        {
            throw new IOException();
        }
        socketChannel.write(writeBuffer);
    }

    /**
     *
     * @return
     */
    public synchronized Selector getSelector()
    {
        return this.selector;
    }

    /**
     * Socket连接是否是正常的
     *
     * @return
     */
    public boolean isConnect()
    {
        boolean isConnect = false;
        if (this.isInitialized)
        {
            isConnect =  this.socketChannel.isConnected();
        }
        return isConnect;
    }

    /**
     * 关闭socket 重新连接
     *
     * @return
     */
    public boolean reConnect()
    {
        closeTCPSocket();

        try
        {
            initialize();
            isInitialized = true;
        } catch (IOException e)
        {
            isInitialized = false;
            e.printStackTrace();
        }
        catch (Exception e)
        {
            isInitialized = false;
            e.printStackTrace();
        }
        return isInitialized;
    }

    /**
     * 服务器是否关闭，通过发送一个socket信息
     *
     * @return
     */
    public boolean canConnectToServer()
    {
        try
        {
            if (socketChannel != null)
            {
                //socketChannel.socket().sendUrgentData(0xff);
                byte[] xintiao = {(byte)0xff};
                sendMsg(xintiao);
            }
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 关闭socket
     */
    public void closeTCPSocket()
    {
        try
        {
            if (socketChannel != null)
            {
                socketChannel.close();
            }

            if (s_Tcp != null) {
                s_Tcp = null;
            }

        } catch (IOException e)
        {

        }
        try
        {
            if (selector != null)
            {
                selector.close();
            }
        } catch (IOException e)
        {
        }
    }

    /**
     * 每次读完数据后，需要重新注册selector，读取数据
     */
    public synchronized void repareRead()
    {
        if (socketChannel != null)
        {
            try
            {
                selector = Selector.open();
                socketChannel.register(selector, SelectionKey.OP_READ);
            } catch (ClosedChannelException e)
            {
                e.printStackTrace();

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}


//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.net.SocketTimeoutException;
//import java.nio.ByteBuffer;
//import java.nio.channels.ClosedChannelException;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.SocketChannel;
//
///**
// * Created by zx on 2017/6/18.
// */
//
//public class TCPClient {
//    // 信道选择器
//    private Selector selector;
//
//    // 与服务器通信的信道
//    SocketChannel socketChannel;
//
//    // 要连接的服务器Ip地址
//    private String hostIp;
//
//    // 要连接的远程服务器在监听的端口
//    private int hostListenningPort;
//
//    private static TCPClient s_Tcp = null;
//
//    public boolean isInitialized = false;
//
//    private Thread conn=null;
//
//    public static synchronized TCPClient instance()
//    {
//        if (s_Tcp == null)
//        {
//
//            s_Tcp = new TCPClient(Const.SOCKET_SERVER,
//                    Const.SOCKET_PORT);
//        }
//        return s_Tcp;
//    }
//
//    /**
//     * 构造函数
//     *
//     * @param HostIp
//     * @param HostListenningPort
//     * @throws IOException
//     */
//    public TCPClient(String HostIp, int HostListenningPort)
//    {
//        this.hostIp = HostIp;
//        this.hostListenningPort = HostListenningPort;
//
//        try
//        {
//            conn=new Thread(new initialize());
//            conn.start();
//            this.isInitialized = true;
//        } catch (Exception e)
//        {
//            this.isInitialized = false;
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 初始化
//     *
//     * @throws IOException
//     */
//    private class initialize implements Runnable
//    {
//        @Override
//        public void run() {
//            boolean done = false;
//
//            try
//            {
//                // 打开监听信道并设置为非阻塞模式
//                socketChannel = SocketChannel.open(new InetSocketAddress(hostIp, hostListenningPort));
//                if (socketChannel != null)
//                {
//                    socketChannel.socket().setTcpNoDelay(false);
//                    socketChannel.socket().setKeepAlive(true);
//                    // 设置 读socket的timeout时间
//                    socketChannel.socket().setSoTimeout(
//                            Const.SOCKET_READ_TIMOUT);
//                    socketChannel.configureBlocking(false);
//
//                    // 打开并注册选择器到信道
//                    selector = Selector.open();
//                    if (selector != null)
//                    {
//                        socketChannel.register(selector, SelectionKey.OP_READ);
//                        done = true;
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }finally
//            {
//                if (!done && selector != null)
//                {
//                    try {
//                        selector.close();
//                    } catch (IOException e) {
//                        isInitialized = false;
//                        e.printStackTrace();
//                    }
//                }
//                if (!done)
//                {
//                    try {
//                        socketChannel.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//    static void blockUntil(SelectionKey key, long timeout) throws IOException
//    {
//
//        int nkeys = 0;
//        if (timeout > 0)
//        {
//            nkeys = key.selector().select(timeout);
//
//        } else if (timeout == 0)
//        {
//            nkeys = key.selector().selectNow();
//        }
//
//        if (nkeys == 0)
//        {
//            throw new SocketTimeoutException();
//        }
//    }
//
//    /**
//     * 发送字符串到服务器
//     *
//     * @param message
//     * @throws IOException
//     */
//    public void sendMsg(String message) throws IOException
//    {
//        ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("utf-8"));
//
//        if (socketChannel == null)
//        {
//            throw new IOException();
//        }
//        socketChannel.write(writeBuffer);
//    }
//
//    /**
//     * 发送数据
//     *
//     * @param bytes
//     * @throws IOException
//     */
//    public void sendMsg(byte[] bytes) throws IOException
//    {
//        ByteBuffer writeBuffer = ByteBuffer.wrap(bytes);
//
//        if (socketChannel == null)
//        {
//            throw new IOException();
//        }
//        socketChannel.write(writeBuffer);
//    }
//
//    /**
//     *
//     * @return
//     */
//    public synchronized Selector getSelector()
//    {
//        return this.selector;
//    }
//
//    /**
//     * Socket连接是否是正常的
//     *
//     * @return
//     */
//    public boolean isConnect()
//    {
//        boolean isConnect = false;
//        if (this.isInitialized)
//        {
//            isConnect =  this.socketChannel.isConnected();
//        }
//        return isConnect;
//    }
//
//    /**
//     * 关闭socket 重新连接
//     *
//     * @return
//     */
//    public boolean reConnect()
//    {
//        closeTCPSocket();
//
//        try
//        {
//            conn=new Thread(new initialize());
//            conn.start();
//            isInitialized = true;
//        } catch (Exception e)
//        {
//            isInitialized = false;
//            e.printStackTrace();
//        }
//        return isInitialized;
//    }
//
//    /**
//     * 服务器是否关闭，通过发送一个socket信息
//     *
//     * @return
//     */
//    public boolean canConnectToServer()
//    {
//        try
//        {
//            if (socketChannel != null)
//            {
//                socketChannel.socket().sendUrgentData(0xff);
//            }
//        } catch (IOException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return false;
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 关闭socket
//     */
//    public void closeTCPSocket()
//    {
//        try
//        {
//            if (socketChannel != null)
//            {
//                socketChannel.close();
//            }
//
//        } catch (IOException e)
//        {
//
//        }
//        try
//        {
//            if (selector != null)
//            {
//                selector.close();
//            }
//        } catch (IOException e)
//        {
//        }
//    }
//
//    /**
//     * 每次读完数据后，需要重新注册selector，读取数据
//     */
//    public synchronized void repareRead()
//    {
//        if (socketChannel != null)
//        {
//            try
//            {
//                selector = Selector.open();
//                socketChannel.register(selector, SelectionKey.OP_READ);
//            } catch (ClosedChannelException e)
//            {
//                e.printStackTrace();
//
//            } catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//}
