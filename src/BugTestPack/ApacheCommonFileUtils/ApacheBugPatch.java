package BugTestPack.ApacheCommonFileUtils;
import java.io.File;


public class ApacheBugPatch 
{

	/*  113 */   private static final char SYSTEM_SEPARATOR = File.separatorChar;

	/*      */   static boolean isSystemWindows()
	/*      */   {
	/*  141 */     return SYSTEM_SEPARATOR == '\\';
	/*      */   }

	/*      */   private static boolean isSeparator(char ch)
	/*      */   {
	/*  152 */     return (ch == '/') || (ch == '\\');
	/*      */   }

	/*      */   public static int getPrefixLength(String filename)
	/*      */   {
	/*  486 */     if (filename == null) {
	/*  487 */       return -1;
	/*      */     }
	/*  489 */     int len = filename.length();
	/*  490 */     if (len == 0) {
	/*  491 */       return 0;
	/*      */     }
	/*  493 */     char ch0 = filename.charAt(0);
	/*  494 */     if (ch0 == ':') {
	/*  495 */       return -1;
	/*      */     }
	/*  497 */     if (len == 1) {
	/*  498 */       if (ch0 == '~') {
	/*  499 */         return 2;
	/*      */       }
	/*  501 */       return isSeparator(ch0) ? 1 : 0;
	/*      */     }
	/*  503 */     if (ch0 == '~') {
	/*  504 */       int posUnix = filename.indexOf('/', 1);
	/*  505 */       int posWin = filename.indexOf('\\', 1);
	/*  506 */       if ((posUnix == -1) && (posWin == -1)) {
	/*  507 */         return len + 1;
	/*      */       }
	/*  509 */       posUnix = posUnix == -1 ? posWin : posUnix;
	/*  510 */       posWin = posWin == -1 ? posUnix : posWin;
	/*  511 */       return Math.min(posUnix, posWin) + 1;
	/*      */     }
	/*  513 */     char ch1 = filename.charAt(1);
	/*  514 */     if (ch1 == ':') {
	/*  515 */       ch0 = Character.toUpperCase(ch0);
	/*  516 */       if ((ch0 >= 'A') && (ch0 <= 'Z')) {
	/*  517 */         if ((len == 2) || (!isSeparator(filename.charAt(2)))) {
	/*  518 */           return 2;
	/*      */         }
	/*  520 */         return 3;
	/*      */       }
	/*  522 */       return -1;
	/*      */     }
	/*  524 */     if ((isSeparator(ch0)) && (isSeparator(ch1))) {
	/*  525 */       int posUnix = filename.indexOf('/', 2);
	/*  526 */       int posWin = filename.indexOf('\\', 2);
	/*  527 */       if (((posUnix == -1) && (posWin == -1)) || (posUnix == 2) || (posWin == 2)) {
	/*  528 */         return -1;
	/*      */       }
	/*  530 */       posUnix = posUnix == -1 ? posWin : posUnix;
	/*  531 */       posWin = posWin == -1 ? posUnix : posWin;
	/*  532 */       return Math.min(posUnix, posWin) + 1;
	/*      */     }
	/*  534 */     return isSeparator(ch0) ? 1 : 0;
	/*      */   }
	/*      */   

	/*      */ 
	/*      */   public static int indexOfLastSeparator(String filename)
	/*      */   {
	/*  552 */     if (filename == null) {
	/*  553 */       return -1;
	/*      */     }
	/*  555 */     int lastUnixPos = filename.lastIndexOf('/');
	/*  556 */     int lastWindowsPos = filename.lastIndexOf('\\');
	/*  557 */     return Math.max(lastUnixPos, lastWindowsPos);
	/*      */   }
	
	/*      */   public static String getPathNoEndSeparator(String filename){
	/*  676 */     return doGetPath(filename, 0);
	/*      */   }
	/*      */   private static String doGetPath(String filename, int separatorAdd){
	/*  687 */     if (filename == null) {
	/*  688 */       return null;
	/*      */     }
	/*  690 */     int prefix = getPrefixLength(filename);
	/*  691 */     if (prefix < 0) {
	/*  692 */       return null;
	/*      */     }
	/*  694 */     int index = indexOfLastSeparator(filename);
	/*  695 */     if ((prefix >= filename.length()) || (index < 0)) {
	/*  696 */       return "";
	/*      */     }
	/*  697 */	   int endIndex = index+separatorAdd;
	/*  698 */	   if (prefix >= filename.length() || index < 0 || prefix > endIndex) {
	/*  699 */    	 return "";
	/*      */		}
	/*  700 */     return filename.substring(prefix, endIndex);
	/*      */	 }

 
	/*      */   public ApacheBugPatch() {}
	
	
	public static void main(String[] args) 
	{
		 String path = "/foo.xml"; 
		 String s = ApacheBugPatch.getPathNoEndSeparator(path);
		 System.out.println(s);
	}
}


