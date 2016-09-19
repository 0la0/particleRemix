package etc.a0la0.particleRemix.ui;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import etc.a0la0.particleRemix.messaging.ParameterService;
import javafx.geometry.Point2D;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;

public class MotionDetectionService {
	
	private ParameterService parameterService;
	private WritablePixelFormat<IntBuffer> format = WritablePixelFormat.getIntArgbInstance();
	
	public MotionDetectionService (ParameterService parameterService) {
		this.parameterService = parameterService;
	}
	
	public void runMotionDetection (WritableImage currentScreenshot, WritableImage previousScreenshot) {
		boolean widthIsEqual = currentScreenshot.getWidth() == previousScreenshot.getWidth();
		boolean heightIsEqual = currentScreenshot.getHeight() == previousScreenshot.getHeight();
		if (!widthIsEqual || !heightIsEqual) return;
		
		int width = (int) currentScreenshot.getWidth();
		int height = (int) currentScreenshot.getHeight();
		
		int totalPixels = width * height;
	
		int[] thisBuffer = new int[totalPixels];
		int[] previousBuffer = new int[totalPixels];
		
		PixelReader currentPixelBuffer = currentScreenshot.getPixelReader();
		PixelReader previousPixelBuffer = previousScreenshot.getPixelReader();
		
		currentPixelBuffer.getPixels(0, 0, width, height, format, thisBuffer, 0, width);
		previousPixelBuffer.getPixels(0, 0, width, height, format, previousBuffer, 0, width);

		List<Point2D> pointList = IntStream.range(0, thisBuffer.length)
				.filter(index -> {
					double colorDistance = getColorDistance(thisBuffer[index], previousBuffer[index]);
					return colorDistance > parameterService.getMotionThreshold();
				})
				.mapToObj(index -> {
					int x = index % width;
					int y = index / width;
					return new Point2D(x, y);
				})
				.collect(Collectors.toList());
		
		parameterService.setMotionPointList(pointList);
	}
	
	private double getColorDistance (int thisImage, int previousImage) {
		int thisR = thisImage >>> 16 & 0xFF;
		int lastR = previousImage >>> 16 & 0xFF;
		
		int thisG = thisImage >>> 8 & 0xFF;
		int lastG = previousImage >>> 8 & 0xFF;
		
		int thisB = thisImage & 0xFF;
		int lastB = previousImage & 0xFF;
		
		double distanceR = Math.pow(thisR - lastR, 2);
		double distanceG = Math.pow(thisG - lastG, 2);
		double distanceB = Math.pow(thisB - lastB, 2);
		
		return Math.sqrt(distanceR + distanceG + distanceB);
	}

}
