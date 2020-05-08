package edu.pja.kasia;
import java.net.*;
import java.io.*;

public class Server implements Runnable
{
    private Socket           socket   = null;
    private ServerSocket     server   = null;
    private DataInputStream  in       =  null;
    private DataOutputStream out       =  null;
    private int port;

    public Server(int port)
    {
        this.port = port;
    }

    @Override
    public void run() {
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());// bufor danych do dalszego wyslania

            String line = "";
            while (!line.equals("Over"))
            {
                try
                {
                    line = in.readUTF();
                    if (line.equals("scores")) {
                        sendMessage(Database.getInstance().getJsonScores());//czyta z bazy
                    } else {
                        Database.getInstance().saveToDatabase(line);//zapisuje do bazy
                    }
                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection");

            // close connection
            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public void sendMessage (String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}