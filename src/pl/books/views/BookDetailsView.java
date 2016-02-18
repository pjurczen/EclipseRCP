package pl.books.views;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import pl.books.model.Book;

public class BookDetailsView extends ViewPart {
    
    private DataBindingContext dbc;
    private Book book;
    
    public BookDetailsView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout(2, false));

        Label lendHistory = new Label(parent, SWT.NONE);
        lendHistory.setText("Lend history");
        new Label(parent, SWT.NONE);
        
        Text lendHistoryData = new Text(parent,  SWT.WRAP | SWT.MULTI);
        lendHistoryData.setLayoutData(new GridData(GridData.FILL_BOTH));
        lendHistoryData.setVisible(false);
        lendHistoryData.setEditable(false);
        
        getSite().getPage().addSelectionListener(new ISelectionListener() {
            @Override
            public void selectionChanged(IWorkbenchPart part, ISelection selection) {
                if (selection != null & selection instanceof IStructuredSelection) {
                    IStructuredSelection strucSelection = (IStructuredSelection) selection;
                    Object obj = strucSelection.getFirstElement();
                    if (obj instanceof Book) {
                        book = (Book) obj;
                        lendHistoryData.setText(book.getLendHistory());
                        lendHistoryData.setVisible(true);
                        
                        dbc = new DataBindingContext();
                        IObservableValue model = BeanProperties.value("lendHistory").observe(book);
                        IObservableValue target = WidgetProperties.text(SWT.Modify).observe(lendHistoryData);
                        dbc.bindValue(target, model);
                    }
                }
            }
        });
    }

    @Override
    public void setFocus() {

    }

}
