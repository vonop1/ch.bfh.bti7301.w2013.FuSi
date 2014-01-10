package GUI;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import Source.CObstacle;
import Source.CWalker;
import Source.CWorld;
import Util.CPosition;
import org.w3c.dom.*;

/**
 * Created with IntelliJ IDEA.
 * User: vonop1
 * Date: 22.11.13
 * Time: 13:00
 * To change this template use File | Settings | File Templates.
 */
public class CXMLConfigFile {

    /**
     * loads a xml file into the world editor
     * @return returns an arraylist with the polygons from the xml file
     */
    public void loadXMLFile(ArrayList<CPolygon> cPolys, ArrayList<CEditorWalker> cWalkers){

        File XMLFile = getJFileChooserDialogFile("Open");
        if(XMLFile != null){
            try {
                //get file and parse it
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(XMLFile);

                //normalize: optional, but recommended (http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work)
                doc.getDocumentElement().normalize();

                //get WorldObjects
                NodeList objList = doc.getElementsByTagName("obj");
                NodeList walkerList = doc.getElementsByTagName("walker");

                //points of the polygons
                int npoints = 0;
                int[] xpoints = null;
                int[] ypoints = null;

                //points and count of the walkers
                int walkers = 0;
                CPosition startPoint = null;
                CPosition targetPoint = null;
                int count = 1;

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
                        CPolygon zpoly = new CPolygon(xpoints, ypoints, npoints);
                        cPolys.add(zpoly);
                    }
                }

                //get points of the walkers
                for(int i=0; i<walkerList.getLength(); i++){
                    Node walker = walkerList.item(i);

                    if (walker.getNodeType() == Node.ELEMENT_NODE) {
                        Element eWalker = (Element) walker;
                        NodeList sourceList = eWalker.getElementsByTagName("source");
                        NodeList targetList = eWalker.getElementsByTagName("target");
                        NodeList countList = eWalker.getElementsByTagName("count");
                        walkers = sourceList.getLength();
                        if(countList.getLength() != 0){
                            for(int j=0; j<countList.getLength(); j++){
                                Node point = countList.item(j);
                                if (point.getNodeType() == Node.ELEMENT_NODE) {
                                    Element ePoint = (Element) point;
                                    count = Integer.parseInt(ePoint.getAttribute("c"));
                                }
                            }
                        }else{
                            count = 1;
                        }

                        for(int j=0; j<sourceList.getLength(); j++){
                            Node point = sourceList.item(j);
                            if (point.getNodeType() == Node.ELEMENT_NODE) {
                                Element ePoint = (Element) point;
                                startPoint = new CPosition(Integer.parseInt(ePoint.getAttribute("x")),Integer.parseInt(ePoint.getAttribute("y")));
                            }
                        }
                        for(int j=0; j<targetList.getLength(); j++){
                            Node point = targetList.item(j);
                            if (point.getNodeType() == Node.ELEMENT_NODE) {
                                Element ePoint = (Element) point;
                                targetPoint = new CPosition(Integer.parseInt(ePoint.getAttribute("x")),Integer.parseInt(ePoint.getAttribute("y")));
                            }
                        }
                    }

                    if(walkers > 0){
                        CEditorWalker cWalker = new CEditorWalker(startPoint, targetPoint, count);
                        cWalkers.add(cWalker);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  Saves the content of the world editor to a xml file
     * @param cPolys ArrayList with the polygons to save to the file
     */
    public void saveXMLFile(ArrayList<CPolygon> cPolys, ArrayList<CEditorWalker> cWalkers){
        File XMLFile = getJFileChooserDialogFile("Save");
        if(XMLFile != null){
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

                //walkers elements
                for (CEditorWalker cWalker : cWalkers){
                    Element walker = doc.createElement("walker");
                    walkerList.appendChild(walker);
                    Element source = doc.createElement("source");
                    Element target = doc.createElement("target");
                    Element count = doc.createElement("count");
                    walker.appendChild(source);
                    walker.appendChild(target);
                    walker.appendChild(count);
                    source.setAttribute("x", Integer.toString(cWalker.getPosition().getX().intValue()));
                    source.setAttribute("y", Integer.toString(cWalker.getPosition().getY().intValue()));
                    target.setAttribute("x", Integer.toString(cWalker.getTarget().getX().intValue()));
                    target.setAttribute("y", Integer.toString(cWalker.getTarget().getY().intValue()));
                    count.setAttribute("c", Integer.toString(cWalker.getCount()));
                }

                // world elements
                for(CPolygon zpoly : cPolys){
                    Element obj = doc.createElement("obj");
                    objList.appendChild(obj);
                    for(int i=0; i<zpoly.npoints; i++){
                        Element point = doc.createElement("point");
                        obj.appendChild(point);
                        point.setAttribute("x", Integer.toString(zpoly.xpoints[i]));
                        point.setAttribute("y", Integer.toString(zpoly.ypoints[i]));
                    }
                }


                // write the content into a xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(XMLFile);

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

    public File getJFileChooserDialogFile(String dialogType){

        // new customized JFileChooser object
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML-Files", "xml");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setCurrentDirectory(new File("./XML"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);

        int returnValue;
        if(dialogType.equals("Open")){
            returnValue = chooser.showOpenDialog(null);
        }else if(dialogType.equals("Save")){
            returnValue = chooser.showSaveDialog(null);
        } else {
            return null;
        }

        if(returnValue==JFileChooser.APPROVE_OPTION){
            return new File(chooser.getSelectedFile().getPath());
        } else {
            return null;
        }
    }

    public boolean loadConfig(File oConfigFile, CWorld simulationWorld) {
        if (oConfigFile == null || simulationWorld == null) {
            return false;
        }

        try {
            // load Config from File Config.xml
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document oConfigDoc = dBuilder.parse(oConfigFile);

            // Get a List off all Obstacles
            NodeList aoObj = oConfigDoc.getElementsByTagName("obj");

            for (int i = 0; i < aoObj.getLength(); i++) {
                Node oObj = aoObj.item(i);
                Vector<CPosition> aoPosition = new Vector<CPosition>();

                if (oObj.hasChildNodes()) {
                    // Get a List off all Points of a Obstacle
                    NodeList aoPoint = oObj.getChildNodes();
                    for (int j = 0; j < aoPoint.getLength(); j++) {
                        Node oPoint = aoPoint.item(j);

                        if (oPoint.getNodeType() == Node.ELEMENT_NODE) {
                            // Get Edge-points of the Obstacle
                            NamedNodeMap oPointAttributes = oPoint.getAttributes();

                            double dX = Integer.parseInt(oPointAttributes.getNamedItem("x").getNodeValue());
                            double dY = Integer.parseInt(oPointAttributes.getNamedItem("y").getNodeValue());
                            aoPosition.add(new CPosition(dX, dY));
                        }


                    }

                    simulationWorld.addObstacle(new CObstacle(aoPosition, simulationWorld));
                }

            }

            // Get a List off all Walkers
            NodeList walkers = oConfigDoc.getElementsByTagName("walker");

            for (int i = 0; i < walkers.getLength(); i++) {
                Node walker = walkers.item(i);

                if (walker.hasChildNodes()) {
                    // Get a List off all Points of a Obstacle
                    NodeList points = walker.getChildNodes();

                    Node point = points.item(0);
                    while (point.getNodeType() != Node.ELEMENT_NODE) {
                        //remove empty elements
                        point = point.getNextSibling();
                    }

                    // Get Source Point with X-Coordinate and Y-Coordinate
                    NamedNodeMap pointAttributes = point.getAttributes();

                    double dX = Integer.parseInt(pointAttributes.getNamedItem("x").getNodeValue());
                    double dY = Integer.parseInt(pointAttributes.getNamedItem("y").getNodeValue());

                    CPosition source = new CPosition(dX, dY);

                    point = point.getNextSibling();

                    while (point.getNodeType() != Node.ELEMENT_NODE) {
                        //remove empty elements
                        point = point.getNextSibling();
                    }

                    // Get Destination Point with X-Coordinate and Y-Coordinate
                    pointAttributes = point.getAttributes();

                    dX = Integer.parseInt(pointAttributes.getNamedItem("x").getNodeValue());
                    dY = Integer.parseInt(pointAttributes.getNamedItem("y").getNodeValue());

                    point = point.getNextSibling();

                    while (point.getNodeType() != Node.ELEMENT_NODE) {
                        //remove empty elements
                        point = point.getNextSibling();
                    }

                    Integer count = 1;
                    pointAttributes = point.getAttributes();
                    if( pointAttributes != null && pointAttributes.getNamedItem("c") != null && pointAttributes.getNamedItem("c").getNodeValue() != null) {
                        count = Integer.parseInt(pointAttributes.getNamedItem("c").getNodeValue());
                    }

                    CPosition destination = new CPosition(dX, dY);


                    simulationWorld.addWalker(source, destination, count);
                }
            }

            return true;
        } catch (Exception e) {
            System.out.print(e);
        }

        return false;
    }
}

