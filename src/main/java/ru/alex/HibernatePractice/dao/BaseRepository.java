package ru.alex.HibernatePractice.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import ru.alex.HibernatePractice.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<E extends BaseEntity<K>,K extends Serializable> implements Repository<E,K> {

    protected final EntityManager entityManager;
    private final Class<E> clazz;

    @Override
    public Optional<E> get(K id) {
        return Optional.ofNullable(entityManager.find(clazz,id));
    }

    @Override
    public List<E> getAll() {
        CriteriaQuery<E> criteria = entityManager.getCriteriaBuilder().createQuery(clazz);
        Root<E> root = criteria.from(clazz);
        criteria.select(root);
        return entityManager.createQuery(criteria).getResultList();
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
