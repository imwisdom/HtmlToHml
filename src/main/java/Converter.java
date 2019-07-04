
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;


public interface Converter {

    StringBuffer convert();
    String convert(Node htmlNode);

}
