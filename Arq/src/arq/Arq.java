/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arq;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author sebas
 */
public class Arq {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {;
        boolean flag = true;
        Utilities U = new Utilities();
        while(flag)
        {
            ASM asm = new ASM();
            File fileParse = null;
            JFrame parentFrame = new JFrame();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Assembler Files", "asm");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file");   
            fileChooser.setFileFilter(filter);
            int userSelection = fileChooser.showSaveDialog(parentFrame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                fileParse = fileChooser.getSelectedFile();
                System.out.println("Save as file: " + fileParse.getAbsolutePath());}
            asm.Read(fileParse);
            flag = U.stay();
        }
        System.exit(0);
    }
}
