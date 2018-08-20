package org.krysalis.barcode4j.servlet;  
  
import java.awt.image.BufferedImage;  
import java.io.ByteArrayOutputStream;  
import java.io.IOException;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.xml.transform.Result;  
import javax.xml.transform.Source;  
import javax.xml.transform.Transformer;  
import javax.xml.transform.TransformerFactory;  
  
import org.apache.avalon.framework.configuration.Configuration;  
import org.apache.avalon.framework.configuration.DefaultConfiguration;  
import org.apache.avalon.framework.logger.ConsoleLogger;  
import org.apache.avalon.framework.logger.Logger;  
import org.krysalis.barcode4j.BarcodeGenerator;  
import org.krysalis.barcode4j.BarcodeUtil;  
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;  
import org.krysalis.barcode4j.output.eps.EPSCanvasProvider;  
import org.krysalis.barcode4j.output.svg.SVGCanvasProvider;  
import org.krysalis.barcode4j.tools.MimeTypes;  
  
/** 
 * Simple barcode servlet. 
 * 
 * @version $Id: BarcodeServlet.java,v 1.6 2007/01/19 12:26:55 jmaerki Exp $ 
 */  
public class BarcodeServlet extends HttpServlet {  
  
    /** 
     *  
     */  
    private static final long serialVersionUID = 1L;  
    /**要编码的信息 必须*/  
    public static final String BARCODE_MSG                 = "msg";  
    /** 条形码类型 必须 */  
    public static final String BARCODE_TYPE                = "type";  
    /**所需条码高度 非必须 */  
    public static final String BARCODE_HEIGHT              = "height";  
    /**条码的宽度 非必须 */  
    public static final String BARCODE_MODULE_WIDTH        = "mw";  
    /**所需的模块宽度（通常是窄模块的宽度）非必须*/  
    public static final String BARCODE_WIDE_FACTOR         = "wf";  
    /** 确定安静区域  非必须*/  
    public static final String BARCODE_QUIET_ZONE          = "qz";  
    /** 确定可读的部分的位置。可能值：“顶”、“底”或“无” 非必须*/  
    public static final String BARCODE_HUMAN_READABLE_POS  = "hrp";  
    /** 条形码的输出格式 默认为SVG 您可以使用MIME类型(如“Image/SVG+XML”)，也可以使用短格式类型(如“SVG”或“EPS”)。支持此servlet的输出格式有：SVG、EPS和任何受支持的位图格式。 */  
    public static final String BARCODE_FORMAT              = "fmt";  
    /**(仅适用于位图)确定位图的分辨率(以dpi(每英寸点)为单位)。默认为300 dpi */  
    public static final String BARCODE_IMAGE_RESOLUTION    = "res";  
    /**(仅适用于位图)确定是否应以单色(黑白)的灰度形式创建位图。有效值：“true”和“false”。 默认为false */  
    public static final String BARCODE_IMAGE_GRAYSCALE     = "gray";  
    /** 确定人类可读部分的字体大小 非必须 */  
    public static final String BARCODE_HUMAN_READABLE_SIZE = "hrsize";  
    /** 确定用于可读的部分的字体 非必须 */  
    public static final String BARCODE_HUMAN_READABLE_FONT = "hrfont";  
    /** 用于格式化人类可读消息的模式的参数名称  */  
    public static final String BARCODE_HUMAN_READABLE_PATTERN = "hrpattern";  
  
  
    private Logger log = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);  
  
    /** 
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest, HttpServletResponse) 
     */  
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  
                throws ServletException, IOException {  
        try {  
        	//获取类型
            String format = determineFormat(request);  
            int orientation = 0;  
            //得到类似xml文件
            Configuration cfg = buildCfg(request);  
            //获取请求的信息
            String msg = request.getParameter(BARCODE_MSG);  
            //默认请求信息
            if (msg == null) msg = "6935840910605";  
            //生成条形码的便利类，该方法返回便利类实例
            BarcodeUtil util = BarcodeUtil.getInstance();  
            //创建生成器
            BarcodeGenerator gen = util.createBarcodeGenerator(cfg); 
            ByteArrayOutputStream bout = new ByteArrayOutputStream(4096);  
            try {  
                if (format.equals(MimeTypes.MIME_SVG)) {  
                    //创建SVG，参数1：是否使用命名空间，参数2:条形码取向（0, 90, 180，270）
                    SVGCanvasProvider svg = new SVGCanvasProvider(false, orientation);  
                    //使用给定的画布生成条形码以将条形码呈现为其输出格式。
                    gen.generateBarcode(svg, msg);  
                    //返回包含SVG条形码的DOM片段
                    org.w3c.dom.DocumentFragment frag = svg.getDOMFragment();  
  
                    //SVG条码序列化  
                    TransformerFactory factory = TransformerFactory.newInstance();  
                    Transformer trans = factory.newTransformer();  
                    Source src = new javax.xml.transform.dom.DOMSource(frag);  
                    Result res = new javax.xml.transform.stream.StreamResult(bout);  
                    trans.transform(src, res);  
                } else if (format.equals(MimeTypes.MIME_EPS)) {  
                	//PostScript输出
                    EPSCanvasProvider eps = new EPSCanvasProvider(bout, orientation);  
                    gen.generateBarcode(eps, msg);  
                    eps.finish();  
                } else {  
                    String resText = request.getParameter(BARCODE_IMAGE_RESOLUTION);  
                    int resolution = 300; //dpi  
                    if (resText != null) {  
                        resolution = Integer.parseInt(resText);  
                    }  
                    if (resolution > 2400) {  
                        throw new IllegalArgumentException(  
                            "Resolutions above 2400dpi are not allowed");  
                    }  
                    if (resolution < 10) {  
                        throw new IllegalArgumentException(  
                            "Minimum resolution must be 10dpi");  
                    }  
                    String gray = request.getParameter(BARCODE_IMAGE_GRAYSCALE);  
                    //用于生成位图的CANVasvor实现
                    BitmapCanvasProvider bitmap = ("true".equalsIgnoreCase(gray)  ? new BitmapCanvasProvider(bout, format, resolution,BufferedImage.TYPE_BYTE_GRAY, true, orientation)  
                    		: new BitmapCanvasProvider(  
                                bout, format, resolution,  
                                BufferedImage.TYPE_BYTE_BINARY, false, orientation));  
                    gen.generateBarcode(bitmap, msg);  
                    bitmap.finish();  
                }  
            } finally {  
                bout.close();  
            }  
            response.setContentType(format);  
            response.setContentLength(bout.size());  
            response.getOutputStream().write(bout.toByteArray());  
            response.getOutputStream().flush();  
        } catch (Exception e) {  
            log.error("Error while generating barcode", e);  
            throw new ServletException(e);  
        } catch (Throwable t) {  
            log.error("Error while generating barcode", t);  
            throw new ServletException(t);  
        }  
    }  
  
    /** 
     * 检测输出的类型
     * @param request the request to use 
     * @return 返回所需的类型
     */  
    protected String determineFormat(HttpServletRequest request) { 
    	//判断请求指定的类型
        String format = request.getParameter(BARCODE_FORMAT);  
        //得到MIME类型
        format = MimeTypes.expandFormat(format); 
        //指定类型
        if (format == null) format = MimeTypes.MIME_JPEG;  
        return format;  
    }  
  
    /** 
     * 在请求中建立一个Avoron结构.该组件配置接口和基于XML的实现 
     * @param request the request to use 
     * @return the newly built COnfiguration object 
     * @todo Change to bean API 
     */  
    protected Configuration buildCfg(HttpServletRequest request) {  
    	//    	创建一个新的DefaultConfiguration
        DefaultConfiguration cfg = new DefaultConfiguration("barcode");  
        //获取请求类型
        String type = request.getParameter(BARCODE_TYPE); 
        //默认为code128
        if (type == null) type = "code128";  
        //创建一个新的DefaultConfiguration
        DefaultConfiguration child = new DefaultConfiguration(type);  
        
        cfg.addChild(child);  
        //Get additional attributes  
        DefaultConfiguration attr;  
        String height = request.getParameter(BARCODE_HEIGHT);  
        if (height != null) {  
            attr = new DefaultConfiguration("height");  
            attr.setValue(height);  
            child.addChild(attr);  
        }  
        String moduleWidth = request.getParameter(BARCODE_MODULE_WIDTH);  
        if (moduleWidth != null) {  
            attr = new DefaultConfiguration("module-width");  
            attr.setValue(moduleWidth);  
            child.addChild(attr);  
        }  
        String wideFactor = request.getParameter(BARCODE_WIDE_FACTOR);  
        if (wideFactor != null) {  
            attr = new DefaultConfiguration("wide-factor");  
            attr.setValue(wideFactor);  
            child.addChild(attr);  
        }  
        String quietZone = request.getParameter(BARCODE_QUIET_ZONE);  
        if (quietZone != null) {  
            attr = new DefaultConfiguration("quiet-zone");  
            if (quietZone.startsWith("disable")) {  
                attr.setAttribute("enabled", "false");  
            } else {  
                attr.setValue(quietZone);  
            }  
            child.addChild(attr);  
        }  
  
        // creating human readable configuration according to the new Barcode Element Mappings  
        // where the human-readable has children for font name, font size, placement and  
        // custom pattern.  
        String humanReadablePosition = request.getParameter(BARCODE_HUMAN_READABLE_POS);  
        String pattern = request.getParameter(BARCODE_HUMAN_READABLE_PATTERN);  
        String humanReadableSize = request.getParameter(BARCODE_HUMAN_READABLE_SIZE);  
        String humanReadableFont = request.getParameter(BARCODE_HUMAN_READABLE_FONT);  
  
        if (!((humanReadablePosition == null)  
                && (pattern == null)  
                && (humanReadableSize == null)  
                && (humanReadableFont == null))) {  
            attr = new DefaultConfiguration("human-readable");  
  
            DefaultConfiguration subAttr;  
            if (pattern != null) {  
                subAttr = new DefaultConfiguration("pattern");  
                subAttr.setValue(pattern);  
                attr.addChild(subAttr);  
            }  
            if (humanReadableSize != null) {  
                subAttr = new DefaultConfiguration("font-size");  
                subAttr.setValue(humanReadableSize);  
                attr.addChild(subAttr);  
            }  
            if (humanReadableFont != null) {  
                subAttr = new DefaultConfiguration("font-name");  
                subAttr.setValue(humanReadableFont);  
                attr.addChild(subAttr);  
            }  
            if (humanReadablePosition != null) {  
              subAttr = new DefaultConfiguration("placement");  
              subAttr.setValue(humanReadablePosition);  
              attr.addChild(subAttr);  
            }  
            child.addChild(attr);  
        }  
        return cfg;  
    }  
}  