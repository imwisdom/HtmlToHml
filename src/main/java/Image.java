import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;

public class Image {

  private int count;
  private StringBuffer header;
  private StringBuffer tail;


  public Image() {
    this.count = 0;
    this.header = new StringBuffer();
    this.tail = new StringBuffer();
  }
  public void appendImageInfo(String src) {
    String extension = "";
    if (src.contains("data:image")) {
      extension = src.substring(src.indexOf("/") + 1, src.indexOf(";"));

      appendImageHead(extension);
      setBase64EncodingData(src, true);
    } else {
      extension = src.substring(src.lastIndexOf(".") + 1);

      appendImageHead(extension);
      setBase64EncodingData(src, false);
    }
  }

  private void appendImageHead(String extension) {
    header.append("<BINITEM BinData=\"" + (count + 1) + "\" Format=\"" + extension + "\""
        + " Type=\"Embedding\"/>");
    count++;
  }

  private void setBase64EncodingData(String src, boolean isEncoded) {
    String encoded_str = "";
    if (isEncoded) {
      encoded_str = src.substring(src.indexOf(",") + 1);
    } else {
      ByteArrayOutputStream byteOutputStream = getBinaryImageData(src);
      byte[] fileArray = byteOutputStream.toByteArray();
      encoded_str = new String(Base64.encodeBase64(fileArray));
    }
    tail.append(
        "<BINDATA Encoding=\"Base64\" Id=\"" + (count) + "\" Size=\"11210\">" + encoded_str
            + "</BINDATA>");
  }

  private ByteArrayOutputStream getBinaryImageData(String url) {
    FileInputStream inputStream = null;
    ByteArrayOutputStream byteOutputStream = null;

    File file = new File(url);

    if (file.exists()) {
      try {
        inputStream = new FileInputStream(file);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      byteOutputStream = new ByteArrayOutputStream();

      int len = 0;
      byte[] buf = new byte[1024];

      while (true) {
        try {
          if ((len = inputStream.read(buf)) == -1) {
            break;
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
        byteOutputStream.write(buf, 0, len);
      }

      return byteOutputStream;
    }
    return null;
  }

  public void putBindataInList() {
    header.insert(0, "<BINDATALIST Count=\"" + count + "\">");
    header.append("</BINDATALIST>");
  }

  public void putBindataInStorage() {
    tail.insert(0, "<BINDATASTORAGE>");
    tail.append("</BINDATASTORAGE>");
  }

  public StringBuffer header() {
    return header;
  }
  public StringBuffer tail() {
    return tail;
  }
  public int count() {
    return count;
  }


}
