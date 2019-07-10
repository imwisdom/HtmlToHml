import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

public class writeHTMLinHMLFile {

  private String htmlUrl;
  private String hwpmlUrl;

  private Borderfill borderfill;
  private CharShape charShape;
  private Image image;

  private ConvertHTMLtoHML convertHTMLtoHML;

  private EmptyHML emptyHML;

  public writeHTMLinHMLFile(String htmlUrl, String hwpmlUrl) {
    this.htmlUrl = htmlUrl;
    this.hwpmlUrl = hwpmlUrl;

    this.borderfill = new Borderfill();
    this.charShape = new CharShape();
    this.image = new Image();

    this.convertHTMLtoHML = new ConvertHTMLtoHML(this.htmlUrl, this.borderfill, this.charShape,
        this.image);

    this.emptyHML = new EmptyHML(this.hwpmlUrl);
  }

  public void write() {
    convertHTMLtoHML.convert();
    StringBuffer html = convertHTMLtoHML.getHmlBuffer();
    StringBuffer bufferInHWPML = emptyHML.getBufferinHwpml();
    //insert
    insertInBody(bufferInHWPML, html);
    insertInHead(bufferInHWPML);

    //delete needless string
    int endofHWPML = bufferInHWPML.indexOf("</HWPML>");
    bufferInHWPML.replace(endofHWPML + 8, bufferInHWPML.length(), "");

    writeConvertedHtml(bufferInHWPML);
  }

  private void insertInBody(StringBuffer buffer, StringBuffer htmlBuffer) {
    int endofSection = buffer.indexOf("</SECTION>");
    buffer.insert(endofSection, htmlBuffer);

    int tableCount = StringUtils.countMatches(borderfill.toString(), "</TABLE>");

    if (tableCount >= 2) {
      buffer.replace(buffer.indexOf(" Table=\""), buffer.indexOf("/>"),
          " Table=\"" + tableCount + "\"");
    }
  }

  private void insertInHead(StringBuffer buffer) {
    insertInTableHead(buffer);
    insertInImageHead(buffer);
    insertInStringHead(buffer);
  }

  private void insertInTableHead(StringBuffer buffer) {
    StringBuffer borderfillBuf = borderfill.getBorderfill();
    if (borderfillBuf != null) {
      buffer.insert(buffer.indexOf("</BORDERFILLLIST>"), borderfillBuf);
      int borderCount = borderfill.count();

      buffer.replace(buffer.indexOf("<BORDERFILLLIST"), buffer.indexOf("<BORDERFILL BackSlash"),
          "<BORDERFILLLIST Count=\"" + borderCount + "\">");
    }
  }

  private void insertInImageHead(StringBuffer buffer) {
    StringBuffer binListBuf = image.header();
    if (binListBuf != null) {
      buffer.insert(buffer.indexOf("<FACENAMELIST>"), binListBuf);

      StringBuffer binDataBuf = image.tail();
      buffer.insert(buffer.indexOf("<SCRIPTCODE"), binDataBuf);
    }
  }

  private void insertInStringHead(StringBuffer buffer) {
    StringBuffer charShapeBuf = charShape.getCharShape();
    if (charShapeBuf != null) {
      buffer.insert(buffer.indexOf("</CHARSHAPELIST>"), charShapeBuf);
      int charshapeCount = charShape.count();

      buffer.replace(buffer.indexOf("</BORDERFILLLIST>") + 17, buffer.indexOf("<CHARSHAPE "),
          "<CHARSHAPELIST Count=\"" + charshapeCount + "\">");
    }
  }

  private void writeConvertedHtml(StringBuffer buffer) {
    File file = new File(hwpmlUrl);
    //rewrite in hml file
    FileWriter writer = null;

    try {
      // 기존 파일의 내용에 이어서 쓰려면 true를, 기존 내용을 없애고 새로 쓰려면 false를 지정한다.
      writer = new FileWriter(file, false);
      writer.write(buffer.toString());
      writer.flush();

      System.out.println("DONE");
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
