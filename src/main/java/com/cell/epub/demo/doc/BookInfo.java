package com.cell.epub.demo.doc;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Some information for ePub document, such as: name, author, date
 * @author zhaokai
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
public class BookInfo {

    private String title;

    private String creator;

}
