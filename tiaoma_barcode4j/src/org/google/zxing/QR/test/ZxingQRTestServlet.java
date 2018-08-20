package org.google.zxing.QR.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.google.zxing.QR.util.QRCodeUtil;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/**
 * Servlet implementation class ZxingQRTestServlet
 */
public class ZxingQRTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ZxingQRTestServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		String method=request.getParameter("method");
		if("show".equals(method)) {
			setQRCode(request, response);
		}else  if("download".equals(method)){
			try {
				getQRCode(request,response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}



	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	/**
	 * 
	* @Title: setQRCode
	* @Description: 页面显示
	* @author Chris
	* @date  2018年8月16日 下午6:03:24
	* @param request
	* @param response
	* @throws IOException
	 */
	public void setQRCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String msg=request.getParameter("msg");
		Properties pro = new Properties();
		  String path = request.getContextPath();
		   String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		   
		FileInputStream in = new FileInputStream(request.getRealPath("/")+"//image//merchantInfo.properties");
		pro.load(in);
		msg=pro.getProperty("txt");
		String format=request.getParameter("format");
		String imageUrl=request.getParameter("imageUrl");
		ServletOutputStream stream=	response.getOutputStream();
		try {
			if(imageUrl!=null&&!"".equals(imageUrl)) {
				String str=request.getRealPath("/") ;				
				QRCodeUtil.QRwrite(msg, format, stream,str+"//image//"+imageUrl);
			}else {
				QRCodeUtil.QRwrite(msg, format, stream,null);				
			}
			
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	* @Title: getQRCode
	* @Description:获得QRCode(下载)
	* @author Chris
	* @date  2018年8月16日 下午6:06:47
	* @param request
	* @param response
	 * @throws Exception 
	 */
	public void getQRCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html");
		javax.servlet.ServletOutputStream out = response.getOutputStream();
		String msg=request.getParameter("msg");
		Properties pro = new Properties();
		 String path = request.getContextPath();
		   String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		 
		   FileInputStream in = new FileInputStream(request.getRealPath("/")+"//image//merchantInfo.properties");
		pro.load(in);
		msg=pro.getProperty("txt");
		String format=request.getParameter("format");
		String imageUrl=request.getParameter("imageUrl");
		// 下载文件
		// 设置响应头和下载保存的文件名
			response.setContentType("application/x-msdownload");
			if(imageUrl!=null&&!"".equals(imageUrl)) {
				response.setHeader("Content-Disposition", "attachment; filename="+imageUrl);
				String str=request.getRealPath("/") ;				
				QRCodeUtil.QRwrite(msg, format, out,str+"//image//"+imageUrl);
			}else {
				response.setHeader("Content-Disposition", "attachment; filename="+imageUrl+".jpg");
				QRCodeUtil.QRwrite(msg, format, out,null);				
			}
			out.close();
	}
}
