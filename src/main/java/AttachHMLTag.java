public class AttachHMLTag {

  static HMLNode P(int paraId, HMLNode hmlNode) {

    String head = "<P Parashape=\"" + paraId + "\" Style=\"0\">";
    String foot = "</P>";

    return new HMLNode(head, foot, hmlNode);
  }

  static HMLNode ImageBody(int imgCount, double width, double height, HMLNode hmlNode) {

    hmlNode.setHeadFirst("<P Parashape=\"3\" Style=\"0\"><TEXT CharShape=\"0\">"
    +"<PICTURE Reverse=\"false\"><SHAPEOBJECT InstId=\"2137949406\" Lock=\"false\" NumberingType=\"Figure\" ZOrder=\"1\"><SIZE Height=\""
        + height + "\" HeightRelTo=\"Absolute\" Protect=\"false\" Width=\"" + width
        + "\" WidthRelTo=\"Absolute\"/><POSITION AffectLSpacing=\"false\" AllowOverlap=\"false\" FlowWithText=\"true\" HoldAnchorAndSO=\"false\" HorzAlign=\"Left\" HorzOffset=\"0\" HorzRelTo=\"Column\" TreatAsChar=\"true\" VertAlign=\"Top\" VertOffset=\"0\" VertRelTo=\"Para\"/><OUTSIDEMARGIN Bottom=\"0\" Left=\"0\" Right=\"0\" Top=\"0\"/><SHAPECOMMENT></SHAPECOMMENT></SHAPEOBJECT><SHAPECOMPONENT CurHeight=\""
        + height + "\" CurWidth=\"" + width
        + "\" GroupLevel=\"0\" HorzFlip=\"false\" InstID=\"1064207583\" OriHeight=\"" + 200000
        + "\" OriWidth=\"" + 200000
        + "\" VertFlip=\"false\" XPos=\"0\" YPos=\"0\"><ROTATIONINFO Angle=\"0\" CenterX=\"9750\" CenterY=\"12990\"/><RENDERINGINFO><TRANSMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/><SCAMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/><ROTMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/></RENDERINGINFO></SHAPECOMPONENT><IMAGERECT X0=\"0\" X1=\""
        + 200000 + "\" X2=\"" + 200000 + "\" X3=\"0\" Y0=\"0\" Y1=\"0\" Y2=\"" + 200000
        + "\" Y3=\"" + 200000
        + "\"/><IMAGECLIP Bottom=\"200000\" Left=\"0\" Right=\"200000\" Top=\"0\"/><INSIDEMARGIN Bottom=\"0\" Left=\"0\" Right=\"0\" Top=\"0\"/><IMAGE Alpha=\"0\" BinItem=\""
        + imgCount
        + "\" Bright=\"0\" Contrast=\"0\" Effect=\"RealPic\"/><EFFECTS/></PICTURE>");
    hmlNode.setFoot("<CHAR/></TEXT></P>");

    return hmlNode;
  }

  static HMLNode Text(int charShapeID, HMLNode hmlNode) {

    String head = "<TEXT CharShape=\"" + charShapeID + "\"><CHAR>";
    String foot = "</CHAR></TEXT>";

    return new HMLNode(head, foot, hmlNode);
  }

  static String TableHeader(int colCount, int rowCount) {
    return "<TEXT CharShape=\"0\"><TABLE BorderFill=\"3\" CellSpacing=\"0\" ColCount=\"" + colCount
        + "\" PageBreak=\"Cell\" RepeatHeader=\"true\" RowCount=\"" + rowCount
        + "\"><SHAPEOBJECT InstId=\"2136179221\" Lock=\"false\" NumberingType=\"Table\" TextWrap=\"TopAndBottom\" ZOrder=\"0\"><SIZE Height=\"3846\" HeightRelTo=\"Absolute\" Protect=\"false\" Width=\"41950\" WidthRelTo=\"Absolute\"/><POSITION AffectLSpacing=\"false\" AllowOverlap=\"false\" FlowWithText=\"true\" HoldAnchorAndSO=\"false\" HorzAlign=\"Left\" HorzOffset=\"0\" HorzRelTo=\"Column\" TreatAsChar=\"false\" VertAlign=\"Top\" VertOffset=\"0\" VertRelTo=\"Para\"/><OUTSIDEMARGIN Bottom=\"283\" Left=\"283\" Right=\"283\" Top=\"283\"/></SHAPEOBJECT><INSIDEMARGIN Bottom=\"141\" Left=\"510\" Right=\"510\" Top=\"141\"/>";
  }

  static String ColumnOfCell(int colAddr, int colSpan) {
    return "ColAddr=\"" + colAddr + "\" ColSpan=\"" + colSpan + "\" ";
  }

  static String RowOfCell(int rowAddr, int rowSpan, double width) {
    return "RowAddr=\"" + rowAddr + "\" RowSpan=\"" + rowSpan + "\" Width=\"" + width + "\"";
  }

  static HMLNode Cell(int borderFillId, HMLNode hmlNode, String colofCell, String rowofCell) {

    String head = "<CELL BorderFill=\"" + borderFillId + "\" " + colofCell
        + "Dirty=\"false\" Editable=\"false\" HasMargin=\"false\" Header=\"false\" Height=\"282\" Protect=\"false\" "
        + rowofCell
        + "><CELLMARGIN Bottom=\"141\" Left=\"510\" Right=\"510\" Top=\"141\"/><PARALIST LineWrap=\"Break\" LinkListID=\"0\" LinkListIDNext=\"0\" TextDirection=\"0\" VertAlign=\"Center\">"
    ;
    String foot = "</PARALIST></CELL>";

    return new HMLNode(head, foot, hmlNode);
  }

  static String BorderFill(int id, long colorValue) {
    return
        "<BORDERFILL BackSlash=\"0\" BreakCellSeparateLine=\"0\" CenterLine=\"0\" CounterBackSlash=\"0\" CounterSlash=\"0\" CrookedSlash=\"0\" Id=\""
            + id
            + "\" Shadow=\"false\" Slash=\"0\" ThreeD=\"false\"><LEFTBORDER Type=\"Solid\" Width=\"0.12mm\"/><RIGHTBORDER Type=\"Solid\" Width=\"0.12mm\"/><TOPBORDER Type=\"Solid\" Width=\"0.12mm\"/><BOTTOMBORDER Type=\"Solid\" Width=\"0.12mm\"/><DIAGONAL Type=\"Solid\" Width=\"0.1mm\"/><FILLBRUSH><WINDOWBRUSH Alpha=\"0\" FaceColor=\""
            + colorValue + "\" HatchColor=\"4278190080\"/></FILLBRUSH></BORDERFILL>";
  }

  static String charShape(int id, double fontSize, boolean isBold) {
    StringBuffer charshape = new StringBuffer(
        "<CHARSHAPE BorderFillId=\"2\" Height=\"" + (fontSize * 100) + "\" Id=\"" + id
            + "\" ShadeColor=\"4294967295\" SymMark=\"0\" TextColor=\"0\" UseFontSpace=\"false\" UseKerning=\"false\"><FONTID Hangul=\"1\" Hanja=\"1\" Japanese=\"1\" Latin=\"1\" Other=\"1\" Symbol=\"1\" User=\"1\"/><RATIO Hangul=\"100\" Hanja=\"100\" Japanese=\"100\" Latin=\"100\" Other=\"100\" Symbol=\"100\" User=\"100\"/><CHARSPACING Hangul=\"0\" Hanja=\"0\" Japanese=\"0\" Latin=\"0\" Other=\"0\" Symbol=\"0\" User=\"0\"/><RELSIZE Hangul=\"100\" Hanja=\"100\" Japanese=\"100\" Latin=\"100\" Other=\"100\" Symbol=\"100\" User=\"100\"/><CHAROFFSET Hangul=\"0\" Hanja=\"0\" Japanese=\"0\" Latin=\"0\" Other=\"0\" Symbol=\"0\" User=\"0\"/></CHARSHAPE>");

    if (isBold) {
      charshape.insert(charshape.indexOf("</CHARSHAPE>"), "<BOLD/>");
    }

    return charshape.toString();
  }
}
