import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.awt.*;
import java.io.*;

import static java.lang.Integer.parseUnsignedInt;
import static org.junit.Assert.*;

public class ConvertedHTMLTest {

    @Test
    public void bodyTest(){

        Element a = getHtmlBody();

        System.out.println(a.tagName());
        assertEquals(a.attr("style"), "");
    }
    @Test
    public void testChildnum()
    {
        Element a = getHtmlBody();

        System.out.println(a.children().size());
    }
    @Test
    public void colorTest(){

        Element a = getHtmlBody();
        System.out.println(a.select("thead").attr("style"));
    }
    @Test
    public void testLongint(){
        long colorValue = 4294967295L;
        String a  = "this is : "+colorValue;

        assertEquals(a, "this is : 4294967295");
    }
    @Test
    public void testTextTag(){
        Element a = getHtmlBody();
        System.out.println(a.select("p").first());
    }
    @Test
    public void testFontSize(){
        Element a = getHtmlBody();
        System.out.println(a.select("p").attr("style"));

        String style_str = a.select("p").attr("style");
        int start_fontsize = style_str.indexOf("font-size:");
        int end_fontsize = style_str.indexOf("px");

        double fontSize = Double.parseDouble(style_str.substring(start_fontsize+10, end_fontsize));

        assertEquals(fontSize, 16, 0);
    }
    @Test
    public void testColCount()
    {
        Element a = getHtmlBody();
        Elements tr = a.select("tr");
        System.out.println(tr);
        System.out.println("-----");
        System.out.println(tr.get(0).children().size());
        System.out.println(tr.get(3).children().size());

    }
    @Test
    public void testColor()
    {
        String hexOfColor = "FF0000";

        int r = parseUnsignedInt(hexOfColor.substring(0, 2), 16);
        int g = parseUnsignedInt(hexOfColor.substring(2, 4), 16);
        int b = parseUnsignedInt(hexOfColor.substring(4, 6), 16);

        System.out.println(b*256*256 + g*256+r);
    }

    @Test
    public void test()
    {

    }
    private String loadHtmlFile()
    {
        File file = new File("./gk2a_ami_le2_amv_cd14_weeklyreport_201905_3.html");
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

}