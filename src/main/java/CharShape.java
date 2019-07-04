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
  public void setCharShape(boolean isBold) {
    if (isBold) {
      ++id;
      ++count;
      charShape.append("<CHARSHAPE BorderFillId=\"2\" Height=\"1000\" Id=\"" + id
          + "\" ShadeColor=\"4294967295\" SymMark=\"0\" TextColor=\"0\" UseFontSpace=\"false\" UseKerning=\"false\"><FONTID Hangul=\"1\" Hanja=\"1\" Japanese=\"1\" Latin=\"1\" Other=\"1\" Symbol=\"1\" User=\"1\"/><RATIO Hangul=\"100\" Hanja=\"100\" Japanese=\"100\" Latin=\"100\" Other=\"100\" Symbol=\"100\" User=\"100\"/><CHARSPACING Hangul=\"0\" Hanja=\"0\" Japanese=\"0\" Latin=\"0\" Other=\"0\" Symbol=\"0\" User=\"0\"/><RELSIZE Hangul=\"100\" Hanja=\"100\" Japanese=\"100\" Latin=\"100\" Other=\"100\" Symbol=\"100\" User=\"100\"/><CHAROFFSET Hangul=\"0\" Hanja=\"0\" Japanese=\"0\" Latin=\"0\" Other=\"0\" Symbol=\"0\" User=\"0\"/></CHARSHAPE>");
    }
    if (isBold) {
      charShape.insert(charShape.indexOf("</CHARSHAPE>"), "<BOLD/>");
    }
  }

  @Override
  public void setCharShape(double fontsize, boolean isBold) {
    ++id;
    ++count;
    charShape.append(
        "<CHARSHAPE BorderFillId=\"2\" Height=\"" + (fontsize * 100) + "\" Id=\"" + id
            + "\" ShadeColor=\"4294967295\" SymMark=\"0\" TextColor=\"0\" UseFontSpace=\"false\" UseKerning=\"false\"><FONTID Hangul=\"1\" Hanja=\"1\" Japanese=\"1\" Latin=\"1\" Other=\"1\" Symbol=\"1\" User=\"1\"/><RATIO Hangul=\"100\" Hanja=\"100\" Japanese=\"100\" Latin=\"100\" Other=\"100\" Symbol=\"100\" User=\"100\"/><CHARSPACING Hangul=\"0\" Hanja=\"0\" Japanese=\"0\" Latin=\"0\" Other=\"0\" Symbol=\"0\" User=\"0\"/><RELSIZE Hangul=\"100\" Hanja=\"100\" Japanese=\"100\" Latin=\"100\" Other=\"100\" Symbol=\"100\" User=\"100\"/><CHAROFFSET Hangul=\"0\" Hanja=\"0\" Japanese=\"0\" Latin=\"0\" Other=\"0\" Symbol=\"0\" User=\"0\"/></CHARSHAPE>");

    if (isBold) {
      charShape.insert(charShape.lastIndexOf("</CHARSHAPE>"), "<BOLD/>");
    }
  }

  @Override
  public int count() {
    return count;   //first id number is 0
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
