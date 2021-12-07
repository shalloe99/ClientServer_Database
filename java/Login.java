
// Example code, from https://beginnersbook.com/2015/07/java-swing-tutorial/
//
import javax.swing.*;
import java.awt.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.GridBagConstraints;

import java.sql.*;
import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Choe: more elaborate swing example. Again, no event handling included.

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField; 

// implmentation of the log in page. 
public class Login implements ActionListener{
    public JButton loginButton = new JButton("login");
    public JPasswordField passwordText = new JPasswordField(20);
    public JTextField userText = new JTextField(20);
    public JDBC jdbc;
    JFrame frame;
// constructor, set the size of the login panel and also place the different components to the panel
    public Login () 
    {
    // Creating instance of JFrame
        frame = new JFrame("My First Swing Example");
        // Setting the width and height of frame
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Creating panel. This is same as a div tag in HTML
         * We can create several panels and add them to specific 
         * positions in a JFrame. Inside panels we can add text 
         * fields, buttons and other components.
         */
        JPanel panel = new JPanel();    

        // adding panel to frame
        frame.add(panel);
        /* calling user defined method for adding components
         * to the panel.
         */
        placeComponents(panel);
        loginButton.addActionListener(this);

        // Setting the frame visibility to true
        frame.setVisible(true);
    }// end of login
// main, create an login varible. 
    public static void main(String[] args) {    

        Login lg = new Login();
    }//end of main

    /**
   * if an action performed to the button
   *
   * @param  evt  the event that happened (rather a click or a input)
   */
    public void actionPerformed(java.awt.event.ActionEvent evt){
        Object src = evt.getSource();
      if(src == loginButton){

        try{
            jdbc = new JDBC();
        jdbc.Connect();
        } catch (SQLException e)
        {
            // do something appropriate with the exception, *at least*:
            e.printStackTrace();
        }
        
        if(userText.getText().trim().equals(jdbc.USER) && passwordText.getText().trim().equals(jdbc.PASS))
        {
            try{
                 Gui temp = new Gui();
                 frame.setVisible(false);
            } catch (SQLException e)
            {
                // do something appropriate with the exception, *at least*:
                e.printStackTrace();
            }
           
        } else {
            JOptionPane.showMessageDialog(null,"Invalid username or password","Error!",JOptionPane.INFORMATION_MESSAGE);
        }

      }

    }// end of actionPerformed 

   /**
   * the setting of the components in the login page.
   *
   * @param  panel  the panel that 
   */
    public void placeComponents(JPanel panel) {

        /* We will discuss about layouts in the later sections
         * of this tutorial. For now we are setting the layout 
         * to null
         */
        panel.setLayout(null);

        // Creating JLabel
        JLabel userLabel = new JLabel("User");
        /* This method specifies the location and size
         * of component. setBounds(x, y, width, height)
         * here (x,y) are cordinates from the top left 
         * corner and remaining two arguments are the width
         * and height of the component.
         */
        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel);

        /* Creating text field where user is supposed to
         * enter user name.
         */
        
        userText.setBounds(100,20,165,25);
        panel.add(userText);

        // Same process for password label and text field.
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10,50,80,25);
        panel.add(passwordLabel);

        /*This is similar to text field but it hides the user
         * entered data and displays dots instead to protect
         * the password like we normally see on login screens.
         */
        
        passwordText.setBounds(100,50,165,25);
        panel.add(passwordText);

        // Creating login button
        
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);
    }// end of the placeComponents

}
