package org.jaweze.hello.ui.view;

public interface EditorViewListener {

    void onViewEntry(EditorView view, String parameters);

    void onBack();

    void onSave();

    void onDelete();
}
