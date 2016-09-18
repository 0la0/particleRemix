package etc.a0la0.particleRemix.ui;

import etc.a0la0.particleRemix.messaging.ParameterService;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

class RenderPoint {
	
	private int samplePointX;
	private int samplePointY;
	private Point3D renderPosition;
	private Color color;
	private ParameterService parameterService;
	private boolean nothingToRender = false;
	
	public RenderPoint (WritableImage screenshot, ParameterService parameterService) {
		this.parameterService = parameterService;
		
		if (parameterService.getMotionThreshold() > 0) {
			if (parameterService.getMotionPointList().size() > 0) {
				//get sample point from lookup
				int randIndex = (int) (parameterService.getMotionPointList().size() * Math.random());
				Point2D samplePoint = parameterService.getMotionPointList().get(randIndex);
				samplePointX = (int) samplePoint.getX();
				samplePointY = (int) samplePoint.getY();
			}
			else {
				nothingToRender = true;
			}
		}
		else {
			int imageWidth = (int) screenshot.getWidth();
			int imageHeight = (int) screenshot.getHeight();
			samplePointX = (int) (imageWidth * Math.random());
			samplePointY = (int) (imageHeight * Math.random());
		}
		
		color = screenshot.getPixelReader()
					.getColor(samplePointX, samplePointY);
		
		renderPosition = new Point3D(
				-samplePointX + (screenshot.getWidth() / 2),
				-samplePointY + (screenshot.getHeight() / 2),
				0);
	}
	
	public Point3D getRenderPosition () {
		return renderPosition;
	}
	
	public Color getColor () {
		return color;
	}
	
	public Point3D createNewVelocity () {
		double x = parameterService.getInitialVelocity() * getPosNeg() * Math.random();
		double y = parameterService.getInitialVelocity() * getPosNeg() * Math.random();
		double z = parameterService.getInitialVelocity() * getPosNeg() * Math.random();
		return new Point3D(x, y, z);
	}
	
	public double getTTL () {
		return parameterService.getTtlUpperBound() * Math.random();
	}
	
	public boolean hasNothingToRender () {
		return nothingToRender;
	}
	
	private int getPosNeg () {
		return (Math.random() < 0.5) ? 1 : -1;
	}
	
}

