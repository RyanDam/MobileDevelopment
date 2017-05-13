package com.rstudio.assmb.latie.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HTMLParser {
    private Document htmlDoc;

    public HTMLParser() {
        this.htmlDoc = Jsoup.parse("<html> </html>");
    }

    public HTMLParser(String htmlStr) {
        this.htmlDoc = Jsoup.parse(htmlStr);
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
        Element content = findNodeContent();
        if (content == null)
            return "";
        else
            return content.text();
    }

    private int countParagraph(Element element) {
        final int thresholdParagraph = 20;

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
