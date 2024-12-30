package com.aluracursos.screenmatch.service;


import com.aluracursos.screenmatch.dto.EpisodioDto;
import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.CategoriaEnum;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.ISerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private ISerieRepository serieRepository;

    public List<SerieDTO> getSeries() {
        return toDTOList(serieRepository.findAll());
    }

    public SerieDTO getSerieByTitulo(@PathVariable String titulo) {
        return toDTO(serieRepository.findByTituloContainsIgnoreCase(titulo.replace("+", " ")));
    }

    public List<SerieDTO> getTop5() {
        return toDTOList(serieRepository.findTop5ByOrderByEvaluacionDesc());
    }

    public List<SerieDTO> getLanzamientosRecientes(){
        return toDTOList(serieRepository.lanzamientosMasRecientes());
    }

    public SerieDTO getById(Long id) {
        return toDTO(serieRepository.findById(id));
    }

    public List<EpisodioDto> getAllTemp(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDto(
                           e.getTemporada(),
                           e.getTitulo(),
                           e.getNumeroEpisodio()
                    )).collect(Collectors.toList());
        }else {
            throw new RuntimeException("Serie not found");
        }
    }

    public List<EpisodioDto> getEpisodiosById(Long id, Long numeroDeTemporada) {
        return serieRepository.obtenerTemporadasPorNumero(id, numeroDeTemporada).stream()
                .map(e -> new EpisodioDto(
                        e.getTemporada(),
                        e.getTitulo(),
                        e.getNumeroEpisodio()
                )).collect(Collectors.toList());
    }

    public List<SerieDTO> getSerieByGenero(String genero) {
        CategoriaEnum categoria = CategoriaEnum.fromEspanol(genero);
        return toDTOList(serieRepository.findByGenero(categoria));
    }

    private List<SerieDTO> toDTOList(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(
                        s.getId(),
                        s.getTitulo(),
                        s.getTotalTemporadas(),
                        s.getEvaluacion(),
                        s.getPoster(),
                        s.getGenero(),
                        s.getActores(),
                        s.getSinopsis()
                )).collect(Collectors.toList());
    }
    private SerieDTO toDTO(Optional<Serie> s) {
        if (s.isPresent()) {
            return new SerieDTO(
                    s.get().getId(),
                    s.get().getTitulo(),
                    s.get().getTotalTemporadas(),
                    s.get().getEvaluacion(),
                    s.get().getPoster(),
                    s.get().getGenero(),
                    s.get().getActores(),
                    s.get().getSinopsis()
            );
        } else {
            throw new RuntimeException("Serie not found");
        }
    }



}
