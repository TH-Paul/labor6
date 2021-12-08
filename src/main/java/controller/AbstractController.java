package controller;

import repository.FileRepository;

import java.io.IOException;
import java.util.List;

public abstract class AbstractController<T> {
    protected FileRepository<T> repository;

    public AbstractController(FileRepository<T> repository) {
        this.repository = repository;
    }

    /**
     *
     * @return List with the objects from the repo
     */
    public List<T> obtainObjects(){
        return this.repository.getAll();
    }

    /**
     * adds an entity to the matching repo
     * @param object - to be added
     */
    public abstract void create(T object) throws IOException;


    /**
     *
     * @param object - to be deleted
     */
    public abstract void delete(T object) throws IOException;
}
