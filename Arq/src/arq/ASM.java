/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arq;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;

/**
 *
 * @author sebas
 */
public class ASM {
    Utilities U = new Utilities();
    ArrayList<String> MainList = new ArrayList<String>();
    Dictionary<String, String> SymbolTable = new Hashtable<String, String>(); 
    int Tablecount = 16;
    public void Read(File originalFile) throws IOException
    {
        Scanner scanner = new Scanner(originalFile);
        String str = null;
        while (scanner.hasNext())
        {         
            str = scanner.nextLine();
            if ( !"".equals(str))
                MainList.add(str);
        }
        scanner.close();
        CreateTable();
        firstscan(MainList);
        SearchLabel();
        SecondScan(MainList);
        createHACK(originalFile.getName(),originalFile.getPath());
    }
    public void CreateTable()
    {
        SymbolTable.put("SP", "0000000000000000");//SP 0, LCL 1, ARG 2, THIS 3, THAT 4
        SymbolTable.put("LCL","0000000000000001");
        SymbolTable.put("ARG", "0000000000000010");
        SymbolTable.put("THIS", "0000000000000011" );
        SymbolTable.put("THAT", "0000000000000100");
        SymbolTable.put("R0", "0000000000000000"); //R0 0, R1 1, R2 2, R3 3...
        SymbolTable.put("R1", "0000000000000001");
        SymbolTable.put("R2", "0000000000000010");
        SymbolTable.put("R3", "0000000000000011");
        SymbolTable.put("R4", "0000000000000100");
        SymbolTable.put("R5", "0000000000000101");
        SymbolTable.put("R6", "0000000000000110");
        SymbolTable.put("R7", "0000000000000111");
        SymbolTable.put("R8", "0000000000001000");
        SymbolTable.put("R9", "0000000000001001");
        SymbolTable.put("R10", "0000000000001010");
        SymbolTable.put("R11", "0000000000001011");
        SymbolTable.put("R12", "0000000000001100");
        SymbolTable.put("R13", "0000000000001101");
        SymbolTable.put("R14", "0000000000001110");
        SymbolTable.put("R15", "0000000000001111");
        SymbolTable.put("SCREEN", "0100000000000000"); //SCREEN 0x4000, KBD 0x600
        SymbolTable.put("KBD", "0110000000000000");
        
    }
    public  void firstscan(ArrayList<String> original){ //delete comments & blank space
        MainList = new ArrayList<String>();
        for (int i = 0; i < original.size(); i++) {
            if(original.get(i).contains("//")) {
                String[] parts = original.get(i).split("/");
                if ( !"".equals(parts[0]))
                MainList.add(parts[0]);}
            else{
                MainList.add(original.get(i));}
        }  
    }
    public void cleanblacnk (ArrayList list)
    {   MainList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            if ( !"".equals(list.get(i))){
                MainList.add((String)list.get(i));
            }
        }
    }
    public void SearchLabel()
    {
        int temp = -1;
        for (int i = 0; i < MainList.size(); i++)
        {  temp++;
            if(MainList.get(i).contains("("))
            {
               SymbolTable.put(MainList.get(i).replaceAll("[()]",""),String.valueOf(U.intToBinary(temp)));
               //MainList.remove(i);
               MainList.set(i,"");
               temp--;
            }
        }   
        cleanblacnk(MainList);
        for (int i = 0; i < MainList.size(); i++) {
            if(MainList.get(i).contains("@"))
            {
                String value = MainList.get(i).replaceAll("[@]","").trim();
                if(!U.checkINT(value))
                {
                    String val = SymbolTable.get(value);
                    if(val == null)
                    {
                        SymbolTable.put(value, String.valueOf(U.intToBinary(Tablecount)));
                        Tablecount++;
                    }
                }
            }
        }
    }
    public void SecondScan(ArrayList<String> original)
    {
        MainList = new ArrayList<String>();
        for (int i = 0; i < original.size(); i++) {
            if(original.get(i).contains("@"))
            {
                String value = original.get(i).replaceAll("[@]","").trim();
                String val = SymbolTable.get(value);
                if(val != null)
                {
                     MainList.add(val);
                }
                else //if(U.checkINT(value))
                {
                   value = U.intToBinary(Integer.valueOf(value));
                   MainList.add(value);
                } 
            }
            else
            {
                MainList.add(CInstructions(original.get(i)));
            }
        }   
    }
    public String CInstructions(String instruction)
    {
       String result ="111";
       String comp ="0000000";
       String dest ="000";
       String jump = "000";
       if(ContainsJumps(instruction.trim()))
       {
           String[] parts = instruction.split(";");
           switch(parts[1].trim())
           {
               case "JGT": jump = "001";
               break;
               case "JEQ": jump = "010";
               break;
               case "JGE": jump="011";
               break;
               case "JLT": jump="100";
               break;
               case "JNE": jump = "101";
               break;
               case "JLE": jump = "110";
               break;
               case "JMP": jump = "111";
               break;
               default:jump ="000";
               break;
           }
           comp = getComp(parts[0]);
           
       } else {
           String[] parts = instruction.split("=");
             try {
            comp = getComp(parts[1]);
            } catch (IndexOutOfBoundsException e) {
                comp = "0000000";
            }
           //comp = getComp(parts[1]);
           switch(parts[0].trim())
           {
               case"M":dest = "001";
               break;
               case"D":dest ="010";
               break;
               case"MD":dest="011";
               break;
               case"A":dest="100";
               break;
               case"AM":dest="101";
               break;
               case"AD":dest="110";
               break;
               case"AMD":dest="111";
               break;
               default: dest ="000";
               break;
           }
       }
       return result + comp + dest + jump;
      /*
        _dest_codes = ['', 'M', 'D', 'MD', 'A', 'AM', 'AD', 'AMD']
        */
    }
    public boolean ContainsJumps(String inputStr) {
        String[] items = {"JGT", "JEQ", "JGE", "JLT", "JNE", "JLE", "JMP"};
        return Arrays.stream(items).parallel().anyMatch(inputStr::contains);
    }
    public String getComp(String parts)
    {
        switch(parts.trim())
           {
               case "0": return "0101010";
               case"1": return  "0111111";
               case "-1": return"0111010";
               case "D": return "0001100";
               case "A": return "0110000";
               case "!D": return"0001101";
               case"!A": return "0110001";
               case "-D":return "0001111";
               case"-A": return "0110011";
               case"D+1": return"0011111";
               case"A+1": return"0110111";
               case"D-1": return"0001110";
               case"A-1": return"0110010";
               case"D+A": return"0000010";
               case "D-A":return"0010011";
               case "A-D":return"0000111";
               case"D&A":return "0000000";
               case"D|A":return "0010101";
               case"M":return   "1110000";
               case "!M":return "1110001";
               case"-M":return  "1110011";
               case"M+1":return "1110111";
               case"M-1":return "1110010";
               case"D+M": return"1000010";
               case"D-M": return"1010011";
               case "M-D":return"1000111";
               case"D&M":return "1000000";
               case"D|M":return "1010101";
               default: return  "0000000";
           }
    }
    
    public void createHACK(String filename, String path) throws IOException
    {
        path = path.replace(filename, "");
        filename = filename.replace(".asm", "");  
       // File hackfile = new File(path,filename+ ".hack");
        //hackfile.createNewFile();
        
        File fout = new File(path,filename+".hack");
	FileOutputStream fos = new FileOutputStream(fout);
 
	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
 
	for (int i = 0; i < MainList.size(); i++) {
		bw.write(MainList.get(i));
		bw.newLine();
	}
	bw.close();
    }
    
}
