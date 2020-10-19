package SecAgent.Utils.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Common {

  public static String MapToJsonStr(Map<String, Object> map) {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      if (value instanceof String) {
        sb.append(String.format("\"%s\": \"%s\",", key, value));
      } else if (value instanceof Byte
          || value instanceof Character
          || value instanceof Integer
          || value instanceof Short) {
        sb.append(String.format("\"%s\": %d,", key, value));
      } else if (value instanceof Double || value instanceof Float) {
        sb.append(String.format("\"%s\": %f,", key, value));
      } else if (value instanceof Long) {
        sb.append(String.format("\"%s\": %d,", key, value));
      } else if (value.getClass().getName().startsWith("[")) {
        ArrayList arrayList = new ArrayList();
        if (value instanceof int[]) {
          for (int obj : (int[]) value) {
            arrayList.add(obj);
          }
        } else if (value instanceof char[]) {
          for (char obj : (char[]) value) {
            arrayList.add(obj);
          }
        } else if (value instanceof byte[]) {
          for (byte obj : (byte[]) value) {
            arrayList.add(obj);
          }
        } else if (value instanceof short[]) {
          for (short obj : (short[]) value) {
            arrayList.add(obj);
          }
        } else if (value instanceof long[]) {
          for (long obj : (long[]) value) {
            arrayList.add(obj);
          }
        } else if (value instanceof String[]) {
          for (String obj : (String[]) value) {
            arrayList.add(obj);
          }
        } else if (value instanceof float[]) {
          for (float obj : (float[]) value) {
            arrayList.add(obj);
          }
        } else if (value instanceof double[]) {
          for (double obj : (double[]) value) {
            arrayList.add(obj);
          }
        } else if (value instanceof boolean[]) {
          for (boolean obj : (boolean[]) value) {
            arrayList.add(obj);
          }
        } else {
          for (Object obj : (Object[]) value) {
            arrayList.add(obj);
          }
        }

        sb.append(String.format("\"%s\": %s,", key, arrayList));
      } else {
        sb.append(String.format("\"%s\": %s,", key, value));
      }
    }
    if (sb.length() > 1) sb.deleteCharAt(sb.length() - 1);
    sb.append("}");

    return sb.toString();
  }

  public static String MapToFormData(Map<String, Object> map) {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      if (value instanceof String) {
        sb.append(String.format("%s=%s&", key, value));
      } else if (value instanceof Byte
          || value instanceof Character
          || value instanceof Integer
          || value instanceof Short) {
        sb.append(String.format("%s=%d&", key, value));
      } else if (value instanceof Double || value instanceof Float) {
        sb.append(String.format("%s=%f&", key, value));
      } else if (value instanceof Long) {
        sb.append(String.format("%s=%d&", key, value));
      } else if (value.getClass().getName().startsWith("[")) {
        ArrayList arrayList = new ArrayList();
        if (value instanceof int[]) {
          for (int i : (int[]) value) {
            arrayList.add(i);
          }
        } else if (value instanceof char[]) {
          for (char c : (char[]) value) {
            arrayList.add(c);
          }
        } else if (value instanceof byte[]) {
          for (byte b : (byte[]) value) {
            arrayList.add(b);
          }
        } else if (value instanceof short[]) {
          for (short s : (short[]) value) {
            arrayList.add(s);
          }
        } else if (value instanceof long[]) {
          for (long l : (long[]) value) {
            arrayList.add(l);
          }
        } else if (value instanceof String[]) {
          for (String S : (String[]) value) {
            arrayList.add(S);
          }
        } else if (value instanceof float[]) {
          for (float f : (float[]) value) {
            arrayList.add(f);
          }
        } else if (value instanceof double[]) {
          for (double d : (double[]) value) {
            arrayList.add(d);
          }
        } else if (value instanceof boolean[]) {
          for (boolean B : (boolean[]) value) {
            arrayList.add(B);
          }
        } else {
          for (Object obj : (Object[]) value) {
            arrayList.add(obj);
          }
        }
        sb.append(String.format("%s=%s&", key, arrayList));
      } else {
        sb.append(String.format("%s=%s&", key, value));
      }
    }
    sb.deleteCharAt(sb.length() - 1);

    return sb.toString();
  }

  public static OutputStream transferTo(InputStream in) {
    Objects.requireNonNull(in, "in");
    ByteArrayOutputStream ba = new ByteArrayOutputStream();
    try {
      int v = -1;
      while ((v = in.read()) > -1) ba.write(v);
    } catch (Exception e) {
      ;
    }
    return ba;
  }

  public static InputStream transferFrom(ByteArrayOutputStream outputStream) {
    Objects.requireNonNull(outputStream, "out");
    InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

    return new InputStream() {
      @Override
      public int read() throws IOException {
        return inputStream.read();
      }
    };
  }
}
