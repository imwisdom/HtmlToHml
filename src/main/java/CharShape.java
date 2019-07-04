public class CharShape implements CharShapeHeader{

    //for style
    private StringBuffer charShape;
    private int charShapeId;

    public CharShape()
    {
        this.charShape = new StringBuffer();
        this.charShapeId = 6;
    }
    public int id(boolean isBold) {

        if(isBold)
        {
            ++charShapeId;

            charShape.append("<CHARSHAPE BorderFillId=\"2\" Height=\"1000\" Id=\""+charShapeId+"\" ShadeColor=\"4294967295\" SymMark=\"0\" TextColor=\"0\" UseFontSpace=\"false\" UseKerning=\"false\"><FONTID Hangul=\"1\" Hanja=\"1\" Japanese=\"1\" Latin=\"1\" Other=\"1\" Symbol=\"1\" User=\"1\"/><RATIO Hangul=\"100\" Hanja=\"100\" Japanese=\"100\" Latin=\"100\" Other=\"100\" Symbol=\"100\" User=\"100\"/><CHARSPACING Hangul=\"0\" Hanja=\"0\" Japanese=\"0\" Latin=\"0\" Other=\"0\" Symbol=\"0\" User=\"0\"/><RELSIZE Hangul=\"100\" Hanja=\"100\" Japanese=\"100\" Latin=\"100\" Other=\"100\" Symbol=\"100\" User=\"100\"/><CHAROFFSET Hangul=\"0\" Hanja=\"0\" Japanese=\"0\" Latin=\"0\" Other=\"0\" Symbol=\"0\" User=\"0\"/><BOLD/></CHARSHAPE>");
            return charShapeId;
        }
        else return 0;
    }
    public int id(double fontsize, boolean isBold) {

        ++charShapeId;

        charShape.append("<CHARSHAPE BorderFillId=\"2\" Height=\""+(fontsize*100)+"\" Id=\""+charShapeId+"\" ShadeColor=\"4294967295\" SymMark=\"0\" TextColor=\"0\" UseFontSpace=\"false\" UseKerning=\"false\"><FONTID Hangul=\"1\" Hanja=\"1\" Japanese=\"1\" Latin=\"1\" Other=\"1\" Symbol=\"1\" User=\"1\"/><RATIO Hangul=\"100\" Hanja=\"100\" Japanese=\"100\" Latin=\"100\" Other=\"100\" Symbol=\"100\" User=\"100\"/><CHARSPACING Hangul=\"0\" Hanja=\"0\" Japanese=\"0\" Latin=\"0\" Other=\"0\" Symbol=\"0\" User=\"0\"/><RELSIZE Hangul=\"100\" Hanja=\"100\" Japanese=\"100\" Latin=\"100\" Other=\"100\" Symbol=\"100\" User=\"100\"/><CHAROFFSET Hangul=\"0\" Hanja=\"0\" Japanese=\"0\" Latin=\"0\" Other=\"0\" Symbol=\"0\" User=\"0\"/></CHARSHAPE>");

        if(isBold)
            charShape.insert(charShape.lastIndexOf("</CHARSHAPE>"), "<BOLD/>");

        return charShapeId;
    }
    public StringBuffer getCharShape()
    {
        return charShape;
    }
    public int count()
    {
        return charShapeId+1;   //first id number is 0
    }
}
