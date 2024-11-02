package org.spring.aop.example.config;

import org.spring.aop.example.storage.FileEntity;
import org.spring.aop.example.storage.StorageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

@Component
public class DataLoader {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Bean
    public CommandLineRunner loadData(StorageRepository repository, ResourcePatternResolver resolver) throws IOException {
        return (args) -> {
            Random random = new Random();

            Resource[] resources = resolver.getResources("classpath:/data/*");

            for (Resource resource : resources) {
                if (!resource.isReadable() || resource.getFilename() == null) {
                    logger.info("Skipping " + resource.getFilename());
                    continue;
                }

                String extension = resource.getFilename().substring(resource.getFilename().lastIndexOf(".") + 1);

                Optional<MediaType> mimeTypeOptional = MediaTypeFactory.getMediaType(resource.getFilename());

                if (mimeTypeOptional.isEmpty()) {
                    logger.warning("Could not determine mime type for " + resource.getFilename());
                    continue;
                }

                String contentType = mimeTypeOptional.get().toString();

                List<String> options = List.of("inline", "attachment");
                String contentDisposition = options.get(random.nextInt(options.size()));

                FileEntity file = FileEntity.builder()
                        .filename(resource.getFilename())
                        .extension(extension)
                        .contentType(contentType)
                        .contentDisposition(contentDisposition)
                        .size(resource.contentLength())
                        .content(resource.getContentAsByteArray())
                        .build();

                repository.save(file);
            }

        };
    }
}
