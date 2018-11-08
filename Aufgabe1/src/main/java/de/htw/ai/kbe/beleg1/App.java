package de.htw.ai.kbe.beleg1;

import de.htw.ai.kbe.beleg1.Utils;

public class App 
{
    public static void main( String[] args )
    {
    	Utils utils = new Utils();	//Utils-Objekt erzeugen
    	utils.readCommandline(args); //in der Utils.java werden die Verarbeitungen durchgeführt
    }
}
