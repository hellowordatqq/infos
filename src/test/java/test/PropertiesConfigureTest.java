package test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PropertiesConfigureTest {

	private int a;
	private int b;
	public int getA() {
		return a;
	}
	public void setA(int a) {
		this.a = a;
	}
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}
	
	@Override
	public String toString() {
		return "PropertiesConfigureTest [a=" + a + ", b=" + b + "]";
	}

	private static ApplicationContext ctx;
	
	@BeforeClass
	public static void before() {
		ctx = new ClassPathXmlApplicationContext("test.xml");
	}
	
	@Test
	public void doTest() {
		PropertiesConfigureTest test = ctx.getBean("test", PropertiesConfigureTest.class);
		System.out.println(test);
	}
	
	@AfterClass
	public static void after() {
		ctx = null;
	}
}
