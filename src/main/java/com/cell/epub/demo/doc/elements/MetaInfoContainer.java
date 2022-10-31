package com.cell.epub.demo.doc.elements;

import com.cell.epub.demo.doc.DocumentWriter;
import com.cell.epub.demo.util.DocumentUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The object model for container.xml.
 * @author zhaokai
 * @since 0.0.1
 */
public class MetaInfoContainer implements DocumentWriter {

    private final Document document;

    public MetaInfoContainer() {
        this.document = DocumentUtil.createDocument();
        Element container = this.document.createElement("container");
        container.setAttribute("version", "1.0");
        container.setAttribute("xmlns", "urn:oasis:names:tc:opendocument:xmlns:container");

        addRootfiles(container);

        this.document.appendChild(container);
    }

    private void addRootfiles(Element container) {
        Element rootfiles = this.document.createElement("rootfiles");

        addRootfile(rootfiles);

        container.appendChild(rootfiles);
    }

    private void addRootfile(Element rootfiles) {
        Element rootfile = this.document.createElement("rootfile");
        rootfile.setAttribute("full-path", "OEBPS/content.opf");
        rootfile.setAttribute("media-type", "application/oebps-package+xml");

        rootfiles.appendChild(rootfile);
    }

    @Override
    public void write(ZipOutputStream zipOutputStream) throws TransformerException, IOException {
        zipOutputStream.putNextEntry(new ZipEntry("META-INF/container.xml"));
        DocumentUtil.save(this.document, zipOutputStream);
    }
}
