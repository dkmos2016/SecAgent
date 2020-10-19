public class Test {
  int id;

  public Test(int id) {
    this.id = id;
    print();
  }

  public void print(Object... obj) {
    System.out.println("id = " + this.id);
  }
}
