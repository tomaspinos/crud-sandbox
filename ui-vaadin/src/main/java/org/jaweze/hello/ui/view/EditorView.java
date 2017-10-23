package org.jaweze.hello.ui.view;

import org.jaweze.hello.model.Customer;

public interface EditorView {

    void showCustomer(Customer customer);

    void showNotFoundError();

    interface EditorViewListener {

        void onViewEntry(EditorView view, String parameters);

        void onBack();

        void onSave();

        void onDelete();
    }
}
