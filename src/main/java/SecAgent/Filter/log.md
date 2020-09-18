
## 抓取HttpServletRequest中InputStream内容(PostBody)

### Request类分析
- HttpServletRequest、ServletRequest为接口类，无法插入代码
``` java
public interface HttpServletRequest extends ServletRequest {...}
public interface ServletRequest{...}
```

- 暂发现apache tomcat系列会使用Request对象进行获取ServletInputStream
``` java
// RequestFacade
public class RequestFacade implements HttpServletRequest
    protected Request request = null;
...
    @Override
    public ServletInputStream getInputStream() throws IOException {

        if (request == null) {
            throw new IllegalStateException(
                            sm.getString("requestFacade.nullRequest"));
        }

        return request.getInputStream();
    }
...
}

// Request
public class Request implements HttpServletRequest {
    protected CoyoteInputStream inputStream =
            new CoyoteInputStream(inputBuffer);

    @Override
    public ServletInputStream getInputStream() throws IOException {

        if (usingReader) {
            throw new IllegalStateException(sm.getString("coyoteRequest.getInputStream.ise"));
        }

        usingInputStream = true;
        if (inputStream == null) {
            inputStream = new CoyoteInputStream(inputBuffer);
        }
        return inputStream;

    }
...
}
```

### xxInputSteam分析
ServletInputStream、InputStream 无法复制， 不支持mark/reset。仅允许读取一次
```
# ServletInputStream
public abstract class ServletInputStream extends InputStream {
...
}

// InputStream
public abstract class InputStream implements Closeable {
}
```


### 方案

1.使用filter过滤，复制Request
``` java


```



2.向Request.class中插入（修改）方法
``` java


```