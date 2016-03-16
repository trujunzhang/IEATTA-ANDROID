package com.tableview.storage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.tableview.adapter.RecyclerItemClickListener;

public class TableViewConfiguration {
    public Builder builder;

    public TableViewConfiguration(Builder builder) {
        this.builder = builder;
    }

    public static class Builder {

        public final Context context;
        private String debugType;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        /**
         * Builds configured {@link TableViewConfiguration} object
         */
        public TableViewConfiguration build() {
            return new TableViewConfiguration(this);
        }

        public RecyclerView.LayoutManager manager;
        public RecyclerView.ItemDecoration decoration;
        public RecyclerItemClickListener itemClickListener;

        public Builder setLayoutManager(RecyclerView.LayoutManager manager) {
            this.manager = manager;
            return this;
        }

        public Builder setItemDecoration(RecyclerView.ItemDecoration decoration) {
            this.decoration = decoration;
            return this;
        }

        public Builder setOnItemClickListener(RecyclerItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }

        public Builder setDebugInfo(String debugType) {
            this.debugType = debugType;
            return this;
        }
    }
}
