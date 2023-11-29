package gov.nist.csd.pm.pap.memory.unmodifiable;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class UnmodifiableAccessRightSet extends AccessRightSet {

    private final AccessRightSet internal;

    public UnmodifiableAccessRightSet(){
        internal = new AccessRightSet();
    }

    public UnmodifiableAccessRightSet(AccessRightSet a, AccessRightSet b){
        internal = new AccessRightSet();
        internal.addAll(a);
        internal.addAll(b);
    }

    public UnmodifiableAccessRightSet(String ... ops) {
        internal = new AccessRightSet(ops);
    }

    public UnmodifiableAccessRightSet(Collection<String> ops) {
        internal = new AccessRightSet(ops);
    }

    @Override
    public Iterator<String> iterator() {
        return internal.iterator();
    }

    @Override
    public int size() {
        return internal.size();
    }

    @Override
    public boolean isEmpty() {
        return internal.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return internal.contains(o);
    }

    @Override
    public Spliterator<String> spliterator() {
        return internal.spliterator();
    }

    @Override
    public Object[] toArray() {
        return internal.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return internal.toArray(a);
    }

    @Override
    public boolean equals(Object o) {
        return internal.equals(o);
    }

    @Override
    public int hashCode() {
        return internal.hashCode();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return internal.containsAll(c);
    }

    @Override
    public String toString() {
        return internal.toString();
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return internal.toArray(generator);
    }

    @Override
    public Stream<String> stream() {
        return internal.stream();
    }

    @Override
    public Stream<String> parallelStream() {
        return internal.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super String> action) {
        internal.forEach(action);
    }

    @Override
    public boolean add(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(Predicate<? super String> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
