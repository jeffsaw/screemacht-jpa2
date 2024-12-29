package br.com.alura.screenmatch.controller;
import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

//Essa anotação indica para o Spring que essa classe é um controlador, que receberá as requisições do frontend
//e conterá os mapeamentos dos endpoints.
@RestController
@RequestMapping("/series")
public class SerieController {

    //classe de servico onde estao os metodos responsaveis por buscar as informações no banco e retorna para
    //o frontend
    @Autowired
    SerieService serieService;

    //Essa anotação indica a "rota", a "URL" ou o endpoint que vira do frontend
    //e abaixo está o método que será executado quando essa URL for chamada.
    @GetMapping
    public List<SerieDTO> obterSerie(){
        return serieService.obterTodasAsSerie();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterTop5Series(){
        return serieService.obterTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos(){
        return serieService.obterLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDTO obeterPorId(@PathVariable Long id){
        return serieService.obterPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id){
        return serieService.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporadasPorNumero(@PathVariable Long id, @PathVariable Long numero){
        return serieService.obterTemporadasPorNumero(id, numero);
    }
}
