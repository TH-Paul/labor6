package repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class InMemoryRepository<T> implements ICrudRepository<T> {

    protected List<T> repoList;

    public InMemoryRepository() {
        this.repoList = new ArrayList<>();
    }


    /**
     *
     * @param obj - will be added to the repo
     * @return object that was added to the repo
     */
    @Override
    public T create(T obj) throws IOException {
        this.repoList.add(obj);
        return obj;
    }


    /**
     *
     * @return the list with all the objects
     */
    @Override
    public List<T> getAll() {
        return this.repoList;
    }

    /**
     *
     * @param obj - to be deleted
     */
    @Override
    public void delete(T obj) throws IOException {
        this.repoList.remove(obj);
    }

}
