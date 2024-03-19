package org.kft.sql.generator;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * file template
 *
 * @author author
 * @since 2024/3/14
 **/
public class FileTemplate {

    private final Configuration configuration;

    private FileTemplate(Configuration configuration) {
        this.configuration = configuration;
    }

    public static FileTemplate newInstance() {
        // 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是FreeMarker对于的版本号。
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

        // 第二步：设置模板文件加载器。
        // 需要指定类加载器为 Thread.currentThread().getContextClassLoader() 加载插件本身resources下的文件
        final String basePackagePath = "templates";
        configuration.setTemplateLoader(new ClassTemplateLoader(Thread.currentThread().getContextClassLoader(), basePackagePath));

        // 第三步：设置模板文件使用的字符集。一般就是utf-8.
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());

        return new FileTemplate(configuration);
    }

    public void setSharedVariable(String key, Object value) {
        try {
            configuration.setSharedVariable(key, value);
        } catch (TemplateModelException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(String templateFile, Object dataModel, File outFile) throws TemplateException, IOException {
        // 第四步：加载一个模板，创建一个模板对象。
        Template template = configuration.getTemplate(templateFile);

        // 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
        //Object dataModel = genOptions;

        // 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
        Writer writer = new FileWriter(outFile);

        // 第七步：调用模板对象的process方法输出文件。
        template.process(dataModel, writer);

        // 第八步：关闭流。
        writer.close();
    }
}
