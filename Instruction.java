package sml;

/**
 * This class is the superclass of the classes for machine instructions
 * 
 * @author someone
 */

public abstract class Instruction {
	protected String label;
	protected String opcode;

	// Constructor: an instruction with label l and opcode op
	// (op must be an operation of the language)

	public Instruction(String l, String op) {
			this.label = l;
			this.opcode = op;
			if(!["add","sub","mul","div","lin","out","bnz"].contains(op)) throw new IOException("Opcode does not exist in SML");
	}

	// = the representation "label: opcode" of this Instruction

	@Override
	public String toString() {
		return label + ": " + opcode;
	}

	// Execute this instruction on machine m.

	public abstract void execute(Machine m);
}
