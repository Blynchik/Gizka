package project.gizka.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.gizka.dto.createDto.CreateBinaryContentDto;
import project.gizka.exception.validation.BinaryContentValidationException;
import project.gizka.model.BinaryContent;
import project.gizka.service.impl.BinaryContentService;
import project.gizka.validator.BinaryContentValidator;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/content")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    private  final BinaryContentValidator binaryContentValidator;

    @Autowired
    public BinaryContentController(BinaryContentService binaryContentService,
                                   BinaryContentValidator binaryContentValidator){
        this.binaryContentService = binaryContentService;
        this.binaryContentValidator = binaryContentValidator;
    }

    @PostMapping("/create")
    public ResponseEntity<BinaryContent> create(@Valid @RequestBody CreateBinaryContentDto contentDto,
                                                BindingResult bindingResult) throws IOException {
        checkForErrors(contentDto, bindingResult);
        BinaryContent createdContent = binaryContentService.create(contentDto.getPathToImage(), contentDto.getName());
        return ResponseEntity.ok(createdContent);
    }

    private void checkForErrors(CreateBinaryContentDto contentDto,
                                BindingResult bindingResult) {
        binaryContentValidator.validate(contentDto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            throw new BinaryContentValidationException(errorMessages);
        }
    }
}
