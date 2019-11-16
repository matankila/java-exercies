package il.co.ilrd.crud;

public interface CRUD<T, K> {
    K create(T entity);
    T read(K specialKey);
    void update(K specialKey, T entity);
    void delete(K specialKey);
}
