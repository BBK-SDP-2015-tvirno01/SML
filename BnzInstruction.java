package sml;

/**
 * This class contains the methods specific to the SML BNZ instruction
 * 
 * @author someone
 * @author tvirno01
 */

public class BnzInstruction extends Instruction {
	private int register;
	private String nextLabel;

	public BnzInstruction(String label, String opcode) {
		super(label, opcode);
	}

	public BnzInstruction(String label, int register, String nextLabel) {
		this(label, "bnz");
		this.register = register;
		this.nextLabel = nextLabel;

	}

	@Override
	public void execute(Machine m) {
		if(m.getRegisters().getResigster(register)!=0 && m.labels.indexOf(nextLabel)!=-1){
			m.setPC(m.labels.indexOf(nextLabel));
		}
	}

	@Override
	public String toString() {
		return super.toString() + " if register " + register + " value is not zero then jump to program statement " + nextLabel;
	}
}
