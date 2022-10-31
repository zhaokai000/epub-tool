package com.cell.epub.tool.doc.elements.oebps;

import com.cell.epub.tool.doc.Chapter;
import com.cell.epub.tool.doc.DocumentWriter;
import com.cell.epub.tool.util.DocumentUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The object model for file named toc.ncx.
 * @author zhaokai
 * @since 0.0.1
 */
public class TocNcx implements DocumentWriter {

    private final Document document;

    public TocNcx(List<Chapter> chapters) {
        this.document = DocumentUtil.createDocument();

        Element ncx = this.document.createElement("ncx");
        ncx.setAttribute("version", "2005-1");
        ncx.setAttribute("xmlns", "http://www.daisy.org/z3986/2005/ncx/");

        addNavMap(chapters, ncx);

        this.document.appendChild(ncx);
    }

    private void addNavMap(List<Chapter> chapters, Element ncx) {
        Element navMap = this.document.createElement("navMap");

        for (int i = 0; i < chapters.size(); i++) {
            addNavPoint(chapters.get(i), String.valueOf(i + 1), navMap);
        }

        ncx.appendChild(navMap);
    }

    private void addNavPoint(Chapter chapter, String order, Element navMap) {
        Element navPoint = this.document.createElement("navPoint");
        navPoint.setAttribute("id", chapter.getName());
        navPoint.setAttribute("playOrder", order);

        addNavLabel(chapter.getTitle(), navPoint);
        addContent(chapter.getName(), navPoint);

        navMap.appendChild(navPoint);
    }

    private void addContent(String name, Element navPoint) {
        Element content = this.document.createElement("content");
        content.setAttribute("src", "sections/" + name);

        navPoint.appendChild(content);
    }

    private void addNavLabel(String name, Element navPoint) {
        Element navLabel = this.document.createElement("navLabel");

        Element text = this.document.createElement("text");
        text.setTextContent(name);

        navLabel.appendChild(text);
        navPoint.appendChild(navLabel);
    }

    @Override
    public void write(ZipOutputStream zipOutputStream) throws IOException, TransformerException {
        zipOutputStream.putNextEntry(new ZipEntry("OEBPS/toc.ncx"));
        DocumentUtil.save(this.document, zipOutputStream);
    }
}
