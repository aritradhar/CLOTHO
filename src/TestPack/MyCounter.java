package TestPack;

public class MyCounter {
	/* the counter, initialize to zero */
	  private static int c = 0;
	 public static void main(String []a)
	 {
		 
	 }
	  /**
	   * increases the counter by <pre>howmany</pre>
	   * @param howmany, the increment of the counter.
	   */
	  public static synchronized void increase(int howmany) {
	    c += howmany;
	  }
	 
	  /**
	   * reports the counter content.
	   */
	  public static synchronized void report() {
	    System.err.println("counter : " + c);
	  }
	  
	  public static synchronized void methodAD() {
		    int a=0;
		    int b=a+2;
		  }
}
