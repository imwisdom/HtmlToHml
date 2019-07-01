import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

public class ConvertedHTML {

    //file url
        private String htmlFile;

    //all
        private StringBuffer convertToHwpml;

    //for image
        private int img_count;
        private StringBuffer binary_img;
        private StringBuffer img_header;

    //for table
        private int table_count;

    //for style
        private StringBuffer borderfillBuf;
        private int borderfillId;
        private StringBuffer charShapeBuf;
        private int charShapeId;

    public ConvertedHTML(String htmlFile)
    {   //file url
        //todo init
        this.htmlFile = htmlFile;

        //all
        this.convertToHwpml = new StringBuffer();

        //for image
        this.img_count = 0;
        this.binary_img = new StringBuffer();
        this.img_header = new StringBuffer();

        //for table
        this.table_count = 0;

        //for style
        this.borderfillBuf = new StringBuffer();
        this.borderfillId = 3;
        this.charShapeBuf = new StringBuffer();
        this.charShapeId = 6;

        convert();
    }
    private void convert()
    {
        Element htmlBody = getHtmlBody();

        for(int i=0;i<htmlBody.children().size();i++)
        {
            Element lineOfBody = htmlBody.child(i);
            String tag = tag(lineOfBody);

            if(tag.equals("img") || tag.equals("image"))
                setImageData(lineOfBody);

            else if(tag.equals("table"))
                setTableData(lineOfBody);
            else
                setStringData(lineOfBody);
        }
        if(img_count>=1) endOfConvert();
    }
    private String loadHtmlFile()
    {
        File file = new File(htmlFile);
        char[] ch = new char[(int)file.length()];
        StringBuffer htmlInBuffer = new StringBuffer();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            br.read(ch);
            htmlInBuffer.append(ch);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlInBuffer.toString();
    }
    private Element getHtmlBody()
    {
        String htmlString = loadHtmlFile();
        Document html = Jsoup.parse(htmlString);
        Element htmlbody = html.child(0).child(1);

        return htmlbody;
    }
    private String tag(Element a)
    {
        String tag = a.cssSelector();
        int start_tag = tag.lastIndexOf("> ");

        return tag.substring(start_tag+2);
    }
    private void setImageData(Element lineOfBody)
    {
        String filename = lineOfBody.attr("src");
        String extension = "";
        if(filename.contains("data:image"))
        {
            extension = filename.substring(filename.indexOf("/")+1, filename.indexOf(";"));

            appendImageInHead(extension);
            appendImageInBody();
            setBase64EncodingData(filename,true);
        }
        else
        {
            extension = filename.substring(filename.lastIndexOf(".")+1);

            appendImageInHead(extension);
            appendImageInBody();
            setBase64EncodingData(filename,false);
        }

    }
    private void appendImageInHead(String extension)
    {
        img_header.append("<BINITEM BinData=\""+(img_count+1)+"\" Format=\""+extension+"\""+" Type=\"Embedding\"/>");
        img_count++;
    }
    private void appendImageInBody()
    {
        convertToHwpml.append("<P ParaShape=\"3\" Style=\"0\"><TEXT CharShape=\"0\"><PICTURE Reverse=\"false\"><SHAPEOBJECT InstId=\"2137949406\" Lock=\"false\" NumberingType=\"Figure\" ZOrder=\"1\"><SIZE Height=\"25980\" HeightRelTo=\"Absolute\" Protect=\"false\" Width=\"30541\" WidthRelTo=\"Absolute\"/><POSITION AffectLSpacing=\"false\" AllowOverlap=\"false\" FlowWithText=\"true\" HoldAnchorAndSO=\"false\" HorzAlign=\"Left\" HorzOffset=\"0\" HorzRelTo=\"Column\" TreatAsChar=\"true\" VertAlign=\"Top\" VertOffset=\"0\" VertRelTo=\"Para\"/><OUTSIDEMARGIN Bottom=\"0\" Left=\"0\" Right=\"0\" Top=\"0\"/><SHAPECOMMENT></SHAPECOMMENT></SHAPEOBJECT><SHAPECOMPONENT GroupLevel=\"0\" HorzFlip=\"false\" InstID=\"1064207583\" OriHeight=\"25980\" OriWidth=\"19500\" VertFlip=\"false\" XPos=\"0\" YPos=\"0\"><ROTATIONINFO Angle=\"0\" CenterX=\"9750\" CenterY=\"12990\"/><RENDERINGINFO><TRANSMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/><SCAMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/><ROTMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/></RENDERINGINFO></SHAPECOMPONENT><IMAGERECT X0=\"0\" X1=\"19500\" X2=\"19500\" X3=\"0\" Y0=\"0\" Y1=\"0\" Y2=\"25980\" Y3=\"25980\"/><IMAGECLIP Bottom=\"19440\" Left=\"0\" Right=\"14640\" Top=\"0\"/><INSIDEMARGIN Bottom=\"0\" Left=\"0\" Right=\"0\" Top=\"0\"/><IMAGE Alpha=\"0\" BinItem=\"1\" Bright=\"0\" Contrast=\"0\" Effect=\"RealPic\"/><EFFECTS/></PICTURE><CHAR/></TEXT></P>");
    }
    private void setTableData(Element lineOfBody)
    {
        appendTableInBody(lineOfBody);
    }
    private void appendTableInBody(Element lineOfBody)
    {
        String filename = lineOfBody.attr("style");
        Elements tr = lineOfBody.select("tr");
        Elements th = lineOfBody.select("th");
        Elements td = lineOfBody.select("td");

        int rowCount = tr.size();
        int colCount = th.size() + td.size();
        colCount /= rowCount;

        appendTableHeader(colCount, rowCount);
        appendBorderfill(0);

        int tHeadBorderfillId = 3;
        int tBodyBorderfillId = 3;

        if(!lineOfBody.select("thead").attr("style").equals(""))
            tHeadBorderfillId = styleOfTable(lineOfBody.select("thead").first());
        if(!lineOfBody.select("tbody").attr("style").equals(""))
            tBodyBorderfillId = styleOfTable(lineOfBody.select("tbody").first());

        int rowaddr = 0;
        int coladdr = 0;

        while(rowaddr < rowCount)
        {
            if(coladdr==0) convertToHwpml.append("<ROW>");

            Element tr_pointer = tr.get(rowaddr);
            Element cell = tr_pointer.child(coladdr);

            String cell_str = cell.text();
            int cellBorderfillId = styleOfTable(cell);

            if(cellBorderfillId == 3)
            {
                if(cell.tagName().equals("th"))
                    cellBorderfillId = tHeadBorderfillId;
                else
                    cellBorderfillId = tBodyBorderfillId;
            }

            convertToHwpml.append("<CELL BorderFill=\""+cellBorderfillId+"\" ColAddr=\""+ coladdr +"\" ColSpan=\"1\" Dirty=\"false\" Editable=\"false\" HasMargin=\"false\" Header=\"false\" Height=\"282\" Protect=\"false\" RowAddr=\""+rowaddr+"\" RowSpan=\"1\" Width=\""+ 41950 / rowCount +"\"><CELLMARGIN Bottom=\"141\" Left=\"510\" Right=\"510\" Top=\"141\"/><PARALIST LineWrap=\"Break\" LinkListID=\"0\" LinkListIDNext=\"0\" TextDirection=\"0\" VertAlign=\"Center\"><P ParaShape=\"3\" Style=\"0\"><TEXT CharShape=\"0\"><CHAR>" + cell_str + "</CHAR></TEXT></P></PARALIST></CELL>");

            coladdr++;
            if(coladdr==colCount)
            {
                coladdr=0;
                rowaddr++;
                convertToHwpml.append("</ROW>");
            }
        }
        convertToHwpml.append("</TABLE></TEXT></P>");
    }
    private int styleOfTable(Element anElement)
    {
        if(anElement.attr("style").equals(""))
            return 3;
        else
        {
            String style_str = anElement.attr("style");
            String hexOfColor  = "FFFFFF";

            if(style_str.contains("background-color"))
            {
                int color_start = style_str.indexOf("#");
                hexOfColor = style_str.substring(color_start+1, color_start+7);

                hexOfColor = new StringBuffer(hexOfColor).reverse().toString();

                long valueOfColor = Integer.parseInt( hexOfColor, 16 );
                return appendBorderfill(valueOfColor);

            }
            return 3;
        }
    }

