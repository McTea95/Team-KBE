package de.htw.ai.kbe;


import de.htw.ai.kbe.beleg1.RunMe;

//Testklasse mit der das Programm durchgeprüft werden kann
public class AppT{
	
	
	@RunMe
	public static boolean methodA() {
		System.out.println("In methodA");
		return false;
	}
	
	public void methodB() {
		System.out.println("In methodB");
	}
	
	public void methodC() {
		System.out.println("In methodC");
	}
	
	public void methodD() {
		System.out.println("In methodD");
	}
	
	@RunMe
	public static void methodE(String str) {
		System.out.println("In methodE");
		System.out.println(str);
	}
	
	
}