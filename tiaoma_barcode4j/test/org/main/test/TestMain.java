package org.main.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Properties;

import org.eclipse.jdt.internal.compiler.batch.Main;

public class TestMain {
	public static void main(String[] args) throws IOException {
//		String string=TestMain.class.getClass().getResource("/tiaoma_barcode4j").getPath();
//		System.out.println(string);
//		Properties pro = new Properties();
//		FileInputStream in = new FileInputStream("D:\\EclipseWork\\tiaoma_barcode4j\\src\\merchantInfo.properties");
//		pro.load(in);
//		System.out.println(pro.getProperty("txt"));
//		URLEncoder.encode(s, enc);
		String str="%7B%22dimname10%22%3A%22%22%2C%22remark%22%3A%22%22%2C%22precost%22%3A0%2C%22dim10%22%3A%22%22%2C%22shortname%22%3A%22%22%2C%22dimname2%22%3A%22%E5%85%A8%E9%83%A8%E5%AD%A3%E8%8A%82%22%2C%22dim3%22%3A%22%22%2C%22dim2%22%3A%22000%22%2C%22dim7%22%3A%22031201%22%2C%22v_sku%22%3A%5B%7B%22sizename%22%3A%22%E5%9D%87%E7%A0%81%22%2C%22sizeid%22%3A%2299%22%2C%22colorname%22%3A%22%E5%9D%87%E8%89%B2%22%2C%22colorid%22%3A%22999%22%2C%22sku%22%3A%2200008154-001A99999%22%7D%5D%2C%22dim6%22%3A%22%22%2C%22dim5%22%3A%2203120103%22%2C%22dim4%22%3A%22%22%2C%22proname%22%3A%22%E6%A0%BC%E5%B1%80%22%2C%22dimname5%22%3A%22%E9%95%BF%E5%B7%BE%22%2C%22dimname6%22%3A%22%E6%A0%BC%E5%B1%80%22%2C%22dimname3%22%3A%22%22%2C%22dimname4%22%3A%22%22%2C%22dimname9%22%3A%22%22%2C%22dimname7%22%3A%22%E5%9B%B4%E5%B7%BE%22%2C%22dimname8%22%3A%22%22%2C%22product%22%3A%2200008154-001A%22%2C%22price%22%3A%222180%22%2C%22sizegroup%22%3A%22%22%2C%22brand%22%3A%22MARC+ROZIER%22%2C%22dim8%22%3A%22%22%2C%22dim9%22%3A%22%22%7D";
		System.out.println(URLDecoder.decode(str,"UTF-8"));
	}
}
