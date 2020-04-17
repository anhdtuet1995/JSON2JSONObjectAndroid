package com.anhdt.convert.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

	private List<ClassInfo> classInfos = new ArrayList<ClassInfo>();

	public int findEndOfBlock(String source, int start) {
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

	public int getClassIndexContainsVariable(int varIndex) {
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
	
	public boolean checkGetVariableFunction(ClassInfo info, VariableInfo varInfo) {
		for (MethodInfo methodInfo: info.getMethodInfoList()) {
			if (varInfo.getFunctionName().equals(methodInfo.getMethodName())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkSetVariableFunction(ClassInfo info, VariableInfo varInfo) {
		for (MethodInfo methodInfo: info.getMethodInfoList()) {
			if (varInfo.setFunctionName().equals(methodInfo.getMethodName())) {
				return true;
			}
		}
		return false;
	}
	
	public String getResult(String input) {

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
			String varType = matcher.group(15);
			String varName = matcher.group(16);
			System.out.println("Var Type: " + varType);
			System.out.println("Var Name: " + varName);
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
					System.out.println("Name: " + matcher2.group(1));
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
			System.out.println(methodName);
			ClassInfo classInfo = classInfos.get(classIndex);
			classInfo.getMethodInfoList().add(methodInfo);
		}
		
		String result = "";
		Collections.sort(classInfos, new Comparator<ClassInfo>() {
			@Override
			public int compare(ClassInfo o1, ClassInfo o2) {
				return o1.getStartIndex() - o2.getStartIndex();
			}
		});
		int beforeIndex = 0;
		for (int i = 0; i < classInfos.size(); i++) {
			ClassInfo info = classInfos.get(i);
			int startIndex = info.getStartIndex();
			String sub = input.substring(beforeIndex, startIndex + 1);
			result += sub;
			result += "\n" + info.convertJSONObject() + "\n";
			for (VariableInfo varInfo: info.getVariableInfoList()) {
				if (!checkGetVariableFunction(info, varInfo)) {
					result += "\n" + varInfo.generateGetFunction() + "\n";
				}
				if (!checkSetVariableFunction(info, varInfo)) {
					result += "\n" + varInfo.generateSetFunction() + "\n";
				}
			}
			beforeIndex = startIndex + 1;
			if (i == classInfos.size() - 1) {
				sub = input.substring(beforeIndex, input.length());
				result += sub;
			}
		}
		
		return result;

	}
	
	public void reset() {
		classInfos = new ArrayList<ClassInfo>();
	}

}
