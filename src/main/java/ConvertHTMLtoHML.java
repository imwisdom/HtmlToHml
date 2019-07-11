import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
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

  }

  private void convertRecursively(Node htmlNode) {
    if (htmlNode == null) {
      return;
    }
    HMLNode aNode = convert(htmlNode);
    if (aNode != EMPTY_NODE) {
      if (htmlNode instanceof TextNode) {
        aNode = AttachHMLTag.Text(0, aNode);
      }
      if (!findTag(aNode, "P")) {
        aNode = AttachHMLTag.P(16, aNode);
      }
      hmlNode.setNewSibling(aNode);
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

    if (hmlNode.getChild() != null) {
      getHmlBufferRecursively(hmlNode.getChild());
    }

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

      switch (tag) {
        case "img":
        case "image":
          return convertImage(htmlElement);
        case "table":
          return convertTable(htmlElement);
        case "br":
          if (htmlElement.parent().children().size() != 1) {
            return AttachHMLTag.P(16, AttachHMLTag.Text(0, EMPTY_NODE));
          } else {
            return AttachHMLTag.Text(0, EMPTY_NODE);
          }
        case "ul":
        case "ol":
          return list_convert(htmlElement);
        default:
          return string_convert(htmlElement);
      }
    } else {
      HMLNode stringNode = new HMLNode(); //just stringNode. no need head, foot, child, sibling...
      stringNode.setHeadFirst(htmlNode.toString());
      return stringNode;
    }
  }

  private HMLNode convertImage(Element htmlElement) {
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

  private HMLNode convertTable(Element htmlElement) {
    //row count and column count
    Elements tr = htmlElement.select("tr");
    int rowCount = tr.size();
    int colCount = getColCount(tr);
    String tableHeader = AttachHMLTag.TableHeader(colCount, rowCount);
    HMLNode converted = convertCell(tr, rowCount, tableHeader);
    converted.setFoot("</TABLE></TEXT>");
    return AttachHMLTag.P(16, converted);
  }

  private HMLNode convertCell(Elements tr, int rowCount, String tableHeader) {
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
    return converted;
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
        if (aNode != EMPTY_NODE) {
          if (!findTag(aNode, "TEXT")) {
            aNode = AttachHMLTag.Text(0, aNode);
          }
          if (!findTag(aNode, "P")) {
            aNode = AttachHMLTag.P(3, aNode);
          }
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
    charShape.setCharShape(htmlElement);
    int charShapeId = charShape.id();
    HMLNode returnNode = new HMLNode();

    if (tag.equals("li")) {
      String symbolOfList = "<TAB/>";
      if (htmlElement.parent().tagName().equals("ol")) {
        symbolOfList = symbolOfList + listIndex + " ";
        listIndex++;
      } else {
        symbolOfList = symbolOfList + "- ";
      }

      HMLNode tabNode = new HMLNode(symbolOfList, "", null);
      tabNode = AttachHMLTag.Text(0, tabNode);
      returnNode.addChild(tabNode);
    }

    if (htmlElement.childNodeSize() > 0) {
      Node childOfHtml = htmlElement.childNode(0);
      while (childOfHtml != null) {
        HMLNode insertNode = convert(childOfHtml);

        if (insertNode != EMPTY_NODE) {
          if (!insertNode.getHead().equals("") && insertNode.getFoot().equals("")) {
            insertNode = AttachHMLTag.Text(charShapeId, insertNode);
          }

          if (childOfHtml.nextSibling() != null) {
            testNextTagForPTag(childOfHtml, insertNode, returnNode);
          } else {
            returnNode.addChild(insertNode);
          }
        }
        childOfHtml = childOfHtml.nextSibling();
      }
    }
    if ((tag.equals("p") || tag.equals("div") || tag.indexOf("h") == 0) && !findTag(returnNode,
        "P")) {
      return AttachHMLTag.P(3, returnNode);
    }
    attachPTagtoAllSiblings(returnNode.getChild());

    return returnNode;
  }

  private void testNextTagForPTag(Node htmlNode, HMLNode insertNode, HMLNode parentNode) {
    Node nextHtmlNode = htmlNode.nextSibling();
    if (nextHtmlNode instanceof Element) {
      if (nextHtmlNode.nextSibling() != null && nextHtmlNode.toString().equals("\n")) {
        nextHtmlNode = nextHtmlNode.nextSibling();
      }
      String nextHtmlTag = ((Element) nextHtmlNode).tagName();
      if (!insertNode.getFoot().contains("</P>") && (nextHtmlTag.equals("p")
          || nextHtmlTag.equals("div") || nextHtmlTag.equals("ol") || nextHtmlTag.equals("ul"))) {
        insertNode = AttachHMLTag.P(3, insertNode);
      } else if (!insertNode.getFoot().contains("</P>") && nextHtmlTag.equals("br")) {

        HMLNode newNode = parentNode.getChild();
        if (newNode != null) {
          newNode.setNewSibling(insertNode);
          parentNode = new HMLNode();
          attachPTagtoAllSiblings(newNode);
          insertNode = newNode;
        } else {
          insertNode = AttachHMLTag.P(3, insertNode);
        }

      }
    }
    parentNode.addChild(insertNode);
  }

  private void attachPTagtoAllSiblings(HMLNode hmlNode) {
    if (!findTag(hmlNode, "P")) {
      return;
    }
    HMLNode aNode = hmlNode;

    for (; findTag(aNode.getSibling(), "P"); aNode = aNode.getSibling()) {
      ;
    }

    if (aNode.getSibling() == null) {
      return;
    }

    HMLNode noPTag = aNode.getSibling();
    aNode.setNextSibling(null);

    HMLNode noPTagFoot = noPTag;
    while (noPTagFoot != null && !findTag((noPTagFoot = noPTagFoot.getSibling()), "P")) {
      ;
    }

    if (noPTagFoot == null) {
      noPTag = AttachHMLTag.P(3, noPTag);
      aNode.setNextSibling(noPTag);
    } else {
      HMLNode yPTag = noPTagFoot.getSibling();
      noPTagFoot.setNextSibling(null);

      noPTag = AttachHMLTag.P(3, noPTag);

      noPTag.setNextSibling(yPTag);
      aNode.setNextSibling(noPTag);

      attachPTagtoAllSiblings(noPTag);
    }
  }

  private boolean findTag(HMLNode hmlNode, String tag) {
    if (hmlNode == null) {
      return false;
    }

    if (hmlNode.getFoot().contains("</" + tag + ">")) {
      return true;
    }

    if (hmlNode.getFoot().equals("") && hmlNode.getHead().equals("")) {
      return findTag(hmlNode.getSibling(), tag) || findTag(hmlNode.getChild(), tag);
    }
    return findTag(hmlNode.getChild(), tag);
  }

  private HMLNode list_convert(Element htmlElement) {
    HMLNode listNode = new HMLNode();
    Node child = htmlElement.childNode(0);  //<li>

    while (child != null) {
      if (child.childNodeSize() > 0) {
        HMLNode childNode = convert(child);
        if (!findTag(childNode, "P")) {
          childNode = AttachHMLTag.P(3, childNode);
        }
        listNode.addChild(childNode);
      }
      child = child.nextSibling();
    }
    listIndex = 1;
    return listNode;
  }
}

