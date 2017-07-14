package io.vavr.hibernate.userstype;

import io.vavr.CheckedFunction0;
import io.vavr.Function1;
import io.vavr.PartialFunction;
import io.vavr.Tuple;
import io.vavr.Tuple1;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.Array;
import io.vavr.collection.CharSeq;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.PriorityQueue;
import io.vavr.collection.Queue;
import io.vavr.collection.Seq;
import io.vavr.collection.SortedMap;
import io.vavr.collection.SortedSet;
import io.vavr.collection.Tree;
import io.vavr.collection.Vector;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;
import org.hibernate.HibernateException;
import org.hibernate.collection.internal.AbstractPersistentCollection;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.loader.CollectionAliases;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.type.Type;

/**
 * @author cbongiorno on 7/5/17.
 */
@EqualsAndHashCode(of = "delegate")
public class PersistentList<T> extends AbstractPersistentCollection implements List<T> {


  private List<T> delegate = List.empty();


  public PersistentList(SharedSessionContractImplementor session) {
    super(session);
  }

  public PersistentList(SharedSessionContractImplementor session, List delegate) {
    super(session);
    this.delegate = delegate;

  }

  @Override
  public boolean empty() {
    return delegate.isEmpty();
  }

  @Override
  public Collection getOrphans(Serializable snapshot, String entityName) throws HibernateException {
    return null;
  }

  @Override
  public void initializeFromCache(CollectionPersister persister, Serializable disassembled,
      Object owner) {
    System.out.println();
  }

  @Override
  public Iterator entries(CollectionPersister persister) {
    return delegate.iterator();
  }

  private transient List<Tuple> loadingEntries;

  @Override
  public Object readFrom(ResultSet rs, CollectionPersister role, CollectionAliases descriptor,
      Object owner) throws HibernateException, SQLException {

    final Object element = role
        .readElement(rs, owner, descriptor.getSuffixedElementAliases(), getSession());
    if (element != null) {
      final Object index = role.readIndex(rs, descriptor.getSuffixedIndexAliases(), getSession());
      loadingEntries = Option.of(loadingEntries).filter(Objects::nonNull).getOrElse(List.empty());
      loadingEntries = loadingEntries.append(Tuple.of(index, element));
    }
    return element;
  }

  @Override
  public Object getIndex(Object entry, int i, CollectionPersister persister) {
    return delegate.get(i);
  }

  @Override
  public Object getElement(Object entry) {
    return entry;
  }

