package br.com.alura.screenmatch.service;
import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//Classe responvel pelo servico que o controlador ira buscar, foi descoplado do controlador os metodos responsaveis
//por buscar informacoes e foi delegado a essa classe controler.
@Service
public class SerieService {

    @Autowired
    private SerieRepository repositoio;

    public List<SerieDTO> obterTodasAsSerie(){
        return converteDados(repositoio.findAll());
    }


    public List<SerieDTO> obterTop5Series() {
        return converteDados(repositoio.findTop5ByOrderByAvaliacaoDesc());
    }

    //Metodo usado para converter os dados de Serie (do banco) para serieDTO (classe record usada para
    //devolver dados para o frontend)
    private List<SerieDTO> converteDados(List<Serie> serie){
        return serie.stream()
                .map(s -> new SerieDTO(s.getId(),
                        s.getTitulo(),
                        s.getTotalTemporadas(),
                        s.getAvaliacao(),
                        s.getGenero(),
                        s.getAtores(),
                        s.getPoster(),
                        s.getSinopse()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(repositoio.encontrarEpisodiosMaisRecentes());
    }

    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = repositoio.findById(id);
        if(serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(),
                    s.getTitulo(),
                    s.getTotalTemporadas(),
                    s.getAvaliacao(),
                    s.getGenero(),
                    s.getAtores(),
                    s.getPoster(),
                    s.getSinopse());
        }
        return null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = repositoio.findById(id);
        if(serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(),
                            e.getNumeroEpisodio(),
                            e.getTitulo()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
        return repositoio.obterEpisodiosPorTemporada(id, numero).stream()
                .map(e -> new EpisodioDTO(e.getTemporada(),
                        e.getNumeroEpisodio(),
                        e.getTitulo()))
                .collect(Collectors.toList());
    }
}
