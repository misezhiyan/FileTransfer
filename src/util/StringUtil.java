package util;

import java.util.List;

public class StringUtil {

	public static boolean isEmpty(String str) {

		return null == str || "".equals(str);
	}

	public static boolean isEmpty(List list) {

		return null == list || list.isEmpty();
	}

}
