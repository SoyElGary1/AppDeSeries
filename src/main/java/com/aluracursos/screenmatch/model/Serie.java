package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Optional;
import java.util.OptionalDouble;

public class Serie {

    private String titulo;
    private Integer totalTemporadas;
    private Double evaluacion;
    private String poster;
    private CategoriaEnum genero;
    private String actores;
    private String sinopsis;


    public Serie() {
    }

    public Serie (DatosSerie datosSerie) {
        this.titulo = datosSerie.titulo();
        this.totalTemporadas  = datosSerie.totalTemporadas();
        this.evaluacion = OptionalDouble.of(Double.parseDouble(datosSerie.evaluacion())).orElse(0);
        this.poster = datosSerie.poster();
        this.genero = CategoriaEnum.fromString(datosSerie.genero().split(",")[0].trim());
        this.actores = datosSerie.actores();
        this.sinopsis = datosSerie.sinopsis();
    }

}
