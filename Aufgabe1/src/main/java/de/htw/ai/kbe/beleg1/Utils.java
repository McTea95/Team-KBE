package de.htw.ai.kbe.beleg1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class Utils {
	
	private String outputFilename;			//Dateiname der Ausgabe-.txt-Datei (vom User über Commandline festgelegt)
	private boolean outputFileExists = false;	//Überprüfung ob eine Output-File vom User angegeben wurde
	private Options options;				//Commandline-Options (-c & -o)
	
	//Konstruktor
	public Utils(){
		options = new Options();
		options.addRequiredOption("c", "className", true, "Class");
        options.addOption("o", true, "Output File");
	}
	
	//Einlesen und Verarbeiten der Commandline (Parameter: args[] aus der main-function)
	public void readCommandline(String[] args){
		if(!(args.length==0)){	//Falls args in der Commandline vorhanden wird die Funktion ausgeführt
			CommandLineParser parser = new DefaultParser();       
	        try {
				CommandLine cmd = parser.parse(getCommandlineOptions(), args);		//Commandline einlesen und Optionen hinzufügen
				if(cmd.hasOption("o")){		//Output File-Option (-o example.txt)
					//System.out.println("Output File: " + cmd.getOptionValue("o"));
					File outputFile = new File(cmd.getOptionValue("o"));
					if(outputFileExists==false	){
						//OutputFile kann erstellt werden
						outputFileExists=true;		
						setOutputFilename(outputFile.getName()+".txt");
					}
				}
				else {
					outputFileExists=false;
					System.out.println("Keine Output-File angegeben, Ausgabe erfolgt in der Konsole.");
				}
				
				
				
				if(cmd.hasOption("c")){		//c-Option (-p example.properties)
					File className = new File(cmd.getOptionValue("c")+".java");
					String Pfad = "de.htw.ai.kbe.beleg1."+className.getName();
					//System.out.println("File Type: " + fileType);	
						if (className.exists()){		//Überprüfen ob die Datei vorhanden ist	//Überprüfen ob der Key "RunMe" in der className vorhanden ist
								//Aufruf von loadClass um die Klasse im Key "RunMe" auszulesen
								CheckResult results = loadClass(Pfad);	//CheckResult ist eine selbsterstellte Klasse, in welche die Ergebnisse gespeichert werden (Methodenzählung, Namen, etc)	
								if(outputFileExists){		//Wenn eine Output-File vom User angegeben wurde wird die createReportFile-function genutzt
									if(createReportFile(getOutputFilename(), results)){
										System.out.println(outputFilename + " wurde erstellt.");
									}
								}
								else {					//Ohne Output-File angegeben wird alles in der Konsole ausgegeben (printReport-function)
									printReport(results);
								}
							
						}
						else {
							System.out.println("Fehler: Die Class-Datei " + className.getName() + " wurde nicht gefunden!");
						}
				}
				
				
			} catch (ParseException e) {
				System.out.println("Commandline konnte nicht geparset werden.");
				e.printStackTrace();
			}
		}
	}
	
	//Funktion zum Laden einer Klasse zur Laufzeit, gibt die Ergebnisse der Methoden-Invokes und Zählungen in dem CheckResult-Objekt zurück
	public CheckResult loadClass(String classpath)
	{
		Class app = null;
			try {
				app = Class.forName(classpath);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Method[] methods = app.getDeclaredMethods();	//Alle Methoden die nur innerhalb der Klasse erstellt sind (keine geerbten) werden im Array methods gespeichert
			Method.setAccessible(methods, true);
			int count = 0;		//Variable zum Zählen der @RunMe-Methoden
			ArrayList<String> notInvokeable = new ArrayList<String>();	//Alle Namen der Nicht-ausführbaren Methoden
			ArrayList<String> methodNames = new ArrayList<String>();	//Alle Namen von allen Methoden in der Klasse
			//Alle Methoden durchiterieren
			for(int i=0; i<methods.length; i++){
			if(methods[i].isAnnotationPresent(de.htw.ai.kbe.beleg1.RunMe.class)) {	//Wenn eine @RunMe-Notation bei der Methode steht
					if(methods[i].getParameterCount()>0){	//Überprüfung ob Parameter in der Methode existieren (wenn ja wird diese nicht ausgeführt)
						System.out.println("Konnte die Methode " + methods[i].getName() + " aufgrund der Parameter nicht ausführen.");
						notInvokeable.add(methods[i].getName());	//@RunMe-Methoden mit Parametern gelten auch als NotInvokeable
					}
					else {
						try {
							try {
								methods[i].invoke(Class.forName(classpath));
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 	//Wenn die Methode ausgeführt werden kann wird sie mit "invoke" aufgerufen
							//System.out.println(methods[i].invoke(null));
						}
						catch(InvocationTargetException e){ //Falls die Methode auf die invoke angewendet wird eine Exception wirft wird diese ausgegeben und die Methode als NotInvokeableMethod gewertet
							notInvokeable.add(methods[i].getName());	//@RunMe-Methoden, die nicht ausgeführt werden konnten gelten auch als NotInvokeable
							System.out.println(e.getCause());
						}
						//System.out.println(methods[i].invoke(null));	
					}
					methodNames.add(methods[i].getName());	//Hinzufügen des Methodennamens für alle @RunMe-Methoden
					count++;	//Zähler für @RunMe-Methoden erhöhen
			}
			else {
				notInvokeable.add(methods[i].getName());	//Alle nicht ausführbaren Methodennamen
			}
}
CheckResult res = new CheckResult(methods.length, count, methodNames, notInvokeable);	//Ergebnis-Objekt erzeugen
return res;
	}

	
	public void setOutputFilename(String n){
		outputFilename = n;
	}
	
	public String getOutputFilename(){
		return outputFilename;
	}
	
	public Options getCommandlineOptions(){
		return options;
	}
	
	//Methode zum Erstellen der Output.txt-Datei
	public boolean createReportFile(String outputFilename, CheckResult results){	//OutputFilename vom User festgelegt, results sind die Ergebnisse der loadClass-Methode (Anzahl der Methoden, Anzahl der @RunMe-Methoden, Liste mit allen @RunMe-Methodennamen, Liste mit allen NotInvokeable-Methodennamen)
		PrintWriter writer;
		ArrayList<String> methodNames = results.getRunMeMethodNames();
		ArrayList<String> notInvokeableMethodNames = results.getNotInvokeableMethodNames();
		try {	//Ausgabe in die Datei
			writer = new PrintWriter(outputFilename, "UTF-8");
			writer.println("Anzahl der Methoden: " + results.getMethodCount());
			writer.println("Anzahl der @RunMe-Methoden: " + results.getRunMeMethodCount());
			writer.println("Namen aller @RunMe-Methoden der Klasse:");
			for(int i=0;i<results.getRunMeMethodNames().size();i++){
				writer.println("\t" + methodNames.get(i));
			}
			writer.println("Namen aller Methoden die nicht Invoked wurden:");
			for(int j=0;j<results.getNotInvokeableMethodNames().size();j++){
				writer.println("\t" + notInvokeableMethodNames.get(j));
			}
	    	writer.close();
	    	return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//Ausgabe der Ergebnisse in der Konsole
	public void printReport(CheckResult results){
		ArrayList<String> methodNames = results.getRunMeMethodNames();
		ArrayList<String> notInvokeableMethodNames = results.getNotInvokeableMethodNames();
		
		System.out.println("----------------------");
		System.out.println("Anzahl der Methoden: " + results.getMethodCount());
		System.out.println("Anzahl der @RunMe-Methoden: " + results.getRunMeMethodCount());
		System.out.println("Namen aller @RunMe-Methoden der Klasse:");
		for(int i=0;i<results.getRunMeMethodNames().size();i++){
			System.out.println("\t" + methodNames.get(i));
		}
		System.out.println("Namen aller Methoden die nicht Invoked wurden:");
		for(int j=0;j<results.getNotInvokeableMethodNames().size();j++){
			System.out.println("\t" + notInvokeableMethodNames.get(j));
		}
		System.out.println("----------------------");
	}
}
