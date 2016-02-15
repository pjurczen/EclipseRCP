package pl.books.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import pl.books.model.Book;
import pl.books.model.BookComparator;
import pl.books.model.BookFilter;
import pl.books.model.ModelProvider;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class BooksListView extends ViewPart {
    
    private TableViewer viewer;
    private BookFilter filter;
    private BookComparator comparator;
    private Text searchText;

    @Override
    public void createPartControl(Composite parent) {
        GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);
        Label searchLabel = new Label(parent, SWT.NONE);
        searchLabel.setText("Search: ");
        searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
        searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
            | GridData.HORIZONTAL_ALIGN_FILL));
        createViewer(parent);
        
        
        filter = new BookFilter();
        viewer.addFilter(filter);
        
        comparator = new BookComparator();
        viewer.setComparator(comparator);
    }

    private void createViewer(Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
            | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns(parent, viewer);
        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());
        // get the content for the viewer, setInput will call getElements in the
        // contentProvider
        viewer.setInput(ModelProvider.INSTANCE.getBooks());
        // make the selection available to other views
        getSite().setSelectionProvider(viewer);
        // set the sorter for the table
        searchText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent keyEvent) {
                filter.setSearchText(searchText.getText());
                viewer.refresh();
            }
        });
        
        // define layout for the viewer
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);
    }
    
    public TableViewer getViewer() {
        return viewer;
    }
    
    // create the columns for the table
    private void createColumns(final Composite parent, final TableViewer viewer) {
        String[] titles = { "Book title", "Author" };
        int[] bounds = { 200, 164 };

        // first column is for the title
        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Book book = (Book) element;
                return book.getTitle();
            }
        });

        // second column is for the author
        col = createTableViewerColumn(titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Book book = (Book) element;
                return book.getAuthor();
            }
        });
    }

    private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(false);
        column.setMoveable(true);
        return viewerColumn;
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }
}
