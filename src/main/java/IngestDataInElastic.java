import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

public class IngestDataInElastic {

	public static void main(String[] args) {
		File f = new File("src/VMQ_10-12.12.log.99");
		Scanner s = null;
		try {
			s = new Scanner(f);
			while(s.hasNext()) {
				String str = s.nextLine();
				//System.out.println("value of line is :"+str);
				if(str.contains("|")) {
					String[] strArray = str.split("\\|");
					//System.out.println("value of strArray[0]:"+strArray[0]);
					//System.out.println("value of strArray[1]:"+strArray[1]);
					if(strArray[1].contains(":")) {
						String[] strArray2 = strArray[1].split(":");
						LogObject l = new LogObject(strArray[0],strArray2[0],strArray2[1]);
						//System.out.println("col1: "+strArray[0] +" col2: "+strArray2[0]+" col3: "+strArray2[1]);
						String idOfLogObject = strArray[0].replaceAll(":", "");
						System.out.println(idOfLogObject);
						sendDataToElasticSearch(l,idOfLogObject);
					}
							
				}
				//System.out.println(s.next());
			}
		}catch(Exception e) {
			
		}
		
	
		
	}
	
	
	public static void sendDataToElasticSearch(LogObject a,String logId) {
		HttpClient client = null;
		try {
			//Accounts a = new Accounts(str);
			System.out.println(a.toString());
			Gson gson = new Gson();

			client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://localhost:9200/vmqlogs/_doc/"+logId);
			StringEntity postingString = new StringEntity(gson.toJson(a));
			post.setEntity(postingString);
			post.setHeader("Content-type", "application/json");
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			System.out.println("some exeption occurred , while pushing data in elastic search" + e.getMessage());
		}
	}
}
