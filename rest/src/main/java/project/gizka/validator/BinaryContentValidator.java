package project.gizka.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.gizka.dto.createDto.CreateBinaryContentDto;

@Component
public class BinaryContentValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CreateBinaryContentDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateBinaryContentDto contentDto = (CreateBinaryContentDto) target;
        String path = contentDto.getPathToImage();
        String type = path.substring(path.lastIndexOf(".") + 1);

        if (!path.isEmpty()) {
            if (!type.equals("jpg") && !type.equals("png") && !type.equals("jpeg")){
                errors.rejectValue("pathToImage", "", "Image type should be jpg, jpeg or png");
            }
        }
    }
}
