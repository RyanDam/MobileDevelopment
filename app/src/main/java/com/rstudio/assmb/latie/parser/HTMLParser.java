package com.rstudio.assmb.latie.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HTMLParser {
    private Document htmlDoc;
    private Element content;

    public HTMLParser() {
        this.htmlDoc = Jsoup.parse("<html> </html>");
        this.content = null;
    }

    public HTMLParser(String htmlStr) {
        this.htmlDoc = Jsoup.parse(htmlStr);
        this.content = this.findNodeContent();
    }

    public void setContent() {
        if (this.content == null)
            return;
        this.content = this.findNodeContent();
    }

    public String getTitle() {
        return this.htmlDoc.title();
    }

    public void setHtmlLink(String url) {
        try {
            this.htmlDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getContent() {
        setContent();
        if (this.content == null)
            return "";
        else {

            return "<html>" +
                    "<head>" +
                        "<title>" + this.getTitle() + "</title>" +
                        "<style> img { width: 100%; height: auto; }  * { color: #202020; } </style>" +
                    "</head>" +
                    "<body>" +
                    content.html() +
                    "</body>" +
                    "</html>";
        }
    }

    public String getParagraph() {
        setContent();
        if (this.content == null)
            return "";
        else {
            if (this.content.select("p").first() == null)
                return "";
            return this.content.select("p").first().text();
        }
    }

    public String getImageUrl() {
        setContent();
        if (this.content == null)
            return "";
        else {
            Elements images = content.select("img");
            if (images.first() == null)
                return "";
            return images.first().attr("src");
        }
    }

    private int countParagraph(Element element) {
        final int thresholdParagraph = 30;

        Elements paragraphs = element.select("> p");

        int count = 0;
        for (Element paragraph : paragraphs) {
            if (paragraph.text().length() > thresholdParagraph)
                count ++;
        }

        return count;
    }

    private Element findNodeContent() {
        Elements divElements = htmlDoc.select("div");
        int maxParagraph = 0;
        Element div = null;

        for (Element element : divElements) {
            int paragraph = countParagraph(element);
            if (paragraph > maxParagraph) {
                maxParagraph = paragraph;
                div = element;
            }
        }

        return div;
    }
}
