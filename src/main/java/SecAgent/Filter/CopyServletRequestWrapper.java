package SecAgent.Filter;

import SecAgent.Conf.Config;
import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class CopyServletRequestWrapper extends HttpServletRequestWrapper {
  //  private byte[] body;
  private static final DefaultLogger logger =
      DefaultLogger.getLogger(CopyServletRequestWrapper.class, Config.EXCEPTION_PATH);

  private ByteArrayOutputStream baout = new ByteArrayOutputStream();
  private Map paramMap = new HashMap();

  public CopyServletRequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
    //    body = this.getBodyString(request).getBytes(Charset.forName("UTF-8"));

    InputStream in = request.getInputStream();
    int v;
    while ((v = in.read()) != -1) {
      this.baout.write(v);
    }

    //    this.body = baout.toByteArray();
    //    in.close();

  }

  public byte[] getBody() {
    return this.baout.toByteArray();
  }

  public void setBody(byte[] body) throws IOException {
    //    this.body = body;
    this.baout = new ByteArrayOutputStream();
    this.baout.write(body);
  }

  @Override
  public Map<String, String[]> getParameterMap() {
    String params = new String(this.baout.toByteArray());
    Map<String, String[]> map = new HashMap<>();
    Map<String, ArrayList<String>> tmp_map = new HashMap<>();

    if (params == null || params.isEmpty()) return map;

    for (String param: params.split("&")) {
      String[] field_item = param.split("=");

      ArrayList<String> arrayList = tmp_map.getOrDefault(field_item[0], new ArrayList());
      arrayList.add(field_item[1]);

      tmp_map.put(field_item[0], arrayList);

      map.put(field_item[0], arrayList.toArray(new String[0]));
    }
    this.paramMap = map;

    return this.paramMap;
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {

    final ByteArrayInputStream bais = new ByteArrayInputStream(this.baout.toByteArray());

    return new ServletInputStream() {
      @Override
      public boolean isFinished() {
        return false;
      }

      @Override
      public boolean isReady() {
        return false;
      }

      @Override
      public void setReadListener(ReadListener readListener) {}

      @Override
      public int read() throws IOException {
        return bais.read();
      }
    };
  }

  @Override
  public String getHeader(String name) {
    return super.getHeader(name);
  }

  @Override
  public Enumeration<String> getHeaderNames() {
    return super.getHeaderNames();
  }

  @Override
  public Enumeration<String> getHeaders(String name) {
    return super.getHeaders(name);
  }

  public String getBodyString(ServletRequest request) {
    StringBuilder sb = new StringBuilder();
    InputStream inputStream = null;
    BufferedReader reader = null;
    try {
      if (this.baout == null) inputStream = request.getInputStream();

      reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
      String line = "";
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
    } catch (IOException e) {
      if (logger != null) logger.error(e);

    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          //          e.printStackTrace();
          if (logger != null) logger.error(e);
        }
      }
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          //          e.printStackTrace();
          if (logger != null) logger.error(e);
        }
      }
    }
    return sb.toString();
  }
}
