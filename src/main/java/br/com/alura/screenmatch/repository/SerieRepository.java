package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

//Interface responsavel por abstrair todas as operações de manipulação dos dados no banco
//Repositorio que fará as operações básicas do CRUD
public interface SerieRepository extends JpaRepository<Serie, Long> {
}
