package model.DataClasses;

import model.ParametersClasses.Parameters;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Andrew on 11.12.2016.
 */
public class DataSender {
/*
    public DataSender(String url){
        ApiURL = url;
    }
*/
    private Parameters parameters = Parameters.getInstance();

    private String ApiURL = parameters.getApiURL();

    public void SendData (Data data){

        try {

            String urlStr = ApiURL;
            String jsonStr =
                    "{\"in\":" + data.getIn() +
                    ",\"out\":" + data.getOut() +
                    ",\"location\":\"" + parameters.getLocation() +
                    "\",\"time\":\"" + data.getDateString() + "\"}";

            StringEntity entityJson = new StringEntity(jsonStr);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlStr);

            Thread thread = new Thread(() -> {
                try {
                    post.setEntity(entityJson);

                    org.apache.http.HttpResponse response = client.execute(post);

                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        System.out.println(line);
                        data.setTransmitted(true);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            thread.start();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
