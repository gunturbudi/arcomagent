/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlineshopagent;

import com.jaunt.*;
import com.jaunt.component.*;
import java.io.*;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guntur
 */
public class Scrap {

    private String urlSearch;
    private int query_id;
    private final String urlBhinneka = "http://www.bhinneka.com/search.aspx?Search=";

    public Scrap() {
    }

    public Scrap(String url) {
        try {
            urlSearch = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Scrap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setKeyword(String url) {
        try {
            urlSearch = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Scrap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setQueryId(int query_id) {
        this.query_id = query_id;
    }

    public void ScrapBhinneka() {
        try {
            Item items = new Item();
            items.site = "Bhinneka.com";
            System.out.println("==Scraping Start at " + urlSearch + " ==");

            UserAgent userAgent = new UserAgent();
            userAgent.visit(urlBhinneka + urlSearch);
            Elements titles = userAgent.doc.findEvery("<span class=\"prod-itm-fullname\">");
            Elements prices = userAgent.doc.findEvery("<div class=\"prod-itm-pricebox\">");
            Elements srcs = userAgent.doc.findEvery("<li class=\"prod-itm\">");
            String[] item_title = new String[100];
            double[][] item_price = new double[3][100];
            String[] item_img = new String[100];
            String[] item_src = new String[100];

            int i = 0;
            for (Element title : titles) {
                item_title[i] = title.getText();
                i++;
            }
            i = 0;
            for (Element src : srcs) {
                item_src[i] = src.getElement(0).getAt("href");
                item_img[i] = src.getElement(0).getElement(0).getAt("src");
                i++;
            }
            i = 0;
            for (Element price : prices) {
                String price_normal = price.getElement(0).getElement(0).getElement(0).getElement(0).getText();
                String price_discount;
                String discount;
                if ("Rp".equals(price_normal.trim())) {
                    price_normal = price.getElement(0).getElement(0).getElement(0).getElement(0).getElement(0).getText();
                    price_discount = price.getElement(0).getElement(0).getElement(0).getElement(1).getText();
                    discount = price.getElement(0).getElement(0).getElement(0).getElement(2).getElement(1).getText();

                    item_price[0][i] = Double.parseDouble(price_normal.replaceAll(",", "").replaceAll("Rp", "").trim());
                    item_price[1][i] = Double.parseDouble(price_discount.replaceAll(",", "").replaceAll("Rp", "").trim());
                    item_price[2][i] = Double.parseDouble(discount.replaceAll("-", "").replaceAll("%", "").trim());
                    items.stock = "Ada";
                } else if ("Call".equals(price_normal.replaceAll(",", "").replaceAll("Rp", "").trim())) {
                    item_price[0][i] = 0;
                    item_price[1][i] = 0;
                    item_price[2][i] = 0;
                    items.stock = "Call";
                } else {
                    item_price[0][i] = Double.parseDouble(price_normal.replaceAll(",", "").replaceAll("Rp", "").trim());
                    item_price[1][i] = Double.parseDouble(price_normal.replaceAll(",", "").replaceAll("Rp", "").trim());
                    item_price[2][i] = 0;
                    items.stock = "Ada";
                }
                i++;
            }

            int scraped = 0;
            for (int j = 0; j < item_title.length; j++) {
                if ("".equals(item_title[j].trim())) {
                    continue;
                }

                items.query_id = this.query_id;
                items.name = item_title[j];
                items.price = item_price[0][j];
                items.discount = item_price[2][j];
                items.price_discount = item_price[1][j];
                items.img_url = item_img[j];
                items.url = item_src[j];
                items.description = "";

                items.saveItem();
                scraped++;
            }
            System.out.println("Scraped in Bhinneka.com (" + urlSearch + ") = " + scraped);

        } catch (JauntException e) {
            //if title element isn't found or HTTP/connection error occurs, handle JauntException.
            System.out.println("Terdapat Error utk URL = " + urlBhinneka + urlSearch);
            System.err.println(e);
        }
    }
}
