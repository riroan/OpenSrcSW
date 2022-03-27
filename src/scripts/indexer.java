package scripts;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class indexer {

    public void processing() {
        try {
            File file = new File("src/scripts/index.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("doc");

            List<String[]> words = new ArrayList<String[]>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    words.add(getTagValue("body", element).split("#"));
                }
            }
            HashMap hashMap = new HashMap();
            HashMap i2s = new HashMap();
            for (int i = 0; i < words.size(); i++) {
                for (int j = 0; j < words.get(i).length; j++) {
                    String word = words.get(i)[j];
                    String[] element = word.split(":");
                    String keyword = element[0];
                    if (hashMap.get(keyword) == null) {
                        hashMap.put(keyword, hashMap.size());
                        i2s.put(i2s.size(), keyword);
                    }
                }
            }
            int sz = hashMap.size();
            int[][] tf = new int[words.size()][sz];
            int[] df = new int[sz];
            double[][] w = new double[words.size()][sz];
            int N = words.size();

            for (int i = 0; i < words.size(); i++) {
                for (int j = 0; j < words.get(i).length; j++) {
                    String word = words.get(i)[j];
                    String[] element = word.split(":");
                    String keyword = element[0];
                    int freq = Integer.parseInt(element[1]);
                    tf[i][(int) hashMap.get(keyword)] = freq;
                    df[(int)hashMap.get(keyword)]++;
                }
            }

            for (int i = 0; i < words.size(); i++) {
                for (int j = 0; j < sz; j++) {
                    // System.out.println(tf[i][j]);
                    if (df[j] > 0) {
                        

                        w[i][j] = Math.round(tf[i][j] * Math.log((double) N / df[j])*100) / 100.0;
                    }
                }
            }
            
            HashMap outHash = new HashMap();
            for (int i = 0; i < sz; i++) {
                double[] item = new double[N];
                for (int j = 0; j < N; j++) {
                    item[j] = w[j][i];
                }
                outHash.put(i2s.get(i), item);
            }

            FileOutputStream fileStream = new FileOutputStream("src/data/index.post");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);

            objectOutputStream.writeObject(outHash);
            objectOutputStream.close();

            FileInputStream fileInputStream = new FileInputStream("src/data/index.post");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            Object object = objectInputStream.readObject();
            objectInputStream.close();
            object.getClass();
            HashMap hs = (HashMap) object;

            Iterator<String> it = hashMap.keySet().iterator();

            while (it.hasNext()) {
                String key = it.next();
                double[] value = (double[]) hs.get(key);
                System.out.println(key);
                for(int i=0;i<value.length;i++)
                    System.out.println(value[i]);
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
