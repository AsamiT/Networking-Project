
package network;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.HashMap;

/**
 * @author: Robert Maloy
 */
public class GUI
    extends JFrame {

    Socket s;
    private JTextField input = new JTextField();
    private JTextArea log = new JTextArea(5, 1);

    GUI() {
        super("Color Protocol Client");
        setLayout(new BorderLayout());
        setSize(320, 240);
        setLocation( 10, 10 );
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(log, BorderLayout.CENTER);
        log.setEditable(false);
        add(input, BorderLayout.SOUTH);
        input.addActionListener(new ActionListener() {
        
            @Override public void actionPerformed(ActionEvent aEvt) {
                Client.writeChars("CLIENT>>> " + input.getText().trim());
                input.setText("");
            }
        
        });
    }
    
    public JTextField getInputBar() { return input; }
    public JTextArea getConsoleField() { return log; }
}