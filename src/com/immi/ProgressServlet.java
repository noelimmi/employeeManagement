package com.immi;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ProgressServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public ProgressServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		printWriter.println("<!DOCTYPE html><html><head><title>Progress Servlet</title></head><body>");
		printWriter.println("<p>entering doGet() ==> threadName :"+Thread.currentThread().getName()+"</p><br/>");
		new Thread(() -> {
			int i = 0;
			while (i <= 100) {
				System.out.println("New Thread ==> threadName :"+Thread.currentThread().getName());
				System.out.println(i);
				i++;
				try {Thread.sleep(100);} 
				catch (InterruptedException e) {}
			}
		}).start();
		printWriter.println("<p>exiting doGet() ==> threadName :"+Thread.currentThread().getName()+"</p><br/>");
		printWriter.println("</body></html>");
		printWriter.close();
	}

}
