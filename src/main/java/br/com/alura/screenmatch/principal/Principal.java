package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private List<DadosSerie> dadosSeriesBuscadas = new ArrayList<>();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=ffea21b";
    Optional<Serie> serieBuscada;

    //Atributo usado na injecao de depencia do Spring para manipular os dados no banco
    private SerieRepository repositorio;

    private List<Serie> seriesSalvas = new ArrayList<>();

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    1 - Buscar séries.
                    2 - Buscar episódios.
                    3 - Listar Séries buscadas.
                    4 - Buscar Serie por titulo.
                    5 - Buscar Serie por Ator.
                    6 - Buscar top 5 series.
                    7 - Buscar Serie por Genero.
                    8 - Buscar Serie por temporadas e avaliacao.
                    9 - Buscar Episodio por trecho.
                    10- Buscar top 5 episodios por serie.
                    11- Buscar episodios a partir de determinado ano.
                    0 - Sair.                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscaSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscaPorCategoria();
                    break;
                case 8:
                    buscarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    buscarTop5Episodios();
                    break;
                case 11:
                    buscarEpisodioAntesDoAno();
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie series = new Serie(dados);
        //dadosSeriesBuscadas.add(dados);
        repositorio.save(series);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){

        listarSeriesBuscadas();
        System.out.println("Digite o nome da serie: ");
        var serieDigitada = leitura.nextLine();

        //Criacão do Optional que verificar se a serie digitada é igual a alguma serie salva no banco
//        Optional<Serie> serieBuscada = seriesSalvas.stream()
//                .filter(s -> s.getTitulo().toLowerCase().contains(serieDigitada.toLowerCase()))
//                .findFirst();

        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(serieDigitada);

        if (serieBuscada.isPresent()) {
            var serieEncontrada = serieBuscada.get();

            //A lista de temporadas é usada para mostrar os epsodios.
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream()
                            .map(e -> new Episodio(t.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);

        }else{
            System.out.println("Serie não encontrada!");
        }
    }

    private void listarSeriesBuscadas(){
        //O metedo agora não busca mais numa lista local salva, e sim no banco de dados, os dados que ja foram salvos
        //O metodo findAll() retorna uma lista do tipo generico, que será determinado com base no tipo que está
        //chamando, nesse caso o tipo Serie

        seriesSalvas = repositorio.findAll();

        seriesSalvas.stream()
                .sorted(Comparator.comparing(Serie::getAvaliacao))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite o nome da serie: ");
        var nomeSerie = leitura.nextLine();

        serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isPresent()){
            System.out.println("Dados da serie: " + serieBuscada.get());
        }else {
            System.out.println("Serie não encontrada.");
        }
    }

    private void buscaSeriePorAtor() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        List<Serie> serieBuscada = repositorio.findByAtoresContainingIgnoreCase(nomeSerie);

        serieBuscada
                .forEach(s -> System.out.println(
                        "Serie: " + s.getTitulo() + " Avaliação: " + s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> serieTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        serieTop.forEach(s ->
                System.out.println(
                        "Serie: " + s.getTitulo() + " Avaliação: " + s.getAvaliacao()));
    }

    private void buscaPorCategoria(){
        System.out.println("Digite o nome da categoria");
        var nomeGenero = leitura.nextLine();

        Categoria categoriaPtbr = Categoria.fromPtbr(nomeGenero);

        List<Serie> serieCategoria = repositorio.findByGenero(categoriaPtbr);

        serieCategoria.forEach(s ->
                System.out.println(
                        "Serie: " + s.getTitulo() + " Avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriesPorTemporadaEAvaliacao(){
        System.out.println("Digite o numero de temporadas: ");
        var numeroTem = leitura.nextInt();
        leitura.nextLine();

        System.out.println("Digite o valor da Avalicao: ");
        var numeroAva = leitura.nextDouble();

        List<Serie> serie = repositorio.seriesPorTemporadaEAValiacao(numeroTem, numeroAva);
        serie.forEach(s ->
                System.out.println(
                        "Serie: " + s.getTitulo() + " Avaliação: " + s.getAvaliacao()));
    }

    private void buscarEpisodioPorTrecho(){
        System.out.println("Digite o trecho do epsodio: ");
        var trechoEpisodio = leitura.nextLine();

        List<Episodio> episodios = repositorio.buscarEpisodiosPorTrecho(trechoEpisodio);

        episodios.forEach(e -> System.out.println(
                "Serie: " + e.getSerie().getTitulo() +
                        " Temporada: " + e.getTemporada() +
                        " Titulo: " + e.getTitulo()));

    }

    private void buscarTop5Episodios(){
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()){
            List<Episodio> topEpisodios = repositorio.buscarTop5Episodios(serieBuscada.get());
            topEpisodios.forEach(e -> System.out.println(
                            "Temporada: " + e.getTemporada() +
                            " Titulo: " + e.getTitulo() +
                            " Avaliação: " + e.getAvaliacao()));

        }
    }

    private void buscarEpisodioAntesDoAno() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()) {
            System.out.println("Digite o Ano: ");
            var ano = leitura.nextInt();
            leitura.nextLine();
            List<Episodio> episodios = repositorio.buscarEpisodioAposAno(serieBuscada.get(), ano);
            episodios.forEach(e -> System.out.println(
                    "Temporada: " + e.getTemporada() +
                            " Titulo: " + e.getTitulo() +
                            " Avaliação: " + e.getAvaliacao()));
        }
    }
}