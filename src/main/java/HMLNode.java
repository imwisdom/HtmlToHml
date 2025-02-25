
public class HMLNode {

  private HMLNode sibling;
  private HMLNode child;
  private String head;
  private String foot;

  public HMLNode() {
    this.head = "";
    this.foot = "";
    this.child = null;
  }

  public HMLNode(String head, String foot, HMLNode child) {
    this.head = head;
    this.foot = foot;
    this.child = child;
  }

  public void setNewSibling(HMLNode sibling) {
    if (this.sibling == null) {
      this.sibling = sibling;
    } else {
      HMLNode aNode = this.sibling;
      while (aNode.sibling != null) {
        aNode = aNode.sibling;
      }
      aNode.sibling = sibling;
    }
  }
  public void setNextSibling(HMLNode sibling){
    this.sibling = sibling;
  }


  public void addChild(HMLNode child) {
    if (this.child == null) {
      this.child = child;
    } else {
      HMLNode aNode = this.child;
      aNode.setNewSibling(child);
    }
  }

  public void setHeadFirst(String head) {
    this.head = head + this.head;
  }

  public void setHeadLast(String head) {
    this.head = this.head + head;
  }

  public void setFoot(String foot) {
    this.foot = this.foot + foot;
  }

  public String getHead() {
    return this.head;
  }

  public String getFoot() {
    return this.foot;
  }

  public HMLNode getSibling() {
    return this.sibling;
  }

  public HMLNode getChild() {
    return this.child;
  }

}
