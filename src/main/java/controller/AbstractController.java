package controller;

import repository.CommunicationDBRepository;
import repository.DBRepository;
import repository.ICrudRepository;

import java.io.IOException;
import java.util.List;

public abstract class AbstractController<T> {
    protected ICrudRepository<T> repository;
    protected CommunicationDBRepository communicationDBRepository;

    public AbstractController(ICrudRepository<T> repository) {
        this.repository = repository;
    }

    public AbstractController(ICrudRepository<T> repository, CommunicationDBRepository communicationDBRepository) {
        this.repository = repository;
        this.communicationDBRepository = communicationDBRepository;
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

    public abstract T findById(int id);

}
