package com.tableview.storage;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.widget.ThemedSpinnerAdapter;

import com.tableview.adapter.RecyclerOnItemClickListener;

public class TableViewConfiguration {
    public Builder builder;

    public TableViewConfiguration(Builder builder) {
        this.builder = builder;
    }

    public static class Builder {

        public final Context context;
        private String debugType;

        @VisibleForTesting
        public Builder() {
            this.context = null;
        }

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Builds configured {@link TableViewConfiguration} object
         */
        public TableViewConfiguration build() {
            return new TableViewConfiguration(this);
        }

        public RecyclerView.LayoutManager manager;
        public RecyclerView.ItemDecoration decoration;
        public RecyclerOnItemClickListener itemOnClickListener;

        public Builder setLayoutManager(RecyclerView.LayoutManager manager) {
            this.manager = manager;
            return this;
        }

        public Builder setItemDecoration(RecyclerView.ItemDecoration decoration) {
            this.decoration = decoration;
            return this;
        }

        public Builder setOnItemClickListener(RecyclerOnItemClickListener itemOnClickListener) {
            this.itemOnClickListener = itemOnClickListener;
            return this;
        }

        public Builder setDebugInfo(String debugType) {
            this.debugType = debugType;
            return this;
        }
    }
}
