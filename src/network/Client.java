/*
 * Copyright (C) 2016 Robert Maloy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, under version 2 of the license
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package network;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 *
 * @author Robert Maloy
 */
public class Client {
    
    static Socket s;
    static GUI myGUI = null;
    static String host = "fa16-cop3330.hpc.lab";
    static int port = 2016;
    
    static ObjectInputStream oIS;
    static ObjectOutputStream oOS;
    
    static List<String> buffer = new ArrayList<String>();
    
    public static void writeChars(String x) {
        try {
            oOS.writeObject(x);
        } catch (IOException ignored) {
            myGUI.getConsoleField().append("ERROR: " + ignored.getMessage());
            ignored.printStackTrace(System.err);
        }
    }
    
    public static void createColorFrame(Color c) {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                
                @Override public void run() {
                    JFrame frm = new JFrame("Color Window");
                    frm.setSize(320, 240);
                    frm.setLocationRelativeTo(null);
                    frm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    frm.getContentPane().setBackground(c);
                    frm.setVisible(true);
                }
                
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String args[])
        throws  InterruptedException, ClassNotFoundException, InvocationTargetException {
        
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override public void run() {
                myGUI = new GUI();
                myGUI.setVisible(true);
            }

        });
        
        try {
            SocketChannel sChannel = SocketChannel.open();
            sChannel.configureBlocking(true);
            myGUI.getConsoleField().append("Attempting to connect to " + host + "\n");
            if (sChannel.connect(new InetSocketAddress("fa16-cop3330.hpc.lab", 2016))) {
                myGUI.getConsoleField().append("Success!\n");
                s = sChannel.socket();
                oOS = new ObjectOutputStream(s.getOutputStream());
                oIS = new ObjectInputStream(s.getInputStream());
                
                writeChars("CLIENT>>> What is your favorite color?");
                
                Object x;
                while ( (x = oIS.readObject()) != null ) {
                    myGUI.getConsoleField().append((String) x + "\n");
                    System.out.println("got obj: " + x);
                    if(x instanceof String) {
                        String result = (String) ((String) x).toLowerCase();
                        Color c = null;
                        if(result.contains("red")) createColorFrame(Color.RED);
                        if(result.contains("green")) createColorFrame(Color.GREEN);
                        if(result.contains("blue")) createColorFrame(Color.BLUE);
                        if(result.contains("yellow")) createColorFrame(Color.yellow);
                        if(result.contains("purple")) createColorFrame(new Color(128,0,128));
                        if (c != null) createColorFrame(c);
                    }
                    
                }
                System.out.println("Got null...");
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}
