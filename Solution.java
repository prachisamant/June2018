import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
* Solution is the class that accepts a url as an input parameter and make an HTTP GET request to a REST endpoint and process the output.
*/
public class Solution {

 
  
  public void buildRequestResponse(String strUrl){
    
    try {
      if (strUrl.trim() == null || strUrl.trim() == "") {
        throw new RuntimeException("Please enter a valid URL.");
      }
      URL url = new URL(strUrl.trim());
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Accept", "application/json");
      connection.connect();
      int responsecode = connection.getResponseCode();
      
      if(responsecode != 200)
        throw new RuntimeException("Sorry! This service is currently not available. HttpResponseCode: " +responsecode);
      else { // Response OK.
        String jsonLines = "";
        Scanner stream = new Scanner(url.openStream());

        while(stream.hasNext()) {
          jsonLines += stream.nextLine();
        }
        stream.close();
        
        JSONParser parser = new JSONParser();
        Long sum = 0L;
        Long runningTotal = 0L;
        String key;
        
        // Process response for REST returning a JSON array of documents
        JSONArray jArr = (JSONArray)parser.parse(jsonLines);
        
        /* Below is the test JSON Array used for testing. */
        //JSONArray jArr = (JSONArray)parser.parse("[{\"id\":1, \"name\":\"john\",\"numbers\":[3,5,7,15],\"status\":\"married\"},{\"id\":2, \"name\":\"mary\",\"numbers\":[6,8,10,2],\"status\":\"married\"},{\"id\":3, \"name\":\"julie\",\"numbers\":[10,3,8,9],\"status\":\"single\"}]");
        
        System.out.println("Displaying all of the keys for each JSON document : ");
        for (int i=0; i<jArr.size(); i++) {
          JSONObject jObj = (JSONObject)jArr.get(i);
          Iterator<?> keyItr = jObj.keySet().iterator();
          
          while(keyItr.hasNext()) {
            key = (String)keyItr.next();
            System.out.print(key + " ");
            
            //Iterate over numbers array to get sum.
            if (key.equals("numbers")) {
              JSONArray intArr = (JSONArray)(jObj.get(key));
              sum = 0L;
              for (int j=0; j<intArr.size(); j++){
                sum += ((Long)intArr.get(j)).intValue();
              }
            }
          }
          // Sum of all integers in the numbers array for each JSON document
          System.out.println(": Sum of numbers = " + sum);
          runningTotal += sum;
        }
        // Print sum of numbers for all documents
        System.out.println("Running Total = "+runningTotal);
        
        //close connection
        connection.disconnect();
      }
    } catch (MalformedURLException e) {
      System.out.println("The URL is malformed. Please enter a valid url.");
      e.printStackTrace();
    } catch (ProtocolException e) {
      System.out.println("Please make sure the protocol is correct for the url.");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("There was an IOException.");
      e.printStackTrace();
    } catch (ParseException e) {
      System.out.println("The response could not be parsed correctly.");
      e.printStackTrace();
    }  
  }
}
