package org.jaweze.hello.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.jaweze.hello.utils.Messages;

@SpringUI
@SpringViewDisplay
public class VaadinUI extends UI implements ViewDisplay {

    private final Messages messages;

    private Panel springViewDisplay;

    public VaadinUI(Messages messages) {
        this.messages = messages;
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle(messages.get("main_screen.title"));

        VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        setContent(root);

        springViewDisplay = new Panel();
        springViewDisplay.setSizeFull();

        root.addComponent(springViewDisplay);
        root.setExpandRatio(springViewDisplay, 1.0f);
    }

    @Override
    public void showView(View view) {
        springViewDisplay.setContent((Component) view);
    }
}
