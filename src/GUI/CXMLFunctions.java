package GUI;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: pvonow
 * Date: 22.11.13
 * Time: 13:00
 * To change this template use File | Settings | File Templates.
 */
public class CXMLFunctions{

    public static void saveXMLFile(ArrayList<ZPolygon> zpolys){
        System.out.println("Save");
    try {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root element
        Document doc = docBuilder.newDocument();
        Element config = doc.createElement("Config");
        doc.appendChild(config);

        // world objects
        Element objList = doc.createElement("objList");
        config.appendChild(objList);

        //walkers
        Element walkerList = doc.createElement("walkerList");
        config.appendChild(walkerList);

        // world elements
        for(ZPolygon zpoly : zpolys){
            Element obj = doc.createElement("obj");
            objList.appendChild(obj);
            for(int i=0; i<zpoly.npoints; i++){
                Element point = doc.createElement("point");
                obj.appendChild(point);
                point.setAttribute("x", Integer.toString(zpoly.xpoints[i]));
                point.setAttribute("y", Integer.toString(zpoly.ypoints[i]));
           }
        }


        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("./XML/config.xml"));

        // Output to console for testing
        //StreamResult result = new StreamResult(System.out);

        transformer.transform(source, result);

        System.out.println("File saved!");

    } catch (ParserConfigurationException pce) {
        pce.printStackTrace();
    } catch (TransformerException tfe) {
        tfe.printStackTrace();
    }
}
}

