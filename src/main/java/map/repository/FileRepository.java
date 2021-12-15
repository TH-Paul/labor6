package map.repository;


import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;

public abstract class FileRepository<T> extends InMemoryRepository<T>{
    protected File file;

    public FileRepository(){
        super();
    }

    /**
     * adds an entity to the matching repo
     * @param obj - to be added
     */
    @Override
    public T create(T obj) throws IOException {
        super.create(obj);
        writeToFile();
        return obj;
    }

    /**
     *
     * @param obj - to be deleted
     */
    @Override
    public void delete(T obj) throws IOException {
        super.delete(obj);
        writeToFile();
    }

    /**
     * writes the objects from the repo in the file
     */
    public void writeToFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(file, repoList);
    }


    public T update(T obj){
        return null;
    }

    /**
     * reads data from file and saves it into the matching repo
     */
    public abstract void loadFromFile() throws IOException;
}
