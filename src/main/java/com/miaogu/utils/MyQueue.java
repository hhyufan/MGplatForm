package com.miaogu.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Iterator;

public class MyQueue<E> implements java.util.Queue<E> {

    private final ArrayDeque<E> deque;

    public MyQueue() {
        deque = new ArrayDeque<>();
    }

    @Override
    public boolean add(E e) {
        if (deque.isEmpty()) {
            deque.add(e);
            deque.add(e); // 添加当前值两次
            return true;
        } else if (deque.size() == 2) {
            deque.remove(); // 移除队首元素
            deque.add(e); // 将当前值添加到队尾
            return true;
        } else {
            return deque.add(e);
        }
    }

    @Override
    public boolean offer(E e) {
        return deque.offer(e);
    }

    @Override
    public E remove() {
        return deque.remove();
    }

    @Override
    public E poll() {
        return deque.poll();
    }

    @Override
    public E element() {
        return deque.element();
    }

    @Override
    public E peek() {
        return deque.peek();
    }

    @Override
    public boolean isEmpty() {
        return deque.isEmpty();
    }

    @Override
    public int size() {
        return deque.size();
    }

    @Override
    public boolean contains(Object o) {
        return deque.contains(o);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return deque.iterator();
    }

    @Override
    public Object @NotNull [] toArray() {
        return deque.toArray();
    }

    @Override
    public <T> T @NotNull [] toArray(T @NotNull [] a) {
        return deque.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        return deque.remove(o);
    }

    @Override
    public boolean containsAll(java.util.@NotNull Collection<?> c) {
        return deque.containsAll(c);
    }

    @Override
    public boolean addAll(java.util.@NotNull Collection<? extends E> c) {
        return deque.addAll(c);
    }

    @Override
    public boolean removeAll(java.util.@NotNull Collection<?> c) {
        return deque.removeAll(c);
    }

    @Override
    public boolean retainAll(java.util.@NotNull Collection<?> c) {
        return deque.retainAll(c);
    }

    @Override
    public void clear() {
        deque.clear();
    }
}