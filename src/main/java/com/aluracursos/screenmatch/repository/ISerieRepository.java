package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.dto.EpisodioDto;
import com.aluracursos.screenmatch.model.CategoriaEnum;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ISerieRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findById(Long id);
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);
    List<Serie> findTop5ByOrderByEvaluacionDesc();
    List<Serie> findByGenero(CategoriaEnum genero);
    //List<Serie> findByTotalTemporadasLessThanAndEvaluacionGreaterThanEqual(Integer totalTemporadas, Double evaluacion);

    //Consulta JPQL
    @Query(value = "SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion")
    List<Serie> seriesPorTemporadaYEvaluacion(Integer totalTemporadas, Double evaluacion);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
    List<Episodio> episodiosPorNombre(String nombreEpisodio);

    @Query(value = "SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5")
    List<Episodio> top5Episodios(Serie serie);

    @Query("SELECT s FROM Serie s " + "JOIN s.episodios e " + "GROUP BY s " + "ORDER BY MAX (e.fechaDeLanzamiento) DESC LIMIT 5")
    List<Serie>  lanzamientosMasRecientes();

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numeroDeTemporada")
    List<Episodio> obtenerTemporadasPorNumero(Long id, Long numeroDeTemporada);
}
