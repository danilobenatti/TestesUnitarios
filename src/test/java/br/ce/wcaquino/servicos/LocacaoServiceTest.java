package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.rules.ErrorCollector;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

//@RunWith(value = ParallelRunner.class)
public class LocacaoServiceTest {

	@InjectMocks
	@Spy
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
//		service = PowerMockito.spy(service);
		System.out.println("iniciando 2...");
		CalculadoraTeste.ordem.append("2");
	}

	@After
	public void tearDown() {
		System.out.println("finalizando 2...");
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
//		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// cen??rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

////		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 4, 2017));
//		Calendar calendar = Calendar.getInstance();
////		calendar.set(28, 4, 2017);
//		calendar.set(Calendar.DAY_OF_MONTH, 28);
//		calendar.set(Calendar.MONTH, Calendar.APRIL);
//		calendar.set(Calendar.YEAR, 2017);
//		PowerMockito.mockStatic(Calendar.class);
//		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		Mockito.doReturn(DataUtils.obterData(28, 4, 2017)).when(service).obterData();

		// a????o
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verifica????o
		error.checkThat(locacao.getValor(), equalTo(5.0));
//		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), equalTo(true));
//		error.checkThat(locacao.getDataLocacao(), ehHoje());
//		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), equalTo(true));
//		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), obterData(28, 4, 2017)), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterData(29, 4, 2017)), is(true));
	}

	@Test(expected = FilmeSemEstoqueException.class) // Forma elegante
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// cen??rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		// a????o
		service.alugarFilme(usuario, filmes);
	}

	@Test
	@Ignore(value = "Forma robusta")
	public void naoDeveAlugarFilmeSemEstoque_2() {
		// cen??rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		// a????o
		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail("Deveria ter lancado uma excess??o");
		} catch (Exception e) {
			assertThat(e.getMessage(), equalTo("Filme sem estoque"));
		}
	}

	@Test
	@Ignore(value = "Forma nova")
	public void naoDeveAlugarFilmeSemEstoque_3() throws Exception {
		// cen??rio
		final Usuario usuario = umUsuario().agora();
		final List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

//		exception.expect(Exception.class);
//		exception.expectMessage("Filme sem estoque");

		// a????o
		ThrowingRunnable throwingRunnable = new ThrowingRunnable() {
			@Override
			public void run() throws Throwable {
				service.alugarFilme(usuario, filmes);
			}
		};
		assertThrows("Filme sem estoque", Exception.class, throwingRunnable);
	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		// cen??rio
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// a????o
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), equalTo("Usuario vazio"));
		}
	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		// cen??rio
		final Usuario usuario = umUsuario().agora();

//		exception.expect(LocadoraException.class);
//		exception.expectMessage("Filme vazio");

		// a????o
		ThrowingRunnable throwingRunnable = new ThrowingRunnable() {

			@Override
			public void run() throws Throwable {
				service.alugarFilme(usuario, null);
			}
		};
		assertThrows("Filme sem filme", Exception.class, throwingRunnable);
	}

	/**
	 * Teste para implementa????o de m??todo que calcula a % de desconto em rela????o a
	 * quantidade de filmes alugados. Desconstos crescentes: 25% de desconto no 3??
	 * filme. 50% de desconto no 4?? filme. 75% de desconto no 5?? filme. 100% de
	 * desconto no 6?? filme.
	 * 
	 * @throws FilmeSemEstoqueException
	 * @throws LocadoraException
	 */
	@Test
	@Ignore(value = "Teste substitu??do pela implementa????o da classe 'CalculoValorLoca????oTest'")
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		// cen??rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0));

		// a????o
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verifica????o
		// 4 + 4 + 3 = 11
		assertThat(locacao.getValor(), equalTo(11.0));
	}

	@Test
	@Ignore(value = "Teste substitu??do pela implementa????o da classe 'CalculoValorLoca????oTest'")
	public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		// cen??rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0));

		// a????o
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verifica????o
		// 4 + 4 + 3 + 2 = 13
		assertThat(locacao.getValor(), equalTo(13.0));
	}

	@Test
	@Ignore(value = "Teste substitu??do pela implementa????o da classe 'CalculoValorLoca????oTest'")
	public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
		// cen??rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0), new Filme("Filme 4", 2, 4.0));

		// a????o
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verifica????o
		// 4 + 4 + 3 + 2 + 1 = 14
		assertThat(locacao.getValor(), equalTo(14.0));
	}

	@Test
	@Ignore(value = "Teste substitu??do pela implementa????o da classe 'CalculoValorLoca????oTest'")
	public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
		// cen??rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0), new Filme("Filme 4", 2, 4.0), new Filme("Filme 4", 2, 4.0),
				new Filme("Filme 4", 2, 4.0));

		// a????o
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verifica????o
		// 4 + 4 + 3 + 2 + 1 + 0 = 14
		assertThat(locacao.getValor(), equalTo(14.0));
	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException, Exception {
//		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// cen??rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		Mockito.doReturn(DataUtils.obterData(29, 4, 2017)).when(service).obterData();

		// a????o
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verifica????o
//		Boolean ehSegundaFeira = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
//		assertTrue(ehSegundaFeira);
//		assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
//		assertThat(locacao.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
		assertThat(locacao.getDataRetorno(), caiNumaSegunda());
//		PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();

//		PowerMockito.verifyStatic(Mockito.times(2));
//		Calendar.getInstance();
	}

	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		// cen??rio
		Usuario usuario = umUsuario().agora();
