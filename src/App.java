import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class App {
    public static String textAnalysis(String src) {

        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(src, true);
        String data = "";
        for (int i = 0; i < kl.size(); i++) {
            Keyword kw = kl.get(i);
            System.out.println(kw.getString() + kw.getCnt());
            if (i > 0)
                data += "#";
            data += kw.getString() + ":" + kw.getCnt();
        }
        return data;
    }
    
    public static void analysis() {
        try {
            // 파일 목록 불러오기
            File dir = new File("./src/html/");
            File files[] = dir.listFiles();

            // xml 생성기
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);

            // doc id변수
            int ix = 0;

            Element docs = doc.createElement("docs");
            doc.appendChild(docs);

            for (File file : files) {
                // jsoup 객체
                org.jsoup.nodes.Document html = Jsoup.parse(file, "UTF-8");

                String titleData = html.title();
                String bodyData = html.body().text();
                String data = textAnalysis(bodyData);

                // doc 요소

                Element code = doc.createElement("doc");
                code.setAttribute("id", Integer.toString(ix++));

                // title 요소

                Element title = doc.createElement("title");
                title.appendChild(doc.createTextNode(titleData));
                code.appendChild(title);

                // body 요소

                Element body = doc.createElement("body");
                body.appendChild(doc.createTextNode(data));
                code.appendChild(body);

                docs.appendChild(code);
            }

            // xml 파일 내보내기
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(new File("./src/index.xml")));

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void preprocessing() {
        try {
            // 파일 목록 불러오기
            File dir = new File("./src/html/");
            File files[] = dir.listFiles();

            // xml 생성기
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);

            // doc id변수
            int ix = 0;
            
            Element docs = doc.createElement("docs");
            doc.appendChild(docs);

            for (File file : files) {
                // jsoup 객체
                org.jsoup.nodes.Document html = Jsoup.parse(file, "UTF-8");

                String titleData = html.title();
                String bodyData = html.body().text();

                // doc 요소
                Element code = doc.createElement("doc");
                code.setAttribute("id", Integer.toString(ix++));

                // title 요소
                Element title = doc.createElement("title");
                title.appendChild(doc.createTextNode(titleData));
                code.appendChild(title);

                // body 요소
                Element body = doc.createElement("body");
                body.appendChild(doc.createTextNode(bodyData));
                code.appendChild(body);

                docs.appendChild(code);
            }
            
            // xml 파일 내보내기
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(new File("./src/out.xml")));

            transformer.transform(source, result);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        preprocessing();
        analysis();
    }
}
