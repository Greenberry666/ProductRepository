package com.example.multi.module.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlUtils {
    public static String convertHtmlToText(String htmlContent) {
        return Jsoup.parse(htmlContent).text();
    }
}
