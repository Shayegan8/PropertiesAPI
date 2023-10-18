package api.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IsNumber {

	private static List<Character> strchars;

	static {
		try {
			strchars = new ArrayList<Character>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isNumber(String str) {
		int i = 0;
		char cstr[] = str.toCharArray();
		while (i < cstr.length) {
			strchars.add(cstr[i]);
			i++;
		}
		i = 0;
		while (i < strchars.size()) {
			if (strchars.stream().filter(Character::isDigit).collect(Collectors.toList()).size() == strchars.size()) {
				return true;
			}
			i++;
		}
		return false;
	}

	public static boolean isNumber(char str[]) {
		int i = 0;
		while (i < str.length) {
			strchars.add(str[i]);
			i++;
		}
		i = 0;
		while (i < strchars.size()) {
			if (strchars.stream().filter(Character::isDigit).collect(Collectors.toList()).size() == strchars.size()) {
				return true;
			}
			i++;
		}
		return false;
	}

	public static boolean isNumber(char chr) {
		return Character.isDigit(chr) ? true : false;
	}

	public static void fakeFree() {
		strchars = null;
	}

}
