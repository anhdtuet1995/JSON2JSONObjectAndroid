package com.anhdt.parser;

public class Utils {
	
	public final static int INT_TYPE = 0;
	public final static int DOUBLE_TYPE = 1;
	public final static int LONG_TYPE = 2;
	public final static int BOOLEAN_TYPE = 3;
	public final static int STRING_TYPE = 4;
	public final static int OBJECT_TYPE = 5;
	public final static int LIST_TYPE = 6;
	public final static int FLOAT_TYPE = 7;
	
	public static int getTypeOfVarType(String varType) {
		if (varType.equals("Integer") || varType.equals("int")) {
			return INT_TYPE;
		} else if (varType.equals("String")) {
			return STRING_TYPE;
		} else if (varType.equals("Long") || varType.equals("long")) {
			return LONG_TYPE;
		} else if (varType.equals("Double") || varType.equals("double")) {
			return DOUBLE_TYPE;
		} else if (varType.equals("Boolean") || varType.equals("boolean")) {
			return BOOLEAN_TYPE;
		} else if (varType.contains("List<") || varType.contains("ArrayList<")) {
			return LIST_TYPE;
		} else if (varType.equals("float")) {
			return FLOAT_TYPE;
		} else {
			return OBJECT_TYPE;
		}
	}
	
	public static String getJSONObjectFunction(String varType) {
		if (varType.equals("Integer") || varType.equals("int")) {
			return "optInt";
		} else if (varType.equals("float")) {
			return "optDouble";
		} else if (varType.equals("String")) {
			return "optString";
		} else if (varType.equals("Long") || varType.equals("long")) {
			return "optLong";
		} else if (varType.equals("Double") || varType.equals("double")) {
			return "optDouble";
		} else if (varType.equals("Boolean") || varType.equals("boolean")) {
			return "optBoolean";
		} else if (varType.contains("List<") || varType.contains("ArrayList<")) {
			return "getJSONArray";
		} else {
			return "getJSONObject";
		}
	}
	
}