  @Override
  public Object getSnapshotElement(Object entry, int i) {
    // supposed to copy this element.
    return entry;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void beforeInitialize(CollectionPersister persister, int anticipatedSize) {
    this.delegate = (List<T>) persister.getCollectionType().instantiate(anticipatedSize);

  }

  @Override
  public boolean equalsSnapshot(CollectionPersister persister) {
    return false;
  }

  @Override
  public boolean isSnapshotEmpty(Serializable snapshot) {
    return ((List) snapshot).isEmpty();
  }

  @Override
  public Serializable disassemble(CollectionPersister persister) {
    return delegate.map(v -> persister.getElementType().disassemble(v, getSession(), null));
  }

  @Override
  public Serializable getSnapshot(CollectionPersister persister) {
    return delegate.map(v -> persister.getElementType().deepCopy(v, persister.getFactory()));
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean entryExists(Object entry, int i) {
    return delegate.contains((T) entry);
  }

  @Override
  public boolean needsInserting(Object entry, int i, Type elemType) {
    return !delegate.get(i).equals(entry);
  }

  @Override
  public boolean needsUpdating(Object entry, int i, Type elemType) {
    return false;
  }

  @Override
  public Iterator getDeletes(CollectionPersister persister, boolean indexIsFormula) {
    return null;
  }

  @Override
  public boolean isWrapper(Object collection) {
    return false;
  }


  @Override
  public T fold(T zero, BiFunction<? super T, ? super T, ? extends T> combine) {
    return delegate.fold(zero, combine);
  }

  @Override
  public boolean isMemoized() {
    return delegate.isMemoized();
  }


  @Override
  public int indexOfSlice(Iterable<? extends T> that, int from) {
    return delegate.indexOfSlice(that, from);
  }

  @Override
  public int indexWhere(Predicate<? super T> predicate, int from) {
    return delegate.indexWhere(predicate, from);
  }


  @Override
  public T apply(Integer index) {
    return delegate.apply(index);
  }

  @Override
  public T reduce(BiFunction<? super T, ? super T, ? extends T> op) {
    return delegate.reduce(op);
  }

  @Override
  public boolean isDefinedAt(Integer index) {
    return delegate.isDefinedAt(index);
  }


  @Override
  public int lastIndexOfSlice(Iterable<? extends T> that, int end) {
    return delegate.lastIndexOfSlice(that, end);
  }


  @Override
  public int lastIndexWhere(Predicate<? super T> predicate, int end) {
    return delegate.lastIndexWhere(predicate, end);
  }

  @Override
  public Option<T> reduceOption(BiFunction<? super T, ? super T, ? extends T> op) {
    return delegate.reduceOption(op);
  }


  @Override
  public boolean isAsync() {
    return delegate.isAsync();
  }


  @Override
  public boolean isEmpty() {
    return delegate.isEmpty();
  }


  @Override
  public boolean isLazy() {
    return delegate.isLazy();
  }

  @Override
  public <K> Option<Map<K, T>> arrangeBy(
      Function<? super T, ? extends K> getKey) {
    return delegate.arrangeBy(getKey);
  }

  @Override
  public int arity() {
    return delegate.arity();
  }

  @Override
  public Function1<Integer, T> curried() {
    return delegate.curried();
  }

  @Override
  public Function1<Tuple1<Integer>, T> tupled() {
    return delegate.tupled();
  }

  @Override
  public <R, A> R collect(Collector<? super T, A, R> collector) {
    return delegate.collect(collector);
  }

  @Override
  public Function1<Integer, T> reversed() {
    return delegate.reversed();
  }

  @Override
  public Function1<Integer, T> memoized() {
    return delegate.memoized();
  }


  @Override
  public <R> R collect(Supplier<R> supplier,
      BiConsumer<R, ? super T> accumulator,
      BiConsumer<R, R> combiner) {
    return delegate.collect(supplier, accumulator, combiner);
  }

  @Override
  public io.vavr.collection.Iterator<T> reverseIterator() {
    return delegate.reverseIterator();
  }

  @Override
  public Option<Double> average() {
    return delegate.average();
  }

  @Override
  public PartialFunction<Integer, T> partial(
      Predicate<? super Integer> isDefinedAt) {
    return delegate.partial(isDefinedAt);
  }

  @Override
  public int segmentLength(Predicate<? super T> predicate, int from) {
    return delegate.segmentLength(predicate, from);
  }

  @Override
  public boolean contains(T element) {
    return delegate.contains(element);
  }


  @Override
  public <U> boolean corresponds(Iterable<U> that,
      BiPredicate<? super T, ? super U> predicate) {
    return delegate.corresponds(that, predicate);
  }

  @Override
  public <V> Function1<Integer, V> andThen(
      Function<? super T, ? extends V> after) {
    return delegate.andThen(after);
  }

  @Override
  public boolean containsSlice(Iterable<? extends T> that) {
    return delegate.containsSlice(that);
  }

  @Override
  public <V> Function1<V, T> compose(
      Function<? super V, ? extends Integer> before) {
    return delegate.compose(before);
  }

  @Override
  public io.vavr.collection.Iterator<Tuple2<T, T>> crossProduct() {
    return delegate.crossProduct();
  }


  @Override
  public boolean eq(Object o) {
    return delegate.eq(o);
  }

  @Override
  public boolean containsAll(Iterable<? extends T> elements) {
    return delegate.containsAll(elements);
  }

  @Override
  public <U> io.vavr.collection.Iterator<Tuple2<T, U>> crossProduct(
      Iterable<? extends U> that) {
    return delegate.crossProduct(that);
  }


  @Override
  public int count(Predicate<? super T> predicate) {
    return delegate.count(predicate);
  }

  @Override
  public int search(T element) {
    return delegate.search(element);
  }

  @Override
  public boolean exists(Predicate<? super T> predicate) {
    return delegate.exists(predicate);
  }

  @Override
  public boolean endsWith(Seq<? extends T> that) {
    return delegate.endsWith(that);
  }


  @Override
  public boolean forAll(Predicate<? super T> predicate) {
    return delegate.forAll(predicate);
  }


  @Override
  public int search(T element, Comparator<? super T> comparator) {
    return delegate.search(element, comparator);
  }

  @Override
  public void forEach(Consumer<? super T> action) {
    delegate.forEach(action);
  }


  @Override
  public int indexOf(T element) {
    return delegate.indexOf(element);
  }


  @Override
  public Option<Integer> indexOfOption(T element) {
    return delegate.indexOfOption(element);
  }

  @Override
  public Option<Integer> indexOfOption(T element, int from) {
    return delegate.indexOfOption(element, from);
  }

  @Override
  public T getOrElse(T other) {
    return delegate.getOrElse(other);
  }

  @Override
  public T getOrElse(Supplier<? extends T> supplier) {
    return delegate.getOrElse(supplier);
  }

  @Override
  public int indexOfSlice(Iterable<? extends T> that) {
    return delegate.indexOfSlice(that);
  }

  @Override
  public Option<Integer> indexOfSliceOption(Iterable<? extends T> that) {
    return delegate.indexOfSliceOption(that);
  }

  @Override
  public <X extends Throwable> T getOrElseThrow(Supplier<X> supplier) throws X {
    return delegate.getOrElseThrow(supplier);
  }

  @Override
  public T getOrElseTry(CheckedFunction0<? extends T> supplier) {
    return delegate.getOrElseTry(supplier);
  }

  @Override
  public T getOrNull() {
    return delegate.getOrNull();
  }

  @Override
  public Option<Integer> indexOfSliceOption(Iterable<? extends T> that, int from) {
    return delegate.indexOfSliceOption(that, from);
  }


  @Override
  public int indexWhere(Predicate<? super T> predicate) {
    return delegate.indexWhere(predicate);
  }



  @Override
  public Option<Integer> indexWhereOption(Predicate<? super T> predicate) {
    return delegate.indexWhereOption(predicate);
  }

  @Override
  public boolean existsUnique(Predicate<? super T> predicate) {
    return delegate.existsUnique(predicate);
  }


  @Override
  public Option<Integer> indexWhereOption(Predicate<? super T> predicate, int from) {
    return delegate.indexWhereOption(predicate, from);
  }


  @Override
  public Option<T> find(Predicate<? super T> predicate) {
    return delegate.find(predicate);
  }

  @Override
//  //@GwtIncompatible("java.io.PrintStream is not implemented")
  public void out(PrintStream out) {
    delegate.out(out);
  }

  @Override
  public Option<T> findLast(Predicate<? super T> predicate) {
    return delegate.findLast(predicate);
  }


  @Override
//  //@GwtIncompatible("java.io.PrintWriter is not implemented")
  public void out(PrintWriter writer) {
    delegate.out(writer);
  }


  @Override
  public <U> U foldLeft(U zero, BiFunction<? super U, ? super T, ? extends U> f) {
    return delegate.foldLeft(zero, f);
  }

  @Override
  public io.vavr.collection.Iterator<T> iterator(int index) {
    return delegate.iterator(index);
  }

  //  //@GwtIncompatible


  @Override
//  //@GwtIncompatible("java.io.PrintStream is not implemented")
  public void stderr() {
    delegate.stderr();
  }

  @Override
  public int lastIndexOf(T element) {
    return delegate.lastIndexOf(element);
  }

  @Override
  public T get() {
    return delegate.get();
  }

  @Override
  public Option<Integer> lastIndexOfOption(T element) {
    return delegate.lastIndexOfOption(element);
  }

  @Override
  //@GwtIncompatible("java.io.PrintStream is not implemented")
  public void stdout() {
    delegate.stdout();
  }


  @Override
  public int lastIndexWhere(Predicate<? super T> predicate) {
    return delegate.lastIndexWhere(predicate);
  }

  @Override
  public Array<T> toArray() {
    return delegate.toArray();
  }

  @Override
  public CharSeq toCharSeq() {
    return delegate.toCharSeq();
  }

  @Override
  public Option<Integer> lastIndexWhereOption(Predicate<? super T> predicate) {
    return delegate.lastIndexWhereOption(predicate);
  }


  @Override
  //@GwtIncompatible
  public CompletableFuture<T> toCompletableFuture() {
    return delegate.toCompletableFuture();
  }

  @Override
  public Option<Integer> lastIndexWhereOption(Predicate<? super T> predicate,
      int end) {
    return delegate.lastIndexWhereOption(predicate, end);
  }


  @Override
  public <U> Validation<T, U> toInvalid(U value) {
    return delegate.toInvalid(value);
  }

  @Override
  public Function1<Integer, Option<T>> lift() {
    return delegate.lift();
  }

  @Override
  public T head() {
    return delegate.head();
  }

  @Override
  public <U> Validation<T, U> toInvalid(
      Supplier<? extends U> valueSupplier) {
    return delegate.toInvalid(valueSupplier);
  }

  @Override
  public Option<T> headOption() {
    return delegate.headOption();
  }

  @Override
  public Object[] toJavaArray() {
    return delegate.toJavaArray();
  }


  @Override
  public Option<Integer> lastIndexOfOption(T element, int end) {
    return delegate.lastIndexOfOption(element, end);
  }


  @Override
  public int lastIndexOfSlice(Iterable<? extends T> that) {
    return delegate.lastIndexOfSlice(that);
  }

  @Override
  //@GwtIncompatible("reflection is not supported")
  public T[] toJavaArray(Class<T> componentType) {
    return delegate.toJavaArray(componentType);
  }

  @Override
  public Option<Integer> lastIndexOfSliceOption(Iterable<? extends T> that) {
    return delegate.lastIndexOfSliceOption(that);
  }


  @Override
  public Option<Integer> lastIndexOfSliceOption(Iterable<? extends T> that, int end) {
    return delegate.lastIndexOfSliceOption(that, end);
  }

  @Override
  public <C extends Collection<T>> C toJavaCollection(
      Function<Integer, C> factory) {
    return delegate.toJavaCollection(factory);
  }

  @Override
  public java.util.List<T> toJavaList() {
    return delegate.toJavaList();
  }

  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  @Override
  public <LIST extends java.util.List<T>> LIST toJavaList(
      Function<Integer, LIST> factory) {
    return delegate.toJavaList(factory);
  }

  @Override
  public <K, V> java.util.Map<K, V> toJavaMap(
      Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
    return delegate.toJavaMap(f);
  }

  @Override
  public boolean isDistinct() {
    return delegate.isDistinct();
  }


  @Override
  public boolean isOrdered() {
    return delegate.isOrdered();
  }

  @Override
  public <K, V, MAP extends java.util.Map<K, V>> MAP toJavaMap(Supplier<MAP> factory,
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends V> valueMapper) {
    return delegate.toJavaMap(factory, keyMapper, valueMapper);
  }

  @Override
  public List<T> append(T element) {
    return delegate.append(element);
  }

  @Override
  public List<T> appendAll(Iterable<? extends T> elements) {
    return delegate.appendAll(elements);
  }

  //  //@GwtIncompatible a package private annotation? That's a first
  @Override
  public java.util.List<T> asJava() {
    return delegate.asJava();
  }

  @Override
  public boolean isSingleValued() {
    return delegate.isSingleValued();
  }

  @Override
  public List<T> asJava(Consumer<? super java.util.List<T>> action) {
    return delegate.asJava(action);
  }

  @Override
  public int prefixLength(Predicate<? super T> predicate) {
    return delegate.prefixLength(predicate);
  }

  //  //@GwtIncompatible
  @Override
  public java.util.List<T> asJavaMutable() {
    return delegate.asJavaMutable();
  }

  @Override
  public <K, V, MAP extends java.util.Map<K, V>> MAP toJavaMap(Supplier<MAP> factory,
      Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
    return delegate.toJavaMap(factory, f);
  }

  //  //@GwtIncompatible
  @Override
  public List<T> asJavaMutable(Consumer<? super java.util.List<T>> action) {
    return delegate.asJavaMutable(action);
  }

  @Override
  public <R> List<R> collect(PartialFunction<? super T, ? extends R> partialFunction) {
    return delegate.collect(partialFunction);
  }

  @Override
  public io.vavr.collection.Iterator<T> iterator() {
    return delegate.iterator();
  }

  @Override
  public List<List<T>> combinations() {
    return delegate.combinations();
  }


  @Override
  public List<List<T>> combinations(int k) {
    return delegate.combinations(k);
  }

  @Override
  public io.vavr.collection.Iterator<List<T>> crossProduct(int power) {
    return delegate.crossProduct(power);
  }

  @Override
  public List<T> distinct() {
    return delegate.distinct();
  }

  @Override
  public Optional<T> toJavaOptional() {
    return delegate.toJavaOptional();
  }

  @Override
  public T last() {
    return delegate.last();
  }

  @Override
  public List<T> distinctBy(Comparator<? super T> comparator) {
    return delegate.distinctBy(comparator);
  }

  @Override
  public Set<T> toJavaSet() {
    return delegate.toJavaSet();
  }

  @Override
  public <U> List<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
    return delegate.distinctBy(keyExtractor);
  }

  @Override
  public Option<T> lastOption() {
    return delegate.lastOption();
  }

  @Override
  public List<T> drop(int n) {
    return delegate.drop(n);
  }

  @Override
  public <SET extends Set<T>> SET toJavaSet(
      Function<Integer, SET> factory) {
    return delegate.toJavaSet(factory);
  }

  @Override
  public List<T> dropUntil(Predicate<? super T> predicate) {
    return delegate.dropUntil(predicate);
  }

  @Override
  public Stream<T> toJavaStream() {
    return delegate.toJavaStream();
  }

  @Override
  public List<T> dropWhile(Predicate<? super T> predicate) {
    return delegate.dropWhile(predicate);
  }

  @Override
  public Stream<T> toJavaParallelStream() {
    return delegate.toJavaParallelStream();
  }

  @Override
  public List<T> dropRight(int n) {
    return delegate.dropRight(n);
  }

  @Override
  public List<T> dropRightUntil(Predicate<? super T> predicate) {
    return delegate.dropRightUntil(predicate);
  }

  @Override
  public Option<T> max() {
    return delegate.max();
  }

  @Override
  public <R> Either<T, R> toLeft(R right) {
    return delegate.toLeft(right);
  }

  @Override
  public List<T> dropRightWhile(Predicate<? super T> predicate) {
    return delegate.dropRightWhile(predicate);
  }

  @Override
  public List<T> filter(Predicate<? super T> predicate) {
    return delegate.filter(predicate);
  }

  @Override
  public <R> Either<T, R> toLeft(Supplier<? extends R> right) {
    return delegate.toLeft(right);
  }

  @Override
  public Option<T> maxBy(Comparator<? super T> comparator) {
    return delegate.maxBy(comparator);
  }

  @Override
  public List<T> toList() {
    return delegate.toList();
  }

  @Override
  public <U> List<U> flatMap(
      Function<? super T, ? extends Iterable<? extends U>> mapper) {
    return delegate.flatMap(mapper);
  }

  @Override
  public <K, V> Map<K, V> toMap(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends V> valueMapper) {
    return delegate.toMap(keyMapper, valueMapper);
  }

  @Override
  public <U extends Comparable<? super U>> Option<T> maxBy(
      Function<? super T, ? extends U> f) {
    return delegate.maxBy(f);
  }

  @Override
  public T get(int index) {
    return delegate.get(index);
  }

  @Override
  public <K, V> Map<K, V> toMap(
      Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
    return delegate.toMap(f);
  }

  @Override
  public <C> Map<C, List<T>> groupBy(
      Function<? super T, ? extends C> classifier) {
    return delegate.groupBy(classifier);
  }

  @Override
  public io.vavr.collection.Iterator<List<T>> grouped(int size) {
    return delegate.grouped(size);
  }

  @Override
  public boolean hasDefiniteSize() {
    return delegate.hasDefiniteSize();
  }

  @Override
  public int indexOf(T element, int from) {
    return delegate.indexOf(element, from);
  }

  @Override
  public Option<T> min() {
    return delegate.min();
  }

  @Override
  public List<T> init() {
    return delegate.init();
  }

  @Override
  public <K, V> Map<K, V> toLinkedMap(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends V> valueMapper) {
    return delegate.toLinkedMap(keyMapper, valueMapper);
  }

  @Override
  public Option<List<T>> initOption() {
    return delegate.initOption();
  }

  @Override
  public Option<T> minBy(Comparator<? super T> comparator) {
    return delegate.minBy(comparator);
  }

  @Override
  public int length() {
    return delegate.length();
  }

  @Override
  public List<T> insert(int index, T element) {
    return delegate.insert(index, element);
  }

  @Override
  public <K, V> Map<K, V> toLinkedMap(
      Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
    return delegate.toLinkedMap(f);
  }

  @Override
  public List<T> insertAll(int index, Iterable<? extends T> elements) {
    return delegate.insertAll(index, elements);
  }

  @Override
  public <U extends Comparable<? super U>> Option<T> minBy(
      Function<? super T, ? extends U> f) {
    return delegate.minBy(f);
  }

  @Override
  public <K extends Comparable<? super K>, V> SortedMap<K, V> toSortedMap(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends V> valueMapper) {
    return delegate.toSortedMap(keyMapper, valueMapper);
  }

  @Override
  public List<T> intersperse(T element) {
    return delegate.intersperse(element);
  }

  @Override
  public CharSeq mkCharSeq() {
    return delegate.mkCharSeq();
  }

  @Override
  public boolean isTraversableAgain() {
    return delegate.isTraversableAgain();
  }

  @Override
  public boolean startsWith(Iterable<? extends T> that) {
    return delegate.startsWith(that);
  }

  @Override
  public int lastIndexOf(T element, int end) {
    return delegate.lastIndexOf(element, end);
  }

  @Override
  public <K extends Comparable<? super K>, V> SortedMap<K, V> toSortedMap(
      Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
    return delegate.toSortedMap(f);
  }

  @Override
  public CharSeq mkCharSeq(CharSequence delimiter) {
    return delegate.mkCharSeq(delimiter);
  }

  @Override
  public <U> List<U> map(Function<? super T, ? extends U> mapper) {
    return delegate.map(mapper);
  }

  @Override
  public boolean startsWith(Iterable<? extends T> that, int offset) {
    return delegate.startsWith(that, offset);
  }

  @Override
  public <K, V> SortedMap<K, V> toSortedMap(
      Comparator<? super K> comparator,
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends V> valueMapper) {
    return delegate.toSortedMap(comparator, keyMapper, valueMapper);
  }

  @Override
  public List<T> orElse(Iterable<? extends T> other) {
    return delegate.orElse(other);
  }

  @Override
  public List<T> orElse(
      Supplier<? extends Iterable<? extends T>> supplier) {
    return delegate.orElse(supplier);
  }

  @Override
  public CharSeq mkCharSeq(CharSequence prefix, CharSequence delimiter,
      CharSequence suffix) {
    return delegate.mkCharSeq(prefix, delimiter, suffix);
  }

  @Override
  public List<T> padTo(int length, T element) {
    return delegate.padTo(length, element);
  }

  @Override
  public List<T> leftPadTo(int length, T element) {
    return delegate.leftPadTo(length, element);
  }

  @Override
  public String mkString() {
    return delegate.mkString();
  }

  @Override
  public <K, V> SortedMap<K, V> toSortedMap(
      Comparator<? super K> comparator,
      Function<? super T, ? extends Tuple2<? extends K, ? extends V>> f) {
    return delegate.toSortedMap(comparator, f);
  }

  @Override
  public List<T> patch(int from, Iterable<? extends T> that, int replaced) {
    return delegate.patch(from, that, replaced);
  }

  @Override
  public String mkString(CharSequence delimiter) {
    return delegate.mkString(delimiter);
  }

  @Override
  public Tuple2<List<T>, List<T>> partition(Predicate<? super T> predicate) {
    return delegate.partition(predicate);
  }

  @Override
  public Option<T> toOption() {
    return delegate.toOption();
  }

  @Override
  public String mkString(CharSequence prefix, CharSequence delimiter, CharSequence suffix) {
    return delegate.mkString(prefix, delimiter, suffix);
  }

  @Override
  public T peek() {
    return delegate.peek();
  }

  @Override
  public <L> Either<L, T> toEither(L left) {
    return delegate.toEither(left);
  }

  @Override
  public boolean nonEmpty() {
    return delegate.nonEmpty();
  }

  @Override
  public Option<T> peekOption() {
    return delegate.peekOption();
  }

  @Override
  public <L> Either<L, T> toEither(
      Supplier<? extends L> leftSupplier) {
    return delegate.toEither(leftSupplier);
  }

  @Override
  public List<T> peek(Consumer<? super T> action) {
    return delegate.peek(action);
  }

  @Override
  public List<List<T>> permutations() {
    return delegate.permutations();
  }

  @Override
  public <L> Validation<L, T> toValidation(L invalid) {
    return delegate.toValidation(invalid);
  }

  @Override
  public List<T> pop() {
    return delegate.pop();
  }

  @Override
  public <L> Validation<L, T> toValidation(
      Supplier<? extends L> invalidSupplier) {
    return delegate.toValidation(invalidSupplier);
  }

  @Override
  public Option<List<T>> popOption() {
    return delegate.popOption();
  }

  @Override
  public Queue<T> toQueue() {
    return delegate.toQueue();
  }

  @Override
  public Tuple2<T, List<T>> pop2() {
    return delegate.pop2();
  }

  @Override
  public PriorityQueue<T> toPriorityQueue() {
    return delegate.toPriorityQueue();
  }

  @Override
  public Number product() {
    return delegate.product();
  }

  @Override
  public Option<Tuple2<T, List<T>>> pop2Option() {
    return delegate.pop2Option();
  }

  @Override
  public List<T> prepend(T element) {
    return delegate.prepend(element);
  }

  @Override
  public List<T> prependAll(Iterable<? extends T> elements) {
    return delegate.prependAll(elements);
  }

  @Override
  public PriorityQueue<T> toPriorityQueue(
      Comparator<? super T> comparator) {
    return delegate.toPriorityQueue(comparator);
  }

  @Override
  public List<T> push(T element) {
    return delegate.push(element);
  }

  @Override
  public T reduceLeft(BiFunction<? super T, ? super T, ? extends T> op) {
    return delegate.reduceLeft(op);
  }

  @Override
  public <L> Either<L, T> toRight(L left) {
    return delegate.toRight(left);
  }

  @Override
  public List<T> push(T[] elements) {
    return delegate.push(elements);
  }

  @Override
  public Option<T> reduceLeftOption(
      BiFunction<? super T, ? super T, ? extends T> op) {
    return delegate.reduceLeftOption(op);
  }

  @Override
  public <L> Either<L, T> toRight(Supplier<? extends L> left) {
    return delegate.toRight(left);
  }

  @Override
  public List<T> pushAll(Iterable<T> elements) {
    return delegate.pushAll(elements);
  }

  @Override
  public io.vavr.collection.Set<T> toSet() {
    return delegate.toSet();
  }

  @Override
  public T reduceRight(BiFunction<? super T, ? super T, ? extends T> op) {
    return delegate.reduceRight(op);
  }

  @Override
  public List<T> remove(T element) {
    return delegate.remove(element);
  }

  @Override
  public io.vavr.collection.Set<T> toLinkedSet() {
    return delegate.toLinkedSet();
  }

  @Override
  public <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
    return delegate.foldRight(zero, f);
  }

