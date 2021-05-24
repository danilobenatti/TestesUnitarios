package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

@RunWith(value = PowerMockRunner.class)
@PrepareForTest(value = { LocacaoService.class })
public class LocacaoServiceTest_PowerMock {

	@InjectMocks
	private LocacaoService service;

	@Mock
	private SPCService spcService;

	@Mock
	private LocacaoDAO dao;

	@Mock
	private EmailService emailService;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		service = PowerMockito.spy(service);
		System.out.println("iniciando 4...");
		CalculadoraTeste.ordem.append(4);
	}

	@After
	public void tearDown() {
		System.out.println("finalizando 4...");
	}

	@BeforeClass
	public static void setupClass() {
	}

	@AfterClass
	public static void tearDownClass() {
		System.out.println(CalculadoraTeste.ordem.toString());
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		// cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 4, 2017));

		// ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		error.checkThat(locacao.getValor(), equalTo(5.0));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));
	}

	/**
	 * Teste para implementação de método que calcula a % de desconto em relação a
	 * quantidade de filmes alugados. Desconstos crescentes: 25% de desconto no 3º
	 * filme. 50% de desconto no 4º filme. 75% de desconto no 5º filme. 100% de
	 * desconto no 6º filme.
	 * 
	 * @throws FilmeSemEstoqueException
	 * @throws LocadoraException
	 */
	@Test
	@Ignore(value = "Teste substituído pela implementação da classe 'CalculoValorLocaçãoTest'")
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		// cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0));

		// ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		// 4 + 4 + 3 = 11
		assertThat(locacao.getValor(), equalTo(11.0));
	}

	@Test
	@Ignore(value = "Teste substituído pela implementação da classe 'CalculoValorLocaçãoTest'")
	public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		// cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0));

		// ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		// 4 + 4 + 3 + 2 = 13
		assertThat(locacao.getValor(), equalTo(13.0));
	}

	@Test
	@Ignore(value = "Teste substituído pela implementação da classe 'CalculoValorLocaçãoTest'")
	public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
		// cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0), new Filme("Filme 4", 2, 4.0));

		// ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		// 4 + 4 + 3 + 2 + 1 = 14
		assertThat(locacao.getValor(), equalTo(14.0));
	}

	@Test
	@Ignore(value = "Teste substituído pela implementação da classe 'CalculoValorLocaçãoTest'")
	public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
		// cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0), new Filme("Filme 4", 2, 4.0),
				new Filme("Filme 4", 2, 4.0));

		// ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		// 4 + 4 + 3 + 2 + 1 + 0 = 14
		assertThat(locacao.getValor(), equalTo(14.0));
	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
		// cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 4, 2017));

		// ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		assertThat(locacao.getDataRetorno(), caiNumaSegunda());
	}

	@Test
	public void deveAlugarFilme_SemCalcularValor() throws Exception {
		// cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);

		// ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificação
		assertThat(locacao.getValor(), equalTo(1.0));
		PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);
	}

	@Test
	public void deveCalculcarValorLocacao() throws Exception {
		// cenário
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// ação
		Double valor = (Double) Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);

		// verificação
		assertThat(valor, equalTo(4.0));
	}

}
