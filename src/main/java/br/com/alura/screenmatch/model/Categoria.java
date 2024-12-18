package br.com.alura.screenmatch.model;

public enum Categoria {

    //constantes que representam as categorias de filmes, foi escolhido o tipo "enum" para
    //garantir que apenas essas categorias sejam utilizadas
    //o tipo "enum" é uma classe especial que representa um grupo de constantes

    ACAO("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime");

    //atributo que representa a categoria no formato da API OMDB
    private String categoriaOmdb;
    private String categoriaPtbr;
    //construtor que recebe a categoria no formato da API OMDB
    //e associa a constante
    Categoria(String categoriaOmdb, String categoriaPtbr){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPtbr = categoriaPtbr;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Categoria fromPtbr(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaPtbr.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
