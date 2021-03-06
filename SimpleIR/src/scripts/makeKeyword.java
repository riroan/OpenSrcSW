package scripts;
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
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class makeKeyword {
    private String input_file;
	private String output_flie = "./index.xml";
	
    public makeKeyword(String file) {
        this.input_file = file;
    }
    
    public String textAnalysis(String src) {

        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(src, true);
        String data = "";
        for (int i = 0; i < kl.size(); i++) {
            Keyword kw = kl.get(i);
            if (i > 0)
                data += "#";
            data += kw.getString() + ":" + kw.getCnt();
        }
        return data;
    }
    
    public void convertXml() throws Exception {
        try {
            // 파일 목록 불러오기
            File file = new File(input_file);

            // xml 생성기
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);

            // doc id변수
            int ix = 0;

            Element docs = doc.createElement("docs");
            doc.appendChild(docs);

            // jsoup 객체
            Document dc = docBuilder.parse(file);
            dc.getDocumentElement().normalize();
            NodeList docList = dc.getElementsByTagName("doc");
            int n = docList.getLength();

            for (int i = 0; i < n; i++) {
                Node docNode = docList.item(i);
                Element docElement = (Element) docNode;
                String titleData = getTagValue("title", docElement);
                String bodyData = getTagValue("body", docElement);
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
            StreamResult result = new StreamResult(new FileOutputStream(new File(output_flie)));

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String getTagValue(String tag, Element element){
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
}
