package project.gizka.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import project.gizka.model.BinaryContent;
import project.gizka.repository.BinaryContentRepo;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BinaryContentService{

    private final BinaryContentRepo binaryContentRepo;
    private final ResourceLoader resourceLoader;

    @Autowired
    public BinaryContentService(BinaryContentRepo binaryContentRepo,
                                ResourceLoader resourceLoader) {
        this.binaryContentRepo = binaryContentRepo;
        this.resourceLoader = resourceLoader;
    }

    public List<BinaryContent> getAll() {
        return binaryContentRepo.findAll();
    }

    public Optional<BinaryContent> getById(Long id) {
        return binaryContentRepo.findById(id);
    }

    @Transactional
    public BinaryContent create(String path, String name) throws IOException {
        BinaryContent binaryContent = new BinaryContent();
        binaryContent.setContentAsBytes(getBytes(path));
        binaryContent.setType(path.substring(path.lastIndexOf(".") + 1));
        binaryContent.setName(name);
        return binaryContentRepo.save(binaryContent);
    }

    @Transactional
    public BinaryContent update(Long id, String path, String name) throws IOException {
        BinaryContent binaryContent = binaryContentRepo.getReferenceById(id);
        BinaryContent updatedEntity = new BinaryContent();
        updatedEntity.setContentAsBytes(getBytes(path));
        updatedEntity.setType(path.substring(path.lastIndexOf(".") + 1));
        updatedEntity.setId(binaryContent.getId());
        updatedEntity.setName(name);
        return binaryContentRepo.save(updatedEntity);
    }

    public void delete(Long id) {
        binaryContentRepo.deleteById(id);
    }

    private byte[] getBytes(String path) throws IOException {
        Resource resource;

        if (path.startsWith("http")) {
            resource = new UrlResource(path);
        } else {
            resource = resourceLoader.getResource("file:" + path);
        }

        return StreamUtils.copyToByteArray(resource.getInputStream());
    }
}
