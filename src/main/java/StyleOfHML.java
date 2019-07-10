import org.jsoup.nodes.Element;

public class StyleOfHML {

  private static final double INITIAL_FONTSIZE = 10;

  static long colorValue(Element anElement) {
    String hexOfColor = "";

    if (anElement.attr("style").contains("background-color")) {
      String style_str = anElement.attr("style");
      int color_start = style_str.indexOf("#");
      int color_end = color_start + 7;
      hexOfColor = style_str.substring(color_start + 1, color_end);
    } else if (anElement.hasAttr("bgcolor")) {

      String style_str = anElement.attr("bgcolor");
      hexOfColor = style_str.substring(1);
    }
    if (!hexOfColor.equals("")) {
      int r = Integer.parseInt(hexOfColor.substring(0, 2), 16);
      int g = Integer.parseInt(hexOfColor.substring(2, 4), 16);
      int b = Integer.parseInt(hexOfColor.substring(4, 6), 16);

      return b * 256 * 256 + g * 256 + r;
    }
    return 0;
  }

  static double fontSize(Element anElement) {
    String style_str = anElement.attr("style");
    double fontSize = INITIAL_FONTSIZE;
    if (anElement.parent() != null && anElement.parent().tagName().equals("font")) {
      fontSize = Integer.parseInt(anElement.parent().attr("size")) * 4; //*4

    } else if (!style_str.equals("")) {
      if (style_str.contains("font-size")) {

        int start_fontsize = style_str.indexOf("font-size:");
        int end_fontsize = 0;

        if (style_str.contains("pt")) {
          end_fontsize = style_str.indexOf("pt");
        } else if (style_str.contains("px")) {
          end_fontsize = style_str.indexOf("px");
        }

        fontSize = Double.parseDouble(style_str.substring(start_fontsize + 10, end_fontsize));
      }
    }
    return fontSize;
  }
}
