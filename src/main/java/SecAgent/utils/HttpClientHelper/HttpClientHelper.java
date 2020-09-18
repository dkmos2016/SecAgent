package SecAgent.utils.HttpClientHelper;

import SecAgent.utils.Common;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpClientHelper {
  public static boolean JSON = false;

  public static void doGet(String url) {
    HttpURLConnection connection = null;
    InputStream in = null;
    BufferedReader br = null;
    String result = null;

    try {
      URL httpurl = new URL(url);
      connection = (HttpURLConnection) httpurl.openConnection();
      connection.setRequestMethod("GET");
      connection.setConnectTimeout(3000);
      connection.setReadTimeout(6000);

      connection.setRequestProperty(
          "Accept",
          "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
      connection.setRequestProperty(
          "user-agent",
          "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/84.0.4147.125 SecAgent For PAB");

      connection.connect();

      in = connection.getInputStream();
      br = new BufferedReader(new InputStreamReader(in));

      StringBuilder sb = new StringBuilder();
      String tmp = null;
      while ((tmp = br.readLine()) != null) {
        sb.append(tmp);
        sb.append("\n");
      }

      result = sb.toString();
      System.out.println("result: " + result);

      connection.disconnect();

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      //             connection.disconnect();
    }
  }

  public static void doPost(String url, HashMap<String, String> headers, Map body) {
    HttpURLConnection connection = null;
    InputStream in = null;
    BufferedReader br = null;

    OutputStream out = null;
    String result = null;

    try {
      URL httpurl = new URL(url);
      connection = (HttpURLConnection) httpurl.openConnection();
      connection.setRequestMethod("POST");
      connection.setConnectTimeout(3000);
      connection.setReadTimeout(6000);

      connection.setDoOutput(true);
      connection.setDoInput(true);

      setHeaders(connection, headers);

      connection.connect();

      out = connection.getOutputStream();

      String reqBody = preparePostBody(body);
      System.out.println("prepared: " + reqBody);

      out.write(reqBody.getBytes());
      // get result
      in = connection.getInputStream();
      br = new BufferedReader(new InputStreamReader(in));

      StringBuilder sb = new StringBuilder();
      String tmp = null;
      while ((tmp = br.readLine()) != null) {
        sb.append(tmp);
        sb.append("\n");
      }

      result = sb.toString();
      System.out.println("result: " + result);
      connection.disconnect();

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      connection.disconnect();
    }
  }

  private static void setHeaders(HttpURLConnection connection, Map<String, String> headers) {
    if (connection == null) return;

    for (Map.Entry<String, String> entry : headers.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (key.toLowerCase().equals("content-type") && value.contains("json")) {
        JSON = true;
      }
      connection.setRequestProperty(key, value);
    }

    if (connection.getRequestProperty("Content-Type") == null) {
      connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    }

    connection.setRequestProperty(
        "user-agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/84.0.4147.125 SecAgent For PAB");
    connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
  }

  private static String preparePostBody(Map<String, Object> body) {
    return JSON ? Common.MapToJsonStr(body) : Common.MapToFormData(body);
  }
}
