package com.keertech.regie_phone.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SetAdapter<T> extends BaseAdapter {

    private List<T> mObjects;
    private final Object mLock;
    private boolean mNotifyOnChange;
    private Context mContext;


    public List<T> getObjects() {
        return mObjects;
    }

    /**
     * 数据为空的构造函数
     *
     * @param context
     */
    public SetAdapter(Context context) {
        this.mContext = context;
        this.mLock = new Object();
        this.mObjects = new ArrayList<T>();
        this.mNotifyOnChange = true;
    }

    /**
     * 添加集合类型数据用于初始化
     *
     * @param context
     * @param dataList
     */
    public SetAdapter(Context context, List<T> dataList) {
        this(context);
        if (dataList != null) {
            this.mObjects.addAll(dataList);
        }
    }

    /**
     * 添加数组类型数据用于初始化
     *
     * @param context
     * @param arrayOfT
     */
    public SetAdapter(Context context, T[] arrayOfT) {
        this(context);
        this.mObjects.addAll(Arrays.asList(arrayOfT));
    }

    public void insert(T object, int index) {
        synchronized (mLock) {

            {
                mObjects.add(index, object);
            }
        }
        if (mNotifyOnChange)
            notifyDataSetChanged();
    }

    /**
     * @param List
     * @param order 如果为true，添加到末尾
     */
    public void addAll(List<? extends T> List, boolean order) {
        synchronized (mLock) {

            if (order)
                mObjects.addAll(List);
            else {
                mObjects.addAll(0, List);
            }

            if (mNotifyOnChange)
                notifyDataSetChanged();
        }
    }

    public void clear() {
        synchronized (mLock) {

            mObjects.clear();

            if (mNotifyOnChange)
                notifyDataSetChanged();
        }
    }

    public void setNotifyOnChange(boolean tag) {
        mNotifyOnChange = false;
    }

    public Context getContext() {
        return this.mContext;
    }

    /**
     * 在子类中重载
     */
    @Override
    public abstract View getView(int position, View view, ViewGroup viewGroup);

    @Override
    public int getCount() {
        return this.mObjects.size();
    }

    @Override
    public T getItem(int position) {
        if (this.mObjects.isEmpty())
            return null;
        return this.mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    public int getPosition(T paramT) {
        return this.mObjects.indexOf(paramT);
    }

    /**
     * 移除指定位置数据
     *
     * @param position
     */
    public void remove(int position) {
        synchronized (mLock) {

            mObjects.remove(position);
            if (mNotifyOnChange)
                notifyDataSetChanged();
        }
    }

    public void remove(T paramT) {
        synchronized (mLock) {

            mObjects.remove(paramT);
            if (mNotifyOnChange)
                notifyDataSetChanged();
        }
    }

    public void removeAll(List<T> arrayOfT) {
        synchronized (mLock) {

            mObjects.removeAll(arrayOfT);
            if (mNotifyOnChange)
                notifyDataSetChanged();
        }
    }

    public void setObjects(List<T> mObjects) {
        this.mObjects = mObjects;
    }

    public void addItem(T item) {
        this.mObjects.add(item);
    }
}
