package params;

import java.io.FileWriter;
import java.io.IOException;

import org.opencv.core.Mat;

public class Img {
	private int id;
	private String path;
	private int height;
	private int width;
	private Mat mat;
	private double[] piexl;

	public Mat getMat(){
		return mat;
	}
	public String getPath() {
		return path;
	}

	public int getId() {
		return id;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	public double[] getPiexl()
	{
		return piexl;
	}
	
	public double[] setPiexl(double[] s){
		piexl = s;
		return piexl;
	}
	
	public void printPiexl() throws IOException{
		

		FileWriter fw = new FileWriter("./data.txt");
	
		for(int i = 0 ;i<piexl.length;i++){
			
				fw.write(this.piexl[i]+" ");
				//System.out.print(this.piexl[i]+" ");
			fw.write("\n");
		}
		fw.close();
	}

	public Img(int id, int width, int height, String path, double[] piexl) {
		this.id = id;
		this.height = height;
		this.width = width;
		this.path = path;
		this.piexl = piexl;
	}

}
