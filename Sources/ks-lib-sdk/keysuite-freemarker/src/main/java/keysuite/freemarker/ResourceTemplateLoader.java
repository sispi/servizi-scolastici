package keysuite.freemarker;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import it.kdm.orchestratore.utils.ResourceUtils;

import java.io.*;

public class ResourceTemplateLoader implements TemplateLoader {

    //final static String viewVar = "view";

    //String masterTemplateName;
    //String masterTemplate;
    String path;
    Configuration cfg;
    //Pattern suffix = null;

    public ResourceTemplateLoader(String path, Configuration cfg){
        this.path = path;
        this.cfg = cfg;
    }

    /*public void setMasterTemplate(String masterTemplateName){
        this.masterTemplateName = masterTemplateName;
    }*/

    @Override
    public Object findTemplateSource(String name) throws IOException {

        /*if (suffix==null){
            String country = Session.getRequest().getLocale().getCountry();
            String language = Session.getRequest().getLocale().getLanguage();

            String re = ".*_"+language+"(_"+country+")?\\.ftl";

            suffix = Pattern.compile(re, Pattern.CASE_INSENSITIVE);
        }*/

        if (!name.toLowerCase().endsWith(".ftl"))
            name += ".ftl";

        //if (suffix.matcher(name).find())
        //    return null;

        /*if (name.startsWith("@") && masterTemplateName!=null){
            if (masterTemplate==null){
                InputStream is = (InputStream) findTemplateSource(masterTemplateName);
                masterTemplate = StringUtils.toString(is);
            }
            String tmpl = "<#assign "+viewVar+"='"+name.substring(1)+"' />\n" + masterTemplate;
            return tmpl;
        }*/

        String fullpath = path + (path.endsWith("/") ? "" : "/") +  name;

        InputStream in = ResourceUtils.getResourceNoExc(fullpath);

        //if (in==null)
        //    throw new KSRuntimeException(Code.F404,"Template non trovato:"+fullpath).addDetail("template",fullpath);

        return in;


        /*InputStream is = KDMUtils.class.getClassLoader().getResourceAsStream(fullpath);

        if (is!=null && "true".equals(Session.getRequest().getParameter("export"))){

            File check = new File(KDMUtils.RSX_FOLDER,fullpath);
            if (!check.exists()){
                try{
                    FileUtils.copyInputStreamToFile(is, check);

                    if (cfg!=null){
                        TemplateLoader tl = cfg.getTemplateLoader();

                        if (tl instanceof MultiTemplateLoader){
                            ((MultiTemplateLoader)tl).setSticky(false);
                        }
                    }

                } catch (Exception io){
                    io.printStackTrace();
                } finally {
                    is = KDMUtils.class.getClassLoader().getResourceAsStream(fullpath);
                }
            }
        }

        return is;*/
    }

    @Override
    public long getLastModified(Object templateSource) {
        return 0;
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {

        if (templateSource instanceof String)
            return new StringReader( (String) templateSource );
        else
            return new InputStreamReader((InputStream) templateSource);
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {

        if (templateSource instanceof InputStream){
            ((InputStream) templateSource).close();
        }
    }

}
