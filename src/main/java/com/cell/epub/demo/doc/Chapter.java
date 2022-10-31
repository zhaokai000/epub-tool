package com.cell.epub.demo.doc;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhaokai
 * @date 2022-10-31
 */
@Getter
@Setter
public class Chapter {

    /**
     * 章节名称
     */
    private String title;

    /**
     * 章节文件的名称
     */
    private String name;

    public Chapter(String title, String name) {
        this.title = title;
        this.name = name;
    }

}
