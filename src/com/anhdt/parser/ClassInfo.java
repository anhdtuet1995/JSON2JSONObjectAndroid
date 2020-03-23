package com.anhdt.parser;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {

	private String className = "";
	private int startIndex = -1;
	private int endIndex = -1;
	private String baseClass;
	private List<MethodInfo> methodInfoList = new ArrayList<MethodInfo>();
	private List<VariableInfo> variableInfoList = new ArrayList<VariableInfo>();

	public ClassInfo() {

	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public String getBaseClass() {
		return baseClass;
	}

	public void setBaseClass(String baseClass) {
		this.baseClass = baseClass;
	}

	public List<MethodInfo> getMethodInfoList() {
		return methodInfoList;
	}

	public void setMethodInfoList(List<MethodInfo> methodInfoList) {
		this.methodInfoList = methodInfoList;
	}

	public List<VariableInfo> getVariableInfoList() {
		return variableInfoList;
	}

	public void setVariableInfoList(List<VariableInfo> variableInfoList) {
		this.variableInfoList = variableInfoList;
	}

	public String convertJSONObject() {
		StringBuilder builder = new StringBuilder();
		builder.append("\npublic " + this.className + "(JSONObject jsonObject) throws JSONException {");
		if (baseClass != null && baseClass.length() > 0) {
			builder.append("\n\tsuper(jsonObject);");
		}
		for (VariableInfo info: variableInfoList) {
			List<String> varJsonNodes = info.getVarJsonNodes();
			if (varJsonNodes.size() > 0) {
				String name = varJsonNodes.get(0);
				switch (Utils.getTypeOfVarType(info.getVarType())) {
				case Utils.INT_TYPE:
				case Utils.LONG_TYPE:
				case Utils.DOUBLE_TYPE:
				case Utils.BOOLEAN_TYPE:
				case Utils.STRING_TYPE :
					builder.append("\n\t" + info.getVarType() + " " + info.getVarName() + " = jsonObject." + Utils.getJSONObjectFunction(info.getVarType()) + "(\"" + name + "\");");
					if (varJsonNodes.size() > 1) {
						for (int i = 1; i < varJsonNodes.size(); i++) {
							String otherName = varJsonNodes.get(i);
							if (i == 1) {
								builder.append("\n\tif (jsonObject.has(\"" + otherName + "\")) {");
							} else {
								builder.append(" else if (jsonObject.has(\"" + otherName + "\")) {");
							}
							builder.append("\n\t\t" + info.getVarName() + " = jsonObject." + Utils.getJSONObjectFunction(info.getVarType()) + "(\"" + otherName + "\");");
							builder.append("\n\t}");
						}
					}
					builder.append("\n\tthis." + info.getVarName() + " = " + info.getVarName() + ";");
					break;
				case Utils.OBJECT_TYPE:
					builder.append("\n\tJSONObject " + info.getVarName() + " = jsonObject." + Utils.getJSONObjectFunction(info.getVarType()) + "(\"" + name + "\");");
					if (varJsonNodes.size() > 1) {
						for (int i = 1; i < varJsonNodes.size(); i++) {
							String otherName = varJsonNodes.get(i);
							if (i == 1) {
								builder.append("\n\tif (jsonObject.has(\"" + otherName + "\")) {");
							} else {
								builder.append(" else if (jsonObject.has(\"" + otherName + "\")) {");
							}
							builder.append("\n\t\t" + info.getVarName() + " = jsonObject." + Utils.getJSONObjectFunction(info.getVarType()) + "(\"" + otherName + "\");");
							builder.append("\n\t}");
						}
					}
					builder.append("\n\tthis." + info.getVarName() + " = new " + info.getVarType() + "(" + info.getVarName() + ");");
					break;
				case Utils.LIST_TYPE:
					builder.append("\n\tJSONArray " + info.getVarName() + " = jsonObject." + Utils.getJSONObjectFunction(info.getVarType()) + "(\"" + name + "\");");
					if (varJsonNodes.size() > 1) {
						for (int i = 1; i < varJsonNodes.size(); i++) {
							String otherName = varJsonNodes.get(i);
							if (i == 1) {
								builder.append("\n\tif (jsonObject.has(\"" + otherName + "\")) {");
							} else {
								builder.append(" else if (jsonObject.has(\"" + otherName + "\")) {");
							}
							builder.append("\n\t\t" + info.getVarName() + " = jsonObject." + Utils.getJSONObjectFunction(info.getVarType()) + "(\"" + otherName + "\");");
							builder.append("\n\t}");
						}
					}
					String listName = info.getVarName() + "List";
					traversalListDepth(builder, info.getVarType(), info.getVarName(), listName);
					builder.append("\n\tthis." + info.getVarName() + " = " + listName + ";");
					break;
				case Utils.FLOAT_TYPE:
					builder.append("\n\tfloat " + info.getVarName() + " = (float) jsonObject." + Utils.getJSONObjectFunction(info.getVarType()) + "(\"" + name + "\");");
					if (varJsonNodes.size() > 1) {
						for (int i = 1; i < varJsonNodes.size(); i++) {
							String otherName = varJsonNodes.get(i);
							if (i == 1) {
								builder.append("\n\tif (jsonObject.has(\"" + otherName + "\")) {");
							} else {
								builder.append(" else if (jsonObject.has(\"" + otherName + "\")) {");
							}
							builder.append("\n\t\t" + info.getVarName() + " = (float) jsonObject." + Utils.getJSONObjectFunction(info.getVarType()) + "(\"" + otherName + "\");");
							builder.append("\n\t}");
						}
					}
					builder.append("\n\tthis." + info.getVarName() + " = " + info.getVarName() + ";");
					break;
				}


			}


		}
		builder.append("\n}");
		return builder.toString();
	}
	
	private char countVar = 'a' - 1;
	private int count = 0;
	
	private void traversalListDepth(StringBuilder builder, String varType, String varName, String listName) {
		if (Utils.getTypeOfVarType(varType) != Utils.LIST_TYPE) {
			//stop condition
			return;
		}
		countVar++;
		count++;
		String currentType = varType.substring(varType.indexOf('<') + 1, varType.lastIndexOf('>'));
		String currentName = varName + count;
		String currentListName = listName + count;
		builder.append("\n\t" + varType + " " + listName + " = new ArrayList<>();");
		builder.append("\n\tfor (int " + countVar + " = 0; " + countVar + " < " + varName + ".length(); " + countVar + "++) {");
		switch (Utils.getTypeOfVarType(currentType)) {
			case Utils.INT_TYPE:
			case Utils.LONG_TYPE:
			case Utils.DOUBLE_TYPE:
			case Utils.BOOLEAN_TYPE:
			case Utils.STRING_TYPE:
				builder.append("\n\t\t" + currentType + " " + currentName + " = " + varName + "." + Utils.getJSONObjectFunction(currentType) + "(" + countVar +");");
				builder.append("\n\t\t" + listName + ".add(" + currentName + ");");
				break;
			case Utils.OBJECT_TYPE:
				builder.append("\n\t\t" + currentType + " " + currentName + " = new " + currentType + "(" + varName + "." + Utils.getJSONObjectFunction(currentType) + "(" + countVar +"));");
				builder.append("\n\t\t" + listName + ".add(" + currentName + ");");
				break;
			case Utils.FLOAT_TYPE:
				builder.append("\n\t\t" + currentType + " " + currentName + " = (float) " + varName + "." + Utils.getJSONObjectFunction(currentType) + "(" + countVar +");");
				builder.append("\n\t\t" + listName + ".add(" + currentName + ");");
				break;
			case Utils.LIST_TYPE:
				builder.append("\n\t\tJSONArray " + currentName + " = " + varName + "." + Utils.getJSONObjectFunction(currentType) + "(" + countVar +");");
				traversalListDepth(builder, currentType, currentName, currentListName);
				builder.append("\n\t" + listName + ".add(" + currentListName + ");");
				break;
		}
		builder.append("\n\t}");
		
	}

	@Override
	public String toString() {
		String dataParse = "Class Name = " + className;
		dataParse += "\nBase Class Name = " + baseClass;
		dataParse += "\n Start index = " + startIndex; 
		dataParse += "\n End index = " + endIndex;
		for (int i = 0; i < variableInfoList.size(); i++) {
			VariableInfo info = variableInfoList.get(i);
			dataParse += "\nVariable " + i + ": ";
			dataParse += info.toString();
		}
		for (int i = 0; i < methodInfoList.size(); i++) {
			MethodInfo info = methodInfoList.get(i);
			dataParse += "\nMethod " + i + ": ";
			dataParse += info.toString();
		}
		dataParse += "\n=======================";
		return dataParse;
	}


}
