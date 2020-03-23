package com.anhdt.convert.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.anhdt.parser.ClassInfo;
import com.anhdt.parser.MethodInfo;
import com.anhdt.parser.RegularExpressions;
import com.anhdt.parser.VariableInfo;

public class CoreJavaCode {
	
	private static final String SERIALIZABLE = "@SerializedName(";
	private static final String EXTENDS = "extends";

	private static List<ClassInfo> classInfos = new ArrayList<ClassInfo>();
	private static List<Integer> classStartIndexs = new ArrayList<Integer>();
	private static List<Integer> classEndIndexs = new ArrayList<Integer>();
	
	public static int findEndOfBlock(String source, int start) {
		Stack<Character> stackCharacter = new Stack<Character>();
		for (int i = start; i < source.length(); i++) {
			if (source.charAt(i) == '{') {
				stackCharacter.push(source.charAt(i));
			} else if (source.charAt(i) == '(') {
				stackCharacter.push(source.charAt(i));
			} else {
				if (source.charAt(i) == '}') {
					if (stackCharacter.size() > 0 && stackCharacter.peek() == '{') {
						stackCharacter.pop();
						if (stackCharacter.size() == 0) {
							return i;
						}
					}
				} else if (source.charAt(i) == ')') {
					if (stackCharacter.size() > 0 && stackCharacter.peek() == '(') {
						stackCharacter.pop();
						if (stackCharacter.size() == 0) {
							return i;
						}
					}
				}
			}
		}
		return -1;
	}
	
