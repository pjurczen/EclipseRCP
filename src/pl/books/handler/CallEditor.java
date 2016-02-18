package pl.books.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import pl.books.editor.BookEditor;
import pl.books.editor.BookEditorInput;
import pl.books.model.Book;

public class CallEditor extends AbstractHandler implements IHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IEditorPart editor = null;
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        IWorkbenchPage page = window.getActivePage();
        ISelection selection = HandlerUtil.getCurrentSelection(event);
        if (selection != null && selection instanceof IStructuredSelection) {
            Object obj = ((IStructuredSelection) selection).getFirstElement();
            if (obj != null) {
                Book book = (Book) obj;
                BookEditorInput input = new BookEditorInput(book);
                try {
                    editor = page.openEditor(input, BookEditor.ID);
                } catch (PartInitException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return editor;
    }

}
