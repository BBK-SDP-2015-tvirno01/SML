package sml;

/**
 * This class contains the methods specific to the SML OUT instruction
 * 
 * @author someone
 * @author tvirno01
 */

public class OutInstruction extends Instruction {
	private int register;

	public OutInstruction(String label, String opcode) {
		super(label, opcode);
	}

	public OutInstruction(String label, int register) {
		this(label, "out");
		this.register = register;
	}

	@Override
	public void execute(Machine m) {
		System.out.println(m.getRegisters().getRegister(register));
	}

	@Override
	public String toString() {
		return super.toString() + " print register " + register;
	}
}