    private void appendTableHeader(int colCount, int rowCount)
    {
        convertToHwpml.append("<P ParaShape=\"3\" Style=\"0\"><TEXT CharShape=\"0\"><TABLE BorderFill=\"3\" CellSpacing=\"0\" ColCount=\"" + colCount + "\" PageBreak=\"Cell\" RepeatHeader=\"true\" RowCount=\"" + rowCount + "\"><SHAPEOBJECT InstId=\"2136179221\" Lock=\"false\" NumberingType=\"Table\" TextWrap=\"TopAndBottom\" ZOrder=\"0\"><SIZE Height=\"3846\" HeightRelTo=\"Absolute\" Protect=\"false\" Width=\"41950\" WidthRelTo=\"Absolute\"/><POSITION AffectLSpacing=\"false\" AllowOverlap=\"false\" FlowWithText=\"true\" HoldAnchorAndSO=\"false\" HorzAlign=\"Left\" HorzOffset=\"0\" HorzRelTo=\"Column\" TreatAsChar=\"false\" VertAlign=\"Top\" VertOffset=\"0\" VertRelTo=\"Para\"/><OUTSIDEMARGIN Bottom=\"283\" Left=\"283\" Right=\"283\" Top=\"283\"/></SHAPEOBJECT><INSIDEMARGIN Bottom=\"141\" Left=\"510\" Right=\"510\" Top=\"141\"/>");
    }
    private int appendBorderfill(long valueOfColor)
    {
        table_count ++;
        int id = 3;
        if(valueOfColor==0L)
            borderfillBuf.append("<BORDERFILL BackSlash=\"0\" BreakCellSeparateLine=\"0\" CenterLine=\"0\" CounterBackSlash=\"0\" CounterSlash=\"0\" CrookedSlash=\"0\" Id=\"3\" Shadow=\"false\" Slash=\"0\" ThreeD=\"false\"><LEFTBORDER Type=\"Solid\" Width=\"0.12mm\"/><RIGHTBORDER Type=\"Solid\" Width=\"0.12mm\"/><TOPBORDER Type=\"Solid\" Width=\"0.12mm\"/><BOTTOMBORDER Type=\"Solid\" Width=\"0.12mm\"/><DIAGONAL Type=\"Solid\" Width=\"0.1mm\"/><FILLBRUSH><WINDOWBRUSH Alpha=\"0\" FaceColor=\"4294967295\" HatchColor=\"4278190080\"/></FILLBRUSH></BORDERFILL>");
        else
        {
            ++borderfillId;
            id = borderfillId;
            borderfillBuf.append("<BORDERFILL BackSlash=\"0\" BreakCellSeparateLine=\"0\" CenterLine=\"0\" CounterBackSlash=\"0\" CounterSlash=\"0\" CrookedSlash=\"0\" Id=\""+borderfillId+"\" Shadow=\"false\" Slash=\"0\" ThreeD=\"false\"><LEFTBORDER Type=\"Solid\" Width=\"0.12mm\"/><RIGHTBORDER Type=\"Solid\" Width=\"0.12mm\"/><TOPBORDER Type=\"Solid\" Width=\"0.12mm\"/><BOTTOMBORDER Type=\"Solid\" Width=\"0.12mm\"/><DIAGONAL Type=\"Solid\" Width=\"0.1mm\"/><FILLBRUSH><WINDOWBRUSH Alpha=\"0\" FaceColor=\""+valueOfColor+"\" HatchColor=\"4278190080\"/></FILLBRUSH></BORDERFILL>");
        }

        return id;
    }
    private void setStringData(Element lineOfBody)
    {
        appendStringData(lineOfBody);
    }
    private void appendStringData(Element anElement)
    {
        String str = anElement.text();
        int strCharShapeId = styleOfString(anElement);

        convertToHwpml.append("<P Parashape=\"16\" Style=\"0\"><TEXT CharShape=\""+strCharShapeId+"\"><CHAR>"+str+"</CHAR></TEXT></P>");
    }
    private int styleOfString(Element anElement)
    {
        String tag = anElement.tagName();

        if(tag.equals("h1"))
            return appendCharShape(24, true);
        else if(tag.equals("h2"))
            return appendCharShape(18, true);
        else if(tag.equals("h3"))
            return appendCharShape(13.55, true);
        else if(tag.equals("h4"))
            return appendCharShape(12, true);
        else if(tag.equals("h5"))
            return appendCharShape(10, true);
        else if(tag.equals("h6"))
            return appendCharShape(7.55, true);
        else
        {
            if(!anElement.attr("style").equals(""))
            {
                String style_str = anElement.attr("style");
                double fontSize = 0.0;
                boolean isBold = false;

                if(style_str.contains("font-size"))
                {
                    int start_fontsize = style_str.indexOf("font-size:");
                    int end_fontsize = style_str.indexOf("px");

                    fontSize = Double.parseDouble(style_str.substring(start_fontsize+10, end_fontsize));
                }
                if(style_str.contains("font-weight"))
                    isBold = true;

                return appendCharShape(fontSize, isBold);
            }
        }
        return 0;
    }
    private int appendCharShape(double fontsize, boolean isBold)
    {
        if(fontsize == 0.0 && !isBold)
            return 0;

        ++charShapeId;
        if(fontsize == 0.0)
            charShapeBuf.append("<CHARSHAPE BorderFillId=\"2\" Height=\"1000\" Id=\""+charShapeId+"\" ShadeColor=\"4294967295\" SymMark=\"0\" TextColor=\"0\" UseFontSpace=\"false\" UseKerning=\"false\"><FONTID Hangul=\"1\" Hanja=\"1\" Japanese=\"1\" Latin=\"1\" Other=\"1\" Symbol=\"1\" User=\"1\"/><RATIO Hangul=\"100\" Hanja=\"100\" Japanese=\"100\" Latin=\"100\" Other=\"100\" Symbol=\"100\" User=\"100\"/><CHARSPACING Hangul=\"0\" Hanja=\"0\" Japanese=\"0\" Latin=\"0\" Other=\"0\" Symbol=\"0\" User=\"0\"/><RELSIZE Hangul=\"100\" Hanja=\"100\" Japanese=\"100\" Latin=\"100\" Other=\"100\" Symbol=\"100\" User=\"100\"/><CHAROFFSET Hangul=\"0\" Hanja=\"0\" Japanese=\"0\" Latin=\"0\" Other=\"0\" Symbol=\"0\" User=\"0\"/></CHARSHAPE>");
        else
            charShapeBuf.append("<CHARSHAPE BorderFillId=\"2\" Height=\""+(fontsize*100)+"\" Id=\""+charShapeId+"\" ShadeColor=\"4294967295\" SymMark=\"0\" TextColor=\"0\" UseFontSpace=\"false\" UseKerning=\"false\"><FONTID Hangul=\"1\" Hanja=\"1\" Japanese=\"1\" Latin=\"1\" Other=\"1\" Symbol=\"1\" User=\"1\"/><RATIO Hangul=\"100\" Hanja=\"100\" Japanese=\"100\" Latin=\"100\" Other=\"100\" Symbol=\"100\" User=\"100\"/><CHARSPACING Hangul=\"0\" Hanja=\"0\" Japanese=\"0\" Latin=\"0\" Other=\"0\" Symbol=\"0\" User=\"0\"/><RELSIZE Hangul=\"100\" Hanja=\"100\" Japanese=\"100\" Latin=\"100\" Other=\"100\" Symbol=\"100\" User=\"100\"/><CHAROFFSET Hangul=\"0\" Hanja=\"0\" Japanese=\"0\" Latin=\"0\" Other=\"0\" Symbol=\"0\" User=\"0\"/></CHARSHAPE>");

        if(isBold)
            charShapeBuf.insert(charShapeBuf.indexOf("</CHARSHAPE>"), "<BOLD/>");

        return charShapeId;
    }
    private void endOfConvert()
    {
        img_header.insert(0, "<BINDATALIST Count=\"" + img_count + "\">");
        img_header.append("</BINDATALIST>");

        binary_img.insert(0, "<BINDATASTORAGE>");
        binary_img.append("</BINDATASTORAGE>");
    }
    private void setBase64EncodingData(String src, boolean isEncoded)
    {
        String encoded_str = "";
        if(isEncoded)
        {
            encoded_str = src.substring(src.indexOf(",")+1);
        }
        else
        {
            ByteArrayOutputStream byteOutputStream = getBinaryImageData(src);
            byte[] fileArray = byteOutputStream.toByteArray();
            encoded_str = new String(Base64.encodeBase64(fileArray));
        }
        binary_img.append("<BINDATA Encoding=\"Base64\" Id=\""+(img_count)+"\" Size=\"10113\">"+encoded_str+"</BINDATA>");
    }
    private ByteArrayOutputStream getBinaryImageData(String url)
    {
        FileInputStream inputStream = null;
        ByteArrayOutputStream byteOutputStream = null;

        File file = new File(url);

        if(file.exists()) {
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byteOutputStream = new ByteArrayOutputStream();

            int len = 0;
            byte[] buf = new byte[1024];

            while (true) {
                try {
                    if ((len = inputStream.read(buf)) == -1) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byteOutputStream.write(buf, 0, len);
            }

            return byteOutputStream;
        }
        return null;
    }
    public StringBuffer getConvertedHTML(){ return convertToHwpml; }
    public StringBuffer getBorderfillBuf() { return borderfillBuf; }
    public StringBuffer getCharShapeBuf(){ return charShapeBuf; }
    public int table() { return table_count; }
    public int image() { return img_count; }
    public StringBuffer getBinaryImage() { return binary_img; }
    public StringBuffer getImgHeader() { return img_header; }

}