  @Override
  public Option<T> reduceRightOption(
      BiFunction<? super T, ? super T, ? extends T> op) {
    return delegate.reduceRightOption(op);
  }

  @Override
  public SortedSet<T> toSortedSet() throws ClassCastException {
    return delegate.toSortedSet();
  }

  @Override
  public List<T> removeFirst(Predicate<T> predicate) {
    return delegate.removeFirst(predicate);
  }

  @Override
  public SortedSet<T> toSortedSet(Comparator<? super T> comparator) {
    return delegate.toSortedSet(comparator);
  }

  @Override
  public List<T> removeLast(Predicate<T> predicate) {
    return delegate.removeLast(predicate);
  }

  @Override
  public List<T> removeAt(int index) {
    return delegate.removeAt(index);
  }

  @Override
  public io.vavr.collection.Stream<T> toStream() {
    return delegate.toStream();
  }

  @Override
  public Try<T> toTry() {
    return delegate.toTry();
  }

  @Override
  public List<T> removeAll(T element) {
    return delegate.removeAll(element);
  }

  @Override
  public List<T> removeAll(Iterable<? extends T> elements) {
    return delegate.removeAll(elements);
  }

  @Override
  public List<T> removeAll(Predicate<? super T> predicate) {
    return delegate.removeAll(predicate);
  }