	public static int getClassIndexContainsVariable(int varIndex) {
		int classIndex = 0;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < classInfos.size(); i++) {
			ClassInfo info = classInfos.get(i);
			if (varIndex - info.getStartIndex() >= 0 && varIndex - info.getStartIndex() < min) {
				min = varIndex - info.getStartIndex();
				classIndex = i;
			}
		}
		return classIndex;
	}
	
	public static void main(String[] args) {
		String input = "public class TokenHistoryResponse {\r\n" + 
				"    @SerializedName(\"status\")\r\n" + 
				"    @Expose\r\n" + 
				"    private int status;\r\n" + 
				"    @SerializedName(\"message\")\r\n" + 
				"    @Expose\r\n" + 
				"    private String message;\r\n" + 
				"    @SerializedName(\"code\")\r\n" + 
				"    @Expose\r\n" + 
				"    private int code;\r\n" + 
				"    @SerializedName(\"data\")\r\n" + 
				"    @Expose\r\n" + 
				"    private TokenData tokenData;\r\n" + 
				"    @SerializedName(\"items\")\r\n" + 
				"    @Expose\r\n" + 
				"    @ColumnInfo(name = \"items\")\r\n" + 
				"    public List<List<HelloBabe>> items;\r\n" + 
				"    \r\n" + 
				"    public TokenHistoryResponse(JSONObject jsonObject) throws JSONException {\r\n" + 
				"        super(jsonObject);\r\n" + 
				"        int code = jsonObject.optInt(\"code\");\r\n" + 
				"        this.tokenData = new TokenData(jsonObject.optDouble(\"abab\"));\r\n" + 
				"        if (jsonObject.has(\"abc\")) {\r\n" + 
				"\r\n" + 
				"        }\r\n" + 
				"        JSONArray jsonArray = jsonObject.getJSONArray();\r\n" + 
				"        for (int i = 0; i < jsonArray.length(); i++) {\r\n" + 
				"            JSONObject obj = jsonArray.getJSONObject(i);\r\n" + 
				"\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    public int getStatus() {\r\n" + 
				"        return status;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    public void setStatus(int status) {\r\n" + 
				"        this.status = status;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    public String getMessage() {\r\n" + 
				"        return message;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    public void setMessage(String message) {\r\n" + 
				"        this.message = message;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    public int getCode() {\r\n" + 
				"        return code;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    public void setCode(int code) {\r\n" + 
				"        this.code = code;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    public TokenData getTokensData() {\r\n" + 
				"        return tokenData;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    public void setDetailTokens(TokenData detailTokens) {\r\n" + 
				"        this.tokenData = detailTokens;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    public List<DetailToken> getTokenDetails() {\r\n" + 
				"        if (tokenData != null) {\r\n" + 
				"            return tokenData.detailTokens;\r\n" + 
				"        }\r\n" + 
				"        return null;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    public float getTokenTotal() {\r\n" + 
				"        if (tokenData != null) {\r\n" + 
				"            return tokenData.tokenTotal;\r\n" + 
				"        }\r\n" + 
				"        return 0f;\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    public class TokenData {\r\n" + 
				"        @SerializedName(\"token_total\")\r\n" + 
				"        @Expose\r\n" + 
				"        private float tokenTotal;\r\n" + 
				"        @SerializedName(value=\"lst_group_token\", alternate={\"list\", \"lst_token_receive\"})\r\n" + 
				"        @Expose\r\n" + 
				"        private List<DetailToken> detailTokens;\r\n" + 
				"\r\n" + 
				"        public float getTokenTotal() {\r\n" + 
				"            return tokenTotal;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public void setTokenTotal(float tokenTotal) {\r\n" + 
				"            this.tokenTotal = tokenTotal;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public List<DetailToken> getDetailTokens() {\r\n" + 
				"            return detailTokens;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public void setDetailTokens(List<DetailToken> detailTokens) {\r\n" + 
				"            this.detailTokens = detailTokens;\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"\r\n" + 
				"    public class DetailToken {\r\n" + 
				"        @SerializedName(\"group_id\")\r\n" + 
				"        @Expose\r\n" + 
				"        private int groupId;\r\n" + 
				"        @SerializedName (\"log_type\")\r\n" + 
				"        @Expose\r\n" + 
				"        private String logType;\r\n" + 
				"        @SerializedName(\"group_key\")\r\n" + 
				"        @Expose\r\n" + 
				"        private String groupKey;\r\n" + 
				"        @SerializedName(value = \"group_title\", alternate = {\"title\"})\r\n" + 
				"        @Expose\r\n" + 
				"        private String groupTitle;\r\n" + 
				"        @SerializedName(\"post_id\")\r\n" + 
				"        @Expose\r\n" + 
				"        private String postId;\r\n" + 
				"        @SerializedName(\"counter\")\r\n" + 
				"        @Expose\r\n" + 
				"        private int counter;\r\n" + 
				"        @SerializedName(\"token_value\")\r\n" + 
				"        @Expose\r\n" + 
				"        private float tokenValue;\r\n" + 
				"        @SerializedName(\"last_updated_timestamp\")\r\n" + 
				"        @Expose\r\n" + 
				"        private long lastUpdateTime;\r\n" + 
				"\r\n" + 
				"        public int getGroupId() {\r\n" + 
				"            return groupId;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public void setGroupId(int groupId) {\r\n" + 
				"            this.groupId = groupId;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public String getLogType() {\r\n" + 
				"            return logType;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public void setLogType(String logType) {\r\n" + 
				"            this.logType = logType;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public String getGroupKey() {\r\n" + 
				"            return groupKey;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public void setGroupKey(String groupKey) {\r\n" + 
				"            this.groupKey = groupKey;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public String getGroupTitle() {\r\n" + 
				"            return groupTitle;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public void setGroupTitle(String groupTitle) {\r\n" + 
				"            this.groupTitle = groupTitle;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public String getPostId() {\r\n" + 
				"            return postId;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public void setPostId(String postId) {\r\n" + 
				"            this.postId = postId;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public int getCounter() {\r\n" + 
				"            return counter;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public void setCounter(int counter) {\r\n" + 
				"            this.counter = counter;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public float getTokenValue() {\r\n" + 
				"            return tokenValue;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public void setTokenValue(float tokenValue) {\r\n" + 
				"            this.tokenValue = tokenValue;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public long getLastUpdateTime() {\r\n" + 
				"            return lastUpdateTime;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        public void setLastUpdateTime(long lastUpdateTime) {\r\n" + 
				"            this.lastUpdateTime = lastUpdateTime;\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"}";
		
		//Handle class
		Matcher matcher = Pattern.compile(RegularExpressions.CLASS_REGEX).matcher(input);
		while (matcher.find()) {
			ClassInfo classInfo = new ClassInfo();
			classInfo.setStartIndex(matcher.end() - 1);
			classInfo.setEndIndex(findEndOfBlock(input, matcher.end() - 1));
			String className = matcher.group(3);
			classInfo.setClassName(className);
			String fullData = matcher.group();
			if (fullData.contains(EXTENDS)) {
				Matcher matcher2 = Pattern.compile(RegularExpressions.EXTEND_CLASS_REGEX).matcher(fullData);
				while (matcher2.find()) {
					classInfo.setBaseClass(matcher2.group(1));
				}
			}
			classInfos.add(classInfo);
		}
		

		//Handle variables
		matcher = Pattern.compile(RegularExpressions.VARIABLE_REGEX).matcher(input);
		while (matcher.find()) {
			int classIndex = getClassIndexContainsVariable(matcher.start());
			VariableInfo varInfo = new VariableInfo();
			String varType = matcher.group(14);
			String varName = matcher.group(15);
			varInfo.setVarName(varName);
			varInfo.setVarType(varType);
			String varFull = matcher.group();
			int startIndex = varFull.indexOf(SERIALIZABLE);
			if (startIndex != -1) {
				int endIndex = findEndOfBlock(varFull, startIndex);
				String seralizableListStr = varFull.substring(startIndex, endIndex + 1);
				Matcher matcher2 = Pattern.compile(RegularExpressions.DOUBLE_QUOTES_REGEX).matcher(seralizableListStr);
				while (matcher2.find()) {
					varInfo.getVarJsonNodes().add(matcher2.group(1));
				}
				ClassInfo classInfo = classInfos.get(classIndex);
				classInfo.getVariableInfoList().add(varInfo);
			}
			
		}
		
		//handle methods
		matcher = Pattern.compile(RegularExpressions.FUNCTION_REGEX).matcher(input);
		while (matcher.find()) {
			int classIndex = getClassIndexContainsVariable(matcher.start());
			MethodInfo methodInfo = new MethodInfo();
			String methodType = matcher.group(3);
			String methodName = matcher.group(4);
			methodInfo.setMethodName(methodName);
			methodInfo.setMethodType(methodType);
			ClassInfo classInfo = classInfos.get(classIndex);
			classInfo.getMethodInfoList().add(methodInfo);
		}
		
		for (int i = 0; i < classInfos.size(); i++) {
			ClassInfo info = classInfos.get(i);
			System.out.println(info.convertJSONObject());
		}

	}

}
