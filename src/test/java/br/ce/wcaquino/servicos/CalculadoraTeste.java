package br.ce.wcaquino.servicos;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

//@RunWith(value = ParallelRunner.class)
public class CalculadoraTeste {

	public static StringBuffer ordem = new StringBuffer();

	private Calculadora calculadora;

	@Before
	public void setup() {
		calculadora = new Calculadora();
		System.out.println("iniciando 1...");
		ordem.append("1");
	}

	@After
	public void tearDown() {
		System.out.println("finalizando 1...");
	}

	@AfterClass
	public static void tearDownClass() {
		System.out.println(ordem.toString());
	}

	@Test
	public void deveSomarDoisValores() {
		// cenário
		int a = 5, b = 3;

		// acao
		int resultado = calculadora.somar(a, b);

		// verificacao
		Assert.assertEquals(8, resultado);
	}

	@Test
	public void deveSubtrairDoisValores() {
		// cenário
		int a = 8, b = 5;

		// acao
		int resultado = calculadora.subtrair(a, b);

		// verificacao
		Assert.assertEquals(3, resultado);
	}

	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		// cenário
		int a = 6, b = 3;

		// acao
		int resultado = calculadora.dividir(a, b);

		// verificacao
		Assert.assertEquals(2, resultado);
	}

	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		//cenário
		int a = 10, b = 0;

		//acao
		calculadora.dividir(a, b);

		//verificacao
	}

	@Test
	public void deveDividir() {
		String a = "6";
		String b = "3";

		int resultado = calculadora.divide(a, b);

		Assert.assertEquals(2, resultado);
	}

}
