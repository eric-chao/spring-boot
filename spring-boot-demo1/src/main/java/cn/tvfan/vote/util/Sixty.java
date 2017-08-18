/*  ------------------------------------------------------------------------------ 
 *                  软件名称:小主健身 
 *                  公司名称:永优科技 
 *                  开发作者:Xiaojun.Zhao
 *       			开发时间:2017年5月11日/2017
 *    				All Rights Reserved 2012-2017
 *  ------------------------------------------------------------------------------
 *    				注意:本内容均来自永优科技，仅限内部交流使用,未经过公司许可 禁止转发    
 *  ------------------------------------------------------------------------------
 *                  prj-name：gofit-common
 *                  fileName：Sixty.java
 *  -------------------------------------------------------------------------------
 */
package cn.tvfan.vote.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author :Xiaojun.Zhao/2017年5月11日
 */
public class Sixty {
	
	static final char[] array = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
	
	static final Map<Character, Integer> map = new HashMap<>();
	
	static {
		int i = 0;
		for(char c: array) {
			map.put(c, i);
			i ++;
		}
	}

	public static String toSixty(long number) {
		Long rest = number;
		Stack<Character> stack = new Stack<Character>();
		StringBuilder result = new StringBuilder(0);
		while (rest != 0) {
			stack.add(array[new Long((rest - (rest / 62) * 62)).intValue()]);
			rest = rest / 62;
		}
		for (; !stack.isEmpty();) {
			result.append(stack.pop());
		}
		return result.toString();

	}

	public static long tuNumber(String sixty_str) {
		int multiple = 1;
		long result = 0;
		Character c;
		for (int i = 0; i < sixty_str.length(); i++) {
			c = sixty_str.charAt(sixty_str.length() - i - 1);
			result += map.get(c) * multiple;
			multiple = multiple * 62;
		}
		return result;
	}

}
