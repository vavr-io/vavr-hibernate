package io.vavr.hibernate.userstype;

import io.vavr.collection.List;
import java.util.Iterator;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.usertype.UserCollectionType;

/**
 * @author cbongiorno on 7/3/17.
 */
public class ListUserType implements UserCollectionType {

  @Override
  public PersistentCollection instantiate(SharedSessionContractImplementor session,
      CollectionPersister persister) throws HibernateException {
    return null;
  }

  @Override
  public PersistentCollection wrap(SharedSessionContractImplementor session, Object collection) {
    return null;
  }

  @Override
  public Iterator getElementsIterator(Object collection) {
    return ((List)collection).iterator();
  }

  @Override
  public boolean contains(Object collection, Object entity) {
    return ((List)collection).contains(entity);
  }

  @Override
  public Object indexOf(Object collection, Object entity) {
    return ((List)collection).indexOf(entity);
  }

  @Override
  public Object replaceElements(Object original, Object target, CollectionPersister persister,
      Object owner, Map copyCache, SharedSessionContractImplementor session)
      throws HibernateException {
    return null;
  }

  @Override
  public Object instantiate(int anticipatedSize) {
    return List.empty();
  }
}
