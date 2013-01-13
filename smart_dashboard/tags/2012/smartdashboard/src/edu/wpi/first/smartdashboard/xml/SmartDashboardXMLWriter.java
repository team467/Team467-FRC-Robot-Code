package edu.wpi.first.smartdashboard.xml;

import java.awt.Dimension;
import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;

public class SmartDashboardXMLWriter {

    String fileName;
    FileWriter writer;

    public SmartDashboardXMLWriter(String fileName) throws IOException {
        this.fileName = fileName;
        writer = new FileWriter(fileName);
        writer.write("<?xml version=\"1.0\"?>\n");
        writer.write("<dashboard>\n");
    }

    public void beginStaticWidget(String className) throws IOException {
        writer.write("\t<static-widget class=\"" + className + "\">\n");
    }

    public void endStaticWidget() throws IOException {
        writer.write("\t</static-widget>\n");
    }

    public void beginWidget(String field, String className) throws IOException {
        writer.write("\t<widget field=\"" + field + "\" class=\"" + className + "\">\n");
    }

    public void beginWidget(String field, String type, String className) throws IOException {
        writer.write("\t<widget field=\"" + field + "\" type=\"" + type + "\" class=\"" + className + "\">\n");
    }

    public void endWidget() throws IOException {
        writer.write("\t</widget>\n");
    }

    public void addHiddenField(String field) throws IOException {
        writer.write("\t<hidden field=\"" + field + "\"/>\n");
    }

    public void addLocation(Point p) throws IOException {
        writer.write("\t\t<location x=\"" + p.x + "\" y=\"" + p.y + "\"/>\n");
    }

    public void addWidth(int width) throws IOException {
        writer.write("\t\t<width>" + width + "</width>\n");
    }

    public void addHeight(int height) throws IOException {
        writer.write("\t\t<height>" + height + "</height>\n");
    }

    public void addProperty(String name, String value) throws IOException {
        writer.write("\t\t<property name=\"" + name + "\" value=\"" + value + "\"/>\n");

    }

    public void close() throws IOException {
        writer.write("</dashboard>");
        writer.close();
    }
}
