package com.skytouch.task.pagecontrollers.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "restEndpoints")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagePropertiesConfiguration {

    private String listProductsUri;
    private String deleteProductUri;
    private String findProductByIdUri;
    private String updateProductUri;
    private String restockInvoicesUri;
    private String addProductUrl;
}
