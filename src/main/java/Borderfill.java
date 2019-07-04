public class Borderfill implements BorderfillHeader {

    private StringBuffer borderfill;
    private int borderfillId;

    public Borderfill()
    {
        this.borderfill = new StringBuffer("<BORDERFILL BackSlash=\"0\" BreakCellSeparateLine=\"0\" CenterLine=\"0\" CounterBackSlash=\"0\" CounterSlash=\"0\" CrookedSlash=\"0\" Id=\"3\" Shadow=\"false\" Slash=\"0\" ThreeD=\"false\"><LEFTBORDER Type=\"Solid\" Width=\"0.12mm\"/><RIGHTBORDER Type=\"Solid\" Width=\"0.12mm\"/><TOPBORDER Type=\"Solid\" Width=\"0.12mm\"/><BOTTOMBORDER Type=\"Solid\" Width=\"0.12mm\"/><DIAGONAL Type=\"Solid\" Width=\"0.1mm\"/><FILLBRUSH><WINDOWBRUSH Alpha=\"0\" FaceColor=\"4294967295\" HatchColor=\"4278190080\"/></FILLBRUSH></BORDERFILL>");
        this.borderfillId = 3;
    }
    public int id()
    {
        return 3;
    }
    public int id(long valueOfColor)
    {
        ++borderfillId;
        borderfill.append("<BORDERFILL BackSlash=\"0\" BreakCellSeparateLine=\"0\" CenterLine=\"0\" CounterBackSlash=\"0\" CounterSlash=\"0\" CrookedSlash=\"0\" Id=\""+borderfillId+"\" Shadow=\"false\" Slash=\"0\" ThreeD=\"false\"><LEFTBORDER Type=\"Solid\" Width=\"0.12mm\"/><RIGHTBORDER Type=\"Solid\" Width=\"0.12mm\"/><TOPBORDER Type=\"Solid\" Width=\"0.12mm\"/><BOTTOMBORDER Type=\"Solid\" Width=\"0.12mm\"/><DIAGONAL Type=\"Solid\" Width=\"0.1mm\"/><FILLBRUSH><WINDOWBRUSH Alpha=\"0\" FaceColor=\""+valueOfColor+"\" HatchColor=\"4278190080\"/></FILLBRUSH></BORDERFILL>");

        return borderfillId;
    }
    public StringBuffer getBorderfill() {
        return borderfill;
    }
    public int borderfillCount()
    {
        return borderfillId;    //last id = number of borderfill
    }
}
