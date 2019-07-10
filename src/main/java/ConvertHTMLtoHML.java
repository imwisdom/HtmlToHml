import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class ConvertHTMLtoHML {

  static final HMLNode EMPTY_NODE = new HMLNode();
  private HTML html;
  private Borderfill borderfill;
  private CharShape charShape;
  private Image image;
  private StringBuffer hmlBuffer;
  //temp
  private HMLNode hmlNode;

  public ConvertHTMLtoHML(String url, Borderfill borderfill, CharShape charShape, Image image) {
    html = new HTML(url);
    this.borderfill = borderfill;
    this.charShape = charShape;
    this.image = image;
    this.hmlBuffer = new StringBuffer();
    //temp
    this.hmlNode = new HMLNode();
  }

  public void convert() {

    Element body = html.getbody();
    Node child = body.childNode(0);
    convertRecursively(child);

    image.putBindataInList();
    image.putBindataInStorage();
  }

  private void convertRecursively(Node htmlNode) {
    if(htmlNode == null) return;

    HMLNode aNode = convert(htmlNode);
    if (aNode!=EMPTY_NODE){
      if (!findTag(aNode, "P")) {
        aNode = AttachHMLTag.P(16, aNode);
      }
      hmlNode.setSibling(aNode);
    }
    convertRecursively(htmlNode.nextSibling());
  }

  public StringBuffer getHmlBuffer() {
    getHmlBufferRecursively(this.hmlNode.getSibling());
    return this.hmlBuffer;
  }

  public void getHmlBufferRecursively(HMLNode hmlNode) {
    if (hmlNode == null) {
      return;
    }
    hmlBuffer.append(hmlNode.getHead());

    if(hmlNode.getChild()!=null)
      getHmlBufferRecursively(hmlNode.getChild());

    hmlBuffer.append(hmlNode.getFoot());
    getHmlBufferRecursively(hmlNode.getSibling());

  }

  private HMLNode convert(Node htmlNode) {

    boolean isNull = (htmlNode == null);
    boolean isTrim = (htmlNode.toString().trim().equals("") ||
        htmlNode.toString().trim().equals("\n"));

    if (isNull || isTrim) {
      return EMPTY_NODE;
    }

    if (htmlNode instanceof Element) {
      Element htmlElement = (Element) htmlNode;
      String tag = htmlElement.tagName();

      if (tag.equals("img") || tag.equals("image")) {
        return img_convert(htmlElement);
      } else if (tag.equals("table")) {
        return table_convert(htmlElement);
      } else {
        return string_convert(htmlElement);
      }
    } else {
      HMLNode stringNode = new HMLNode(); //just stringNode. no need head, foot, child, sibling...
      stringNode.setHeadFirst(htmlNode.toString());
      return stringNode;
    }
  }

  private HMLNode img_convert(Element htmlElement) {
    HMLNode aNode = new HMLNode();
    String src = htmlElement.attr("src");
    double width = 40000;
    double height = 40000;
    if (htmlElement.hasAttr("width")) {
      width = Double.parseDouble(htmlElement.attr("width")) * 67;
    }
    if (htmlElement.hasAttr("height")) {
      height = Double.parseDouble(htmlElement.attr("height")) * 67;
    }

    image.appendImageInfo(src);
    aNode = AttachHMLTag.ImageBody(image.count(), width, height, aNode);

    return aNode;
  }

  private HMLNode table_convert(Element htmlElement) {
    //row count and column count
    Elements tr = htmlElement.select("tr");

    int rowCount = tr.size();
    int colCount = getColCount(tr);
    String tableHeader = AttachHMLTag.TableHeader(colCount, rowCount);

    HMLNode converted = new HMLNode();
    converted.setHeadFirst(tableHeader);

    int rowaddr = 0;
    int coladdr = 0;

    HMLNode trNode = new HMLNode();
    while (rowaddr < rowCount) {
      if (coladdr == 0) {
        trNode.setHeadLast("<ROW>");
      }

      Element tr_pointer = tr.get(rowaddr);
      int numberOfCell = tr_pointer.children().size();  //in tr

      HMLNode cellNode = getCellWithInfoTag(tr_pointer, coladdr, rowaddr);
      trNode.addChild(cellNode);
      coladdr++;
      if (coladdr == numberOfCell) {
        coladdr = 0;
        rowaddr++;
        trNode.setFoot("</ROW>");
        converted.addChild(trNode);
        trNode = new HMLNode();
      }
    }
    converted.setFoot("</TABLE></TEXT>");
    return AttachHMLTag.P(16, converted);
  }

  private HMLNode getCellWithInfoTag(Element tr_pointer, int coladdr, int rowaddr) {
    Element cell = tr_pointer.child(coladdr);   //<td></td> or <th></th>
    int numberOfCell = tr_pointer.children().size();  //in tr

    borderfill.setBorderfill(cell);
    int borderFillIdOfCell = borderfill.id();

    int colSpan = 1;
    int rowSpan = 1;

    if (cell.hasAttr("colspan")) {
      colSpan = Integer.parseInt(cell.attr("colspan"));
    }
    if (cell.hasAttr("rowspan")) {
      rowSpan = Integer.parseInt(cell.attr("rowspan"));
    }

    String colInfo = AttachHMLTag.ColumnOfCell(coladdr, colSpan);
    double widthOfCell = (double) 41950 / numberOfCell;
    String rowInfo = AttachHMLTag.RowOfCell(rowaddr, rowSpan, widthOfCell);

    HMLNode cellContent = getCellContent(cell);
    cellContent = AttachHMLTag.Cell(borderFillIdOfCell, cellContent, colInfo, rowInfo);
    return cellContent;
  }

  private HMLNode getCellContent(Element cell) {
    HMLNode cellContent = new HMLNode();

    if (cell.childNodeSize() > 0) {
      Node cell_child = cell.childNode(0);

      while (cell_child != null) {
        HMLNode aNode = convert(cell_child);
        if(aNode!=EMPTY_NODE){
          if(!findTag(aNode, "TEXT"))
            aNode = AttachHMLTag.Text(0, aNode);
          if(!findTag(aNode, "P"))
            aNode = AttachHMLTag.P(3, aNode);
          cellContent.addChild(aNode);
        }
        cell_child = cell_child.nextSibling();
      }
    }
    return cellContent;
  }

  private int getColCount(Elements tr) {
    Element tr_first = tr.first();
    int max_tdCount = 0;

    while (tr_first != null) {
      if (tr_first.children().size() > max_tdCount) {
        max_tdCount = tr_first.children().size();
      }

      tr_first = tr_first.nextElementSibling();
    }

    return max_tdCount;
  }

  int listIndex = 1;
  private HMLNode string_convert(Element htmlElement) {
    String tag = htmlElement.tagName();
    if (tag.equals("br")) {
      if(htmlElement.parent().children().size()!=1)
        return AttachHMLTag.P(16, AttachHMLTag.Text(0, EMPTY_NODE));
      else
        return AttachHMLTag.Text(0, EMPTY_NODE);
    } else if (tag.equals("ul") || tag.equals("ol")) {
      return listString_convert(htmlElement);
    }
    charShape.setCharShape(htmlElement);
    int charShapeId = charShape.id();
    HMLNode strNode = new HMLNode();

    if(tag.equals("li")){
      String headString = "<TAB/>";
      if (htmlElement.parent().tagName().equals("ol")) {
        headString = headString + listIndex + " ";
        listIndex++;
      } else
        headString = headString + "- ";

      HMLNode tabNode = new HMLNode(headString, "", null);
      tabNode = AttachHMLTag.Text(0, tabNode);
      strNode.addChild(tabNode);
    }

    if (htmlElement.childNodeSize() > 0) {
      Node child = htmlElement.childNode(0);
      while (child != null) {
        HMLNode aNode = convert(child);

        if (aNode != EMPTY_NODE){
          if (!aNode.getHead().equals("") && aNode.getFoot().equals("")) {
            aNode = AttachHMLTag.Text(charShapeId, aNode);
          }

          Node nextChild = child.nextSibling();
          if(nextChild instanceof Element)
          {
            if(nextChild.nextSibling()!=null && nextChild.toString().equals("\n")){
              nextChild = nextChild.nextSibling();
            }
            String nextChildTag = ((Element) nextChild).tagName();
            if(!aNode.getFoot().contains("</P>") && (
                nextChildTag.equals("br") || nextChildTag.equals("p")
                    || nextChildTag.equals("ol") || nextChildTag.equals("ul"))){
              aNode = AttachHMLTag.P(3, aNode);
            }
          }
          strNode.addChild(aNode);
        }
        child = child.nextSibling();
      }
    }
    if((tag.equals("p") || tag.indexOf("h")==0) && !findTag(strNode, "P"))
      return AttachHMLTag.P(3, strNode);
    else
      return strNode;
  }
//  private HMLNode attachPTag(HMLNode hmlNode)
//  {
//    HMLNode aNode = hmlNode;
//
//
//  }
  private boolean findTag(HMLNode hmlNode, String tag)
  {
    if(hmlNode==null)
      return false;

    if(hmlNode.getFoot().contains("</"+tag+">"))
      return true;

    if(hmlNode.getFoot().equals("") && hmlNode.getHead().equals(""))
      return findTag(hmlNode.getSibling(), tag) || findTag(hmlNode.getChild(), tag);
    return findTag(hmlNode.getChild(), tag);
  }

  private HMLNode listString_convert(Element htmlElement) {
    HMLNode listNode = new HMLNode();
    Node child = htmlElement.childNode(0);

    while (child != null) {
      if (child.childNodeSize()>0)
      {
        HMLNode childNode = convert(child);
        if(!findTag(childNode, "P"))
          childNode = AttachHMLTag.P(3, childNode);
        listNode.addChild(childNode);
      }
      child = child.nextSibling();
    }
    listIndex = 0;
    return listNode;
  }
}

