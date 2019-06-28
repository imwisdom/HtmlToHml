import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class HTMLtoHML {

    private String htmlFile = null;
    private String hwpmlFile = null;

    public HTMLtoHML(String htmlFile, String hwpmlFile)
    {
        this.htmlFile = htmlFile;
        this.hwpmlFile = hwpmlFile;
    }
    public void toHwpml()
    {
        ConvertedHTML convertedHTML = new ConvertedHTML(this.htmlFile);
        StringBuffer htmlBuffer = convertedHTML.getConvertedHTML();

        //add contexts of buffer to hwpml file
        File file = new File(hwpmlFile);
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
        //insert in section tag
        int endofSection = buffer.indexOf("</SECTION>");
        buffer.insert(endofSection, htmlBuffer);

        if(convertedHTML.table()>=2)
            buffer.replace(buffer.indexOf(" Table=\""), buffer.indexOf("/>"), " Table=\""+convertedHTML.table()+"\"");

        //table header
        StringBuffer borderfillBuf = convertedHTML.getBorderfillBuf();
        if(borderfillBuf!=null)
        {
            buffer.insert(buffer.indexOf("</BORDERFILLLIST>"), borderfillBuf);
            int borderCount = StringUtils.countMatches(buffer, "</BORDERFILL>");

            buffer.replace(buffer.indexOf("<BORDERFILLLIST"), buffer.indexOf("<BORDERFILL BackSlash"), "<BORDERFILLLIST Count=\""+borderCount+"\">");

        }

        //img header
        StringBuffer binListBuf = convertedHTML.getImgHeader();
        if(binListBuf !=null)
        {
            buffer.insert(buffer.indexOf("<FACENAMELIST>"), binListBuf);

            StringBuffer binDataBuf = convertedHTML.getBinaryImage();
            buffer.insert(buffer.indexOf("<SCRIPTCODE"), binDataBuf);

        }

        //delete needless string
        int endofHWPML = buffer.indexOf("</HWPML>");
        buffer.replace(endofHWPML+8, buffer.length(), "");

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
