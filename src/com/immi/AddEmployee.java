package com.immi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;



@WebServlet(asyncSupported = true, urlPatterns = { "/AddEmployee" })
@MultipartConfig(fileSizeThreshold=1024*1024*10, 
maxFileSize=1024*1024*50,      	
maxRequestSize=1024*1024*100) 
public class AddEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String UPLOAD_DIR = null;
    public AddEmployee() {
        super();
    }
    public void init(){
    	UPLOAD_DIR = getServletContext().getInitParameter("file-upload");
    	  File fileSaveDir = new File(UPLOAD_DIR);
          if (!fileSaveDir.exists()) {
              fileSaveDir.mkdirs();
          }
     }
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
    	
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName = null;
		String message = null;
		Collection<Part> pratList = request.getParts();
		Part firstPart = pratList.iterator().next();
		boolean isValid = isValidFile(firstPart.getHeader("content-disposition"));
		if(isValid) {
			for (Part part :pratList) {
				fileName = getFileName(part);
				part.write(UPLOAD_DIR + File.separator + fileName);
			}
			message = "{\"message\":\"file has been uploaded\"}";
			BackgroundController backgroundController = new BackgroundController(UPLOAD_DIR + File.separator + fileName);
			System.out.println("Old Thread ==> threadName :"+Thread.currentThread().getName());
			new Thread(() -> {
		    	try {
		    		System.out.println("New Thread ==> threadName :"+Thread.currentThread().getName());
					backgroundController.initAddEmployees();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}).start();
		}else	
			message =  "{\"message\":\"Only csv file is supported\"}";
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(message);
		out.close();
	}
	
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length()-1);
            }
        }
        return "";
    }
    
    private boolean isValidFile(String contentDisposition) {
        for (String token : contentDisposition.split(";")) {
            if (token.trim().startsWith("filename")) {
                String fileName =  token.substring(token.indexOf("=") + 2, token.length()-1);
                if(!fileName.substring(fileName.indexOf(".")+1).equals("csv")) 
                	return false;
                return true;
            }
        }
		return false;
	}
}
