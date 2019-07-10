import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class EmptyHML {

  private String url;

  public EmptyHML(String url) {
    this.url = url;
  }

  public StringBuffer getBufferinHwpml() {
    //add contexts of buffer to hwpml file
    File file = new File(url);
    BufferedReader br = null;
    char[] ch = new char[(int) file.length()];
    StringBuffer buffer = new StringBuffer();

    try {
      br = new BufferedReader(new FileReader(file));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    try {
      br.read(ch);
      buffer.append(ch);

      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return buffer;
  }

}
