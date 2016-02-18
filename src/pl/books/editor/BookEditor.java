package pl.books.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import pl.books.model.Book;

public class BookEditor extends EditorPart {

    public static final String ID = "pl.books.editor.BookEditor";
    private BookEditorInput input;
    private Book book;
    protected boolean dirty = false;
    private Text bookTitle;
    private Text bookAuthors;
    private Text bookLendHistory;
    
    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
      if (!(input instanceof BookEditorInput)) {
        throw new RuntimeException("Wrong input");
      }

      this.input = (BookEditorInput) input;
      setSite(site);
      setInput(input);
      
      book = this.input.getBook();
      setPartName("Editing book: " + book.getTitle());
    }
    
    @Override
    public void createPartControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        parent.setLayout(layout);
        
        new Label(parent, SWT.NONE).setText("Book title:");
        bookTitle = new Text(parent, SWT.BORDER);
        bookTitle.setText(book.getTitle());
        bookTitle.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        
        new Label(parent, SWT.NONE).setText("Book authors:");
        bookAuthors = new Text(parent, SWT.BORDER);
        bookAuthors.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        bookAuthors.setText(book.getAuthor());
        
        new Label(parent, SWT.NONE).setText("Book lend history:");
        bookLendHistory = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        bookLendHistory.setLayoutData(new GridData(GridData.FILL_BOTH));
        bookLendHistory.setText(book.getLendHistory());
        
        ModifyListener listener = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                setDirty(true);
            }
        };
        bookTitle.addModifyListener(listener);
        bookAuthors.addModifyListener(listener);
        bookLendHistory.addModifyListener(listener);
        
    }
    
    @Override
    public void doSave(IProgressMonitor monitor) {
        book.setTitle(bookTitle.getText());
        book.setAuthor(bookAuthors.getText());
        book.setLendHistory(bookLendHistory.getText());
        setDirty(false);
    }

    private void setDirty(boolean dirty) {
        this.dirty = dirty;
        firePropertyChange(PROP_DIRTY);
    }

    @Override
    public void doSaveAs() {
        doSave(null);
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void setFocus() {

    }

}
