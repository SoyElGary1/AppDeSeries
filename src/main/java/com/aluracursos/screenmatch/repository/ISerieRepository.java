package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.CategoriaEnum;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ISerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);
    List<Serie> findTop5ByOrderByEvaluacionDesc();
    List<Serie> findByGenero(CategoriaEnum genero);
    Optional<Serie> findByTotalTemporadasAndEvaluacionGreaterThanEqual(Integer totalTemporadas, Double evaluacion);

}
//una serie por un cierto numero de temporadas y por una evaluacion en especifico