package com.cell.epub.demo.doc;

import java.util.zip.ZipOutputStream;

/**
 * A document writer to write document to ZipOutputStream.
 * @author zhaokai
 * @since 0.0.1
 */
public interface DocumentWriter {

    void write(ZipOutputStream zipOutputStream) throws Exception;

}
