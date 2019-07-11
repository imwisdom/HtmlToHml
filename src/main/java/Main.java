
public class Main {

  /**
   * @param args - args[0] : src html args[1] : dist path
   */
  public static void main(String[] args) {

    HmlGenerator hmlGenerator = new HmlGenerator(args[0], args[1]);
    hmlGenerator.generate();
  }
}
