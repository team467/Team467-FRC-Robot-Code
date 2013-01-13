package edu.wpi.first.smartdashboard.xml;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class SmartDashboardXMLReader {

    private List<XMLWidget> widgets = new ArrayList<XMLWidget>();
    private List<String> hiddenFields = new ArrayList<String>();
    private Map<String, String> properties = new HashMap<String, String>();
    private boolean finishedReading = false;

    private class ReaderThread extends Thread {

        File xmlFile;

        ReaderThread(String fileName) {
            super();

            xmlFile = new File(fileName);
            start();
        }

        @Override
        public void run() {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(xmlFile);
                doc.getDocumentElement().normalize();

                NodeList list = doc.getElementsByTagName("dashboard");

                if (list.getLength() == 0) {
                    return;
                }

                NodeList elements = list.item(0).getChildNodes();

                for (int e = 0; e < elements.getLength(); e++) {
                    boolean isWidget = elements.item(e).getNodeName().equals("widget");
                    if (isWidget || elements.item(e).getNodeName().equals("static-widget")) {
                        NamedNodeMap atrib = elements.item(e).getAttributes();
                        XMLWidget widget = new XMLWidget();
                        for (int a = 0; a < atrib.getLength(); a++) {
                            if (atrib.item(a).getNodeName().equals("field")) {
                                widget.setField(atrib.item(a).getNodeValue());
                            } else if (atrib.item(a).getNodeName().equals(
                                    "class")) {
                                widget.setClass(atrib.item(a).getNodeValue());
                            } else if (atrib.item(a).getNodeName().equals("type")) {
                                widget.setType(atrib.item(a).getNodeValue());
                            }
                        }

                        NodeList values = elements.item(e).getChildNodes();
                        for (int a = 0; a < values.getLength(); a++) {
                            if (values.item(a).getNodeName().trim().equals("location")) {
                                int x = 0, y = 0;
                                NamedNodeMap location = values.item(a).getAttributes();
                                for (int b = 0; b < location.getLength(); b++) {

                                    if (location.item(b).getNodeName().equals("x")) {
                                        x = Integer.parseInt(location.item(
                                                b).getNodeValue());
                                    } else if (location.item(b).getNodeName().equals("y")) {
                                        y = Integer.parseInt(location.item(
                                                b).getNodeValue());
                                    }
                                }
                                widget.setLocation(new Point(x, y));
                            } else if (values.item(a).getNodeName().trim().equals("width")) {
                                widget.setWidth(Integer.parseInt(values.item(a).getChildNodes().item(0).getNodeValue()));
                            } else if (values.item(a).getNodeName().trim().equals("height")) {
                                widget.setHeight(Integer.parseInt(values.item(a).getChildNodes().item(0).getNodeValue()));
                            } else if (values.item(a).getNodeName().trim().equals("property")) {
                                NamedNodeMap propAtribs = values.item(a).getAttributes();
                                String name = null, value = null;
                                for (int b = 0; b < propAtribs.getLength(); b++) {
                                    if (propAtribs.item(b).getNodeName().equals("name")) {
                                        name = propAtribs.item(b).getNodeValue();
                                    } else if (propAtribs.item(b).getNodeName().equals("value")) {
                                        value = propAtribs.item(b).getNodeValue();
                                    }
                                }
                                if (name != null && value != null) {
                                    widget.addProperty(name, value);
                                }
                            }
                        }

                        widgets.add(widget);
                    } else if (elements.item(e).getNodeName().equals("hidden")) {
                        NamedNodeMap atrib = elements.item(e).getAttributes();
                        for (int a = 0; a < atrib.getLength(); a++) {
                            if (atrib.item(a).getNodeName().equals("field")) {
                                hiddenFields.add(atrib.item(a).getNodeValue());
                            }
                        }
                    } else if (elements.item(e).getNodeName().equals("property")) {
                        NamedNodeMap propAtribs = elements.item(e).getAttributes();
                        String name = null, value = null;
                        for (int b = 0; b < propAtribs.getLength(); b++) {
                            if (propAtribs.item(b).getNodeName().equals("name")) {
                                name = propAtribs.item(b).getNodeValue();
                            } else if (propAtribs.item(b).getNodeName().equals("value")) {
                                value = propAtribs.item(b).getNodeValue();
                            }
                        }
                        if (name != null && value != null) {
                            properties.put(name, value);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            finishedReading = true;
        }
    }

    public SmartDashboardXMLReader(String fileName)
            throws FileNotFoundException {
        new ReaderThread(fileName);
    }

    private void waitToFinish() {
        while (!finishedReading) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public List<XMLWidget> getXMLWidgets() {
        waitToFinish();
        return widgets;
    }

    public List<String> getHiddenFields() {
        waitToFinish();
        return hiddenFields;
    }

    public Map<String, String> getProperties() {
        waitToFinish();
        return properties;
    }

    public boolean isFinishedReading() {
        return finishedReading;
    }
}
