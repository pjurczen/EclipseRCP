package pl.books.model;

import java.util.ArrayList;
import java.util.List;

public enum ModelProvider {
    INSTANCE;

    private List<Book> books;

    private ModelProvider() {
        books = new ArrayList<Book>();
        books.add(new Book("W pustyni i w puszczy", "Henryk Sienkiewicz"));
        books.add(new Book("Pilot i ja", "Adam Bohdaj"));
    }

    public List<Book> getBooks() {
      return books;
    }
}