//		Para testar um caso de erro
//		Usuario usuario2 = umUsuario().comNome(" 2").agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		when(spcService.possuiNeagativacao(Mockito.any(Usuario.class))).thenReturn(true);

		// a????o
		try {
			service.alugarFilme(usuario, filmes);
			// verifica????o
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), equalTo("Usuario Negativado"));
		}

		verify(spcService).possuiNeagativacao(usuario);
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		// cen??rio
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome(" em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();
		List<Locacao> locacoes = Arrays.asList(
				umLocacao().atrasada().comUsuario(usuario).agora(),
				umLocacao().comUsuario(usuario2).agora(),
				umLocacao().atrasada().comUsuario(usuario3).agora(),
				umLocacao().atrasada().comUsuario(usuario3).agora());
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

		// a????o
		service.notificarAtrasos();

		// verifica????o com Mokito
		verify(emailService, times(3)).notificarAtraso(Mockito.any(Usuario.class));
		verify(emailService).notificarAtraso(usuario);
		verify(emailService, never()).notificarAtraso(usuario2);
		verify(emailService, atLeastOnce()).notificarAtraso(usuario3);
		verifyNoMoreInteractions(emailService);
//		verifyZeroInteractions(spcService);
	}

	@Test
	public void deveTratarErroNoSPC() throws Exception {
		// cen??rio
		final Usuario usuario = umUsuario().agora();
		final List<Filme> filmes = Arrays.asList(umFilme().agora());

		when(spcService.possuiNeagativacao(usuario)).thenThrow(new Exception("Falha catrastrofica!"));

		// verificacao
		ThrowingRunnable throwingRunnable = new ThrowingRunnable() {

			@Override
			public void run() throws Throwable {
				// a????o
				service.alugarFilme(usuario, filmes);
			}
		};
		assertThrows("Problemas com SPC, tente novamente", LocadoraException.class, throwingRunnable);

	}

	@Test
	public void deveProrrogarUmaLocacao() {
		// cen??rio
		Locacao locacao = umLocacao().agora();

		// a????o
		service.prorrogarLocacao(locacao, 3);

		// verifica????o
		ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argumentCaptor.capture());
		Locacao locacao2 = argumentCaptor.getValue();

		error.checkThat(locacao2.getValor(), equalTo(12.0));
		error.checkThat(locacao2.getDataLocacao(), ehHoje());
		error.checkThat(locacao2.getDataRetorno(), ehHojeComDiferencaDias(3));
	}

//	@Test
//	public void deveAlugarFilme_SemCalcularValor() throws Exception {
//		// cen??rio
//		Usuario usuario = umUsuario().agora();
//		List<Filme> filmes = Arrays.asList(umFilme().agora());
//
//		PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);
//
//		// a????o
//		Locacao locacao = service.alugarFilme(usuario, filmes);
//
//		// verifica????o
//		assertThat(locacao.getValor(), equalTo(1.0));
//		PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);
//	}

	@Test
	public void deveCalculcarValorLocacao() throws Exception {
		// cen??rio
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// a????o
		Class<LocacaoService> class1 = LocacaoService.class;
		Method method = class1.getDeclaredMethod("calcularValorLocacao", List.class);
		method.setAccessible(true);
		Double valor = (Double) method.invoke(service, filmes);

//		Double valor = (Double) Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);

		// verifica????o
		assertThat(valor, equalTo(4.0));
	}

}
