import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class ConvertHTMLtoHML implements Converter, AttachHMLTag {

  private HTML html;
  private Borderfill borderfill;
  private CharShape charShape;
  private Image image;

  public ConvertHTMLtoHML(String url, Borderfill borderfill, CharShape charShape, Image image) {
    html = new HTML(url);
    this.borderfill = borderfill;
    this.charShape = charShape;
    this.image = image;
  }

  public StringBuffer convert() {

    Element body = html.getbody();
    Node child = body.childNode(0);

    String converted = "";

    while ((child = child.nextSibling()) != null) {
      if (child.toString().trim().equals("\n") || child.toString().trim().equals("")) {
        continue;
      }
      String converted_child = convert(child);
      if (converted_child.indexOf("<P ") != 0) {
        converted_child = AttachHMLTag.P(16, converted_child);
      }
      converted = converted + converted_child;
    }

    image.putBindataInList();
    image.putBindataInStorage();

    return new StringBuffer(converted);
  }

  public String convert(Node htmlNode) {

    if (htmlNode == null || htmlNode.toString().trim().equals("") || htmlNode.toString().trim()
        .equals("\n")) {
      return "";
    }

    if (htmlNode instanceof Element) {
      Element htmlElement = (Element) htmlNode;
      String tag = htmlElement.tagName();
      System.out.println("tag :: " + tag);

      if (tag.equals("img") || tag.equals("image")) {
        return img_convert(htmlElement);
      } else if (tag.equals("table")) {
        return table_convert(htmlElement);
      } else {
        return string_convert(htmlElement);
      }
    } else {
      return htmlNode.toString();
    }
  }

  private String img_convert(Element htmlElement) {
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
    return appendImageBody(width, height);
  }
  private String appendImageBody(double width, double height) {
    return AttachHMLTag.ImageBody(image.count(), width, height);
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

  private String table_convert(Element htmlElement) {
    Elements tr = htmlElement.select("tr");
    Elements th = htmlElement.select("th");
    Elements td = htmlElement.select("td");

    int rowCount = tr.size();
    int colCount = th.size() + td.size();

    if (colCount % rowCount == 0) {
      colCount /= rowCount;
    } else {
      colCount = getColCount(tr);
    }

    String ret = TableHeader(colCount, rowCount);

    int tHeadBorderfillId = 3;
    int tBodyBorderfillId = 3;

    if (!htmlElement.select("thead").attr("style").equals("")) {
      tHeadBorderfillId = borderfillId(htmlElement.select("thead").first());
    }
    if (!htmlElement.select("tbody").attr("style").equals("")) {
      tBodyBorderfillId = borderfillId(htmlElement.select("tbody").first());
    }

    int rowaddr = 0;
    int coladdr = 0;

    while (rowaddr < rowCount) {
      if (coladdr == 0) {
        ret = ret + "<ROW>";
      }

      Element tr_pointer = tr.get(rowaddr);
      Element cell = tr_pointer.child(coladdr);   //<td></td> or <th></th>

      String cell_context = "";

      int cellBorderfillId = borderfillId(cell);

      if (cellBorderfillId == 3) {
        if (cell.tagName().equals("th")) {
          cellBorderfillId = tHeadBorderfillId;
        } else {
          cellBorderfillId = tBodyBorderfillId;
        }
      }
      int colSpan = 1;
      int rowSpan = 1;

      if (cell.hasAttr("colspan")) {
        colSpan = Integer.parseInt(cell.attr("colspan"));
      }
      if (cell.hasAttr("rowspan")) {
        rowSpan = Integer.parseInt(cell.attr("rowspan"));
      }

      if (cell.childNodeSize() > 0) {
        Node cell_child = cell.childNode(0);

        while (cell_child != null) {
          cell_context = cell_context + convert(cell_child);

          cell_child = cell_child.nextSibling();
        }
      }
      if (!cell_context.contains("<TEXT")) {
        cell_context = AttachHMLTag.Text(0, cell_context);
      }
      if (cell_context.indexOf("<P ") != 0) {//parashape = 3
        cell_context = AttachHMLTag.P(3, cell_context);
      }

      String colInfo = AttachHMLTag.ColumnOfCell(coladdr, colSpan);
      String rowInfo = AttachHMLTag.RowOfCell(rowaddr, rowSpan, (41950 / tr_pointer.children().size()));

      ret = ret + AttachHMLTag.Cell(cellBorderfillId, cell_context, colInfo, rowInfo);

      coladdr++;

      if (coladdr == tr_pointer.children().size()) {
        coladdr = 0;
        rowaddr++;
        ret = ret + "</ROW>";
      }
    }
    ret = ret + "</TABLE></TEXT>";

    return ret;
  }

  private String TableHeader(int colCount, int rowCount) {

    return AttachHMLTag.TableHeader(colCount, rowCount);
  }

  private int borderfillId(Element anElement) {
    if (anElement.attr("bgcolor").equals("")) {
      return 3;
    } else {
      String style_str = anElement.attr("bgcolor");
      String hexOfColor = "FFFFFF";

      int color_start = style_str.indexOf("#");
      hexOfColor = style_str.substring(color_start + 1);

      int r = Integer.parseInt(hexOfColor.substring(0, 2), 16);
      int g = Integer.parseInt(hexOfColor.substring(2, 4), 16);
      int b = Integer.parseInt(hexOfColor.substring(4, 6), 16);

      long valueOfColor = b * 256 * 256 + g * 256 + r;

      borderfill.setBorderfill(valueOfColor);
      return borderfill.id();
    }
  }

  private String string_convert(Element htmlElement) {
    String tag = htmlElement.tagName();
    if (tag.equals("br")) {
      return AttachHMLTag.P(16, AttachHMLTag.Text(0, ""));
    }

    int charShapeId = charShapeId(htmlElement);

    String str = "";
    if (htmlElement.childNodeSize() > 0) {
      Node child = htmlElement.childNode(0);

      while (child != null) {
        String converted = convert(child);
        if (!converted.equals("")) {
          if (converted.indexOf("<TEXT CharShape") == 0
              && converted.lastIndexOf("</TEXT>") == converted.length() - 7
              || converted.indexOf("<P ") == 0) {
            str = str + converted;
          } else {  //parashape = 3

            str = str + AttachHMLTag.P(3, AttachHMLTag.Text(charShapeId, converted));
          }
        }
        child = child.nextSibling();
      }
    }
    return str;
  }

  private int charShapeId(Element anElement) {
    String tag = anElement.tagName();
    boolean isBold = false;

    if (tag.equals("h1")) {
      charShape.setCharShape(24, true);
    } else if (tag.equals("h2")) {
      charShape.setCharShape(18, true);
    } else if (tag.equals("h3")) {
      charShape.setCharShape(13.55, true);
    } else if (tag.equals("h4")) {
      charShape.setCharShape(12, true);
    } else if (tag.equals("h5")) {
      charShape.setCharShape(10, true);
    } else if (tag.equals("h6")) {
      charShape.setCharShape(7.55, true);
    } else {
      if (tag.equals("b")) {
        isBold = true;
      }
      String style_str = anElement.attr("style");
      double fontSize = 0.0;

      if (anElement.parent().tagName().equals("font")) {
        fontSize = Integer.parseInt(anElement.parent().attr("size"));
        charShape.setCharShape(fontSize * 4, isBold);
      } else if (!style_str.equals("")) {
        if (style_str.contains("font-weight")) {
          isBold = true;
        }
        if (style_str.contains("font-size")) {

          int start_fontsize = style_str.indexOf("font-size:");

          int end_fontsize = 0;

          if (style_str.contains("pt")) {
            end_fontsize = style_str.indexOf("pt");
          } else if (style_str.contains("px")) {
            end_fontsize = style_str.indexOf("px");
          }

          fontSize = Double.parseDouble(style_str.substring(start_fontsize + 10, end_fontsize));

          charShape.setCharShape(fontSize, isBold);
        } else {
          charShape.setCharShape(isBold);
        }
      }
      else{
        charShape.setCharShape(isBold);
      }
    }
    return charShape.id();
  }
}

