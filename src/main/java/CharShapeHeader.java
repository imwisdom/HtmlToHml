public interface CharShapeHeader {

    void setCharShape(boolean isBold);
    void setCharShape(double fontsize, boolean isBold);
    int count();
    int id();
    StringBuffer getCharShape();
}
