package org.google.zxing.Bar.util;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 
* @ClassName: BarCodeUtil
* @Description:条形码生成类（zxing）
* @author Chris
* @date 2018年8月17日 上午10:35:28
 */
public class BarCodeUtil {
	private static final int WIDTH=300;
	private static final int HEIGHT=100;
	private static final String CHARACTER="UTF-8";
	/**
	 * 
	* @Title: createBarCode
	* @Description: 生成条形码
	* @author Chris
	* @date  2018年8月17日 上午10:46:03
	* @param msg 内容
	* @param type 规格型号
	* @return
	 * @throws WriterException 
	 */
	public static BitMatrix createBarCode(String msg,BarcodeFormat type) throws WriterException {
		BarcodeFormat format=type;
		Map<EncodeHintType, Object> hints=new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET,CHARACTER);
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.MARGIN, 1);
		//MultiFormatWriter工厂类
		if(type==null) format=BarcodeFormat.CODE_128;
		BitMatrix matrix=new MultiFormatWriter().encode(msg, format, WIDTH, HEIGHT, hints);
		
		return matrix;
	}
	/**
	 * 
	* @Title: createBarCodeImage
	* @Description: 创建一维码的图像
	* @author Chris
	* @date  2018年8月17日 下午2:19:17
	* @param content 内容
	* @param type  规格型号
	* @return
	* @throws WriterException
	 */
	public static BufferedImage createBarCodeImage(String content,BarcodeFormat type) throws WriterException {
		BarcodeFormat format=type;
		if(format==null)format=BarcodeFormat.CODE_128;
		Map<EncodeHintType,Object> hints=new HashMap<EncodeHintType,Object>();
		hints.put(EncodeHintType.CHARACTER_SET,CHARACTER);
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.MARGIN,1);
		BitMatrix bitMatrix=	new MultiFormatWriter().encode(content, format, WIDTH, HEIGHT,hints);
		int width=	bitMatrix.getWidth();
		int height=bitMatrix.getHeight();
		BufferedImage image=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int x=0;x<width;x++) {
			for(int y=0;y<height;y++) {
				image.setRGB(x, y, bitMatrix.get(x, y)?0xFF000000: 0xFFFFFFFF);
			}
		}
		return image;
	}

	public static void insertStringImage(BufferedImage image,String str) {
		Graphics2D g2D=image.createGraphics();
		 Font font = new Font("Serif", Font.BOLD, 10);     
		FontRenderContext context=	g2D.getFontRenderContext();  
		 Rectangle2D bounds = font.getStringBounds(str, context);   
		 	double x = image.getWidth() / 2;     
	        double y = image.getHeight()+100;
	        double ascent = -bounds.getY();     
	        double baseY = y + ascent;     
	             
	        g2D.drawString(str, (int)x, (int)baseY);  
//		int x= image.getWidth()/2;
//		int y=image.getHeight()+100;
//		int width=50;
//		int height=20;
//		g2D.drawString(str, x, y);
//		Shape shape = new java.awt.geom.RoundRectangle2D.Float(x, y, width, height, 6, 6);
//		g2D.setStroke(new BasicStroke(3f));
//		g2D.draw(shape);
//		g2D.dispose();
	}
	
	
	/**
	 * 
	* @Title: barwrite
	* @Description:条形码输出
	* @author Chris
	* @date  2018年8月17日 上午11:10:32
	* @param msg 内容
	* @param out 输出流
	* @param format 规格型号
	* @param type  图像类型
	* @param imageStr 图像文字
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public static void barwrite(String msg,OutputStream out,String format,String type,String imageStr) throws WriterException, IOException {
		BarcodeFormat barFormat=BarcodeFormat.valueOf(format);
		if(imageStr!=null&&!"".equals(imageStr)) {
		
			BufferedImage image=	BarCodeUtil.createBarCodeImage(msg, barFormat);
			BarCodeUtil.insertStringImage(image, imageStr);
			ImageIO.write(image, type,out);
		}else {
			BitMatrix matrix=	BarCodeUtil.createBarCode(msg, barFormat);
			//用javase工具类
			MatrixToImageWriter.writeToStream(matrix, type, out);
		}
		
	}
}
