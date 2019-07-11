import org.jsoup.nodes.Element;

public class CharShape implements CharShapeHeader {

  //for style
  private StringBuffer charShape;
  private int id;
  private int count;

  public CharShape() {
    this.charShape = new StringBuffer();
    this.id = 6;
    this.count = 7;
  }

  @Override
  public void setCharShape(Element anElement) {
    if (anElement == null) {
      id = 0;
      return;
    }

    String tag = anElement.tagName();
    double fontsize = 1000;
    boolean isBold = false;

    if (tag.equals("h1")) {
      fontsize = 24;
      isBold = true;
    } else if (tag.equals("h2")) {
      fontsize = 18;
      isBold = true;
    } else if (tag.equals("h3")) {
      fontsize = 13.55;
      isBold = true;
    } else if (tag.equals("h4")) {
      fontsize = 12;
      isBold = true;
    } else if (tag.equals("h5")) {
      fontsize = 10;
      isBold = true;
    } else if (tag.equals("h6")) {
      fontsize = 7.55;
      isBold = true;
    } else {
      fontsize = StyleOfHML.fontSize(anElement);
      isBold = StyleOfHML.bold(anElement);
    }

    if (fontsize == 1000 && !isBold) {
      setCharShape(anElement.parent());
    } else {
      ++count;
      id = count - 1;
      charShape.append(AttachHMLTag.charShape(id, fontsize, isBold));
    }
  }

  @Override
  public int count() {
    return count;
  }

  @Override
  public int id() {
    return id;
  }

  @Override
  public StringBuffer getCharShape() {
    return charShape;
  }
}
