
/**
* RESTConnection is the class used for call the Solution class's method that connects to the REST url.
*/
public class RESTConnection {

	 /**
	   * @param args
	   */
	  public static void main(String[] args) {
	    Solution conn = new Solution();
	    /* REST call with test url */
	    conn.buildRequestResponse("https://jsonplaceholder.typicode.com/users/");
	    //conn.buildRequestResponse(""); // Placeholder for REST url
	  }
	  
}
