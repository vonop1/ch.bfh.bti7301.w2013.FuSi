package GUI;

import java.io.File;
import java.util.ArrayList;
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

import Source.CWalker;
import Util.CPosition;
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

    /**
     * loads a xml file into the world editor
     * @return returns an arraylist with the polygons from the xml file
     */
    public void loadXMLFile(ArrayList<CPolygon> cPolys, ArrayList<CWalker> cWalkers){

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
                NodeList walkerList = doc.getElementsByTagName("walkerList");


                //points of the polygons
                int npoints = 0;
                int[] xpoints = null;
                int[] ypoints = null;

                //points of the walkers
                int walkers = 0;
                CPosition startPoint = null;
                CPosition targetPoint = null;

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
                    if (walkerList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element eWalker = (Element) walkerList.item(i);

                        startPoint = null;
                        targetPoint = null;

                        NodeList sourceList = eWalker.getElementsByTagName("source");
                        for(int j=0; j<sourceList.getLength(); j++){
                            Node point = sourceList.item(j);
                            if (point.getNodeType() == Node.ELEMENT_NODE) {
                                Element ePoint = (Element) point;
                                startPoint = new CPosition(Integer.parseInt(ePoint.getAttribute("x")),Integer.parseInt(ePoint.getAttribute("y")));
                                break;
                            }
                        }

                        NodeList targetList = eWalker.getElementsByTagName("target");
                        for(int j=0; j<targetList.getLength(); j++){
                            Node point = targetList.item(j);
                            if (point.getNodeType() == Node.ELEMENT_NODE) {
                                Element ePoint = (Element) point;
                                targetPoint = new CPosition(Integer.parseInt(ePoint.getAttribute("x")),Integer.parseInt(ePoint.getAttribute("y")));
                                break;
                            }
                        }

                        if(startPoint != null && targetPoint != null){
                            cWalkers.add(new CWalker(startPoint, targetPoint, null));
                        }
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
    public void saveXMLFile(ArrayList<CPolygon> cPolys, ArrayList<CWalker> cWalkers){
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
                for (CWalker cWalker : cWalkers){
                    Element walker = doc.createElement("walker");
                    walkerList.appendChild(walker);
                    Element source = doc.createElement("source");
                    Element target = doc.createElement("target");
                    walker.appendChild(source);
                    walker.appendChild(target);
                    source.setAttribute("x", Integer.toString(cWalker.getPosition().getX().intValue()));
                    source.setAttribute("y", Integer.toString(cWalker.getPosition().getY().intValue()));
                    target.setAttribute("x", Integer.toString(cWalker.getTarget().getX().intValue()));
                    target.setAttribute("y", Integer.toString(cWalker.getTarget().getY().intValue()));
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
}

