package kohgylw.kiftd.server.configation;

import com.google.gson.Gson;
import kohgylw.kiftd.server.pojo.ExtendStores;
import kohgylw.kiftd.server.util.ConfigureReader;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <h2>Web功能-MVC相关配置类</h2>
 * <p>
 * 该Spring配置类主要负责配置kiftd网页服务器的处理行为。
 * </p>
 * 
 * @author 信运研创(ceicit)
 * @version 1.0
 */
@Configurable
@ComponentScan({ "kohgylw.kiftd.server.controller", "kohgylw.kiftd.server.service.impl", "kohgylw.kiftd.server.util" })
@ServletComponentScan({ "kohgylw.kiftd.server.listener", "kohgylw.kiftd.server.filter" })
@Import({ DataAccess.class })
public class MVC extends ResourceHttpRequestHandler implements WebMvcConfigurer {

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		// TODO 自动生成的方法存根
		configurer.enable();
	}

	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		// 将静态页面资源所在文件夹加入至资源路径中
		registry.addResourceHandler(new String[] { "/**" }).addResourceLocations(new String[] {
				"file:" + ConfigureReader.instance().getPath() + File.separator + "webContext" + File.separator });
		// 将所有文件块的保存路径加入至资源路径中（提供某些预览服务）
		List<String> paths = new ArrayList<>();
		paths.add("file:" + ConfigureReader.instance().getFileBlockPath());
		for (ExtendStores es : ConfigureReader.instance().getExtendStores()) {
			paths.add(
					"file:" + (es.getPath().getAbsolutePath().endsWith(File.separator) ? es.getPath().getAbsolutePath()
							: es.getPath().getAbsolutePath() + File.separator));
		}
		registry.addResourceHandler(new String[] { "/fileblocks/**" })
				.addResourceLocations(paths.toArray(new String[0]));
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		final MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(-1L);
		factory.setLocation(ConfigureReader.instance().getTemporaryfilePath());
		return factory.createMultipartConfig();
	}

	@Bean
	public Gson gson() {
		return new Gson();
	}
}
