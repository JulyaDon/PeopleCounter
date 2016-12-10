package sample;

import java.io.*;

/**
 * Created by July on 10.12.2016.
 */
public class XMLwriterReader {
    /*String address;
    public XMLwriterReader(String address){
        this.address = address;
    }
    XStream xstream = new XStream(new DomDriver());

    public void WriteFile(T object, Class c) throws IOException {
        xstream.alias(c.getClass().getName(), c);

        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter(this.address));

        try {
            out.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }

    public T ReadFile(Class c) throws IOException, ClassNotFoundException {
        xstream.alias(c.getClass().getName(), c);
        ObjectInputStream in = null;
        try {
            in = xstream.createObjectInputStream(new FileReader(address));
        } catch (IOException e) {
            e.printStackTrace();
        }
        T newObject = (T) in.readObject();

        return newObject;*/
}