  @Override
  public Try<T> toTry(
      Supplier<? extends Throwable> ifEmpty) {
    return delegate.toTry(ifEmpty);
  }

  @Override
  public List<T> replace(T currentElement, T newElement) {
    return delegate.replace(currentElement, newElement);
  }

  @Override
  public Tree<T> toTree() {
    return delegate.toTree();
  }

  @Override
  public List<T> replaceAll(T currentElement, T newElement) {
    return delegate.replaceAll(currentElement, newElement);
  }

  @Override
  public <E> Validation<E, T> toValid(E error) {
    return delegate.toValid(error);
  }

  @Override
  public Function1<Integer, T> withDefaultValue(T defaultValue) {
    return delegate.withDefaultValue(defaultValue);
  }

  @Override
  public List<T> retainAll(Iterable<? extends T> elements) {
    return delegate.retainAll(elements);
  }

  @Override
  public <E> Validation<E, T> toValid(
      Supplier<? extends E> errorSupplier) {
    return delegate.toValid(errorSupplier);
  }

  @Override
  public List<T> reverse() {
    return delegate.reverse();
  }

  @Override
  public Function1<Integer, T> withDefault(
      Function<? super Integer, ? extends T> defaultFunction) {
    return delegate.withDefault(defaultFunction);
  }

