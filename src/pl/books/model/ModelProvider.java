package pl.books.model;

import java.util.ArrayList;
import java.util.List;

public enum ModelProvider {
    INSTANCE;

    private List<Book> books;

    private ModelProvider() {
        books = new ArrayList<Book>();
        books.add(new Book("W pustyni i w puszczy", "Henryk Sienkiewicz", "1. Karol Nowak \n"+ "2. Bogdan Ziemek"));
        books.add(new Book("Pilot i ja", "Adam Bohdaj", "1. Piotr Adamowski \n"+"2. Leszek Karzol"));
    }

    public List<Book> getBooks() {
      return books;
    }

}
