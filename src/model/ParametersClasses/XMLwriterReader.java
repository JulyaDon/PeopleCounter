package model.ParametersClasses;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import model.DataClasses.DataLogger;

import java.io.*;

/**
 * This class implements writing to XML and reading from it
 */
public class XMLwriterReader {
    XStream xstream = new XStream(new DomDriver());

    public void WriteParametres(Parameters Parameter) throws IOException {
        xstream.alias("Parameter", Parameters.class);
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter("resources/parameters.xml"));

        try {
            out.writeObject(Parameter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }

    public void WriteDataLog(DataLogger log) throws IOException {
        xstream.alias("DataLogger", DataLogger.class);
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter("resources/dataLog.xml"));

        try {
            out.writeObject(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }

    public DataLogger ReadDataLog() throws IOException{
        xstream.alias("DataLogger", DataLogger.class);

        ObjectInputStream in = null;
        try {
            in = xstream.createObjectInputStream(new FileReader("resources/dataLog.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataLogger newLog = null;
        try {
            newLog = (DataLogger) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return newLog;
    }
//
    public Parameters ReadParameters() throws IOException{
        xstream.alias("Parameter", Parameters.class);

        ObjectInputStream in = null;
        try {
            in = xstream.createObjectInputStream(new FileReader("resources/parameters.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parameters newParameter = null;
        try {
            newParameter = (Parameters) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return newParameter;
    }
}
