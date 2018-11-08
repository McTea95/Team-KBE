package de.htw.ai.kbe.beleg1;

import java.util.ArrayList;

//Objekt zum Speichern der Ergebnisse der Utils.loadClass-function
public class CheckResult {

	private int methodCount;		//Anzahl aller Methoden der Klasse
	private int runMeMethodCount;	//Anzahl aller Methoden der Klasse, mit @RunMe-Annotation
	private ArrayList<String> runMeMethodNames;	//Bezeichner aller @RunMe-Methoden der Klasse
	private ArrayList<String> notInvokeableMethodNames;	//Bezeichner aller nicht-ausführbaren Methoden der Klasse
	
	//Konstruktor
	public CheckResult(int mc, int rmc, ArrayList<String> rmn, ArrayList<String> nimn){
		this.setMethodCount(mc);
		this.setRunMeMethodCount(rmc);
		this.setRunMeMethodNames(rmn);
		this.setNotInvokeableMethodNames(nimn);
	}

	//Getter und Setter
	public int getMethodCount() {
		return methodCount;
	}

	public void setMethodCount(int methodCount) {
		this.methodCount = methodCount;
	}

	public int getRunMeMethodCount() {
		return runMeMethodCount;
	}

	public void setRunMeMethodCount(int runMeMethodCount) {
		this.runMeMethodCount = runMeMethodCount;
	}

	public ArrayList<String> getRunMeMethodNames() {
		return runMeMethodNames;
	}

	public void setRunMeMethodNames(ArrayList<String> runMeMethodNames) {
		this.runMeMethodNames = runMeMethodNames;
	}

	public ArrayList<String> getNotInvokeableMethodNames() {
		return notInvokeableMethodNames;
	}

	public void setNotInvokeableMethodNames(ArrayList<String> notInvokeableMethodNames) {
		this.notInvokeableMethodNames = notInvokeableMethodNames;
	}
	
	
}
