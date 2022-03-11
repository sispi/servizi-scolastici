package keysuite.desktop;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource(value = {"file:${KEYSUITE_CONFIG}/system.properties"},ignoreResourceNotFound = false)
public class DesktopConfig implements WebMvcConfigurer {
}
