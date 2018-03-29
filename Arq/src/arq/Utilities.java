/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arq;

import javax.swing.JOptionPane;

/**
 *
 * @author sebas
 */
public class Utilities {
    public boolean checkINT(String posint)
    {
        try {
            int op = Integer.parseInt(posint);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public String intToBinary (int n) {
        if (n == 0) {
           return "0000000000000000";
       }
       String binary = "";
       while (n > 0) {
           int rem = n % 2;
           binary = rem + binary;
           n = n / 2;
       }
       while(binary.length()<16)
       {
           binary = "0"+binary;
       }
       return binary;
    }
    public boolean stay()
    {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to continue?","",dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
            return true;
        }
        return false;
    }
}