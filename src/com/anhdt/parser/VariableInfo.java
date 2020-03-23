package com.anhdt.parser;

import java.util.ArrayList;
import java.util.List;

public class VariableInfo {
	
	private final String GET = "get";
	private final String SET = "set";
	
	
	
	private String varName;
	private String varType;
	private List<String> varJsonNodes = new ArrayList<String>();
	private boolean isSet = false;
	private boolean isGet = false;
	
	public String getVarName() {
		return varName;
	}
	
	public void setVarName(String varName) {
		this.varName = varName;
	}
	
	public String getVarType() {
		return varType;
	}
	
	public void setVarType(String varType) {
		this.varType = varType;
	}
	
	public boolean isSet() {
		return isSet;
	}
	
	public void setSet(boolean isSet) {
		this.isSet = isSet;
	}
	
	public boolean isGet() {
		return isGet;
	}
	
	public void setGet(boolean isGet) {
		this.isGet = isGet;
	}
	
	public List<String> getVarJsonNodes() {
		return varJsonNodes;
	}
	
	public void setVarJsonNodes(List<String> varJsonNodes) {
		this.varJsonNodes = varJsonNodes;
	}
	
	public String getFunctionName() {
		String result = GET;
		if (varName != null && varName.length() > 0) {
			result += varName.substring(0, 1).toUpperCase() + varName.substring(1);
		}
		return result;
	}
	
	public String setFunctionName() {
		String result = SET;
		if (varName != null && varName.length() > 0) {
			result += varName.substring(0, 1).toUpperCase() + varName.substring(1);
		}
		return result;
	}
	
	public String generateSetFunction() {
		StringBuilder builder = new StringBuilder();
		builder.append("\npublic void " + setFunctionName() + "(" + varType + " " + varName + ") {");
		builder.append("\n\tthis." + varName + " = " + varName + ";");
		builder.append("\n}");
		return builder.toString();
	}
	
	public String generateGetFunction() {
		StringBuilder builder = new StringBuilder();
		builder.append("\npublic " + varType + " " + getFunctionName() + "() {");
		builder.append("\n\treturn this." + varName + ";");
		builder.append("\n}");
		return builder.toString();
	}
	
	@Override
	public String toString() {
		String dataParse = "\nVar Name = " + varName;
		dataParse += "\n Var Type = " + varType;
		for (int i = 0; i < varJsonNodes.size(); i++) {
			String info = varJsonNodes.get(i);
			dataParse += "\nVariable node " + i + " = ";
			dataParse += info;
		}
		
		return dataParse;
	}
	
}
