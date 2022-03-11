package keysuite.desktop.proxy;

import freemarker.template.TemplateException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import keysuite.desktop.security.ConfigAppBean;
import keysuite.freemarker.TemplateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyMenu extends HttpFilter {

  Map app;

  private static final Logger log = LoggerFactory.getLogger(ProxyMenu.class);

  public ProxyMenu(){
    super();
  }


  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain next) throws IOException, ServletException {
    String url = ((HttpServletRequest) req).getRequestURL().toString();
    app = ConfigAppBean.getApp(url);

    boolean isProxedApp = (app != null && !app.isEmpty() && app.containsKey("targetUri"));
    String content =  null;
    byte[] b = null;
    HttpEntity contentProxy = (HttpEntity) req.getAttribute("contentProxy");
    Header contentTypeProxy = contentProxy != null ? contentProxy.getContentType() : null;
    // (Header)req.getAttribute("contentTypeProxy");


    if(isProxedApp) {
      if (contentTypeProxy != null && contentTypeProxy.getValue().contains("html")) {
        content = getHtml(contentProxy.getContent());
        if (StringUtils.isNotEmpty(content)) {

          /*String menu = (String) mappa.get("menu");
          String header = (String) mappa.get("header");
          String footer = (String) mappa.get("footer");*/
          //content = appendbeaforeEndTag(content, "<h2>Test inject</h2>", "</body>");

//          String link = ((String)app.get("link")).endsWith("/") ? (String)app.get("link") : ((String)app.get("link")) + "/";
//
//          content = content.replace("src=\"/", "src=\""+link);
//          content = content.replace("href=\"/", "href=\""+link);
//          content = content.replace("srcset=\"/", "srcset=\""+link);

          /*if(StringUtils.isNotEmpty(header)) {
            header = "<!-- INIZIO SEZIONE HEADER -->" + header + "<!-- FINE SEZIONE HEADER -->";
            content = appendbeaforeEndTag(content, header, "</body>");
          }
          if(StringUtils.isNotEmpty(menu)) {
            menu = "<!-- INIZIO SEZIONE MENU -->" + menu + "<!-- FINE SEZIONE MENU -->";
            content = appendbeaforeEndTag(content, menu, "</body>");
          }

          if(StringUtils.isNotEmpty(footer)) {
            footer = "<!-- INIZIO SEZIONE FOOTER -->" + footer +"<!-- FINE SEZIONE FOOTER -->";
            content = appendbeaforeEndTag(content, footer, "</body>");
          }

          if(mappa.containsKey("extraHtml")){
            InputStream is = ResourceUtils.getResourceNoExc(mappa.get("extraHtml").toString());
            if(is != null) {
              StringWriter writer = new StringWriter();
              IOUtils.copy(is, writer, "UTF-8");
              String extraHtml = writer.toString();
              extraHtml = "<!-- INIZIO SEZIONE extraHtml -->" + extraHtml + "<!-- FINE SEZIONE ExtraHtml -->";
              content = appendbeaforeEndTag(content, extraHtml, "</body>");
            }
          }*/

          Map mappa = null; //(Map) ConfigAppBean.getBeanForApp(app, (HttpServletRequest) req);

          Map<String,Object> model = new HashMap<>();
          model.put("menu",mappa.get("menu"));
          model.put("header",mappa.get("header"));
          model.put("app",mappa);

          String template = (String) mappa.getOrDefault("template","proxy/proxy");

          try {
            String extra = TemplateUtils.ftlHandler(template, model);
            content = appendbeaforeEndTag(content, extra, "</body>");
          } catch (TemplateException e) {
            throw new RuntimeException(e);
          }

          b = content.getBytes(Charset.forName("UTF-8"));

          System.out.println(content);

          resp.setContentLength(b.length);
          resp.setContentType(contentTypeProxy.getValue());
          resp.getOutputStream().write(b);
          return;

        }
      }else {
        resp.setContentType(contentTypeProxy.getValue());
        contentProxy.writeTo(resp.getOutputStream());
        return;
      }
    }

//    if(contentTypeProxy != null && contentTypeProxy.getValue().contains("html")) {
//      if(isProxedApp) {
//
//        }
//
//      }
//    }else if (isProxedApp){
//
//    }
      next.doFilter(req, resp);
      return;
    }

  private String appendbeaforeEndTag(String document, String toAppend, String endTag){
    String result = document;

    if(document != null && document.contains(endTag)){
      if(toAppend != null && !toAppend.trim().equalsIgnoreCase("")){
        toAppend += endTag;
        result = result.replace(endTag,toAppend);
      }
    }

    return result;
  }

  private String getHtml(InputStream is){
    StringBuffer sb = new StringBuffer();
    try {

      BufferedReader br = new BufferedReader(new InputStreamReader(
              (is),"UTF-8"));


      String output;
      System.out.println("Output from Server .... \n");
      while ((output = br.readLine()) != null) {
        sb.append(output+"\n");

      }
    }catch (Exception e){

    }finally {
      try {
        is.close();
      }catch (Exception e1){

      }
    }

    return sb.toString();

  }

}
