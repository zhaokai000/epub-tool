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
 * The object model for file named toc.xhtml.
 * @author zhaokai
 * @since 0.0.1
 */
public class TocXhtml implements DocumentWriter {

    private final Document document;

    public TocXhtml(List<Chapter> chapters) {
        this.document = DocumentUtil.createDocument();

        Element html = this.document.createElement("html");
        html.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        html.setAttribute("xmlns:epub", "http://www.idpf.org/2007/ops");

        addBody(chapters, html);

        this.document.appendChild(html);
    }

    private void addBody(List<Chapter> chapters, Element html) {
        Element body = this.document.createElement("body");

        addNav(chapters, body);

        html.appendChild(body);
    }

    private void addNav(List<Chapter> chapters, Element body) {
        Element nav = this.document.createElement("nav");
        nav.setAttribute("epub:type", "toc");

        addOl(chapters, nav);

        body.appendChild(nav);
    }

    private void addOl(List<Chapter> chapters, Element nav) {
        Element ol = this.document.createElement("ol");

        for (Chapter chapter : chapters) {
            addLi(chapter, ol);
        }

        nav.appendChild(ol);
    }

    private void addLi(Chapter chapter, Element ol) {
        Element li = this.document.createElement("li");

        Element a = this.document.createElement("a");
        a.setAttribute("href", "sections/" + chapter.getName());
        a.setTextContent(chapter.getTitle());

        li.appendChild(a);
        ol.appendChild(li);
    }

    @Override
    public void write(ZipOutputStream zipOutputStream) throws IOException, TransformerException {
        zipOutputStream.putNextEntry(new ZipEntry("OEBPS/toc.xhtml"));
        DocumentUtil.save(this.document, zipOutputStream);
    }
}
