package sml;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.*;

public class SMLTest {

	Machine m;
	Translator t;
	
	@Before
	public void testSetup() {
		m = new Machine();
		t = new Translator("code.sml");
	}
	
	@Test
	public void testLinInstruction() {
		LinInstruction l = new LinInstruction("f0",1,3);
		l.execute(m);
		assertEquals(m.getRegisters().getRegister(1),3);
	}
	
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
	
	@Test
	public void testBnzInstruction() {
		t.readAndTranslate(m.getLabels(),m.getProg());
		LinInstruction l1 = new LinInstruction("f0",2,1);
		BnzInstruction b = new BnzInstruction("f1",1,"f5");
		l1.execute(m);
		b.execute(m);
		assertEquals(m.getPc(),5);	
	}
	
	@Test
	public void testOutInstruction() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		LinInstruction l1 = new LinInstruction("f0",2,17);
		OutInstruction o = new OutInstruction("f1",2);
		l1.execute(m);
		o.execute(m);
		assertEquals("2",outContent.toString());
	}
	
	@After
	public void closeTestMachine() {
		m = null;
		t = null;
	}

}
