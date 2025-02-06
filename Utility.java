package projectDAO;

import java.util.ArrayList;
import java.util.List;

public class Utility {
	
		
	public static boolean isValid(int a) {
		
		return a>=1 && a<=6;
		
	}
	
	public static boolean isValidPiority(int num) {

		return num>=1 && num<=10;
		
	}
	
	public static boolean isValidName(String categoryName) {
		if(categoryName==null)
			return false;
		if(categoryName.isEmpty())
			return false;
		if(categoryName.contains(" "))
			return false;
		if(!Character.isAlphabetic(categoryName.charAt(0)))
			return false;
		for(int i=1;i<categoryName.length();i++) {
			if(!Character.isLetterOrDigit(categoryName.charAt(i))) {
				return false;
			}
		}
		return true;

//		for(int i=1;i<categoryName.length();i++) {
//			if(!(Character.isAlphabetic(categoryName.charAt(i)) || Character.isDigit(categoryName.charAt(i)))) {
//				return false;
//			}
//		}
		
	}
	
}
