
import org.apache.commons.codec.binary.Base64;

import java.io.*;

public class Image{

    private int img_count;
    private StringBuffer img_header;
    private StringBuffer img_tail;


    public Image()
    {
        this.img_count = 0;
        this.img_header = new StringBuffer();
        this.img_tail = new StringBuffer();
    }
    public void appendImageInfo(String src)
    {
        String extension = "";
        if(src.contains("data:image"))
        {
            extension = src.substring(src.indexOf("/")+1, src.indexOf(";"));

            appendImageHead(extension);
            setBase64EncodingData(src,true);
        }
        else
        {
            extension = src.substring(src.lastIndexOf(".")+1);

            appendImageHead(extension);
            setBase64EncodingData(src,false);
        }
    }
    private void appendImageHead(String extension)
    {
        img_header.append("<BINITEM BinData=\""+(img_count+1)+"\" Format=\""+extension+"\""+" Type=\"Embedding\"/>");
        img_count++;
    }
    private void setBase64EncodingData(String src, boolean isEncoded)
    {
        String encoded_str = "";
        if(isEncoded)
        {
            encoded_str = src.substring(src.indexOf(",")+1);
        }
        else
        {
            ByteArrayOutputStream byteOutputStream = getBinaryImageData(src);
            byte[] fileArray = byteOutputStream.toByteArray();
            encoded_str = new String(Base64.encodeBase64(fileArray));
        }
        img_tail.append("<BINDATA Encoding=\"Base64\" Id=\""+(img_count)+"\" Size=\"11210\">"+encoded_str+"</BINDATA>");
    }
    private ByteArrayOutputStream getBinaryImageData(String url)
    {
        FileInputStream inputStream = null;
        ByteArrayOutputStream byteOutputStream = null;

        File file = new File(url);

        if(file.exists()) {
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byteOutputStream = new ByteArrayOutputStream();

            int len = 0;
            byte[] buf = new byte[1024];

            while (true) {
                try {
                    if ((len = inputStream.read(buf)) == -1) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byteOutputStream.write(buf, 0, len);
            }

            return byteOutputStream;
        }
        return null;
    }
    public void putBindataInList()
    {
        img_header.insert(0, "<BINDATALIST Count=\"" + img_count + "\">");
        img_header.append("</BINDATALIST>");
    }
    public void putBindataInStorage()
    {
        img_tail.insert(0, "<BINDATASTORAGE>");
        img_tail.append("</BINDATASTORAGE>");
    }
    public StringBuffer header()
    {
        return img_header;
    }
    public StringBuffer tail()
    {
        return img_tail;
    }
    public int imageCount()
    {
        return img_count;
    }


}
