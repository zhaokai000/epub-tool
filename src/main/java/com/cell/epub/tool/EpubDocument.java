package com.cell.epub.tool;

import com.cell.epub.tool.doc.BookInfo;
import com.cell.epub.tool.doc.Chapter;
import com.cell.epub.tool.doc.Section;
import com.cell.epub.tool.doc.elements.MetaInfoContainer;
import com.cell.epub.tool.doc.elements.Mimetype;
import com.cell.epub.tool.doc.elements.oebps.Content;
import com.cell.epub.tool.doc.elements.oebps.TocNcx;
import com.cell.epub.tool.doc.elements.oebps.TocXhtml;
import lombok.Getter;
import lombok.Setter;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * A ePub document model.
 * @author zhaokai
 * @since 0.0.1
 */
public class EpubDocument {

    /**
     * The name of chapter file, the chars '%s' in it will be replaced with actual number, such as: chapter1.xhtml.
     */
    private static final String SECTION_NAME = "chapter%s.xhtml";

    /**
     * The number to record sort of chapter file.
     */
    private int no = 1;

    /**
     * The sorted list for chapter name and chapter file name.
     */
    @Getter
    private final List<Chapter> chapters = new ArrayList<>();

    /**
     * The sorted list for section.
     */
    private final List<Section> sections = new ArrayList<>();

    /**
     * The file named mimetype in ePub document.
     */
    private final Mimetype mimetype = new Mimetype();

    /**
     * The file named container.xml in ePub document.
     */
    private final MetaInfoContainer container = new MetaInfoContainer();

    /**
     * Some information for ePub document.
     */
    @Setter
    @Getter
    private BookInfo bookInfo;

    public EpubDocument(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public void write(String path) throws Exception {
        try (
            OutputStream os = new FileOutputStream(path)
        ) {
            write(os);
        }
    }

    public void write(OutputStream os) throws Exception {
        ZipOutputStream zipOutputStream = new ZipOutputStream(os);

        this.mimetype.write(zipOutputStream);
        this.container.write(zipOutputStream);

        TocNcx tocNcx = new TocNcx(this.chapters);
        tocNcx.write(zipOutputStream);

        TocXhtml tocXhtml = new TocXhtml(this.chapters);
        tocXhtml.write(zipOutputStream);

        Content content = new Content(this.bookInfo, this.chapters);
        content.write(zipOutputStream);

        for (Section section : this.sections) {
            section.write(zipOutputStream);
        }

        zipOutputStream.flush();
        zipOutputStream.close();
    }

    public void addSection(String title, String content) {
        String name = String.format(SECTION_NAME, no++);
        this.chapters.add(new Chapter(title, name));
        Section section = new Section(name, title, content);
        this.sections.add(section);
    }
}