  @Override
  public List<T> scan(T zero,
      BiFunction<? super T, ? super T, ? extends T> operation) {
    return delegate.scan(zero, operation);
  }

  @Override
  public Vector<T> toVector() {
    return delegate.toVector();
  }

  @Override
  public <U> List<U> scanLeft(U zero,
      BiFunction<? super U, ? super T, ? extends U> operation) {
    return delegate.scanLeft(zero, operation);
  }

  @Override
  public boolean isSequential() {
    return delegate.isSequential();
  }

  @Override
  public T single() {
    return delegate.single();
  }

  @Override
  public <U> List<U> scanRight(U zero,
      BiFunction<? super T, ? super U, ? extends U> operation) {
    return delegate.scanRight(zero, operation);
  }

  @Override
  public List<T> shuffle() {
    return delegate.shuffle();
  }

  @Override
  public List<T> slice(int beginIndex, int endIndex) {
    return delegate.slice(beginIndex, endIndex);
  }

  @Override
  public Option<T> singleOption() {
    return delegate.singleOption();
  }

  @Override
  public int size() {
    return delegate.size();
  }

  @Override
  public io.vavr.collection.Iterator<List<T>> slideBy(
      Function<? super T, ?> classifier) {
    return delegate.slideBy(classifier);
  }

