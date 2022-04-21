package scripts;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Midterm {
    private String input_file;

    public Midterm(String file) {
        this.input_file = file;
    }

    void showSnippet(String query) throws Exception {
        try {
            KeywordExtractor ke = new KeywordExtractor();
            KeywordList kl = ke.extractKeyword(query, true);
            List<String> ss = new ArrayList<String>();
            for (int i = 0; i < kl.size(); i++) {
                Keyword kw = kl.get(i);
                boolean flag = false;
                for (int j = 0; j < ss.size(); j++) {
                    if (ss.get(j) == kw.getString()) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    ss.add(kw.getString());
                }
            }

            String[] keywords = new String[ss.size()];
            for (int i = 0; i < ss.size(); i++) {
                keywords[i] = ss.get(i);
                System.out.println(keywords[i]);
            }


            File file = new File(input_file);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document dc = docBuilder.parse(file);
            dc.getDocumentElement().normalize();
            NodeList docList = dc.getElementsByTagName("doc");
            int n = docList.getLength();
            int max_score= 0;
            for (int i = 0; i < n; i++) {
                Node docNode = docList.item(i);
                Element docElement = (Element) docNode;
                String bodyData = getTagValue("body", docElement);
                int sz = bodyData.length();
                for (int j = 0; j < sz - 30; j++) {
                    int score = 0;
                    String tmp = bodyData.substring(j, j + 30);
                    for (String s : keywords) {
                        if (tmp.contains(s)) {
                            score++;
                        }
                    }
                    max_score = Math.max(max_score, score);
                }
            }
            List<String> answer = new ArrayList<String>();
            for (int i = 0; i < n; i++) {
                Node docNode = docList.item(i);
                Element docElement = (Element) docNode;
                String bodyData = getTagValue("body", docElement);
                String titleData = getTagValue("title", docElement);
                int sz = bodyData.length();
                for (int j = 0; j < sz - 30; j++) {
                    int score = 0;
                    String tmp = bodyData.substring(j, j + 30);
                    for (String s : keywords) {
                        if (tmp.contains(s)) {
                            score++;
                        }
                    }
                    if (score == max_score) {
                        String tt = "";
                        tt += titleData + "," + tmp + "," + score;
                        answer.add(tt);
                    }
                }
            }
            for (int i = 0; i < answer.size(); i++) {
                System.out.println(answer.get(i));
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
