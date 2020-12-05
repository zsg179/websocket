package com.tk.sz.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Test {

	// 流水号加1后返回，流水号长度为4
	private static final String STR_FORMAT = "0000";

	public static String haoAddOne_2(String liuShuiHao) {
		Integer intHao = Integer.parseInt(liuShuiHao);
		intHao++;
		DecimalFormat df = new DecimalFormat(STR_FORMAT);
		return df.format(intHao);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 待测试数据
		int i = 1;
		// 得到一个NumberFormat的实例
		NumberFormat nf = NumberFormat.getInstance();
		// 设置是否使用分组
		nf.setGroupingUsed(false);
		// 设置最大整数位数
		nf.setMaximumIntegerDigits(4);
		// 设置最小整数位数
		nf.setMinimumIntegerDigits(4);
		// 输出测试语句
		System.out.println(nf.format(i));
	}

}
