import org.jsoup.nodes.Element;

public class Borderfill implements BorderfillHeader {

  private StringBuffer borderfill;
  private int id;
  private int count;

  public Borderfill() {
    this.borderfill = new StringBuffer(AttachHMLTag.BorderFill(3, 4294967295L));
    this.id = 3;
    this.count = 3;
  }

  @Override
  public void setBorderfill(Element anElement) {
    if (anElement == null) {
      id = 3;
      return;
    }

    long colorValue = StyleOfHML.colorValue(anElement);
    if (colorValue > 0) {
      ++count;
      id = count;
      borderfill.append(AttachHMLTag.BorderFill(id, colorValue));
    } else {
      setBorderfill(anElement.parent());
    }

  }

  @Override
  public int id() {
    return id;
  }

  @Override
  public StringBuffer getBorderfill() {
    return borderfill;
  }

  @Override
  public int count() {
    return count;
  }
}
