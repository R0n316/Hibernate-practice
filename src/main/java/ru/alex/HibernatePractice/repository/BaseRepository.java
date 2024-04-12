package ru.alex.HibernatePractice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Query;
import ru.alex.HibernatePractice.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseRepository<E extends BaseEntity<K>,K extends Serializable> implements Repository<E,K> {

    @Getter
    protected final EntityManager entityManager;
    private final Class<E> clazz;


    public Optional<E> get(K id, Map<String,Object> properties){
        return Optional.ofNullable(entityManager.find(clazz,id,properties));
    }

    @Override
    public List<E> getAll(Map<String,Object> properties) {
        CriteriaQuery<E> criteria = entityManager.getCriteriaBuilder().createQuery(clazz);
        Root<E> root = criteria.from(clazz);

        criteria.select(root);
        TypedQuery<E> query = entityManager.createQuery(criteria);
        for(Map.Entry<String,Object> entry: properties.entrySet()){
            query.setHint(entry.getKey(),entry.getValue());
        }
        return query.getResultList();
    }

    @Override
    public void save(E entity) {
        entityManager.persist(entity);
    }

    @Override
    public void update(E entity) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(E entity) {
        entityManager.remove(entity);
    }
}