  @Override
  public io.vavr.collection.Iterator<List<T>> sliding(int size) {
    return delegate.sliding(size);
  }

  @Override
  public String toString() {
    return delegate.toString();
  }

  @Override
  public io.vavr.collection.Iterator<List<T>> sliding(int size, int step) {
    return delegate.sliding(size, step);
  }

  @Override
  public List<T> sorted() {
    return delegate.sorted();
  }

  @Override
  public List<T> sorted(Comparator<? super T> comparator) {
    return delegate.sorted(comparator);
  }

  @Override
  public <U extends Comparable<? super U>> List<T> sortBy(
      Function<? super T, ? extends U> mapper) {
    return delegate.sortBy(mapper);
  }

  @Override
  public <U> List<T> sortBy(Comparator<? super U> comparator,
      Function<? super T, ? extends U> mapper) {
    return delegate.sortBy(comparator, mapper);
  }

  @Override
  public Tuple2<List<T>, List<T>> span(Predicate<? super T> predicate) {
    return delegate.span(predicate);
  }

  @Override
  public Tuple2<List<T>, List<T>> splitAt(int n) {
    return delegate.splitAt(n);
  }

  @Override
  public Tuple2<List<T>, List<T>> splitAt(Predicate<? super T> predicate) {
    return delegate.splitAt(predicate);
  }

