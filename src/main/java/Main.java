
public class Main {

    /**
     *
     * @param args - args[0] : src html args[1] : dist path
     */
    public static void main(String args[]){

        writeHTMLinHMLFile writeHTMLinHMLFile = new writeHTMLinHMLFile(args[0], args[1]);
        writeHTMLinHMLFile.write();
    }
}
