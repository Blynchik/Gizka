package project.gizka.service;


import java.util.List;
import java.util.Optional;

public interface CRUDService<T>{

    List<T> getAll();

    Optional<T> getById(Long id);

    T create(T entity);

    T update(Long id, T updatedEntity);

    void delete(Long id);
}
