import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HTML {

  private String url;

  public HTML(String url) {
    this.url = url;
  }

  public Element getbody() {
    File file = new File(url);
    Document html = null;

    try {
      html = Jsoup.parse(file, "euc-kr"); //euc-kr or utf-8
    } catch (IOException e) {
      e.printStackTrace();
    }

    Element htmlbody = html.child(0).child(1);

    return htmlbody;
  }
}
