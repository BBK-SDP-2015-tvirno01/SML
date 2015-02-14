package sml;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.Class;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/*
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
 */
public class Translator {

	// word + line is the part of the current line that's not yet processed
	// word has no whitespace
	// If word and line are not empty, line begins with whitespace
	private String line = "";
	private Labels labels; // The labels of the program being translated
	private ArrayList<Instruction> program; // The program to be created
	private String fileName; // source file of SML code

	private static final String SRC = "src";

	public Translator(String fileName) {
		this.fileName = SRC + "/" + fileName;
	}

	// translate the small program in the file into lab (the labels) and
	// prog (the program)
	// return "no errors were detected"
	public boolean readAndTranslate(Labels lab, ArrayList<Instruction> prog) {

		try (Scanner sc = new Scanner(new File(fileName))) {
			// Scanner attached to the file chosen by the user
			labels = lab;
			labels.reset();
			program = prog;
			program.clear();

			try {
				line = sc.nextLine();
			} catch (NoSuchElementException ioE) {
				return false;
			}

			// Each iteration processes line and reads the next line into line
			while (line != null) {
				// Store the label in label
				String label = scan();
				
				//ignore lines duplicating earlier labels and lines with no instruction			
				if (label.length() > 0 && labels.indexOf(label)==-1) {
					Instruction ins = getInstruction(label);
					if (ins != null) {
						labels.addLabel(label);
						program.add(ins);
					}
				}

				try {
					line = sc.nextLine();
				} catch (NoSuchElementException ioE) {
					return false;
				}
			}
		} catch (IOException ioE) {
			System.out.println("File: IO error " + ioE.getMessage());
			return false;
		}
		return true;
	}

	// line should consist of an SML instruction, with its label already
	// removed. Translate line into an instruction with label label
	// and return the instruction
	public Instruction getInstruction(String label) {

		if (line.equals("")) return null;

		String ins = scan();
		
		//Use of reflection to instantiate instruction subclass, as indicated by the opcode scanned, which is then returned
		try{
			//Create instruction subclass name from the opcode scanned
			//[Note: The format of the instruction subclass name must follow the standard convention of opcode with the first
			//character capitalized, concatenated with the string 'Instruction']
			String insformat = "sml." + ins.substring(0,1).toUpperCase() + ins.substring(1).toLowerCase() + "Instruction";
			
			//Select Instruction subclass using the formatted string
			Class<?> c = Class.forName(insformat);
			
			//Create List data structures to hold the class types and instances for the constructor parameters and add a first
			//record for the label of the line of SML code being scanned 
			List<Object> params = new ArrayList<Object>();
			List<Class<?>> ptypes = new ArrayList<Class<?>>();
			params.add(label);
			ptypes.add(String.class);
			
			//read operands from the line of SML code in the current specification these can only be integers or strings 
			String scn = "";
			do{
				try{
					scn = scan();
					params.add(Integer.parseInt(scn));
					ptypes.add(int.class);
				}catch(NumberFormatException ex){
					params.add(scn);
					ptypes.add(String.class);
				}
			}while(!line.equals(""));
			
			//Identify the constructor of class c from the signature of the parameters 
			Class<?>[] ptypesA = new Class<?>[ptypes.size()];
			Constructor<?> cons = c.getConstructor(ptypes.toArray(ptypesA));
			
			//create instance of class c and return
			Instruction result = (Instruction) cons.newInstance(params.toArray());
			return result;
			
		}catch(ClassNotFoundException ex){
			System.out.println(ex.getMessage());
			return null;
		} catch (NoSuchMethodException ex) {
			System.out.println(ex.getMessage());
			return null;
		} catch (SecurityException ex) {
			System.out.println(ex.getMessage());
			return null;
		} catch (InstantiationException ex) {
			System.out.println(ex.getMessage());
			return null;
		} catch (IllegalAccessException ex) {
			System.out.println(ex.getMessage());
			return null;
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
			return null;
		} catch (InvocationTargetException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
		
		//The below section has been commented out. This contains the original switch statement used to test the Instruction sub classes 
		
		// Possible operands of the instruction
		// int s1; 
		// int s2;
		// int r;
		// int x;
		// String L2;
		
		//		switch (ins) {
		//		case "add":
		//			r = scanInt();
		//			s1 = scanInt();
		//			s2 = scanInt();
		//			if(verifyInputRegisters(r,s1,s2)){
		//				return new AddInstruction(label, r, s1, s2);
		//			}
		//		case "lin":
		//			r = scanInt();
		//			x = scanInt();
		//			if(verifyInputRegisters(r)){
		//				return new LinInstruction(label, r, x);
		//			}
		//		case "sub":
		//			r = scanInt();
		//			s1 = scanInt();
		//			s2 = scanInt();
		//			if(verifyInputRegisters(r,s1,s2)){
		//				return new SubInstruction(label, r, s1, s2);
		//			}
		//		case "mul":
		//			r = scanInt();
		//			s1 = scanInt();
		//			s2 = scanInt();
		//			if(verifyInputRegisters(r,s1,s2)){
		//				return new MulInstruction(label, r, s1, s2);
		//			}
		//		case "div":
		//			r = scanInt();
		//			s1 = scanInt();
		//			s2 = scanInt();
		//			if(verifyInputRegisters(r,s1,s2)){
		//				return new DivInstruction(label, r, s1, s2);
		//			}
		//		case "out":
		//			s1 = scanInt();
		//			if(verifyInputRegisters(s1)){
		//				return new OutInstruction(label, s1);
		//			}
		//		case "bnz":
		//			s1 = scanInt();
		//			L2 = scan();
		//			if(verifyInputRegisters(s1)){
		//				return new BnzInstruction(label, s1, L2);
		//			}
		//		}	
		//		return null;
	}
	
	/*
	 * Validate reigster number inputs
	 * There are only 32 registers hence given register numbers should be <=31
	 */
	//	private static boolean verifyInputRegisters(int... inputs){
	//		for(int i : inputs){
	//		Registers r = new Registers();
	//		if(i>r.getRegisters().length) return false;
	//		}
	//		return true;
	//	}

	/*
	 * Return the first word of line and remove it from line. If there is no
	 * word, return ""
	 */
	private String scan() {
		line = line.trim();
		if (line.length() == 0)
			return "";

		int i = 0;
		while (i < line.length() && line.charAt(i) != ' ' && line.charAt(i) != '\t') {
			i = i + 1;
		}
		String word = line.substring(0, i);
		line = line.substring(i);
		return word;
	}

	// Return the first word of line as an integer. If there is
	// any error, return the maximum int
	private int scanInt() {
		String word = scan();
		if (word.length() == 0) {
			return Integer.MAX_VALUE;
		}

		try {
			return Integer.parseInt(word);
		} catch (NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
	}
}