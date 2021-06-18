package cn.com.project.common;

import java.util.Random;
import java.util.UUID;

public class CommonUtils {
	
	/**
	 * 生成无'-'的uuid
	 * @return
	 */
	public static String createUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 字符串串数组里，是否包含某一字符串
	 * @param arr
	 * @param targetValue
	 * @return
	 */
	public static boolean strInArr(String[] arr,String targetValue){
		for(String s:arr){
			if(s.equals(targetValue))
				return true;
		}
		return false;
	}

	/**
	 * 生成指定长度，随机码
	 * @param length
	 * @return
	 */
	public static String getCode(int length) {
		String code = "";
		Random random = new Random();
		// 参数length，表示生成几位随机数
		for (int i = 0; i < length; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
//				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				int temp = 65;
				code += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				code += String.valueOf(random.nextInt(10));
			}
		}
		return code;
	}

	public static void main(String[] args) {
		System.out.println(getCode(15));
//		for(int i=0;i<10;i++){
//			System.out.println(getUUID());
//		}
	}
}
