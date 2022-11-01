package com.cell.epub.tool.doc.elements.oebps;

import com.cell.epub.tool.doc.BookInfo;
import com.cell.epub.tool.doc.Chapter;
import com.cell.epub.tool.doc.DocumentWriter;
import com.cell.epub.tool.util.DocumentUtil;
import com.cell.epub.tool.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The object model for file named content.opf.
 * @author zhaokai
 * @since 0.0.1
 */
public class Content implements DocumentWriter {

    private final Document document;
    private final BookInfo bookInfo;

    public Content(BookInfo bookInfo, List<Chapter> chapters) {
        this.bookInfo = bookInfo;
        this.document = DocumentUtil.createDocument();

        Element aPackage = this.document.createElement("package");
        aPackage.setAttribute("unique-identifier", "unique-identifier");
        aPackage.setAttribute("version", "3.0");
        aPackage.setAttribute("xmlns", "http://www.idpf.org/2007/opf");
        aPackage.setAttribute("xmlns:dc", "http://purl.org/dc/elements/1.1/");
        aPackage.setAttribute("xmlns:dcterms", "http://purl.org/dc/terms/");
        aPackage.setAttribute("xmlns:opf", "http://www.idpf.org/2007/opf");

        // add metadata element and children elements.
        addMetadata(aPackage);

        // add manifest element and children elements.
        addManifest(chapters, aPackage);

        // add spine element and children elements.
        addSpine(chapters, aPackage);

        this.document.appendChild(aPackage);
    }

    private void addSpine(List<Chapter> chapters, Element aPackage) {
        Element spine = this.document.createElement("spine");
        spine.setAttribute("toc", "toc.ncx");

        for (Chapter chapter : chapters) {
            addItemref(chapter, spine);
        }

        aPackage.appendChild(spine);
    }

    private void addItemref(Chapter chapter, Element spine) {
        Element itemref = this.document.createElement("itemref");
        itemref.setAttribute("idref", chapter.getName());

        spine.appendChild(itemref);
    }

    private void addManifest(List<Chapter> chapters, Element aPackage) {
        Element manifest = this.document.createElement("manifest");

        addItem("toc.ncx", "toc.ncx", "application/x-dtbncx+xml", null, manifest);
        addItem("toc.xhtml", "toc.xhtml", "application/xhtml+xml", "nav", manifest);

        for (Chapter chapter : chapters) {
            addItem("sections/" + chapter.getName(), chapter.getName(), "application/xhtml+xml", null, manifest);
        }

        aPackage.appendChild(manifest);
    }

    private void addItem(String href, String id, String mediaType, String properties, Element manifest) {
        Element item = this.document.createElement("item");

        item.setAttribute("href", href);
        item.setAttribute("id", id);
        item.setAttribute("media-type", mediaType);
        if (StringUtil.isNotBlank(properties)) {
            item.setAttribute("properties", properties);
        }

        manifest.appendChild(item);
    }

    private void addMetadata(Element aPackage) {
        Element metadata = this.document.createElement("metadata");

        BookInfo bookInfo = this.bookInfo;

        // identifier
        addIdentifier(metadata);

        // book name
        addTitle(bookInfo.getTitle(), metadata);

        // author
        addCreator(bookInfo.getCreator(), metadata);

        aPackage.appendChild(metadata);
    }

    private void addCreator(String author, Element metadata) {
        Element dcCreator = this.document.createElement("dc:creator");
        dcCreator.setTextContent(author);

        metadata.appendChild(dcCreator);
    }

    private void addTitle(String title, Element metadata) {
        Element dcTitle = this.document.createElement("dc:title");
        dcTitle.setTextContent(title);

        metadata.appendChild(dcTitle);
    }

    private void addIdentifier(Element metadata) {
        Element identifier = this.document.createElement("dc:identifier");
        identifier.setAttribute("id", "unique-identifier");
        identifier.setTextContent(UUID.randomUUID().toString());

        metadata.appendChild(identifier);
    }

    @Override
    public void write(ZipOutputStream zipOutputStream) throws IOException, TransformerException {
        zipOutputStream.putNextEntry(new ZipEntry("OEBPS/content.opf"));
        DocumentUtil.save(this.document, zipOutputStream);
    }
}
