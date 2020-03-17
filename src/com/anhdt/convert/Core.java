package com.anhdt.convert;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Core {
	private HashMap<String, Integer> countVariable = new HashMap<String, Integer>();
	private char countVar = 'a' - 1;
	private StringBuilder result = new StringBuilder();
	
	public Map<String, Object> jsonToJSONObject(JSONObject json) throws JSONException {
		Map<String, Object> retMap = new HashMap<String, Object>();
		result.append("JSONObject rootNode = new JSONObject(json);\n");
		if(json != JSONObject.NULL) {
			retMap = toMap(json, "rootNode", true);
		}
		return retMap;
	}

	public Map<String, Object> toMap(JSONObject object, String parentNode, boolean fromObject) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();

		Iterator<String> keysItr = object.keys();
		while(keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);
			String nodeName = key;
			if (countVariable.containsKey(key)) {
				int count = countVariable.get(key) + 1;
				countVariable.put(key, count);
				nodeName += count + "";
			} else {
				countVariable.put(key, 0);
			}
			if(value instanceof JSONArray) {
				result.append("JSONArray " + nodeName + " = " + parentNode + ".getJSONArray(\"" + key +"\");\n");
				value = toList((JSONArray) value, nodeName, true);
			} else if(value instanceof JSONObject) {
				result.append("JSONObject " + nodeName + " = " + parentNode + ".getJSONObject(\"" + key +"\");\n");
				value = toMap((JSONObject) value, nodeName, true);
			} else {
				if (value instanceof Integer) {
					result.append("int " + nodeName + " = " + parentNode + ".optInt(\"" + key +"\");\n");
				} else if (value instanceof Boolean) {
					result.append("boolean " + nodeName + " = " + parentNode + ".optBoolean(\"" + key +"\");\n");
				} else if (value instanceof String) {
					result.append("String " + nodeName + " = " + parentNode + ".optString(\"" + key +"\");\n");
				} else if (value instanceof String) {
					result.append("double " + nodeName + " = " + parentNode + ".optDouble(\"" + key +"\");\n");
				}  else if (value instanceof Long) {
					result.append("long " + nodeName + " = " + parentNode + ".optLong(\"" + key +"\");\n");
				}

			}
			map.put(key, value);
		}
		return map;
	}

	public List<Object> toList(JSONArray array, String parentNode, boolean fromObject) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		if (array.length() <= 0) return list;
		countVar++;
		String nodeName = "child" + parentNode.substring(0, 1).toUpperCase() + parentNode.substring(1);
		result.append("for (int " + countVar + " = 0; " + countVar + " < "+ parentNode +".length(); " + countVar + "++) {\n");
		if (countVariable.containsKey(nodeName)) {
			int count = countVariable.get(nodeName) + 1;
			countVariable.put(nodeName, count);
			nodeName += count + "";
		} else {
			countVariable.put(nodeName, 0);
		}
		Object value = array.get(0);
		if(value instanceof JSONArray) {
			result.append("JSONArray " + nodeName + " = " + parentNode + ".getJSONArray(" + countVar + ");\n");
			value = toList((JSONArray) value, nodeName, false);
		} else if(value instanceof JSONObject) {
			result.append("JSONObject " + nodeName + " = " + parentNode + ".getJSONObject(" + countVar + ");\n");
			value = toMap((JSONObject) value, nodeName, false);
		} else {
			if (value instanceof Integer) {
				result.append("int " + nodeName + " = " + parentNode + ".optInt(" + countVar +");\n");
			} else if (value instanceof Boolean) {
				result.append("boolean " + nodeName + " = " + parentNode + ".optBoolean(" + countVar + ");\n");
			} else if (value instanceof String) {
				result.append("String " + nodeName + " = " + parentNode + ".optString(" + countVar + ");\n");
			} else if (value instanceof String) {
				result.append("double " + nodeName + " = " + parentNode + ".optDouble(" + countVar + ");\n");
			}  else if (value instanceof Long) {
				result.append("long " + nodeName + " = " + parentNode + ".optLong(" + countVar + ");\n");
			}
		}
		list.add(value);
		result.append("}\n");
		return list;
	}
	
	public String getResult() {
		return result.toString();
	}
	
	public String getProcessResult() {
		String result = this.getResult();
		String processResult = "";
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0; i < result.length(); i++) {
			processResult += result.charAt(i);
			if (result.charAt(i) == '{') {
				stack.push(result.charAt(i));
			} else if (result.charAt(i) == '}') {
				if (stack.size() > 0 && stack.peek() == '{') {
					stack.pop();
				}
			} else if (result.charAt(i) == '\n') {
				if (i < result.length() - 1 && result.charAt(i + 1) == '}') {
					if (stack.size() >= 0) {
						processResult += addTab(stack.size() - 1);
					} else {
						processResult += addTab(stack.size());
					}
				} else {
					processResult += addTab(stack.size());
				}
			}
		}
		return processResult;
	}
	
	public String addTab(int number) {
		String result = "";
		for (int i = 0; i < number; i++) {
			result += "\t";
		}
		return result;
	}
	
	public void reset() {
		countVariable = new HashMap<String, Integer>();
		countVar = 'a' - 1;
		result = new StringBuilder();
	}
}
