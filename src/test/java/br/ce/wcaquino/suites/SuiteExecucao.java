package br.ce.wcaquino.suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.CalculoValorLocacaoTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;

//@RunWith(value = Suite.class)
@SuiteClasses(value = { 
//		CalculadoraTeste.class, 
		CalculoValorLocacaoTest.class, 
		LocacaoServiceTest.class })
public class SuiteExecucao {

	@BeforeClass
	public static void Before() {
		System.out.println("antes");
	}

	@AfterClass
	public static void after() {
		System.out.println("depois");

	}
}
