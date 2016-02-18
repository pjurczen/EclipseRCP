package pl.books.dialog;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import pl.books.model.Book;

public class BookDialog extends TitleAreaDialog {

    private Text titleText;
    private Text authorText;
    private Text lendText;

    private String title = "";
    private String author = "";
    private String lendHistory = "";
    
    private Book book;
    
    public BookDialog(Shell parentShell) {
        super(parentShell);
    }
    
    public BookDialog(Shell parentShell, String tittle, String author, String lendHistory) {
        super(parentShell);
        this.title = tittle;
        this.author = author;
        this.lendHistory = lendHistory;
    }

    @Override
    protected Control createContents(Composite parent) {
        Control contents = super.createContents(parent);
        if(title.isEmpty()) {
            setTitle("Add a book");
            setMessage("Please fill in the book data", IMessageProvider.INFORMATION);
        } else {
            setTitle("Edit a book");
            setMessage("Please change the data you would like to edit", IMessageProvider.INFORMATION);
        }
        return contents;
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        createTitleText(container);
        createAuthorText(container);
        createLendHistoryText(container);

        return area;
    }
    
    @Override
    protected boolean isResizable() {
        return true;
    }
    
    @Override
    protected void okPressed() {
        if (titleText.getText().length() != 0 && authorText.getText().length() != 0) {
            book = new Book(titleText.getText(), authorText.getText(), lendText.getText());
            super.okPressed();
        } else {
            setErrorMessage("Both title and author are obligatory.");
        }
    }

    private void createTitleText(Composite container) {
        Label lblFirstName = new Label(container, SWT.NONE);
        lblFirstName.setText("Title");

        GridData dataTitle = new GridData();
        dataTitle.grabExcessHorizontalSpace = true;
        dataTitle.horizontalAlignment = GridData.FILL;

        titleText = new Text(container, SWT.BORDER);
        titleText.setLayoutData(dataTitle);
        titleText.setText(title);
    }

    private void createAuthorText(Composite container) {
        Label lblLastName = new Label(container, SWT.NONE);
        lblLastName.setText("Author");

        GridData dataAuthor = new GridData();
        dataAuthor.grabExcessHorizontalSpace = true;
        dataAuthor.horizontalAlignment = GridData.FILL;

        authorText = new Text(container, SWT.BORDER);
        authorText.setLayoutData(dataAuthor);
        authorText.setText(author);
    }

    private void createLendHistoryText(Composite container) {
        Label lblLastName = new Label(container, SWT.NONE);
        lblLastName.setText("Lend history");
        
        lendText = new Text(container, SWT.BORDER | SWT.WRAP | SWT.MULTI);
        lendText.setLayoutData(new GridData(GridData.FILL_BOTH));
        lendText.setText(lendHistory);
    }
    
    public Book getBook() {
        return book;
    }
}
