package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.repository.SerieRepository;
import jakarta.persistence.*;
import org.hibernate.annotations.IdGeneratorType;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

//A anotação "Entity" é usada para indicar que a classe será modelada em uma tabela
//Já a anotação "Table" indica que o nome da tabela no banco será diferente do nome da classe
//Por isso foi passado o parametro name.
@Entity
@Table(name = "series")
public class Serie {

    //Essa anotação indica que esse atributo sera usado como uma chave primaria
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Essa anotação indica que o conteudo da gravado na coluna não pode se repetir
    //tem um indice unico
    @Column(unique = true)
    private String titulo;

    private Integer totalTemporadas;
    private Double avaliacao;

    //Indica o tipo de enumeracao do Enum, nessa caso seŕa usado as string do Enum "Categoria"
    @Enumerated(EnumType.STRING)
    private Categoria genero;

    private String atores;
    private String poster;
    private String sinopse;

    //Essa anotação indica que esse atributo não será persistido no banco por enquanto
    //@Transient
    //Anotation que indica o a cardinalidade do relacionamento,
    //O atributo mappedBy indica por qual atributo na outra classe mapeada será feito o mapeamento
    //O cascade significa que os "episodios" serão salvos a partir da classe serie, por isso é operação em cascata
    //o "all" significa que tudo que acontecer com a classe Serie salva acontece com a classe episodio
    //O atributo "fecth" indica como as entidades relacionadas são carregadas do banco,
    //No caso especifico o "EAGER" indica ansioso, traz as entidades mesmo sem pedir
    //Traz de uma forma implicita
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();


    //Construtor padrao exigido pela JPA
    public Serie(){}

    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.atores = dadosSerie.atores();
        this.poster = dadosSerie.poster();
        this.sinopse = dadosSerie.sinopse();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    @Override
    public String toString() {
        return  "genero=" + genero +
                ", titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao=" + avaliacao +
                ", atores='" + atores + '\'' +
                ", poster='" + poster + '\'' +
                ", sinopse='" + sinopse + '\'' +
                ", episodios='" + episodios + '\'';
    }
}
