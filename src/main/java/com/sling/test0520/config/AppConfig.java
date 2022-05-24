package com.sling.test0520.config;

import com.core.epril.utils.DateUtils;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class AppConfig {


    @Bean
    public ModelMapper mapper() {
        ModelMapper mm = new ModelMapper();

        mm.addConverter(new AbstractConverter<LocalDateTime, String>() {
            @Override protected String convert(LocalDateTime source) {
                if (source == null) return "";
                return source.format(DateUtils.DATETIME_FORMATTER);
            }
        });

        mm.addConverter(new AbstractConverter<LocalDate, String>() {
            @Override protected String convert(LocalDate source) {
                if (source == null) return "";
                return source.format(DateUtils.DATE_FORMATTER);
            }
        });

        mm.addConverter(new AbstractConverter<String, LocalDate>() {
            @Override protected LocalDate convert(String source) {
                if (source == null) return null;
                return LocalDate.parse(source);
            }
        });

        return mm;
    }
}
