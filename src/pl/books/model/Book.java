package pl.books.model;

public class Book extends ModelObject {
    private String title;
    private String author;
    private String lendHistory = "";
    
    public Book() {
        
    }
    
    public Book(String title, String author) {
        this.author = author;
        this.title = title;
    }
    
    public Book(String title, String author, String lendHistory) {
        this.author = author;
        this.title = title;
        this.lendHistory = lendHistory;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getLendHistory() {
        return lendHistory;
    }
    
    public void setAuthor(String author) {
        propertyChangeSupport.firePropertyChange("author", this.author, this.author = author);
    }
    
    public void setTitle(String title) {
        propertyChangeSupport.firePropertyChange("title", this.title, this.title = title);
    }
    
    public void setLendHistory(String lendHistory) {
        propertyChangeSupport.firePropertyChange("lendHistory", this.lendHistory, this.lendHistory = lendHistory);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((author == null) ? 0 : author.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Book other = (Book) obj;
        if (author == null) {
            if (other.author != null)
                return false;
        } else if (!author.equals(other.author))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }
}
