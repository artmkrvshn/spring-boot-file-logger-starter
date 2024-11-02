package org.spring.aop.example.storage;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageService {

    private final StorageRepository storageRepository;

    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public FileEntity findById(Long id) {
        return storageRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
    }

    public FileEntity save(FileEntity file) {
        return storageRepository.save(file);
    }

    public List<FileEntity> findAll() {
        return storageRepository.findAll();
    }
}
