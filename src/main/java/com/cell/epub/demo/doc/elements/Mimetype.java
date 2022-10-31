package com.cell.epub.demo.doc.elements;

import com.cell.epub.demo.doc.DocumentWriter;

import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The object model for mimetype file.
 * @author zhaokai
 * @since 0.0.1
 */
public class Mimetype implements DocumentWriter {

    /**
     * The content of file named mimetype in a ePub file is stationary.
     */
    private static final String TEXT = "application/epub+zip";

    @Override
    public void write(ZipOutputStream zipOutputStream) throws Exception {
        zipOutputStream.putNextEntry(new ZipEntry("mimetype"));
        zipOutputStream.write(TEXT.getBytes(StandardCharsets.UTF_8));
    }
}
