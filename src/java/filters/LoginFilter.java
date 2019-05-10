package filters;

import beans.Login;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter implements Filter{
    
    @Inject
    private Login session;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest myRequest = (HttpServletRequest) request;
        HttpServletResponse myResponse = (HttpServletResponse) response;
        String url = myRequest.getRequestURI();
        
        System.out.println(toString());
        System.out.println("getRequestUrl: " + url);
        
        if(session == null || !session.isLoggedin()){
            if((url.indexOf("guestbook.xhtml")) >= 0){
                myResponse.sendRedirect(myRequest.getServletContext().getContextPath() + "/login.xhtml");
            }else{
                chain.doFilter(request, response);
            }
        }
        
        if(session == null || !session.isLoggedin()){
            if((url.indexOf("logout.xhtml")) >= 0){
                session.setLoggedin(false);
                myResponse.sendRedirect(myRequest.getServletContext().getContextPath() + "/index.xhtml");
            }else{
                chain.doFilter(request, response);
            }
        }
        
        if(session != null && session.isLoggedin()){
            if((url.indexOf("register.xhtml")) >= 0){
                myResponse.sendRedirect(myRequest.getServletContext().getContextPath() + "/guestbook.xhtml");
            }else{
                chain.doFilter(request, response);
            }
        }
        
        if(session != null && session.isLoggedin()){
            if((url.indexOf("login.xhtml")) >= 0){
                myResponse.sendRedirect(myRequest.getServletContext().getContextPath() + "/guestbook.xhtml");
            }else{
                chain.doFilter(request, response);
            }
        }
        
        if(session != null && session.isLoggedin()){
            if((url.indexOf("register.xhtml")) >= -1 && (url.indexOf("login.xhtml")) >= -1 && (url.indexOf("logout.xhtml")) >= 0){
                session.reset();
                myResponse.sendRedirect(myRequest.getServletContext().getContextPath() + "/index.xhtml");
            }
        }
    }
    
    @Override
    public String toString(){
        return "LoginFilter [getClass()=" + getClass() + "}";
    }

    @Override
    public void destroy() {
        
    }

}
