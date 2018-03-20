
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

public class ElasticSearchClient {
	
	/*
	 * this method is used to send data to elasticsearch. HttpPost post = new
	 * HttpPost(
	 * "http://localhost:9200/bank/_search?q=*&sort=account_number:asc&pretty");
	 * StringEntity postingString = new StringEntity(
	 * "{\"query\":{\"bool\":{\"must\":[{\"match\":{\"address\":\"mill\"}},{\"match\":{\"address\":\"lane\"}}]}}}"
	 * ); use the below url to check results :
	 * http://localhost:9200/customer/_search?q=*
	 */

	public static void sendDataToElasticSearch(String str) {
		HttpClient client = null;
		try {
			Accounts a = new Accounts(str);
			System.out.println(a.toString());
			Gson gson = new Gson();

			client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://localhost:9200/accounts/_doc");
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
