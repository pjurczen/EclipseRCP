package pl.books.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;

public enum ModelProvider {
    INSTANCE;

    private List<Book> books;

    private ModelProvider() {
        books = new ArrayList<Book>();
        books.add(new Book("W pustyni i w puszczy", "Henryk Sienkiewicz", "1. Karol Nowak \r\n"+ "2. Bogdan Ziemek"));
        books.add(new Book("Pilot i ja", "Adam Bohdaj", "1. Piotr Adamowski \r\n"+"2. Leszek Karzol"));
        books.add(new Book("Harry Potter i kamieñ filozoficzny", "J. K. Rowling", "1. Piotr Jurczenko"));
    }

    public WritableList getBooks() {
      return new WritableList(books, Book.class);
    }

}