  @Override
  public Tuple2<List<T>, List<T>> splitAtInclusive(
      Predicate<? super T> predicate) {
    return delegate.splitAtInclusive(predicate);
  }

  @Override
  public Spliterator<T> spliterator() {
    return delegate.spliterator();
  }

  @Override
  public String stringPrefix() {
    return delegate.stringPrefix();
  }

  @Override
  public List<T> subSequence(int beginIndex) {
    return delegate.subSequence(beginIndex);
  }

  @Override
  public List<T> subSequence(int beginIndex, int endIndex) {
    return delegate.subSequence(beginIndex, endIndex);
  }

  @Override
  public Number sum() {
    return delegate.sum();
  }

  @Override
  public List<T> tail() {
    return delegate.tail();
  }

  @Override
  public Option<List<T>> tailOption() {
    return delegate.tailOption();
  }

  @Override
  public List<T> take(int n) {
    return delegate.take(n);
  }

  @Override
  public List<T> takeRight(int n) {
    return delegate.takeRight(n);
  }

  @Override
  public List<T> takeUntil(Predicate<? super T> predicate) {
    return delegate.takeUntil(predicate);
  }

  @Override
  public List<T> takeWhile(Predicate<? super T> predicate) {
    return delegate.takeWhile(predicate);
  }

