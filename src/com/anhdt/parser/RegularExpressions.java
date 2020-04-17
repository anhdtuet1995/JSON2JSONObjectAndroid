package com.anhdt.parser;

public class RegularExpressions {
	public final static String SPACE_OR_NEWLINE_REGEX = "(\\s|(\r\n))";
	public final static String NO_SPACE = "(\\s|())";
	//Format: @Expose
	public final static String ANOTATION_REGEX_CASE_2 = "@(\\w+)";
	//Format: @SerializedName("status")
	public final static String ANOTATION_REGEX_CASE_1 = "@(\\w+)\\(\\\"(\\w+)\\\"\\)";
	public final static String ANOTATION_REGEX_CASE_3 = "@(\\w+)\\s*\\(\\s*\\w+\\s*=\\s*\"\\w+\"\\s*\\)";
	public final static String ANOTATION_REGEX_CASE_4 = "@(\\w+)\\s*\\(\\s*\\w+\\s*=\\s*\"\\w+\"\\s*,\\s*\\w+\\s*=\\s*\\\"\\w+\\\"\\s*\\)";
	//Format: @SerializedName(value="lst_group_token", alternate={"list", "lst_token_receive"})
	public final static String ANOTATION_REGEX_CASE_0 = "@(\\w+)\\s*\\(\\s*(\\w)+\\s*=\\s*\\\"(\\w+)\\\"\\s*,\\s*\\w+\\s*=\\s*\\{\\s*\\\"(\\w+)\\\"\\s*(\\s*,\\s*\\\"\\w+\\\")*\\}\\)";
	
	public final static String ANNOTATION_REGEX = "(" + ANOTATION_REGEX_CASE_0 + "|" + ANOTATION_REGEX_CASE_4 + "|" + ANOTATION_REGEX_CASE_3 + "|" + ANOTATION_REGEX_CASE_1 +"|" + ANOTATION_REGEX_CASE_2 + ")";
	public final static String ANNOTATIONS_REGEX = "(" + ANNOTATION_REGEX + "\\s+)+" ;
	public final static String MODIFIERS = "((final|volatile|transient|static|public|private|protected)\\s+)+";
	
	//vartype index = 13, varName index = 14
	public final static String VARIABLE_REGEX = (ANNOTATIONS_REGEX + MODIFIERS + "([a-zA-Z_0-9<>\\[\\]]+)\\s*(\\w+)(((\\s*=\\s*new\\s*[a-zA-Z_0-9<> \\[\\]()]+)|(\\s*=\\s*[a-zA-Z_0-9\\\"]+))*\\s*;)");
	
	//format: public class TokenHistoryResponse {
	public final static String CLASS_REGEX = MODIFIERS + "class\\s+(\\w+)+((\\s+extends\\s+(\\w+))*)((\\s+implements\\s+\\w+(\\s*,\\s*\\w*)*)*)\\s*\\{";
	public final static String EXTEND_CLASS_REGEX = "extends\\s+(\\w+)\\s*";
	
	//format: public String getLogType(
	public final static String FUNCTION_REGEX = MODIFIERS + "([a-zA-Z_0-9<>\\[\\]]+)\\s+(\\w+)\\s*\\(";
	
	public final static String DOUBLE_QUOTES_REGEX = "\"(\\w+)\"";
}
