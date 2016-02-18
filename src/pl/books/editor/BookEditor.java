package pl.books.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
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
    private DataBindingContext dbc;
    
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
        
        setBookPropertyListeners();
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
        
        setTextModifyListeners();
        
        UpdateValueStrategy strategy = setValidator();
        
        setBookTitleValidation(strategy);
        setBookAuthorsValidation(strategy);
        
        setDirty(false);
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

    private void setTextModifyListeners() {
        ModifyListener listener = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                if(isFieldsContentsNotEmpty() && isFieldsContentsChanged()) {
                    setDirty(true);
                } else {
                    setDirty(false);
                }
            }

            private boolean isFieldsContentsChanged() {
                return !(bookTitle.getText().equals(book.getTitle()) &&
                        bookAuthors.getText().equals(book.getAuthor()) && bookLendHistory.getText().equals(book.getLendHistory()));
            }

            private boolean isFieldsContentsNotEmpty() {
                return !bookTitle.getText().isEmpty() && !bookAuthors.getText().isEmpty();
            }
        };
        bookTitle.addModifyListener(listener);
        bookAuthors.addModifyListener(listener);
        bookLendHistory.addModifyListener(listener);
    }

    private void setBookTitleValidation(UpdateValueStrategy strategy) {
        Book bookModel = new Book(book.getTitle(), null, null);
        IObservableValue model = BeanProperties.value("title").observe(bookModel);
        IObservableValue target = WidgetProperties.text(SWT.Modify).observe(bookTitle);
        
        dbc = new DataBindingContext();
        Binding bindValue = dbc.bindValue(target, model, strategy, null); 
        
        ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);
    }
    
    private void setBookAuthorsValidation(UpdateValueStrategy strategy) {
        Book bookModel = new Book(null, book.getAuthor(), null);
        IObservableValue model = BeanProperties.value("author").observe(bookModel);
        IObservableValue target = WidgetProperties.text(SWT.Modify).observe(bookAuthors);
        
        dbc = new DataBindingContext();
        Binding bindValue = dbc.bindValue(target, model, strategy, null); 
        
        ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);
    }

    private UpdateValueStrategy setValidator() {
        IValidator validator = new IValidator() {
            @Override
            public IStatus validate(Object value) {
                if (value != null && value.toString().length() > 0) {
                    return ValidationStatus.ok();
                }
                return ValidationStatus.error("Field cannot be empty");
            }
        };
        
        UpdateValueStrategy strategy = new UpdateValueStrategy();
        strategy.setBeforeSetValidator(validator);
        return strategy;
    }

    private void setBookPropertyListeners() {
        this.input.getBook().addPropertyChangeListener("title", new PropertyChangeListener() {
              @Override
              public void propertyChange(PropertyChangeEvent evt) {
                  book.setTitle(evt.getNewValue().toString());
                  bookTitle.setText(book.getTitle());
              }
          });
          
          this.input.getBook().addPropertyChangeListener("author", new PropertyChangeListener() {
              @Override
              public void propertyChange(PropertyChangeEvent evt) {
                  book.setAuthor(evt.getNewValue().toString());
                  bookAuthors.setText(book.getAuthor());
              }
          });
          
          this.input.getBook().addPropertyChangeListener("lendHistory", new PropertyChangeListener() {
              @Override
              public void propertyChange(PropertyChangeEvent evt) {
                  book.setLendHistory(evt.getNewValue().toString());
                  bookLendHistory.setText(book.getLendHistory());
              }
          });
    }
}
