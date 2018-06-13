package com.ote.test;

import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.stream.Stream;

@SpringUI
@Title("File Storage Client")
@Slf4j
public class FileStorageMainPage extends UI {

    private Label user = new Label("Olivier Terrien");// should be mapped to the connected user
    private ComboBox<String> application = new ComboBox<>("Application"); // all applications on which this user is granted
    private ComboBox<String> perimeter = new ComboBox<>("Perimeter"); // all perimeters of application on which this user is granted
    private ComboBox<String> folder = new ComboBox<>("Folder");
    private ComboBox<String> file = new ComboBox<>("File");

    @Override
    protected void init(VaadinRequest request) {

        GridLayout layout = new GridLayout(3, 1);
        layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        layout.setMargin(new MarginInfo(false, true, true, true));
        layout.setSpacing(true);
        layout.setSizeFull();
        layout.setRowExpandRatio(1, 1);
        layout.setColumnExpandRatio(2, 1);
        setContent(layout);

        FormLayout storageFormLayout = new FormLayout();
        storageFormLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        layout.addComponent(storageFormLayout, 1, 0);
        //layout.setComponentAlignment(storageFormLayout, Alignment.MIDDLE_CENTER);

        storageFormLayout.addComponent(user);

        application.setPlaceholder("select...");
        application.setItems("SLA", "TDA");
        application.addValueChangeListener(e -> updatePerimeter(e.getValue()));
        storageFormLayout.addComponent(application);

        perimeter.setEnabled(false);
        perimeter.addValueChangeListener(e -> updateFolder(e.getValue()));
        storageFormLayout.addComponent(perimeter);

        folder.setEnabled(false);
        folder.addValueChangeListener(e -> updateFile(e.getValue()));
        storageFormLayout.addComponent(folder);

        file.setEnabled(false);
        storageFormLayout.addComponent(file);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button download = new Button("Download", VaadinIcons.DOWNLOAD);
        download.addClickListener(e -> Notification.show("Download not implemented", Notification.Type.WARNING_MESSAGE));
        Button upload = new Button("Upload", VaadinIcons.UPLOAD);
        upload.addClickListener(e -> Notification.show("Upload not implemented", Notification.Type.WARNING_MESSAGE));
        buttonLayout.addComponents(download, upload);
        storageFormLayout.addComponent(buttonLayout);


    }

    private void updatePerimeter(String newApplication) {

        log.info("new application " + newApplication + " -> update Perimeter");

        if ("SLA".equals(newApplication)) {
            perimeter.setItems("GLE", "LGE");
            perimeter.setValue("GLE");
            perimeter.setSelectedItem("GLE");
            perimeter.setEnabled(true);
            return;
        }

        perimeter.setItems(Stream.empty());
        perimeter.setValue("");
        perimeter.setSelectedItem("");
        perimeter.setEnabled(false);
    }

    private void updateFolder(String newPerimeter) {

        log.info("new perimeter " + newPerimeter + " -> update Folder");

        Optional<String> app = application.getSelectedItem();

        if (app.isPresent()) {
            if ("SLA".equals(app.get())) {
                if ("GLE".equals(newPerimeter)) {
                    folder.setItems("target");
                    folder.setValue("target");
                    folder.setSelectedItem("target");
                    folder.setEnabled(true);
                    return;
                }
            }
        }

        folder.setItems(Stream.empty());
        folder.setValue("");
        folder.setSelectedItem("");
        folder.setEnabled(false);
    }

    private void updateFile(String newFolder) {

        log.info("new folder " + newFolder + " -> update File");

        Optional<String> app = application.getSelectedItem();
        Optional<String> perim = perimeter.getSelectedItem();

        if (app.isPresent() && perim.isPresent()) {
            if ("SLA".equals(app.get())) {
                if ("GLE".equals(perim.get())) {
                    if ("target".equals(newFolder)) {
                        file.setItems("test.txt");
                        file.setValue("test.txt");
                        file.setSelectedItem("test.txt");
                        file.setEnabled(true);
                        return;
                    }
                }
            }
        }

        file.setItems(Stream.empty());
        file.setValue("");
        file.setSelectedItem("");
        file.setEnabled(false);
    }
}