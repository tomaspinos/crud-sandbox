package org.jaweze.hello.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditorView implements View {

    private final Logger logger = LoggerFactory.getLogger(EditorView.class);

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        logger.debug("Editor view");
    }
}
