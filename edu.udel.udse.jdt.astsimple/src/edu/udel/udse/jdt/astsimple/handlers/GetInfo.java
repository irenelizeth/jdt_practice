package edu.udel.udse.jdt.astsimple.handlers;

import javax.xml.bind.ParseConversionEvent;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class GetInfo extends AbstractHandler {

	private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		
		//get all projects in the workspace
		IProject[] projects = workspaceRoot.getProjects();
		
		for(IProject prj : projects){
			
			try{
				
				if(prj.isOpen() && prj.isNatureEnabled(JDT_NATURE)){
					analyzeMethods(prj);
				}
				
			}catch(CoreException e){
				e.printStackTrace();
			}
			
		}
		
		/*IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"Astsimple Plug-in",
				"Get Information AST");
		*/
		return null;
	}

	private void analyzeMethods(IProject prj) 
		throws JavaModelException {
		
		IPackageFragment[] packages = 
				JavaCore.create(prj).getPackageFragments();
		
		for (IPackageFragment pkg : packages){
			if(pkg.getKind() == IPackageFragmentRoot.K_SOURCE){
				createAST(pkg);
			}
		}
		
	}

	private void createAST(IPackageFragment pkg)
		throws JavaModelException {

		for(ICompilationUnit unit : pkg.getCompilationUnits()){
			
			//crete AST for compilation units
			CompilationUnit parse = parse(unit);
			MethodVisitor visitor = new MethodVisitor();
			
			parse.accept(visitor);
			System.out.println("\tFor Package: "+ pkg.getElementName());
			
			for(MethodDeclaration method: visitor.getMethods()){
				System.out.println("Method name "+ method.getName() 
				+ " --> Return type " + method.getReturnType2());
				
			}
		}
		
	}

	/**
	 * Reads a ICompilationUnit and creates the AST DOM for manupulating
	 * the Java Source file
	 * @param unit
	 * @return
	 * */
	private CompilationUnit parse(ICompilationUnit unit) {

		// AST.JLS8 to support latest Java version 1.8 and previous ones
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null);
	}
}
