import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class ConvertHTMLtoHML implements Converter{

    private HTML html;
    private Borderfill borderfill;
    private CharShape charShape;
    private Image image;

    public ConvertHTMLtoHML(String url, Borderfill borderfill, CharShape charShape, Image image)
    {
        html = new HTML(url);
        this.borderfill = borderfill;
        this.charShape = charShape;
        this.image = image;
    }
    public StringBuffer convert() {

        Element body = html.getbody();
        Node child = body.childNode(0);

        String converted = "";

//        while((child=child.nextSibling())!=null)
//            converted = converted + "<P Parashape=\"16\" Style=\"0\">"+convert(child)+"</P>";

        while((child=child.nextSibling())!=null)
        {
            if(child.toString().trim().equals("\n") || child.toString().trim().equals("")) continue;
            String converted_child = convert(child);
            if(converted_child.indexOf("<P ")!=0)
                converted_child = "<P Parashape=\"16\" Style=\"0\">"+converted_child+"</P>";
            converted = converted + converted_child;
        }

        image.putBindataInList();
        image.putBindataInStorage();

        return new StringBuffer(converted);
    }
    public String convert(Node htmlNode) {

        if(htmlNode==null || htmlNode.toString().trim().equals("") || htmlNode.toString().trim().equals("\n")) return "";

        if(htmlNode instanceof Element)
        {
            Element htmlElement = (Element)htmlNode;
            String tag = htmlElement.tagName();
            System.out.println("tag :: "+tag);

            if(tag.equals("img") || tag.equals("image"))
                return img_convert(htmlElement);
            else if(tag.equals("table"))
                return table_convert(htmlElement);
            else
                return string_convert(htmlElement);
        }
        else
            return htmlNode.toString();
    }
    private String img_convert(Element htmlElement)
    {
        String src = htmlElement.attr("src");
        double width = 40000;
        double height = 40000;
        if(htmlElement.hasAttr("width"))
            width = Double.parseDouble(htmlElement.attr("width"))*67;
        if(htmlElement.hasAttr("height"))
            height = Double.parseDouble(htmlElement.attr("height"))*67;

        image.appendImageInfo(src);
        return appendImageBody(width, height);
    }
    private String appendImageBody(double width, double height)
    {//parashape = 3
        return "<P ParaShape=\"3\"><TEXT CharShape=\"0\"><PICTURE Reverse=\"false\"><SHAPEOBJECT InstId=\"2137949406\" Lock=\"false\" NumberingType=\"Figure\" ZOrder=\"1\"><SIZE Height=\""+height+"\" HeightRelTo=\"Absolute\" Protect=\"false\" Width=\""+width+"\" WidthRelTo=\"Absolute\"/><POSITION AffectLSpacing=\"false\" AllowOverlap=\"false\" FlowWithText=\"true\" HoldAnchorAndSO=\"false\" HorzAlign=\"Left\" HorzOffset=\"0\" HorzRelTo=\"Column\" TreatAsChar=\"true\" VertAlign=\"Top\" VertOffset=\"0\" VertRelTo=\"Para\"/><OUTSIDEMARGIN Bottom=\"0\" Left=\"0\" Right=\"0\" Top=\"0\"/><SHAPECOMMENT></SHAPECOMMENT></SHAPEOBJECT><SHAPECOMPONENT CurHeight=\""+height+"\" CurWidth=\""+width+"\" GroupLevel=\"0\" HorzFlip=\"false\" InstID=\"1064207583\" OriHeight=\""+200000+"\" OriWidth=\""+200000+"\" VertFlip=\"false\" XPos=\"0\" YPos=\"0\"><ROTATIONINFO Angle=\"0\" CenterX=\"9750\" CenterY=\"12990\"/><RENDERINGINFO><TRANSMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/><SCAMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/><ROTMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/></RENDERINGINFO></SHAPECOMPONENT><IMAGERECT X0=\"0\" X1=\""+200000+"\" X2=\""+200000+"\" X3=\"0\" Y0=\"0\" Y1=\"0\" Y2=\""+200000+"\" Y3=\""+200000+"\"/><IMAGECLIP Bottom=\"200000\" Left=\"0\" Right=\"200000\" Top=\"0\"/><INSIDEMARGIN Bottom=\"0\" Left=\"0\" Right=\"0\" Top=\"0\"/><IMAGE Alpha=\"0\" BinItem=\""+image.imageCount()+"\" Bright=\"0\" Contrast=\"0\" Effect=\"RealPic\"/><EFFECTS/></PICTURE><CHAR/></TEXT></P>";
    }
    private int getColCount(Elements tr)
    {
        Element tr_first = tr.first();
        int max_tdCount = 0;

        while(tr_first!=null)
        {
            if(tr_first.children().size()>max_tdCount)
                max_tdCount = tr_first.children().size();

            tr_first = tr_first.nextElementSibling();
        }

        return max_tdCount;
    }
    private String table_convert(Element htmlElement)
    {
        Elements tr = htmlElement.select("tr");
        Elements th = htmlElement.select("th");
        Elements td = htmlElement.select("td");

        int rowCount = tr.size();
        int colCount = th.size() + td.size();

        if(colCount%rowCount == 0)
            colCount /= rowCount;
        else
            colCount =  getColCount(tr);

        String ret = TableHeader(colCount, rowCount);

        int tHeadBorderfillId = 3;
        int tBodyBorderfillId = 3;

        if(!htmlElement.select("thead").attr("style").equals(""))
            tHeadBorderfillId = borderfillId(htmlElement.select("thead").first());
        if(!htmlElement.select("tbody").attr("style").equals(""))
            tBodyBorderfillId = borderfillId(htmlElement.select("tbody").first());

        int rowaddr = 0;
        int coladdr = 0;

        while(rowaddr < rowCount)
        {
            if(coladdr==0) ret = ret + "<ROW>";

            Element tr_pointer = tr.get(rowaddr);
            Element cell = tr_pointer.child(coladdr);   //<td></td> or <th></th>

            String cell_context = "";

            int cellBorderfillId = borderfillId(cell);

            if(cellBorderfillId == 3)
            {
                if(cell.tagName().equals("th"))
                    cellBorderfillId = tHeadBorderfillId;
                else
                    cellBorderfillId = tBodyBorderfillId;
            }
            int colSpan = 1; int rowSpan = 1;

            if(cell.hasAttr("colspan"))
                colSpan = Integer.parseInt(cell.attr("colspan"));
            if(cell.hasAttr("rowspan"))
                rowSpan = Integer.parseInt(cell.attr("rowspan"));

            if(cell.childNodeSize() > 0)
            {
                Node cell_child = cell.childNode(0);

                while(cell_child!=null)
                {
                    cell_context = cell_context + convert(cell_child);

                    cell_child = cell_child.nextSibling();
                }
            }
            if(!cell_context.contains("<TEXT"))
                cell_context = "<TEXT CharShape=\"0\"><CHAR>"+cell_context+"</CHAR></TEXT>";
            if(cell_context.indexOf("<P ")!=0)
                cell_context = "<P ParaShape=\"3\" Style=\"0\">"+cell_context+"</P>";
            ret = ret + "<CELL BorderFill=\""+cellBorderfillId+"\" ColAddr=\""+ coladdr +"\" ColSpan=\""+colSpan+"\" Dirty=\"false\" Editable=\"false\" HasMargin=\"false\" Header=\"false\" Height=\"282\" Protect=\"false\" RowAddr=\""+rowaddr+"\" RowSpan=\""+rowSpan+"\" Width=\""+ (41950/tr_pointer.children().size()) +"\"><CELLMARGIN Bottom=\"141\" Left=\"510\" Right=\"510\" Top=\"141\"/><PARALIST LineWrap=\"Break\" LinkListID=\"0\" LinkListIDNext=\"0\" TextDirection=\"0\" VertAlign=\"Center\">"+cell_context+"</PARALIST></CELL>";

            coladdr++;
            if(coladdr==tr_pointer.children().size())
            {
                coladdr=0; rowaddr++;
                ret = ret + "</ROW>";
            }
        }
        ret = ret + "</TABLE></TEXT>";

        return ret;
    }
    private String TableHeader(int colCount, int rowCount)
    {
        return "<TEXT CharShape=\"0\"><TABLE BorderFill=\"3\" CellSpacing=\"0\" ColCount=\"" + colCount + "\" PageBreak=\"Cell\" RepeatHeader=\"true\" RowCount=\"" + rowCount + "\"><SHAPEOBJECT InstId=\"2136179221\" Lock=\"false\" NumberingType=\"Table\" TextWrap=\"TopAndBottom\" ZOrder=\"0\"><SIZE Height=\"3846\" HeightRelTo=\"Absolute\" Protect=\"false\" Width=\"41950\" WidthRelTo=\"Absolute\"/><POSITION AffectLSpacing=\"false\" AllowOverlap=\"false\" FlowWithText=\"true\" HoldAnchorAndSO=\"false\" HorzAlign=\"Left\" HorzOffset=\"0\" HorzRelTo=\"Column\" TreatAsChar=\"false\" VertAlign=\"Top\" VertOffset=\"0\" VertRelTo=\"Para\"/><OUTSIDEMARGIN Bottom=\"283\" Left=\"283\" Right=\"283\" Top=\"283\"/></SHAPEOBJECT><INSIDEMARGIN Bottom=\"141\" Left=\"510\" Right=\"510\" Top=\"141\"/>";
    }
    private int borderfillId(Element anElement)
    {
        if(anElement.attr("bgcolor").equals(""))
            return 3;
        else
        {
            String style_str = anElement.attr("bgcolor");
            String hexOfColor  = "FFFFFF";

            int color_start = style_str.indexOf("#");
            hexOfColor = style_str.substring(color_start+1);

            hexOfColor = new StringBuffer(hexOfColor).reverse().toString();

            long valueOfColor = Integer.parseInt( hexOfColor, 16 );

            return borderfill.id(valueOfColor);
        }
    }
    private String string_convert(Element htmlElement)
    {
        String tag = htmlElement.tagName();
        if(tag.equals("br"))
            return "<P Parashape=\"16\" Style=\"0\"><TEXT CharShape=\"0\"><CHAR></CHAR></TEXT></P>";

        int charShapeId = charShapeId(htmlElement);

        String str = "";
        if(htmlElement.childNodeSize() > 0)
        {
            Node child = htmlElement.childNode(0);

            while(child!=null)
            {
                String converted = convert(child);
                if(!converted.equals(""))
                {
                    if(converted.indexOf("<TEXT CharShape")==0 && converted.lastIndexOf("</TEXT>")==converted.length()-7 || converted.indexOf("<P ")==0)
                        str = str + converted;
                    else
                        str = str + "<P Parashape=\"3\" Style=\"0\"><TEXT CharShape=\""+charShapeId+"\"><CHAR>"+converted+"</CHAR></TEXT></P>";
                }
                child = child.nextSibling();
            }
        }
        return str;
    }
    private int charShapeId(Element anElement)
    {
        String tag = anElement.tagName();
        boolean isBold = false;

        if(tag.equals("h1"))
            return charShape.id(24, true);
        else if(tag.equals("h2"))
            return charShape.id(18, true);
        else if(tag.equals("h3"))
            return charShape.id(13.55, true);
        else if(tag.equals("h4"))
            return charShape.id(12, true);
        else if(tag.equals("h5"))
            return charShape.id(10, true);
        else if(tag.equals("h6"))
            return charShape.id(7.55, true);
        else if(tag.equals("b"))
            isBold = true;

        String style_str = anElement.attr("style");
        double fontSize = 0.0;

        if(anElement.parent().tagName().equals("font"))
        {
            fontSize = Integer.parseInt(anElement.parent().attr("size"));
            return charShape.id(fontSize*4, isBold);
        }
        else if(!style_str.equals(""))
        {
            if(style_str.contains("font-weight")) isBold = true;

            if(style_str.contains("font-size"))
            {
                System.out.println(style_str);
                int start_fontsize = style_str.indexOf("font-size:");

                int end_fontsize = 0;

                if(style_str.contains("pt")) end_fontsize = style_str.indexOf("pt");
                else if(style_str.contains("px")) end_fontsize = style_str.indexOf("px");

                fontSize = Double.parseDouble(style_str.substring(start_fontsize+10, end_fontsize));

                return charShape.id(fontSize, isBold);
            }
            else
                return charShape.id(isBold);
        }

        return charShape.id(isBold);
    }

}

