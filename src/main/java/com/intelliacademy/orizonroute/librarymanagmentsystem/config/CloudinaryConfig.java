package com.intelliacademy.orizonroute.librarymanagmentsystem.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dzzrwmoqv",
                "api_key", "793183582892856",
                "api_secret", "uzXHhD34GSNdkdD8WfMLotXJHug",
                "secure", true
        ));
    }
}
