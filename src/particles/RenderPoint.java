package particles;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point3D;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

class RenderPoint {
	
	private int samplePointX;
	private int samplePointY;
	private Point3D renderPosition;
	private Color color;
	ParameterService parameterService;
	
	public RenderPoint (BufferedImage screenCapture, ParameterService parameterService) {
		this.parameterService = parameterService;
		WritableImage screenshot = SwingFXUtils.toFXImage(screenCapture, null);	
		
		if (this.parameterService.isMotionDetection()) {
			//get sample point from lookup
		}
		else {
			int imageWidth = (int) screenshot.getWidth();
			int imageHeight = (int) screenshot.getHeight();
			this.samplePointX = (int) (imageWidth * Math.random());
			this.samplePointY = (int) (imageHeight * Math.random());
		}
		
		this.color = screenshot.getPixelReader()
					.getColor(this.samplePointX, this.samplePointY);
		
		this.renderPosition = new Point3D(
				-this.samplePointX + (screenshot.getWidth() / 2),
				-this.samplePointY + (screenshot.getHeight() / 2),
				0);
	}
	
	public Point3D getRenderPosition () {
		return this.renderPosition;
	}
	
	public Color getColor () {
		return this.color;
	}
	
	public Point3D createNewVelocity () {
		double x = this.parameterService.getInitialVelocity() * this.getPosNeg() * Math.random();
		double y = this.parameterService.getInitialVelocity() * this.getPosNeg() * Math.random();
		double z = this.parameterService.getInitialVelocity() * this.getPosNeg() * Math.random();
		return new Point3D(x, y, z);
	}
	
	public double getTTL () {
		return this.parameterService.getTtlUpperBound() * Math.random();
	}
	
	private int getPosNeg () {
		return (Math.random() < 0.5) ? 1 : -1;
	}
	
}

