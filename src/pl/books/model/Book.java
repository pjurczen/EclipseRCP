package pl.books.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Book {
    private String title;
    private String author;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    public Book() {
        
    }
    
    public Book(String title, String author) {
        this.author = author;
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void addPropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    
    public void setAuthor(String author) {
        propertyChangeSupport.firePropertyChange("author", this.author, this.author = author);
    }
    
    public void setTitle(String title) {
        propertyChangeSupport.firePropertyChange("title", this.title, this.title = title);
    }

    
}
