# BookInfo

Create a BookInfo object with build mode.

```java
BookInfo.builder()
    .title("文章名称")
    .creator("作者")
    .build();
```

# EpubDocument

```java
EpubDocument document = new EpubDocument(bookInfo);
document.addSection("第一章", "第一章内容...");
document.write(System.getProperty("user.home") + "/Desktop/result.epub");
```
