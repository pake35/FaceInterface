package function;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import params.Img;

public class compareFace {
	public void compare1(Img img1, Img img2){
		
	}
	public void compare(Img img1, Img img2) {
		int similiar = 0;
		int usimiliar = 0;
		
		double[] value1 = img1.getPiexl();
		Mat mat1 = new Mat(img1.getWidth(),img1.getHeight(),CvType.CV_8UC1);
		for(int row=0;row<img1.getWidth();row++){
			for(int col = 0;col<img1.getHeight();col++){
				System.out.println(value1[row*col]);
				mat1.put(row, col, value1[row*col]);
			}	
		}

		double[] value2 = img2.getPiexl();
		Mat mat2 = new Mat(img2.getWidth(),img2.getHeight(),CvType.CV_8UC1);
		for(int row=0;row<img2.getWidth();row++){
			for(int col = 0;col<img2.getHeight();col++){
				System.out.println(value2[row*col]);
				mat2.put(row, col, value2[row*col]);
			}	
		}
//		int length1 = value1.length;
//		int length2 = value2.length;
//		float scale = (float)length1/length2;
//		Size dst_cvsize = new Size();
//		dst_cvsize.width = mat1.width() / scale;
//		dst_cvsize.height = mat1.height() / scale;
//		Mat dst = new Mat(dst_cvsize, CvType.CV_8UC1);
//      Imgproc.resize(mat1, dst, dst_cvsize); //缩放源图像到目标图像  
		Imgproc.equalizeHist( mat1, mat1);
		Imgproc.equalizeHist(mat2, mat2);
        
		
        
		String res = "";
		try {
			res = ((Double.parseDouble(similiar + "") / Double
					.parseDouble((similiar + usimiliar) + "")) + "");
			res = res.substring(res.indexOf(".") + 1, res.indexOf(".") + 3);
		} catch (Exception e) {
			res = "0";
		}
		if (res.length() <= 0) {
			res = "0";
		}
		if (usimiliar == 0) {
			res = "100";
		}
		System.out.println("相似像素数量" + similiar + "不相似像素数量" + usimiliar + "相似率"
				+ Integer.parseInt(res) + "%");
	}
}
