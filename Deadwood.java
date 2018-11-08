import org.w3c.dom.Document;

public class Deadwood {
    public static void main(String args[]) {
        String xmlFile = args[0];
        Scene[] sceneArr;

        Document doc;
        XMLParse parser = new XMLParse();
        try {
            doc = parser.getDocFromFile(xmlFile);
            sceneArr = parser.parseScenes(doc);

            System.out.println(sceneArr.length);
            for(int i = 0; i < sceneArr.length; i++) {
                System.out.println(sceneArr[i]);
            }
        } catch (Exception e) {
            System.out.println("Error = " + e);
        }
    }
}
