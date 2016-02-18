package pl.books.app;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private IWorkbenchAction saveAction;
    private IWorkbenchAction saveAllAction;
    
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

    protected void makeActions(final IWorkbenchWindow window) {
        saveAction = ActionFactory.SAVE.create(window);
        register(saveAction);

        saveAllAction = ActionFactory.SAVE_ALL.create(window);
        register(saveAllAction);
    }


    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager saveToolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        saveToolbar.add(saveAction);
        saveToolbar.add(saveAllAction);
        coolBar.add(new ToolBarContributionItem(saveToolbar, "save"));
    }
}
