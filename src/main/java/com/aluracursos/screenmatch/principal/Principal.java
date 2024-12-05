package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.ISerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=2225374d";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private ISerieRepository serieRepository;
    private List<Serie> serieList;


    public Principal() {

    }

    public Principal(ISerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void muestraElMenu() {



        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4-  Buscar series por titulo
                    5-  Buscar top 5 series con mejores evaluaciones
                    6-  Buscar series por categoria
                    7-  Buscar serie por una cantidad de temporadas y por su evaluación
                    8-  Buscar episodios por titulo
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    BuscarTop5Series();
                    break;
                case 6:
                    BuscarSeriePorCategoria();
                    break;
                case 7:
                    BuscarSeriePorNumeroTemporadasYEvaluacion();
                    break;
                case 8:
                    BuscarEpisodiosPorTitulo();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }




    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el serie que deseas buscar sus episodios");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = serieList.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodioList =  temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodioList);
            serieRepository.save(serieEncontrada);
        }

    }

    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        serieRepository.save(serie);
        //datosSeries.add(datos);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {

        serieList = serieRepository.findAll();

        serieList.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);


    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escribe el serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        Optional<Serie> serieBuscada = serieRepository.findByTituloContainsIgnoreCase(nombreSerie.toLowerCase());
        if (serieBuscada.isPresent()) {
            System.out.println("Serie encontrada es: " + serieBuscada.get());
        }else {
            System.out.println("Serie no encontrada");
        }
    }

    private void BuscarTop5Series() {
        List<Serie> top5Series = serieRepository.findTop5ByOrderByEvaluacionDesc();
        top5Series.forEach(s-> System.out.printf("%s - %s\n", s.getTitulo(), s.getEvaluacion()));
    }

    private void BuscarSeriePorCategoria() {
        System.out.println("Escribe el genero de las series que deseas buscar");
        var nombreGenero = teclado.nextLine();
        var categoria = CategoriaEnum.fromEspanol(nombreGenero);
        List<Serie> seriesPorCategoria = serieRepository.findByGenero(categoria);
        System.out.println("Las series del genero: " + nombreGenero);
        seriesPorCategoria.forEach(s-> System.out.printf("%s - %s\n", s.getTitulo(), s.getGenero()));
    }

    private void BuscarSeriePorNumeroTemporadasYEvaluacion() {
        System.out.println("Ingrese la cantidad de temporadas de la serie");
        var numeroTemporadas = teclado.nextInt();
        System.out.println("Ingrese la evaluacion de la serie");
        var evaluacion = teclado.nextDouble();

        List<Serie> serie = serieRepository.seriesPorTemporadaYEvaluacion(numeroTemporadas, evaluacion);
        if (!serie.isEmpty()) {
            for (Serie serieEncontrada : serie) {
                System.out.println("Serie encontrada es: " + serieEncontrada);
            }
        }
    }

    private void BuscarEpisodiosPorTitulo() {
        System.out.println("Escribe el titulo del episodio que deseas buscar");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodioList = serieRepository.episodiosPorNombre(nombreEpisodio);
        episodioList.forEach(e-> System.out.printf("Serie: %s Temporada %s Episodio %s Evaluación %s\n",
                e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));
    }



}

