package BugTestPack.apacheCocoonBug;

//*************************************************************************************
//*********************************************************************************** *
//author Aritra Dhar																* *
//MT12004																			* *
//M.TECH CSE																		* * 
//INFORMATION SECURITY																* *
//IIIT-Delhi																		* *	 
//---------------------------------------------------------------------------------	* *																				* *
/////////////////////////////////////////////////									* *
//The program will do the following::::         //									* *
/////////////////////////////////////////////////									* *
//version 1.0																		* *
//*********************************************************************************** * 
//*************************************************************************************
public class CocoonBug 
{
	 private String parseName(String s) 
	 {
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < s.length(); i++) {
	            char c = s.charAt(i);
	            switch (c) {
	                case '+':
	                    sb.append(' ');
	                    break;
	                case '%':
	                    try {
	                        sb.append((char) Integer.parseInt(s.substring(i+1, i+3),
	                              16));
	                        i += 2;
	                    } catch (NumberFormatException e) {
	                        throw new IllegalArgumentException();
	                    } catch (StringIndexOutOfBoundsException e) {
	                        String rest  = s.substring(i);
	                        sb.append(rest);
	                        if (rest.length()==2)
	                            i++;
	                    }

	                    break;
	                default:
	                    sb.append(c);
	                    break;
	            }
	        }
	        return sb.toString();
	    }
	 
	 public static void main(String[] args) 
	 {
		 
		 String s = "http://localhost:8080/service?navigationID=/media/mmc2"
		 		+ "&selectionIDList=420000000000053228%09420000000000053227%09420000000000053226%09420000000000053225"
		 		+ "&selectionIDListDisp=%u4F0F%u5C14%u52A0%09%u4F0F%u5C14%u52A0%20%3E%20%u673A%u52A8%u8F66%09%u4F0F%u5"
		 		+ "C14%u52A0%20%3E%20%u673A%u52A8%u8F66%20%3E%20%u658B%09%u4F0F%u5C14%u52A0%20%3E%20%u673A%u52A8%u8F66%"
		 		+ "20%3E%20%u658B%20%3E%20%u5149%u5B66%u8BC6%u522B%u7CFB%u7EDF&selectionTopNList=420000000000053228%09%"
		 		+ "u4F0F%u5C14%u52A0%09420000000000053227%09%u4F0F%u5C14%u52A0%20%3E%20%u673A%u52A8%u8F66%0942000000000"
		 		+ "0053226%09%u4F0F%u5C14%u52A0%20%3E%20%u673A%u52A8%u8F66%20%3E%20%u658B%09420000000000053225%09%u4F0F%"
		 		+ "u5C14%u52A0%20%3E%20%u673A%u52A8%u8F66%20%3E%20%u658B%20%3E%20%u5149%u5B66%u8BC6%u522B%u7CFB%u7EDF&";
		 
		 String st = new CocoonBug().parseName(s);
		 System.out.println(st);
	 }
}
