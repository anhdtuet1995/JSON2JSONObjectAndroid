package com.anhdt.parser;

public class MethodInfo {
	
	private String methodName;
	private String methodType;
	
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getMethodType() {
		return methodType;
	}
	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
	
	@Override
	public String toString() {
		String dataParse = "\nMethod Name = " + methodName;
		dataParse += "\n Method Type = " + methodType;
		return dataParse;
	}
}
