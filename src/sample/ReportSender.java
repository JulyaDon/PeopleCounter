package sample;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Andrew on 14.12.2016.
 */
public class ReportSender {

    Settings settings = Settings.getInstance();

    public ArrayList<Report> report;
    public String token = settings.getReportApiToken();

    public ReportSender(ArrayList<Report> reportsToSend){
        report = reportsToSend;
    }

    public void sendReport(){
        JSONObject jsonInstance = new JSONObject();

        JSONArray jreport = new JSONArray();

        for (Report r : report){
            jreport.put(r.getJSONObject());
        }

        try {
            jsonInstance.put("token", "tls");
            jsonInstance.put("report", report);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost request = new HttpPost(settings.getReportApiLink());
            StringEntity params = new StringEntity(jsonInstance.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);


            org.apache.http.HttpResponse response = httpClient.execute(request);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
                //data.setTransmitted(true);
            }
// handle response here...
        } catch (Exception ex) {
            // handle exception here
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendReportTest(){

        boolean result = false;
        try {

            String urlStr = settings.getReportApiLink();

            String jsonObjects ="";

            for (int i = 0; i < report.size()-1; i++){
                jsonObjects += report.get(i).toString();
                jsonObjects += ",";
            }

            if ((report.size()-1)>=0)
                jsonObjects += report.get(report.size()-1);

            String jsonStr =
                    "{\"token\":\"tls\"," +
                    "\"report\":[" + jsonObjects + "]}";


            StringEntity entityJson = new StringEntity(jsonStr);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlStr);

            try {
                post.setEntity(entityJson);

                org.apache.http.HttpResponse response = client.execute(post);

                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    System.out.println(line);
                    //data.setTransmitted(true);
                    result = true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //return result;
    }

}
