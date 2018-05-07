package org.architecture;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomButton extends Button {
    public CustomButton(Image image) {
        final ImageView imgView = new ImageView(image);
        this.getChildren().add(imgView);

        super.setGraphic(imgView);
    }
}
