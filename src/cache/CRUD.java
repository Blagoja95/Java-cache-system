package cache;

public interface CRUD {
     void create(String[] commands);
     void read(String[] commands);
    void update(String[] commands);
    void delete(String target);
}
