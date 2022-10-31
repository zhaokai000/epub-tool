package com.cell.epub.demo;

import com.cell.epub.demo.doc.BookInfo;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhaokai
 * @date 2022-10-30
 */
public class MainClass {

    private static final String REGEX = "第[ ]*[0-9]+[ ]*章";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static void main(String[] args) throws Exception {
        BookInfo bookInfo = BookInfo.builder()
            .title("校园全能高手")
            .creator("安山狐狸")
            .build();
        EpubDocument document = new EpubDocument(bookInfo);

        String path = "/Users/zhaokai/Desktop/Temp/校园全能高手.txt";
        try (
            RandomAccessFile file = new RandomAccessFile(path, "r");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            byte[] buffer = new byte[2048];
            int length;
            while ((length = file.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            String gbk = byteArrayOutputStream.toString("GBK");
            addSections(gbk, document);
            document.write("/Users/zhaokai/Desktop/校园全能高手.epub");
        }
    }


    private static void addSections(String str, EpubDocument document) throws IOException {
        String[] contents = str.split("\n");
        // 标题
        String title = "";
        // 内容
        StringBuilder chapter = new StringBuilder();

        int length = contents.length;
        for (int i = 0; i < length; i++) {
            String content = contents[i];
            Matcher matcher = PATTERN.matcher(content);
            if (matcher.find()) {
                // 章节
                if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(chapter)) {
                    document.addSection(title, chapter.toString());
                    chapter.delete(0, chapter.length());
                }

                title = content.trim();
            } else {
                // 内容
                chapter.append(content).append("\n");
            }

            if (i == length - 1) {
                // 最后了
                if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(chapter)) {
                    document.addSection(title, chapter.toString());
                }
            }
        }
    }

}
