package edu.pja.kasia;

import java.net.*;
import java.io.*;

public class Client
{
    private Socket socket            = null;
    private DataInputStream  input   = null;
    private DataOutputStream out     = null;

    private static Client instance = null;
    public static Client getInstance() {
        if(instance == null) {
            instance = new Client("127.0.0.1", 5000);
        }
        return instance;
    }

    public Client(String address, int port)
    {
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");

            input  = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out    = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public void closeConnection () {
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public String getHighScores () {
        try {
            out.writeUTF("scores");

            String line = "";
            while (!line.equals("Over"))
            {
                try
                {
                    line = input.readUTF();
                    return line;
                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void sendScore (String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}