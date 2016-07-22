package edu.udel.udse.jdt.newelements.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class AddPackage extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		
		//get all projects in the workspace
		IProject[] projects = root.getProjects();
		
		for(IProject prj : projects){
			try{
				//only work with open projects with java nature
				if(prj.isOpen() && prj.isNatureEnabled(JavaCore.NATURE_ID)){
					
					createPackage(prj);
				}
			}catch(CoreException e){
				e.printStackTrace();
			}
		}
		
		/*
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"Newelements",
				"Adding package to project!");
		*/
		return null;
	}

	private void createPackage(IProject prj) 
		throws JavaModelException {

		IJavaProject javaPrj = JavaCore.create(prj);
		IFolder folder = prj.getFolder("src");
		IPackageFragmentRoot
				srcFolder = javaPrj.getPackageFragmentRoot(folder);
		
		IPackageFragment fragment = 
				srcFolder.createPackageFragment(prj.getName(), true, null);
		
	}
}
