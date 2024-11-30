package com.aluracursos.screenmatch.model;

public enum CategoriaEnum {

    ACCION("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIMEN("Crime");

    private String categoriaOmdb;

    CategoriaEnum(String categoriaOmdb) {
        this.categoriaOmdb = categoriaOmdb;
    }

    public static CategoriaEnum fromString(String text) {
        for (CategoriaEnum categoria : CategoriaEnum.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }

}
