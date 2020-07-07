package com.immi;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProgressAsyncServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public ProgressAsyncServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		printWriter.println("<!DOCTYPE html><html><head><title>Progress Servlet</title></head><body>");
		printWriter.println("<p>entering doGet() ==> threadName :"+Thread.currentThread().getName()+"</p><br/>");
		printWriter.println("<progress id='progress' max='100'></progress>");
		AsyncContext asyncContext = request.startAsync();
		asyncContext.start(()->{
			printWriter.println("<p>InAsyncContext doGet() ==> threadName :"+Thread.currentThread().getName()+"</p><br/>");
			int i = 0;
			while (i <= 100) {
				printWriter.println("<script>document.getElementById('progress').value=\""+ i++ +"\"</script>");
				printWriter.flush();
				try {Thread.sleep(100);} 
				catch (InterruptedException e) {}
			}
			printWriter.println("<script>document.getElementById('progress').style.display='none';</script>");
			printWriter.println("Resolved<br/>");
			printWriter.println("<p>exiting doGet() ==> threadName :"+Thread.currentThread().getName()+"</p><br/>");
			printWriter.println("</body></html>");
			asyncContext.complete();
		});
	}

}
