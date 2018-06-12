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


public class Solution {

   /**
     * @param args
     */
    public static void main(String[] args) {
      
    try {    
    Solution conn = new Solution();
      /* REST call with test url */
      String strUrl = "https://raw.githubusercontent.com/prachisamant/June2018/master/db.json";
      if (strUrl.trim() == null || strUrl.trim() == "") {
          throw new RuntimeException("Please enter a valid URL.");
        }
      URL url = new URL(strUrl.trim());
      
      conn.buildRequestResponse(url);
    }
      catch (MalformedURLException e) {
          System.out.println("The URL is malformed. Please enter a valid url.");
          e.printStackTrace();
        } 
    }
  
  public void buildRequestResponse(URL url){
    
    try {
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
        Integer sum = 0;
        Integer runningTotal = 0;
        String key;
        
        JSONArray jArr = (JSONArray)parser.parse(jsonLines);

        System.out.println("Displaying all of the keys for each JSON document : ");
        for (int i=0; i<jArr.size(); i++) {
          JSONObject jObj = (JSONObject)jArr.get(i);
          Iterator<?> keyItr = jObj.keySet().iterator();
          
          while(keyItr.hasNext()) {
            key = (String)keyItr.next();
            System.out.print(key + " ");//Value of the key "jObj.get(key)" is not included.
            
            //Iterate over numbers array of every record to get sum.
            if (key.equals("numbers")) {
              JSONArray intArr = (JSONArray)(jObj.get(key));
              sum = 0;
              for (int j=0; j<intArr.size(); j++){
                sum += Integer.parseInt(intArr.get(j).toString());
              }
            }
          }
          // Sum of all integers in the numbers array for each JSON document
          System.out.print(": Individual sum of numbers = " + sum);
          runningTotal += sum;
          // Print sum of numbers for all documents
          System.out.println(": Running Total so far = "+runningTotal);
        }
        //close connection
        connection.disconnect();
      }
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
