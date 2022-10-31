package com.cell.epub.tool.doc;

import com.cell.epub.tool.util.DocumentUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A section file.
 * @author zhaokai
 * @date 2022-10-30
 */
public class Section implements DocumentWriter {

    /**
     * The name of chapter file, the chars '%s' in it will be replaced with actual number, such as: chapter1.xhtml.
     */
    @Getter
    private final String sectionName;

    private final Document document;

    @Getter
    private final String title;

    public Section(String sectionName, String title, String content) {
        this.sectionName = sectionName;
        this.title = title;
        this.document = DocumentUtil.createDocument();
        Element html = this.document.createElement("html");
        html.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");

        addHead(html);
        addBody(content, html);

        this.document.appendChild(html);
    }

    private void addBody(String content, Element html) {
        Element body = this.document.createElement("body");
        body.setAttribute("xmlns:epub", "http://www.idpf.org/2007/ops");

        addTitle(body);
        parseContent(content, body);

        html.appendChild(body);
    }

    private void addTitle(Element body) {
        if (StringUtils.isNotBlank(this.title)) {
            Element h1 = this.document.createElement("h1");

            h1.setTextContent(this.title);

            body.appendChild(h1);
        }
    }

    private void parseContent(String content, Element body) {
        String[] contents = content.split("\n");
        for (String c : contents) {
            addP(c, body);
        }
    }

    private void addP(String content, Element body) {
        Element p = this.document.createElement("p");

        p.setTextContent(content.trim());

        body.appendChild(p);
    }

    private void addHead(Element html) {
        Element head = this.document.createElement("head");

        addMeta("text/html; charset=UTF-8", "content-type", head);

        html.appendChild(head);
    }

    private void addMeta(String content, String httpEquiv, Element head) {
        Element meta = this.document.createElement("meta");
        meta.setAttribute("content", content);
        meta.setAttribute("http-equiv", httpEquiv);

        head.appendChild(meta);
    }

    @Override
    public void write(ZipOutputStream zipOutputStream) throws Exception {
        zipOutputStream.putNextEntry(new ZipEntry("OEBPS/sections/" + this.sectionName));
        DocumentUtil.save(this.document, zipOutputStream);
    }
}
