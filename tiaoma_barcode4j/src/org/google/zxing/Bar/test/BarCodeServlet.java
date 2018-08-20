package org.google.zxing.Bar.test;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.google.zxing.Bar.util.BarCodeUtil;

import com.google.zxing.WriterException;

/**
 * Servlet implementation class BarCodeServlet
 */
public class BarCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BarCodeServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		//mehtod=show
		String method=	request.getParameter("mehtod");
		if("show".equals(method)) {
			try {
				showBarCode(request, response);
			} catch (WriterException e) {
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
	* @Title: showBarCode
	* @Description: 显示条形码
	* @author Chris
	* @date  2018年8月17日 上午11:35:40
	* @param request
	* @param response
	* @throws ServletException
	* @throws IOException
	* @throws WriterException
	 */
	public void showBarCode(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, WriterException{
		String msg=	request.getParameter("msg");
		String type=request.getParameter("type");
		String format=request.getParameter("format");
		String str=request.getParameter("imageStr");
		ServletOutputStream out= response.getOutputStream();
		BarCodeUtil.barwrite(msg, out, format, type,str);
	}
}