  @Override
  public <U> U transform(
      Function<? super List<T>, ? extends U> f) {
    return delegate.transform(f);
  }

  @Override
  public <T1, T2> Tuple2<List<T1>, List<T2>> unzip(
      Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
    return delegate.unzip(unzipper);
  }

  @Override
  public <T1, T2, T3> Tuple3<List<T1>, List<T2>, List<T3>> unzip3(
      Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
    return delegate.unzip3(unzipper);
  }

  @Override
  public List<T> update(int index, T element) {
    return delegate.update(index, element);
  }

  @Override
  public List<T> update(int index, Function<? super T, ? extends T> updater) {
    return delegate.update(index, updater);
  }

  @Override
  public <U> List<Tuple2<T, U>> zip(Iterable<? extends U> that) {
    return delegate.zip(that);
  }

  @Override
  public <U, R> List<R> zipWith(Iterable<? extends U> that,
      BiFunction<? super T, ? super U, ? extends R> mapper) {
    return delegate.zipWith(that, mapper);
  }

  @Override
  public <U> List<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem) {
    return delegate.zipAll(that, thisElem, thatElem);
  }

  @Override
  public List<Tuple2<T, Integer>> zipWithIndex() {
    return delegate.zipWithIndex();
  }

  @Override
  public <U> List<U> zipWithIndex(
      BiFunction<? super T, ? super Integer, ? extends U> mapper) {
    return delegate.zipWithIndex(mapper);
  }
}
