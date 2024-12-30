package com.aluracursos.screenmatch.controller;

import com.aluracursos.screenmatch.dto.EpisodioDto;
import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService servicio;

    @GetMapping()
    public List<SerieDTO> getSeries() {
        return servicio.getSeries() != null ? servicio.getSeries() : new ArrayList<>();
    }

    /*@GetMapping("/{titulo}")
    public SerieDTO getSerieByTitulo(@PathVariable String titulo) {
        return servicio.getSerieByTitulo(titulo);
    }*/

    @GetMapping("/top5")
    public List<SerieDTO> getTop5Series() {
        return servicio.getTop5();
    }

    @GetMapping("/lanzamientos")
    public List<SerieDTO> getLanzamientos() {
        return servicio.getLanzamientosRecientes();
    }

    @GetMapping("/{id}")
    public SerieDTO getSerieById(@PathVariable Long id) {
        return servicio.getById(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDto> getAllSerieById(@PathVariable Long id) {
        return servicio.getAllTemp(id);
    }

    @GetMapping("/{id}/temporadas/{numeroDeTemporada}")
    public List<EpisodioDto> getAllSerieById(@PathVariable Long id, @PathVariable Long numeroDeTemporada) {
        return servicio.getEpisodiosById(id, numeroDeTemporada);
    }

    @GetMapping("/categoria/{genero}")
    public List<SerieDTO> getAllSerieByGenero(@PathVariable String genero) {
        return servicio.getSerieByGenero(genero);
    }
}
