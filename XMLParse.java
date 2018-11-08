import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.*;

public class XMLParse {

    public Document getDocFromFile(String filename) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = null;

        try {
            doc = db.parse(filename);

        } catch (Exception ex) {
            System.out.println("XML parse failure");
            ex.printStackTrace();
        }
        return doc;
    }

    public Scene[] parseScenes(Document d) {
        Element root = d.getDocumentElement();

        NodeList cards = root.getElementsByTagName("card");
        int cardNum = cards.getLength();

        Scene[] newScenes = new Scene[cardNum];

        /* getting card information */
        for(int i = 0; i < cardNum; i++) {
            int num = 0;
            int budget;
            String img;
            String sceneName;
            String desc = "";

            Node scene = cards.item(i);
            sceneName = scene.getAttributes().getNamedItem("name").getNodeValue();
            img = scene.getAttributes().getNamedItem("img").getNodeValue();
            budget = Integer.parseInt(scene.getAttributes().getNamedItem("budget").getNodeValue());

            NodeList children = scene.getChildNodes();
            List<Role> sceneRoles = new ArrayList<>();

            /* getting children information */
            for(int j = 0; j < children.getLength(); j++) {
                Node sub = children.item(j);

                if("scene".equals(sub.getNodeName())) {
                    num = Integer.parseInt(sub.getAttributes().getNamedItem("number").getNodeValue());
                    desc = sub.getTextContent();
                }
                else if("part".equals(sub.getNodeName())) {
                    String name = sub.getAttributes().getNamedItem("name").getNodeValue();
                    int rank = Integer.parseInt(sub.getAttributes().getNamedItem("level").getNodeValue());
                    String line = "";
                    // area variables

                    NodeList subChildren = sub.getChildNodes();

                    for(int m = 0; m < subChildren.getLength(); m++) {
                        Node subChild = subChildren.item(m);

                        if("area".equals(subChild.getNodeName())) {
                            // area variables
                        }
                        else if("line".equals(subChild.getNodeName())) {
                            line = subChild.getTextContent();
                        }
                    }

                    sceneRoles.add(new Role(name, rank, line));
                }
            }

            newScenes[i] = new Scene(sceneName, budget, img, sceneRoles, num, desc);
        }
        return newScenes;
    }
}
