package br.com.alura.screenmatch.model;

public enum Categoria {

    //constantes que representam as categorias de filmes, foi escolhido o tipo "enum" para
    //garantir que apenas essas categorias sejam utilizadas
    //o tipo "enum" é uma classe especial que representa um grupo de constantes

    ACAO("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime");

    //atributo que representa a categoria no formato da API OMDB
    private String categoriaOmdb;

    //construtor que recebe a categoria no formato da API OMDB
    //e associa a constante
    Categoria(String categoriaOmdb){
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
