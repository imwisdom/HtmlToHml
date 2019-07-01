import java.io.File;

public class Main {

    /**
     *
     * @param args - args[0] : src html args[1] : dist path
     */
    public static void main(String args[]){

        HTMLtoHML htmLtoHML = new HTMLtoHML(args[0], args[1]);
        htmLtoHML.toHwpml();

//        ConvertedHTML convertedHTML = new ConvertedHTML("./test.html");
//        convertedHTML.getConvertedHTML();
    }
}
