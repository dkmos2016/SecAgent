package SecAgent.Filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map;

public class CopyServletRequestWrapper extends HttpServletRequestWrapper {
//  private byte[] body;
  private ByteArrayOutputStream baout = new ByteArrayOutputStream();

  public CopyServletRequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
//    body = this.getBodyString(request).getBytes(Charset.forName("UTF-8"));

    InputStream in = request.getInputStream();
    int v;
    while ((v=in.read()) != -1) {
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
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(getInputStream(), "UTF-8"));
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

  @Override
  public Map<String, String[]> getParameterMap() {
    return super.getParameterMap();
  }

  public String getBodyString(ServletRequest request) {
    StringBuilder sb = new StringBuilder();
    InputStream inputStream = null;
    BufferedReader reader = null;
    try {
      if (this.baout == null)
       inputStream = request.getInputStream();

      reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
      String line = "";
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return sb.toString();
  }
}
