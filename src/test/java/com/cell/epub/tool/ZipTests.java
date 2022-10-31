package com.cell.epub.tool;

import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author zhaokai
 * @date 2022-10-31
 */
public class ZipTests {

    @Test
    public void test01() {
        try (
            OutputStream os = new FileOutputStream("/Users/zhaokai/Desktop/result.zip");
            ZipOutputStream zipOutputStream = new ZipOutputStream(os)
        ) {
            zipOutputStream.putNextEntry(new ZipEntry("123/123.txt"));
            String content = "这个是文本文档!";
            zipOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
