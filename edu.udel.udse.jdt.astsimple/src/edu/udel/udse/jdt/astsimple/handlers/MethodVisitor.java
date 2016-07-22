package edu.udel.udse.jdt.astsimple.handlers;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

public class MethodVisitor extends ASTVisitor {

	List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	
	@Override
	public boolean visit(MethodDeclaration node){
		methods.add(node);
		return super.visit(node);
	}
	
	public List<MethodDeclaration> getMethods(){
		
		return methods;
	}
}
