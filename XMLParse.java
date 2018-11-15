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

    public List<Scene> parseScenes(Document d) {
        Element root = d.getDocumentElement();

        NodeList cards = root.getElementsByTagName("card");
        int cardNum = cards.getLength();

        List<Scene> newScenes = new ArrayList<Scene>(cardNum);

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

                    sceneRoles.add(new Role(name, rank, line, true));
                }
            }

            newScenes.add(new Scene(sceneName, budget, img, sceneRoles, num, desc));
        }
        return newScenes;
    }

    // reads data from XML file and prints data
    public Room[] parseBoard(Document d){

        Element root = d.getDocumentElement();

        NodeList sets = root.getElementsByTagName("set");
        int roomNum = sets.getLength();

        Room[] newRooms = new Room[roomNum + 2]; // Add space for trailer and office
        int officeUpgrades[][] = new int[2][5];

        //System.out.println("Sets: " + roomNum); //10
        int setCount = 0;
        for (int i=0; i < roomNum;i++){

            setCount = i+1;

            String setName = "";
            int shots = 0;
            List<Role> boardRoles = new ArrayList<Role>();
            Node setNode = sets.item(i);
            List<String> neighborNames = new ArrayList<String>();
            if("set".equals(setNode.getNodeName()))
            {
                setName = setNode.getAttributes().getNamedItem("name").getNodeValue();
                NodeList children = setNode.getChildNodes();
                int count = 0;
                for (int j=0; j< children.getLength(); j++){
                    Node sub = children.item(j);

                    if("neighbors".equals(sub.getNodeName())){

                        NodeList neighbors = sub.getChildNodes();

                        for (int k = 0; k < neighbors.getLength(); k++)
                        {
                            Node neighbor = neighbors.item(k);

                            if("neighbor".equals(neighbor.getNodeName()))
                            {
                                String neighborName = neighbor.getAttributes().getNamedItem("name").getNodeValue();
                                neighborNames.add(neighborName);
                            }
                        }
                    }

                    else if("area".equals(sub.getNodeName())){
                        String x, y, h, w;

                        x = sub.getAttributes().getNamedItem("x").getNodeValue();
                        y = sub.getAttributes().getNamedItem("y").getNodeValue();
                        h = sub.getAttributes().getNamedItem("h").getNodeValue();
                        w = sub.getAttributes().getNamedItem("w").getNodeValue();
                        //System.out.println(x + " " + y + " " + h + " " + w);
                    }

                    else if("takes".equals(sub.getNodeName())){

                        NodeList takes = sub.getChildNodes();
                        int maxTake = 0;
                        for (int k = 0; k < takes.getLength(); k++)
                        {
                            Node take = takes.item(k);
                            if("take".equals(take.getNodeName()))
                            {
                                String takeNumber = take.getAttributes().getNamedItem("number").getNodeValue();
                                int takeNumInt = Integer.parseInt(takeNumber);
                                String takeX, takeY, takeH, takeW;
                                if(takeNumInt > maxTake)
                                {
                                    maxTake = takeNumInt;
                                }

                                Node takeArea = take.getFirstChild();
                                if("take".equals(take.getNodeName()))
                                {
                                    takeX = takeArea.getAttributes().getNamedItem("x").getNodeValue();
                                    takeY = takeArea.getAttributes().getNamedItem("y").getNodeValue();
                                    takeH = takeArea.getAttributes().getNamedItem("h").getNodeValue();
                                    takeW = takeArea.getAttributes().getNamedItem("w").getNodeValue();
                                }else{
                                    takeX = takeY = takeH = takeW = "";
                                }
                                //System.out.println("Takes area: " + takeX + " " + takeY + " " + takeH + " " + takeW);
                            }
                        }
                        shots = maxTake;

                    }

                    else if("parts".equals(sub.getNodeName())){
                        NodeList parts = sub.getChildNodes();

                        for (int k = 0; k < parts.getLength(); k++)
                        {
                            Node part = parts.item(k);
                            if("part".equals(part.getNodeName()))
                            {
                                String partName = part.getAttributes().getNamedItem("name").getNodeValue();
                                //System.out.println("Part name: " + partName);
                                int partLvl = Integer.parseInt(part.getAttributes().getNamedItem("level").getNodeValue());
                                String partX, partY, partH, partW;
                                String partLine = "";
                                NodeList subparts = part.getChildNodes();
                                for(int l = 0; l < subparts.getLength(); l++)
                                {
                                    Node partinfo = subparts.item(l);
                                    if("area".equals(partinfo.getNodeName()))
                                    {
                                        partX = partinfo.getAttributes().getNamedItem("x").getNodeValue();
                                        partY = partinfo.getAttributes().getNamedItem("y").getNodeValue();
                                        partH = partinfo.getAttributes().getNamedItem("h").getNodeValue();
                                        partW = partinfo.getAttributes().getNamedItem("w").getNodeValue();
                                        //System.out.println("Part area: " + partX + " " + partY + " " + partH + " " + partW);
                                    }

                                    if("line".equals(partinfo.getNodeName()))
                                    {
                                        partLine = partinfo.getTextContent();
                                        //System.out.println("Part line: " + partLine);
                                    }
                                }

                                boardRoles.add(new Role(partName, partLvl, partLine, false));
                            }
                        }
                    }
                }

                //System.out.println("\n");
            }
            newRooms[i] = new Room(setName, shots, neighborNames, boardRoles);
        }

        NodeList trailers = root.getElementsByTagName("trailer");
        for (int i=0; i < trailers.getLength();i++){

            //reads data from the nodes
            Node trailer = trailers.item(i);
            String setName = "trailer";
            ArrayList<String> neighborNames = new ArrayList<>();
            if("trailer".equals(trailer.getNodeName()))
            {
                //System.out.println("Room: Trailer");

                //reads data

                NodeList children = trailer.getChildNodes();
                int count = 0;
                for (int j=0; j< children.getLength(); j++){
                    Node sub = children.item(j);

                    if("neighbors".equals(sub.getNodeName())){

                        NodeList neighbors = sub.getChildNodes();

                        for (int k = 0; k < neighbors.getLength(); k++)
                        {
                            Node neighbor = neighbors.item(k);

                            if("neighbor".equals(neighbor.getNodeName()))
                            {
                                String neighborName = neighbor.getAttributes().getNamedItem("name").getNodeValue();
                                neighborNames.add(neighborName);
                                //System.out.println(neighborName);
                            }
                        }

                    }
                    else if("area".equals(sub.getNodeName())){
                        String x, y, h, w;
                        x = sub.getAttributes().getNamedItem("x").getNodeValue();
                        y = sub.getAttributes().getNamedItem("y").getNodeValue();
                        h = sub.getAttributes().getNamedItem("h").getNodeValue();
                        w = sub.getAttributes().getNamedItem("w").getNodeValue();
                        //System.out.println(x + " " + y + " " + h + " " + w);
                    }
                }

                newRooms[setCount++] = new Room(setName, neighborNames);
                //System.out.println("\n");
            }
        }

        NodeList offices = root.getElementsByTagName("office");
        for (int i=0; i < offices.getLength();i++){

            //reads data from the nodes
            Node office = offices.item(i);
            String setName = "office";
            ArrayList<String> neighborNames = new ArrayList<>();
            if("office".equals(office.getNodeName()))
            {
                //System.out.println("Room: Office");

                //reads data

                NodeList children = office.getChildNodes();
                int count = 0;
                for (int j=0; j< children.getLength(); j++){
                    Node sub = children.item(j);

                    if("neighbors".equals(sub.getNodeName())){

                        NodeList neighbors = sub.getChildNodes();

                        for (int k = 0; k < neighbors.getLength(); k++)
                        {
                            Node neighbor = neighbors.item(k);

                            if("neighbor".equals(neighbor.getNodeName()))
                            {
                                String neighborName = neighbor.getAttributes().getNamedItem("name").getNodeValue();
                                neighborNames.add(neighborName);
                                //System.out.println(neighborName);
                            }
                        }

                    }
                    else if("area".equals(sub.getNodeName())){
                        String x, y, h, w;
                        x = sub.getAttributes().getNamedItem("x").getNodeValue();
                        y = sub.getAttributes().getNamedItem("y").getNodeValue();
                        h = sub.getAttributes().getNamedItem("h").getNodeValue();
                        w = sub.getAttributes().getNamedItem("w").getNodeValue();
                        //System.out.println(x + " " + y + " " + h + " " + w);
                    }
                }
                newRooms[setCount++] = new Room(setName, neighborNames);
            }
        }
        return newRooms;
    }




    public int[][] parseUpgrades(Document d){

        Element root = d.getDocumentElement();

        NodeList sets = root.getElementsByTagName("set");
        int roomNum = sets.getLength();

        int officeUpgrades[][] = new int[2][5];


        
        NodeList offices = root.getElementsByTagName("office");
        for (int i=0; i < offices.getLength();i++){

            //reads data from the nodes
            Node office = offices.item(i);
            String setName = "office";
            ArrayList<String> neighborNames = new ArrayList<>();
            if("office".equals(office.getNodeName()))
            {
                NodeList children = office.getChildNodes();
                int count = 0;
                for (int j=0; j< children.getLength(); j++){
                    Node sub = children.item(j);
                    if("upgrades".equals(sub.getNodeName())){
                        NodeList upgrades = sub.getChildNodes();
                        for (int k = 0; k < upgrades.getLength(); k++)
                        {
                            Node upgrade = upgrades.item(k);

                            if("upgrade".equals(upgrade.getNodeName()))
                            {
                                int level = Integer.parseInt(upgrade.getAttributes().getNamedItem("level").getNodeValue());
                                String currency = upgrade.getAttributes().getNamedItem("currency").getNodeValue();
                                int amt = Integer.parseInt(upgrade.getAttributes().getNamedItem("amt").getNodeValue());
                                //System.out.println("Upgrade lvl: " + level + " currency: " + currency + " amt: " + amt);

                                if("dollar".equals(currency))
                                {
                                    officeUpgrades[0][level-2] = amt;
                                }else if("credit".equals(currency))
                                {
                                    officeUpgrades[1][level-2] = amt;
                                }

                                String x, y, h, w;
                                NodeList subparts = upgrade.getChildNodes();
                                for(int l = 0; l < subparts.getLength(); l++)
                                {
                                    Node partinfo = subparts.item(l);
                                    if("area".equals(partinfo.getNodeName()))
                                    {
                                        x = partinfo.getAttributes().getNamedItem("x").getNodeValue();
                                        y = partinfo.getAttributes().getNamedItem("y").getNodeValue();
                                        h = partinfo.getAttributes().getNamedItem("h").getNodeValue();
                                        w = partinfo.getAttributes().getNamedItem("w").getNodeValue();
                                        //System.out.println("Part area: " + x + " " + y + " " + h + " " + w);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return officeUpgrades;
    }
}
