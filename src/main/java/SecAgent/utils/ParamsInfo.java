package SecAgent.utils;

import org.objectweb.asm.Type;

public class ParamsInfo {
  private final Type[] in_types;
  private final String clazz;
  private final String method;
  private final String desc;
  private final String signature;
  private final int access;
  private int size;

  public ParamsInfo(String clazz, int access, String method, Type[] types, String desc, String signature) {
    this.in_types = types;
    this.method = method;
    this.clazz = clazz;
    this.desc = desc;
    this.signature = signature;
    this.access = access;
  }

  public int getSize() {
    int size = 0;
    for (Type type : this.in_types) {
      size += type.getSize();
    }

    this.size = size;
    return this.size;
  }

  public Type[] getIn_types() {
    return this.in_types;
  }

  public String getMethod() {
    return this.method;
  }

  public String getClazz() {
    return this.clazz;
  }

  public String getDesc() {
    return this.desc;
  }

  public String getSignature() {
    return this.signature;
  }

  public int getAccess() {
    return this.access;
  }

  @Override
  public String toString() {
    return String.format("%s.%s%s", clazz.replace('/', '.'), method, desc).replace('/', '.');
  }
}
