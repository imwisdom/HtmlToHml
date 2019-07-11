public class ImageConverter {

  public ImageConverter(){

  }
  HMLNode ImageBody(int imgCount, double width, double height, HMLNode hmlNode) {

    hmlNode.setHeadFirst("<P Parashape=\"3\" Style=\"0\"><TEXT CharShape=\"0\">"
        +"<PICTURE Reverse=\"false\"><SHAPEOBJECT InstId=\"2137949406\" Lock=\"false\" NumberingType=\"Figure\" ZOrder=\"1\"><SIZE Height=\""
        + height + "\" HeightRelTo=\"Absolute\" Protect=\"false\" Width=\"" + width
        + "\" WidthRelTo=\"Absolute\"/><POSITION AffectLSpacing=\"false\" AllowOverlap=\"false\" FlowWithText=\"true\" HoldAnchorAndSO=\"false\" HorzAlign=\"Left\" HorzOffset=\"0\" HorzRelTo=\"Column\" TreatAsChar=\"true\" VertAlign=\"Top\" VertOffset=\"0\" VertRelTo=\"Para\"/><OUTSIDEMARGIN Bottom=\"0\" Left=\"0\" Right=\"0\" Top=\"0\"/><SHAPECOMMENT></SHAPECOMMENT></SHAPEOBJECT><SHAPECOMPONENT CurHeight=\""
        + height + "\" CurWidth=\"" + width
        + "\" GroupLevel=\"0\" HorzFlip=\"false\" InstID=\"1064207583\" OriHeight=\"" + 200000
        + "\" OriWidth=\"" + 200000
        + "\" VertFlip=\"false\" XPos=\"0\" YPos=\"0\"><ROTATIONINFO Angle=\"0\" CenterX=\"9750\" CenterY=\"12990\"/><RENDERINGINFO><TRANSMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/><SCAMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/><ROTMATRIX E1=\"1.00000\" E2=\"0.00000\" E3=\"0.00000\" E4=\"0.00000\" E5=\"1.00000\" E6=\"0.00000\"/></RENDERINGINFO></SHAPECOMPONENT><IMAGERECT X0=\"0\" X1=\""
        + 200000 + "\" X2=\"" + 200000 + "\" X3=\"0\" Y0=\"0\" Y1=\"0\" Y2=\"" + 200000
        + "\" Y3=\"" + 200000
        + "\"/><IMAGECLIP Bottom=\"200000\" Left=\"0\" Right=\"200000\" Top=\"0\"/><INSIDEMARGIN Bottom=\"0\" Left=\"0\" Right=\"0\" Top=\"0\"/><IMAGE Alpha=\"0\" BinItem=\""
        + imgCount
        + "\" Bright=\"0\" Contrast=\"0\" Effect=\"RealPic\"/><EFFECTS/></PICTURE>");
    hmlNode.setFoot("<CHAR/></TEXT></P>");

    return hmlNode;
  }
}
