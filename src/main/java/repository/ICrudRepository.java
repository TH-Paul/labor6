package repository;

import java.io.IOException;
import java.util.List;

public interface ICrudRepository<T> {

    T create(T obj) throws IOException;

    List<T> getAll();

    T update(T obj);

    void delete(T obj) throws IOException;
}
