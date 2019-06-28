import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class ConvertedHTML {

    //file url
        private String htmlFile;

    //all
        private StringBuffer htmlcontext;

    //for image
        private int img_c;
        private StringBuffer binary_img;
        private StringBuffer img_header;

    //for table
        private int table_c;

    //for style
        private StringBuffer borderfillBuf;
        private StringBuffer fontBuf;

    public ConvertedHTML(String htmlFile)
    {   //file url
        this.htmlFile = htmlFile;

        //all
        this.htmlcontext = new StringBuffer();

        //for image
        this.img_c = 0;
        this.binary_img = new StringBuffer();
        this.img_header = new StringBuffer();

        //for table
        this.table_c = 0;

        //for style
        this.borderfillBuf = new StringBuffer();
        this.fontBuf = new StringBuffer();


        convert();
    }
    private void convert()
    {
        File file = new File(htmlFile);
        char[] ch = new char[(int)file.length()];
        StringBuffer buffer = new StringBuffer();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {

            br.read(ch);
            buffer.append(ch);

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int start_parsing = buffer.indexOf("<body>");
        buffer.replace(0, start_parsing+7, "");
        int end_parsing = buffer.indexOf("</body>");
        buffer.replace(end_parsing, buffer.length()-1, "");

        while(buffer.length()!=0)
        {
            int start = buffer.indexOf("<");
            int end = buffer.indexOf(">");

            if(start!=-1 && end!=-1)
            {
                int endOfsubContext;
                String subContext;
                String tag = buffer.substring(start+1, end);

                if(tag.contains("src"))
                {
                    subContext = tag;
                    subContext = subContext.trim();

                    tag = (tag.substring(0, 3).equals("img")? tag.substring(0, 3) : tag.substring(0, 5));

                    // put an img

                    String filename = subContext.replaceAll("src|=|img|image","");

                    int startOfName = filename.indexOf("./");
                    int endOfName = filename.indexOf("\" ");

                    filename = filename.substring(startOfName, endOfName);
                    String extension = filename.substring(filename.lastIndexOf(".")+1);

                    setBinaryImage(filename);

                    img_header.append("<BINITEM BinData=\""+(img_c+1)+"\" Format=\""+extension+"\""+" Type=\"Embedding\"/>");

                    int remove_start = buffer.indexOf("<"+tag);
                    buffer.replace(remove_start, end+1, "");
                    img_c += 1;

                    htmlcontext.append("<P ParaShape=\"3\" Style=\"0\"><TEXT CharShape=\"0\">" +
                            "<PICTURE Reverse=\"false\">" +
                            "<SHAPEOBJECT InstId=\"2137949406\" Lock=\"false\" NumberingType=\"Figure\" ZOrder=\"1\">" +
                            "<SIZE Height=\"25980\" HeightRelTo=\"Absolute\" Protect=\"false\" Width=\"30541\" WidthRelTo=\"Absolute\"/>" +
                            "<POSITION AffectLSpacing=\"false\" AllowOverlap=\"false\" FlowWithText=\"true\" HoldAnchorAndSO=\"false\" HorzAlign=\"Left\" HorzOffset=\"0\" HorzRelTo=\"Column\" TreatAsChar=\"true\" VertAlign=\"Top\" VertOffset=\"0\" VertRelTo=\"Para\"/>" +
                            "<OUTSIDEMARGIN Bottom=\"0\" Left=\"0\" Right=\"0\" Top=\"0\"/><SHAPECOMMENT></SHAPECOMMENT></SHAPEOBJECT>" +
                            "<SHAPECOMPONENT GroupLevel=\"0\" HorzFlip=\"false\" InstID=\"1064207583\" OriHeight=\"25980\" OriWidth=\"19500\" VertFlip=\"false\" XPos=\"0\" YPos=\"0\"><ROTATIONINFO Angle=\"0\" CenterX=\"9750\" CenterY=\"12990\"/><RENDERINGINFO><TRANSMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/><SCAMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/><ROTMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/></RENDERINGINFO></SHAPECOMPONENT><IMAGERECT X0=\"0\" X1=\"19500\" X2=\"19500\" X3=\"0\" Y0=\"0\" Y1=\"0\" Y2=\"25980\" Y3=\"25980\"/><IMAGECLIP Bottom=\"19440\" Left=\"0\" Right=\"14640\" Top=\"0\"/><INSIDEMARGIN Bottom=\"0\" Left=\"0\" Right=\"0\" Top=\"0\"/><IMAGE Alpha=\"0\" BinItem=\"1\" Bright=\"0\" Contrast=\"0\" Effect=\"RealPic\"/><EFFECTS/></PICTURE><CHAR/></TEXT></P>");

                }
                else
                {
                    endOfsubContext = buffer.indexOf("</"+tag+">");

                    subContext = buffer.substring(end+1,endOfsubContext);
                    subContext = subContext.trim();

                    if(tag.indexOf("table")==0)
                    {                           //countMatches : count number of a string
                        int rowCount = StringUtils.countMatches(subContext, "</tr>");   //3
                        int colCount = StringUtils.countMatches(subContext, "</th>") + StringUtils.countMatches(subContext, "</td>");
                        colCount = colCount / rowCount;   //3

                        int rowaddr = 0;
                        int coladdr = 0;
                        //table summary
                        htmlcontext.append("<P ParaShape=\"3\" Style=\"0\"><TEXT CharShape=\"0\"><TABLE BorderFill=\"3\" CellSpacing=\"0\" ColCount=\"" + colCount + "\" PageBreak=\"Cell\" RepeatHeader=\"true\" RowCount=\"" + rowCount + "\">" +
                                "<SHAPEOBJECT InstId=\"2136179221\" Lock=\"false\" NumberingType=\"Table\" TextWrap=\"TopAndBottom\" ZOrder=\"0\">" +
                                "<SIZE Height=\"3846\" HeightRelTo=\"Absolute\" Protect=\"false\" Width=\"41950\" WidthRelTo=\"Absolute\"/>" +
                                "<POSITION AffectLSpacing=\"false\" AllowOverlap=\"false\" FlowWithText=\"true\" HoldAnchorAndSO=\"false\" HorzAlign=\"Left\" HorzOffset=\"0\" HorzRelTo=\"Column\" TreatAsChar=\"false\" VertAlign=\"Top\" VertOffset=\"0\" VertRelTo=\"Para\"/>" +
                                "<OUTSIDEMARGIN Bottom=\"283\" Left=\"283\" Right=\"283\" Top=\"283\"/></SHAPEOBJECT><INSIDEMARGIN Bottom=\"141\" Left=\"510\" Right=\"510\" Top=\"141\"/>"
                        );

                        while (rowaddr < rowCount) {
                            int p_start = 0;
                            int p_end = 0;

                            if(coladdr==0) htmlcontext.append("<ROW>");

                            if (buffer.indexOf("<th>") != -1) {
                                p_start = buffer.indexOf("<th>");
                                p_end = buffer.indexOf("</th>");

                            } else if (buffer.indexOf("<td>") != -1) {
                                p_start = buffer.indexOf("<td>");
                                p_end = buffer.indexOf("</td>");
                            } else {
                                htmlcontext.append("</ROW></TABLE>");
                            }
                            String cell_str = buffer.substring(p_start, p_end);
                            cell_str = cell_str.substring(cell_str.indexOf(">") + 1);

                            htmlcontext.append("<CELL BorderFill=\"3\" ColAddr=\"" + coladdr
                                    + "\" ColSpan=\"1\" Dirty=\"false\" Editable=\"false\" HasMargin=\"false\" Header=\"false\" Height=\"282\" Protect=\"false\" RowAddr=\""
                                    + rowaddr + "\" RowSpan=\"1\" Width=\"" + 41950 / rowCount
                                    + "\"><CELLMARGIN Bottom=\"141\" Left=\"510\" Right=\"510\" Top=\"141\"/><PARALIST LineWrap=\"Break\" LinkListID=\"0\" LinkListIDNext=\"0\" TextDirection=\"0\" VertAlign=\"Center\"><P ParaShape=\"3\" Style=\"0\"><TEXT CharShape=\"0\"><CHAR>"
                                    + cell_str + "</CHAR></TEXT></P></PARALIST></CELL>");

                            coladdr++;

                            buffer.replace(p_start, p_end+3, "");
                            if(coladdr == colCount){ coladdr = 0; rowaddr++; htmlcontext.append("</ROW>");}

                        }
                        htmlcontext.append("</TABLE></TEXT></P>");
                        borderfillBuf.append("<BORDERFILL BackSlash=\"0\" BreakCellSeparateLine=\"0\" CenterLine=\"0\" CounterBackSlash=\"0\" CounterSlash=\"0\" CrookedSlash=\"0\" Id=\"3\" Shadow=\"false\" Slash=\"0\" ThreeD=\"false\"><LEFTBORDER Type=\"Solid\" Width=\"0.12mm\"/><RIGHTBORDER Type=\"Solid\" Width=\"0.12mm\"/><TOPBORDER Type=\"Solid\" Width=\"0.12mm\"/><BOTTOMBORDER Type=\"Solid\" Width=\"0.12mm\"/><DIAGONAL Type=\"Solid\" Width=\"0.1mm\"/></BORDERFILL>");
                        table_c += 1;

                    }
                    else
                        htmlcontext.append("<P Parashape=\"16\" Style=\"0\"><TEXT CharShape=\"7\"><CHAR>"+subContext+"</CHAR></TEXT></P>");

                    String end_tag = "</"+tag+">";
                    int remove_start = buffer.indexOf("<"+tag);
                    int remove_end = buffer.indexOf(end_tag);

                    buffer.replace(remove_start, remove_end+end_tag.length(), "");
                }
            }
            buffer = new StringBuffer(buffer.toString().trim());
        }
        if(img_c>=1)
        {
            img_header.insert(0, "<BINDATALIST Count=\""+img_c+"\">");
            img_header.append("</BINDATALIST>");

            binary_img.insert(0, "<BINDATASTORAGE>");
            binary_img.append("</BINDATASTORAGE>");

        }

    }
    private void setBinaryImage(String url)
    {
        FileInputStream inputStream = null;
        ByteArrayOutputStream byteOutputStream = null;

        File file = new File(url);

        if(file.exists())
        {
            try {
                inputStream  = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byteOutputStream  = new ByteArrayOutputStream();

            int len = 0;
            byte[] buf = new byte[1024];

            while(true)
            {
                try {
                    if (!((len = inputStream.read(buf)) != -1)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byteOutputStream.write(buf, 0, len);
            }


            byte[] fileArray = byteOutputStream.toByteArray();
            String encoded_str = new String(Base64.encodeBase64(fileArray));

            binary_img.append("<BINDATA Encoding=\"Base64\" Id=\""+(img_c+1)+"\" Size=\"10113\">"+encoded_str+"</BINDATA>");

        }
    }
    public StringBuffer getConvertedHTML(){ return htmlcontext; }
    public StringBuffer getBorderfillBuf() { return borderfillBuf; }
    public StringBuffer getFontBuf(){ return fontBuf; }
    public int table() { return table_c; }
    public int image() { return img_c; }
    public StringBuffer getBinaryImage() { return binary_img; }
    public StringBuffer getImgHeader() { return img_header; }
}
