package edu.udel.udse.jdt.infos.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		//get the root of the workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		
		//get all projects in the workspace
		IProject[] projects = root.getProjects();
		
		//loop aver all projects
		for (IProject prj : projects){
			try{
				printProjectInfo(prj);
			}catch(CoreException e){
				e.printStackTrace();
			}
			
		}
		
		
		  /*
		     //IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		      MessageDialog.openInformation(
				window.getShell(),
				"Infos",
				"Hello, Eclipse world");*/
		
		return null;
	}

	private void printProjectInfo(IProject prj)
			throws CoreException, JavaModelException{
		
		System.out.println("Working in project " +prj.getName());
		
		//check if we have a java project
		if(prj.isNatureEnabled("org.eclipse.jdt.core.javanature")){
			IJavaProject javaPrj = JavaCore.create(prj);
			System.out.println("Java Project!");
			printPackageInfos(javaPrj);
		}else{
			System.out.println("Something bad happened!");
		}
		
		
	}

	private void printPackageInfos(IJavaProject javaPrj) throws JavaModelException{

		IPackageFragment[] packages = javaPrj.getPackageFragments();
		
		for(IPackageFragment pkg : packages){
			// Package fragments include all packages from classpath
			// print information about packages in source folder
			//K_BINARY also include JARs, e.g., jr.jar
			if(pkg.getKind() == IPackageFragmentRoot.K_SOURCE){
				System.out.println("Package "+pkg.getElementName());
				printICompilationUnitInfo(pkg);
			}
		}
	}

	private void printICompilationUnitInfo(IPackageFragment pkg) 
			throws JavaModelException{

		for(ICompilationUnit unit : pkg.getCompilationUnits()){
			printCompilationUnitDetails(unit);
		}
		
	}

	private void printCompilationUnitDetails(ICompilationUnit unit) 
		throws JavaModelException{

		System.out.println("Source file "+unit.getElementName());
		Document doc = new Document(unit.getSource());
		System.out.println("Has number of lines "+doc.getNumberOfLines());
		printIMethods(unit);
		
	}

	private void printIMethods(ICompilationUnit unit)
		throws JavaModelException {
		IType[] allTypes = unit.getAllTypes();
		for(IType type : allTypes){
			printMethodDetails(type);
		}
		
		
	}

	private void printMethodDetails(IType type)
		throws JavaModelException{
		IMethod[] methods = type.getMethods();
		
		for(IMethod method : methods){
			System.out.println("Method name "+ method.getElementName());
			System.out.println("Signature "+ method.getSignature());
			System.out.println("Return Type: "+ method.getReturnType());
		}
	}
}
