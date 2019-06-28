import java.io.File;

public class Main {

    public static void main(String args[]){

        HTMLtoHML htmLtoHML = new HTMLtoHML("./test.html", "./empty.hml");
        htmLtoHML.toHwpml();

//        ConvertedHTML convertedHTML = new ConvertedHTML("./test.html");
//        convertedHTML.getConvertedHTML();
    }
}
