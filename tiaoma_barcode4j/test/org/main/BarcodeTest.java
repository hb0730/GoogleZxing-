package org.main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

public class BarcodeTest {
	/**
	 * 
	* @Title: getFilePath
	* @Description: 获取生成的文件
	* @author Chris
	* @date  2018年8月15日 下午4:43:46
	* @param msg 条码内容
	* @param filePath 要生成的路径
	* @return
	 */
	public static File generateFile(String msg,String filePath) {
		File file=new File(filePath);
		try {
			generate(msg, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	/**
	 * 
	* @Title: generate
	* @Description: 将内容转成字节
	* @author Chris
	* @date  2018年8月15日 下午4:46:10
	* @param msg
	* @return
	 */
	public static byte[] generate(String msg) {
		ByteArrayOutputStream bout=new ByteArrayOutputStream();
		generate(msg, bout);
		return bout.toByteArray();
		
	}
	/**
	 * 
	* @Title: generate
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @author Chris
	* @date  2018年8月15日 下午4:48:13
	* @param msg
	* @param ous
	 */
	public static void generate(String msg, OutputStream ous) {
		if(ous==null||msg==null||"".equals(msg)) {
			return;
		}
		Code39Bean bean = new Code39Bean();
		// 精细度
        final int dpi = 150;
        // module宽度
        final double moduleWidth = UnitConv.in2mm(1.0f / dpi);

        // 配置对象
        bean.setModuleWidth(moduleWidth);
        bean.setWideFactor(3);
        bean.doQuietZone(false);

        String format = "image/png";
        try {

            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi,
                    BufferedImage.TYPE_BYTE_BINARY, false, 0);

            // 生成条形码
            bean.generateBarcode(canvas, msg);

            // 结束绘制
            canvas.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		String msg="12345678";
		String path = "C:/Users/12780/Desktop/barcode.png";
		generateFile(msg, path);
	}
}
