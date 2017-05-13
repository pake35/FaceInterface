package function;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/*
 * Detects faces in an image, draws boxes around them, and writes the 
 results
 * to "faceDetection.png".
 */
public class DetectFaceDemo {
	public void detectface(String url) {
		Queue<Mat> facefinal = new PriorityQueue<Mat>(7, idComparator);
		// System.out.println("\nRunning DetectFaceDemo");

		// Create a face detector from the cascade file in the resources
		// directory.
		CascadeClassifier faceDetector = new CascadeClassifier(
				"./data/haarcascade_frontalface_alt2.xml");

		Mat image = Imgcodecs.imread(url);


		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);

		int i = 1;
		for (Rect rect : faceDetections.toArray()) {
			Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(
					rect.x + rect.width, rect.y + rect.height), new Scalar(255,
					0, 0));
			Rect rect_1 = new Rect(new Point(rect.x, rect.y), new Point(rect.x
					+ rect.width, rect.y + rect.height));
			Mat mat = new Mat(image, rect_1);
			System.out.println(mat.height());
			facefinal.add(mat);
			i++;
		}
		System.out.println(facefinal.size()+"���г���");
		if(facefinal.isEmpty()) return;
		Mat mat = facefinal.poll();
		Imgcodecs.imwrite(url, mat);


	}

	public void detectEyes(Mat image) {
		CascadeClassifier eyesdetect = new CascadeClassifier(
				"./data/haarcascade_eye.xml");

	}

	public static Comparator<Mat> idComparator = new Comparator<Mat>() {

		public int compare(Mat c1, Mat c2) {
			return (int) (c2.height() - c1.height());
		}
	};

}
