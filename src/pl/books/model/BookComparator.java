package pl.books.model;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

public class BookComparator extends ViewerComparator {
    
    private int propertyIndex;
    private static final int DESCENDING = 1;
    private int direction = DESCENDING;

    public BookComparator() {
        this.propertyIndex = 0;
        direction = DESCENDING;
    }

    public int getDirection() {
        return direction == 1 ? SWT.DOWN : SWT.UP;
    }

    public void setColumn(int column) {
        if (column == this.propertyIndex) {
            direction = 1 - direction;
        } else {
            this.propertyIndex = column;
            direction = DESCENDING;
        }
    }
    
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        Book p1 = (Book) e1;
        Book p2 = (Book) e2;
        int rc = 0;
        switch (propertyIndex) {
        case 0:
            rc = p1.getTitle().compareTo(p2.getTitle());
            break;
        case 1:
            rc = p1.getAuthor().compareTo(p2.getAuthor());
            break;
        default:
            rc = 0;
        }
        if (direction == DESCENDING) {
            rc = -rc;
        }
        return rc;
    }
}
