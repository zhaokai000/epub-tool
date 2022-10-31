package com.cell.epub.tool.util;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.OutputStream;

/**
 * @author zhaokai
 * @date 2022-10-30
 */
public class DocumentUtil {

    private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private static DocumentBuilder db;

    // 创建TransformerFactory对象
    private static final TransformerFactory tff = TransformerFactory.newInstance();
    // 创建 Transformer对象
    private static Transformer tf;

    static {
        try {
            db = factory.newDocumentBuilder();
            tf = tff.newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static Document createDocument() {
        Document document = db.newDocument();
        document.setXmlStandalone(true);
        return document;
    }

    public static void save(Document document, String path) {
        try {
            tf.transform(new DOMSource(document), new StreamResult(new File(path)));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void save(Document document, OutputStream os) throws TransformerException {
        tf.transform(new DOMSource(document), new StreamResult(os));
    }

}
