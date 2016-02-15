package pl.books.model;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class BookFilter extends ViewerFilter {

    private String searchString;

    public void setSearchText(String s) {
        this.searchString = ".*" + s + ".*";
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if(searchString == null || searchString.length() == 0) {
            return true;
        }
        Book book = (Book) element;
        if(book.getTitle().toLowerCase().matches(searchString.toLowerCase())) {
            return true;
        }
        if(book.getAuthor().toLowerCase().matches(searchString.toLowerCase())) {
            return true;
        }
        return false;
    }
}
