package scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class searcher {
    private String input_file;
    private String collection_file = "./collection.xml";

    public searcher(String file) {
        this.input_file = file;
    }

    public void CalcSim(String query) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            File file = new File(collection_file);
            Document dc = docBuilder.parse(file);
            dc.getDocumentElement().normalize();
            NodeList docList = dc.getElementsByTagName("doc");
            int n = docList.getLength();
            String[] titles = new String[n];
            for (int i = 0; i < n; i++) {
                Node docNode = docList.item(i);
                Element docElement = (Element) docNode;
                titles[i] = getTagValue("title", docElement);
            }

            FileInputStream fileInputStream = new FileInputStream(input_file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            Object object = objectInputStream.readObject();
            objectInputStream.close();
            object.getClass();
            HashMap hs = (HashMap) object;
            Set<String> keySet = hs.keySet();
            KeywordExtractor ke = new KeywordExtractor();
            KeywordList kl = ke.extractKeyword(query, true);

            double[] values = new double[n];

            for (int i = 0; i < kl.size(); i++) {
                Keyword kw = kl.get(i);
                // System.out.println(kw.getString());
                double[] value = (double[]) hs.get(kw.getString());
                for (int j = 0; j < value.length; j++) {
                    // System.out.println(value[j]);
                    values[j] += value[j];
                }
            }
            int cnt = 0;
            for (int i = 0; i < 3; i++) { // 상위 3개까지 출력하시오.
                double m = 0.0;
                int ix = -1;
                for (int j = 0; j < n; j++) {
                    if (m < values[j]) {
                        m = values[j];
                        ix = j;
                    }
                }
                if (ix == -1)
                    break;
                System.out.println("제목 : " + titles[ix] + " 유사도 : " + Math.round(values[ix] * 100) / 100.0 + "%");
                values[ix] = 0.0;
                cnt++;
            }
            if (cnt == 0) {
                System.out.println("검색된 문서가 없습니다.");
            }
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