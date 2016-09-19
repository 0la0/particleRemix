package etc.a0la0.particleRemix.ui.util;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;

public class DraggableFxWorld {
	
	private PerspectiveCamera camera = new PerspectiveCamera(true);
	
	private Xform cameraXform;
	private Xform cameraXform2 = new Xform();
	private Xform cameraXform3 = new Xform();
	private double cameraDistance = 100;
	
	private double mousePosX;
	private double mousePosY;
	private double mouseOldX;
	private double mouseOldY;
	private double mouseDeltaX;
	private double mouseDeltaY;
	
	
	public DraggableFxWorld (SubScene scene, Group root, Xform cameraXform) {
		this.cameraXform = cameraXform;
		this.buildCamera(root);
		this.handleMouse(scene);
		scene.setCamera(camera);
	}
	
	private void buildCamera(Group root) {
		root.getChildren().add(cameraXform);
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(cameraXform3);
		cameraXform3.getChildren().add(camera);
		cameraXform3.setRotateZ(180.0);

		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		camera.setTranslateZ(-cameraDistance);
		//cameraXform.ry.setAngle(320.0);
	}
	
	/*
	 * 3D camera rotate on mouse drag
	 * from http://docs.oracle.com/javafx/8/3d_graphics/sampleapp.htm
	 */
	public void handleMouse(SubScene scene) {
		
		scene.setOnMousePressed(mouseEvent -> {
			mousePosX = mouseEvent.getSceneX();
			mousePosY = mouseEvent.getSceneY();
			mouseOldX = mouseEvent.getSceneX();
			mouseOldY = mouseEvent.getSceneY();
		});
		
		scene.setOnMouseDragged(mouseEvent -> {
			mouseOldX = mousePosX;
			mouseOldY = mousePosY;
			mousePosX = mouseEvent.getSceneX();
			mousePosY = mouseEvent.getSceneY();
			mouseDeltaX = (mousePosX - mouseOldX);
			mouseDeltaY = (mousePosY - mouseOldY);

			double modifier = 1.0;
			double modifierFactor = 0.1;

			if (mouseEvent.isControlDown()) {
				modifier = 0.1;
			}
			if (mouseEvent.isShiftDown()) {
				modifier = 10.0;
			}
			if (mouseEvent.isPrimaryButtonDown()) {
				cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX * modifierFactor * modifier * 2.0);  // +
				cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY * modifierFactor * modifier * 2.0);  // -
			} else if (mouseEvent.isSecondaryButtonDown()) {
				double z = camera.getTranslateZ();
				double newZ = z + mouseDeltaX * modifierFactor * modifier;
				camera.setTranslateZ(newZ);
			} else if (mouseEvent.isMiddleButtonDown()) {
				cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX * modifierFactor * modifier * 0.3);  // -
				cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY * modifierFactor * modifier * 0.3);  // -
			}
		});
		
	}
	
	public void setCameraDistance (double cameraDistance) {
		this.cameraDistance = cameraDistance;
		camera.setTranslateZ(-cameraDistance);
	}

}
