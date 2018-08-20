package org.google.zxing.QR.util;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

/**
 * 
* @ClassName: QRCodeUtil
* @Description: 2D工具
* @author Chris
* @date 2018年8月16日 上午11:35:37
 */
public class QRCodeUtil {
	private static final int  HEIGHT=100;
	private static final int  WIDTH=300;
	private static final String CHARACTER="UTF-8";
	// 二维码尺寸
	private static final int DEFAULT_QRCODE_SIZE = 300;
	// LOGO宽度
	private static final int DEFAULT_LOGO_WIDTH = 50;
	// LOGO高度
	private static final int DEFAULT_LOGO_HEIGHT = 50;

	private static String FORMAT="jpg";
	/**
	 * 
	* @Title: createQRCode
	* @Description: 创建QRCode
	* @author Chris
	* @date  2018年8月16日 下午2:51:14
	* @param msg 内容
	* @return 
	* @throws WriterException
	 */
	public  static BitMatrix createQRCode(String msg) throws WriterException {
		QRCodeWriter qeCoed=new QRCodeWriter();
		Map<EncodeHintType,Object> hints =new HashMap<EncodeHintType,Object>();
		hints.put(EncodeHintType.CHARACTER_SET, CHARACTER);
		hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.L);
		 //创建比特矩阵(位矩阵)的QR码编码的字符串
		BitMatrix bitMatrix=qeCoed.encode(msg, BarcodeFormat.QR_CODE, DEFAULT_QRCODE_SIZE, DEFAULT_QRCODE_SIZE,hints);
		return bitMatrix;
	}
	/**
	 * 生成二维码图片
	 * 
	 * @param content
	 *            二维码包含内容
	 * @param QRcodeSize
	 *            正方形二维码图片的大小（单位：像素）	
	 * @throws Exception
	 */
    public static BufferedImage createImage(String content, int QRcodeSize) throws Exception {
    	
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, CHARACTER);
		hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.QR_CODE, DEFAULT_QRCODE_SIZE,DEFAULT_QRCODE_SIZE, hints);
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
						: 0xFFFFFFFF);
			}
		}		
		return image;
	}
    /**
	 * 向二维码图片中插入LOGO
	 * 
	 * @param source
	 *            二维码图片对象
	 * @param QRcodeSize
	 *            正方形二维码图片的大小（单位：像素）	
	 * @param logoImageFile
	 *            二维码图片文件对象
	 * @param logoNeedCompress
	 *            logo图片是否压缩         
	 * @param logoWidth
	 *            logo图片宽度
	 * @param logoHeight
	 *            logo图片高度
	 * @throws Exception
	 */
    public static void insertLogoImage(BufferedImage source, int QRcodeSize, File imgFile,
			boolean logoNeedCompress,int logoWidth, int logoHeight) throws Exception {
		Image src = ImageIO.read(imgFile);
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		if(logoWidth<=0)logoWidth=DEFAULT_LOGO_WIDTH;
		if(logoHeight<=0)logoHeight=DEFAULT_LOGO_HEIGHT;
		if (logoNeedCompress) { // 压缩LOGO
			if (width > logoWidth) {
				width = logoWidth;
			}
			if (height > logoHeight) {
				height = logoHeight;
			}
			Image image = src.getScaledInstance(width, height,
					Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			src = image;
		}
		// 插入LOGO
		Graphics2D graph = source.createGraphics();
		int x = (QRcodeSize - width) / 2;
		int y = (QRcodeSize - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		Shape shape = new java.awt.geom.RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}

	/**
	 * 
	* @Title: QRwrite
	* @Description: 输出2DCode
	* @author Chris
	* @date  2018年8月16日 下午3:03:54
	* @param msg内容
	* @param format 图像类型
	* @param stream 输出流
	* @param imgpath logo位置
	 * @throws Exception 
	 */
	public static void QRwrite(String msg,String format,OutputStream stream,String imgPath) throws Exception {
	
		BitMatrix bitMatrix= createQRCode(msg);
		FORMAT=format;
		if(imgPath!=null&&!"".equals(imgPath.toString())) {
			File codeFile=new File(imgPath);
	
			BufferedImage image=	QRCodeUtil.createImage(msg, DEFAULT_QRCODE_SIZE);
			
			QRCodeUtil.insertLogoImage(image, DEFAULT_QRCODE_SIZE, codeFile, true, DEFAULT_LOGO_WIDTH, DEFAULT_LOGO_HEIGHT);
			ImageIO.write(image, FORMAT, stream);
		}else {			
			MatrixToImageWriter.writeToStream(bitMatrix, FORMAT, stream);
		}
	}
}
