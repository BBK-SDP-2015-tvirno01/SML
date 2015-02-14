package sml;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.*;

public class SMLTest {

	Machine m;
	Translator t;
	
	
	//create instance of machine and translator for use in testing
	@Before
	public void testSetup() {
		m = new Machine();
		m.setRegisters(new Registers());
		t = new Translator("code.sml");
	}
	
	//test of the 'lin' instruction 
	@Test
	public void testLinInstruction() {
		LinInstruction l = new LinInstruction("f0",1,3);
		l.execute(m);
		assertEquals(m.getRegisters().getRegister(1),3);
	}
	
	//test of the 'add' instruction
	@Test
	public void testAddInstruction() {
		LinInstruction l1 = new LinInstruction("f0",2,5);
		LinInstruction l2 = new LinInstruction("f1",3,21);
		AddInstruction a = new AddInstruction("f2",1,2,3);
		l1.execute(m);
		l2.execute(m);
		a.execute(m);
		assertEquals(m.getRegisters().getRegister(1),26);
	}
	
	//test of the 'sub' instruction
	@Test
	public void testSubInstruction() {
		LinInstruction l1 = new LinInstruction("f0",2,21);
		LinInstruction l2 = new LinInstruction("f1",3,5);
		SubInstruction s = new SubInstruction("f2",1,2,3);
		l1.execute(m);
		l2.execute(m);
		s.execute(m);
		assertEquals(m.getRegisters().getRegister(1),16);	
	}
	
	//test of the 'mul' instruction
	@Test
	public void testMulInstruction() {
		LinInstruction l1 = new LinInstruction("f0",2,3);
		LinInstruction l2 = new LinInstruction("f1",3,5);
		MulInstruction x = new MulInstruction("f2",1,2,3);
		l1.execute(m);
		l2.execute(m);
		x.execute(m);
		assertEquals(m.getRegisters().getRegister(1),15);
	}
	
	
	//test of the 'div' instruction
	@Test
	public void testDivInstruction() {
		LinInstruction l1 = new LinInstruction("f0",2,21);
		LinInstruction l2 = new LinInstruction("f1",3,3);
		DivInstruction d = new DivInstruction("f2",1,2,3);
		l1.execute(m);
		l2.execute(m);
		d.execute(m);
		assertEquals(m.getRegisters().getRegister(1),7);	
	}
	
	//test of the 'bnz' instruction
	@Test
	public void testBnzInstruction() {
		t.readAndTranslate(m.getLabels(),m.getProg());
		LinInstruction l1 = new LinInstruction("f0",2,1);
		BnzInstruction b = new BnzInstruction("f1",2,"f5");
		l1.execute(m);
		b.execute(m);
		assertEquals(5,m.getPc());	
	}
	
	//test of the 'out' instruction
	@Test
	public void testOutInstruction() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		LinInstruction l1 = new LinInstruction("f0",2,17);
		OutInstruction o = new OutInstruction("f1",2);
		l1.execute(m);
		o.execute(m);
		assertEquals("17",outContent.toString());
	}
	
	//close machine and translator instances following test
	@After
	public void closeTestMachine() {
		m = null;
		t = null;
	}

}
