public class Borderfill implements BorderfillHeader{

  private StringBuffer borderfill;
  private int id;
  private int count;

  public Borderfill() {
    this.borderfill = new StringBuffer(
        "<BORDERFILL BackSlash=\"0\" BreakCellSeparateLine=\"0\" CenterLine=\"0\" CounterBackSlash=\"0\" CounterSlash=\"0\" CrookedSlash=\"0\" Id=\"3\" Shadow=\"false\" Slash=\"0\" ThreeD=\"false\"><LEFTBORDER Type=\"Solid\" Width=\"0.12mm\"/><RIGHTBORDER Type=\"Solid\" Width=\"0.12mm\"/><TOPBORDER Type=\"Solid\" Width=\"0.12mm\"/><BOTTOMBORDER Type=\"Solid\" Width=\"0.12mm\"/><DIAGONAL Type=\"Solid\" Width=\"0.1mm\"/><FILLBRUSH><WINDOWBRUSH Alpha=\"0\" FaceColor=\"4294967295\" HatchColor=\"4278190080\"/></FILLBRUSH></BORDERFILL>");
    this.id = 3;
  }

  @Override
  public void setBorderfill(long valueOfColor) {
    ++id; ++count;
    borderfill.append(
        "<BORDERFILL BackSlash=\"0\" BreakCellSeparateLine=\"0\" CenterLine=\"0\" CounterBackSlash=\"0\" CounterSlash=\"0\" CrookedSlash=\"0\" Id=\""
            + id
            + "\" Shadow=\"false\" Slash=\"0\" ThreeD=\"false\"><LEFTBORDER Type=\"Solid\" Width=\"0.12mm\"/><RIGHTBORDER Type=\"Solid\" Width=\"0.12mm\"/><TOPBORDER Type=\"Solid\" Width=\"0.12mm\"/><BOTTOMBORDER Type=\"Solid\" Width=\"0.12mm\"/><DIAGONAL Type=\"Solid\" Width=\"0.1mm\"/><FILLBRUSH><WINDOWBRUSH Alpha=\"0\" FaceColor=\""
            + valueOfColor + "\" HatchColor=\"4278190080\"/></FILLBRUSH></BORDERFILL>");
  }
  @Override
  public int id() {
    return id;
  }

  public StringBuffer getBorderfill() {
    return borderfill;
  }
  public int count() {
    return count;
  }
}
