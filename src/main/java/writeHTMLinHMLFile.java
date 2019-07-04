
import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class writeHTMLinHMLFile {

    private String htmlUrl;
    private String hwpmlUrl;

    private Borderfill borderfill;
    private CharShape charShape;
    private Image image;

    private ConvertHTMLtoHML convertHTMLtoHML;

    public writeHTMLinHMLFile(String htmlUrl, String hwpmlUrl)
    {
        this.htmlUrl = htmlUrl;
        this.hwpmlUrl = hwpmlUrl;

        this.borderfill = new Borderfill();
        this.charShape = new CharShape();
        this.image = new Image();

        this.convertHTMLtoHML = new ConvertHTMLtoHML(this.htmlUrl, this.borderfill, this.charShape, this.image);
    }
    public void write()
    {
        StringBuffer html = convertHTMLtoHML.convert();
        StringBuffer bufferInHWPML = getBufferinHwpml();
        //insert
        insertInBody(bufferInHWPML, html);
        insertInHead(bufferInHWPML);

        //delete needless string
        int endofHWPML = bufferInHWPML.indexOf("</HWPML>");
        bufferInHWPML.replace(endofHWPML+8, bufferInHWPML.length(), "");

        writeConvertedHtml(bufferInHWPML);
    }
    private StringBuffer getBufferinHwpml()
    {
        //add contexts of buffer to hwpml file
        File file = new File(hwpmlUrl);
        BufferedReader br = null;
        char[] ch = new char[(int)file.length()];
        StringBuffer buffer = new StringBuffer();

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

        return buffer;
    }
    private void insertInBody(StringBuffer buffer, StringBuffer htmlBuffer)
    {
        int endofSection = buffer.indexOf("</SECTION>");
        buffer.insert(endofSection, htmlBuffer);

        int tableCount = StringUtils.countMatches(borderfill.toString(), "</TABLE>");

        if(tableCount>=2)
            buffer.replace(buffer.indexOf(" Table=\""), buffer.indexOf("/>"), " Table=\""+tableCount+"\"");
    }
    private void insertInHead(StringBuffer buffer)
    {
        insertInTableHead(buffer);
        insertInImageHead(buffer);
        insertInStringHead(buffer);
    }
    private void insertInTableHead(StringBuffer buffer)
    {
        StringBuffer borderfillBuf = borderfill.getBorderfill();
        if(borderfillBuf!=null)
        {
            buffer.insert(buffer.indexOf("</BORDERFILLLIST>"), borderfillBuf);
            int borderCount = borderfill.borderfillCount();

            buffer.replace(buffer.indexOf("<BORDERFILLLIST"), buffer.indexOf("<BORDERFILL BackSlash"), "<BORDERFILLLIST Count=\""+borderCount+"\">");
        }
    }
    private void insertInImageHead(StringBuffer buffer)
    {
        StringBuffer binListBuf = image.header();
        if(binListBuf !=null)
        {
            buffer.insert(buffer.indexOf("<FACENAMELIST>"), binListBuf);

            StringBuffer binDataBuf = image.tail();
            buffer.insert(buffer.indexOf("<SCRIPTCODE"), binDataBuf);
        }
    }
    private void insertInStringHead(StringBuffer buffer)
    {
        StringBuffer charShapeBuf = charShape.getCharShape();
        if(charShapeBuf != null)
        {
            buffer.insert(buffer.indexOf("</CHARSHAPELIST>"), charShapeBuf);
            int charshapeCount = charShape.count();

            buffer.replace(buffer.indexOf("</BORDERFILLLIST>")+17, buffer.indexOf("<CHARSHAPE "), "<CHARSHAPELIST Count=\""+charshapeCount+"\">");
        }
    }
    private void writeConvertedHtml(StringBuffer buffer)
    {
        File file = new File(hwpmlUrl);
        //rewrite in hml file
        FileWriter writer = null;

        try {
            // 기존 파일의 내용에 이어서 쓰려면 true를, 기존 내용을 없애고 새로 쓰려면 false를 지정한다.
            writer = new FileWriter(file, false);
            writer.write(buffer.toString());
            writer.flush();

            System.out.println("DONE");
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null) writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void renameFile(String filename, String newFilename) {
        File file = new File( filename );
        File fileNew = new File( newFilename );
        if( file.exists() ) file.renameTo( fileNew );
    }
}
