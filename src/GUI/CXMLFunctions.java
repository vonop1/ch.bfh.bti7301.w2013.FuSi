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

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created with IntelliJ IDEA.
 * User: vonop1
 * Date: 22.11.13
 * Time: 13:00
 * To change this template use File | Settings | File Templates.
 */
public class CXMLFunctions{

    public static void loadXMLFile(){

        ArrayList<ZPolygon> zpolys = new ArrayList<ZPolygon>();

        try {

            File XMLFile = new File ("./XML/config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(XMLFile);

            //normalize: optional, but recommended (http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work)
            doc.getDocumentElement().normalize();

            //get WorldObjects
            NodeList objList = doc.getElementsByTagName("obj");

            //points of the polygons
            int npoints = 0;
            int[] xpoints = null;
            int[] ypoints = null;

            //get points of the object
            for(int i=0; i<objList.getLength(); i++){

                Node obj = objList.item(i);
                if (obj.getNodeType() == Node.ELEMENT_NODE) {
                    Element eObj = (Element) obj;
                    NodeList pointList = eObj.getElementsByTagName("point");

                    npoints = pointList.getLength();
                    xpoints = new int[npoints];
                    ypoints = new int[npoints];
                    for(int j=0; j<pointList.getLength(); j++){
                        Node point = pointList.item(j);
                        if (point.getNodeType() == Node.ELEMENT_NODE) {
                            Element ePoint = (Element) point;
                            xpoints[j] = Integer.parseInt(ePoint.getAttribute("x"));
                            ypoints[j] = Integer.parseInt(ePoint.getAttribute("y"));
                        }
                    }
                }
                if(npoints > 0){
                    ZPolygon zpoly = new ZPolygon(xpoints, ypoints, npoints);
                }
            }
            /*
            //set npoints, xpoints and ypoints
            npoints = objList.getLength();
            xpoints = new int[npoints];
            ypoints = new int[npoints];
            for(int i=0; i<npoints; i++){
                xpoints[i] = ;
                ypoints[i] = ;
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Saves the content of the world editor to a xml file
     * @param zpolys ArrayList with the polygons to save to the file
     */
    public static void saveXMLFile(ArrayList<ZPolygon> zpolys){

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

        // do the XML transformation
        transformer.transform(source, result);

        // give feedback
        System.out.println("File saved!");

    //catch exeptions
    } catch (ParserConfigurationException pce) {
        pce.printStackTrace();
    } catch (TransformerException tfe) {
        tfe.printStackTrace();
    }
}
}

