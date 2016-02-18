package pl.books.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import pl.books.dialog.BookDialog;
import pl.books.model.Book;
import pl.books.model.BookComparator;
import pl.books.model.BookFilter;
import pl.books.model.ModelProvider;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class BooksListView extends ViewPart {
    public BooksListView() {
    }
    
    private TableViewer viewer;
    private BookFilter filter;
    private BookComparator comparator;
    private Text searchText;
    private IObservableList input;

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
        hookDoubleClickCommand();
    }
    
    public TableViewer getViewer() {
        return viewer;
    }
    
    private void hookDoubleClickCommand() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                IHandlerService handlerService = getSite().getService(IHandlerService.class);
                try {
                    handlerService.executeCommand("pl.books.command.openEditor", null);
                } catch (Exception ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }
        });
    }

    private void createViewer(Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
            | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns(parent, viewer);
        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        input = new WritableList(ModelProvider.INSTANCE.getBooks(), Book.class);
        ViewerSupport.bind(viewer, input, BeanProperties.values(new String[] { "title", "author" })); 
        
        getSite().setSelectionProvider(viewer);
        
        searchText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent keyEvent) {
                filter.setSearchText(searchText.getText());
                viewer.refresh();
            }
        });
        
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);
        
        createContextMenu(viewer);
    }
    
    private void createColumns(final Composite parent, final TableViewer viewer) {
        String[] titles = { "Book title", "Author" };
        int[] bounds = { 200, 164 };

        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Book book = (Book) element;
                return book.getTitle();
            }
        });

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
        column.addSelectionListener(getSelectionAdapter(column, colNumber));
        return viewerColumn;
    }
    
    private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
        SelectionAdapter selectionAdapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                comparator.setColumn(index);
                int dir = comparator.getDirection();
                viewer.getTable().setSortDirection(dir);
                viewer.getTable().setSortColumn(column);
                viewer.refresh();
            }
        };
        return selectionAdapter;
    }

    private void createContextMenu(Viewer viewer) {
        MenuManager contextMenu = new MenuManager("#ViewerMenu"); //$NON-NLS-1$
        contextMenu.setRemoveAllWhenShown(true);
        contextMenu.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager mgr) {
                fillContextMenu(mgr);
            }
        });

        Menu menu = contextMenu.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
    }
    
    private void fillContextMenu(IMenuManager contextMenu) {
        contextMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

        IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
        
        contextMenu.add(new Action("Add") {
            @Override
            public void run() {
                BookDialog dialog = new BookDialog(getViewSite().getShell());
                dialog.open();
                if (dialog.getBook() != null) {
                    input.add(dialog.getBook());
                }
                viewer.refresh();
            }
        });
        
        contextMenu.add(new Action("Edit") {
            @Override
            public void run() {
                Book selectedBook = (Book) selection.getFirstElement();
                try {
                    BookDialog dialog = new BookDialog(getViewSite().getShell(),
                            selectedBook.getTitle(), selectedBook.getAuthor(), selectedBook.getLendHistory());
                    dialog.open();
                    if (dialog.getBook() != null && !dialog.getBook().equals(selectedBook)) {
                        selectedBook.setAuthor(dialog.getBook().getAuthor());
                        selectedBook.setTitle(dialog.getBook().getTitle());
                    }
                } catch (NullPointerException e) {
                    MessageDialog.openError(getViewSite().getShell(), "Failure", "Select book to edit!");
                }
                viewer.refresh();
            }
        });
        
        contextMenu.add(new Action("Remove") {
            @Override
            public void run() {
                if(!input.remove((Book) selection.getFirstElement())) {
                    MessageDialog.openError(getViewSite().getShell(), "Failure", "Select book to remove!");
                }
                viewer.refresh();
            }
        });
    }
    
    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }
}
