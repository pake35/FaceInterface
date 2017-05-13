package function;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;

import params.Img;

public class Pretreatment {
	float[][] aa;
	 final int shu = 1;//高斯模糊半径
	 final int  size = 2*shu+1;//数组大小   
	// 色彩均衡，取消光照误
	public BufferedImage getColorImage(String url) throws IOException {
		File filename = new File(url);
		BufferedImage slt = ImageIO.read(filename);
		BufferedImage image = new BufferedImage(30,30,BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(slt,0,0,30,30,null);
		int RedTotal = 0;
		int GreenTotal = 0;
		int BlueTotal = 0;
		int GrayTotal = 0;
		int NumTotal, RedAverage, GreenAverage, BlueAverage, GrayAverage;
		int RedTemp, GreenTemp, BlueTemp;

		float Kr, Kb, Kg;
		Color myWhite;
		int width = image.getWidth();
		int height = image.getHeight();

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);
				RedTotal += (rgb & 0xff0000) >> 16;
				GreenTotal += (rgb & 0xff00) >> 8;
				BlueTotal += (rgb & 0xff);

			}
		}

		NumTotal = width * height;
		// System.out.println(NumTotal);

		RedAverage = RedTotal / NumTotal;
		GreenAverage = GreenTotal / NumTotal;
		BlueAverage = BlueTotal / NumTotal;
		// System.out.println(RedAverage + " " + GreenAverage + " " +
		// BlueAverage);

		GrayAverage = (RedAverage + GreenAverage + BlueAverage) / 3;

		// System.out.println("hhh" + GrayAverage);
		Kr = (float) GrayAverage / RedAverage;
		Kg = (float) GrayAverage / GreenAverage;
		Kb = (float) GrayAverage / BlueAverage;

		// System.out.println(Kr + " " + Kg + " " + Kb);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);

				RedTemp = (int) (((rgb & 0xff0000) >> 16) * Kr);
				GreenTemp = (int) (((rgb & 0xff00) >> 8) * Kg);
				BlueTemp = (int) (((rgb & 0xff)) * Kb);

				float factor = Max(RedTemp, GreenTemp, BlueTemp);
				factor = (float) factor / 255;
				/*
				 * RedTemp = RedTemp > 255 ? 255 : RedTemp; GreenTemp =
				 * GreenTemp > 255 ? 255 : GreenTemp; BlueTemp = BlueTemp > 255
				 * ? 255 : BlueTemp;
				 */

				// System.out.println(factor);
				if (factor > 1) {
					RedTemp = (int) (RedTemp / factor);
					GreenTemp = (int) (GreenTemp / factor);
					BlueTemp = (int) (BlueTemp / factor);
				}
				myWhite = new Color(RedTemp, GreenTemp, BlueTemp);

				int color = myWhite.getRGB();

				image.setRGB(i, j, color);
				// System.out.println(color);

			}
		}
		// ImageIO.write(image,"jpg",new File("./test/test1.jpg"));
		return image;

	}

	private int Max(int redTemp, int greenTemp, int blueTemp) {
		// TODO Auto-generated method stub
		int index;
		if (redTemp > greenTemp) {
			index = redTemp;
		} else {
			index = greenTemp;
		}
		if (index < blueTemp) {
			index = blueTemp;
		}
		return index;
	}

	// 高斯平滑
	public BufferedImage GausslanBlur(BufferedImage image) throws IOException {
		// File file = new File(url);
		// BufferedImage image = ImageIO.read(file);
		aa = GaussUtil.get2(GaussUtil.get2DKernalData(shu, 1.5f));
		int width = image.getWidth();
		int height = image.getHeight();
		// System.out.println("�?"+width+"�?"+height);
		int[][] martrix = new int[3][3];
		int[] values = new int[9];
		// System.out.println("for begin:"+values[8]);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				readPixel(image, i, j, values);
				fillMatrix(martrix, values);
				image.setRGB(i, j, avgMatrix(martrix));
			}
		}
		// ImageIO.write(image,"jpg",new File("./test/test2.jpg"));
		return image;
	}

	private int avgMatrix(int[][] martrix) {
		// TODO Auto-generated method stub
		int r = 0;
		int g = 0;
		int b = 0;
		for (int i = 0; i < martrix.length; i++) {
			int[] x = martrix[i];
			for (int j = 0; j < x.length; j++) {
				if (j == 1) {
					continue;
				}
				Color c = new Color(x[j]);
				r += c.getRed();
				g += c.getGreen();
				b += c.getBlue();

			}
		}
		return new Color(r / 8, g / 8, b / 8).getRGB();
	}

	private void fillMatrix(int[][] martrix, int[] values) {
		// TODO Auto-generated method stub
		int filled = 0;
		for (int i = 0; i < martrix.length; i++) {
			int[] x = martrix[i];
			for (int j = 0; j < x.length; j++) {
				x[j] = values[filled++];
			}
		}
	}

	private void readPixel(BufferedImage image, int i, int j, int[] values) {
		// TODO Auto-generated method stub
		int xStart = i - 1;
		int yStart = j - 1;

		int current = 0;
		for (int x = xStart; x < 3 + xStart; x++) {
			for (int y = yStart; y < 3 + yStart; y++) {
				// System.out.println("x:"+x+"y:"+y);
				int tx = x;
				if (tx < 0) {
					tx = -tx;
				} else if (tx >= image.getWidth()) {
					tx = i;
				}
				int ty = y;
				if (ty < 0) {
					ty = -ty;
				} else if (ty >= image.getHeight()) {
					ty = j;
				}
				// System.out.println("tx:"+tx+"ty:"+ty);
				// System.out.println(current);
				values[current++] = image.getRGB(tx, ty);
			}
		}
	}

	// 灰度处理
	public BufferedImage getGrayImage(BufferedImage image) throws IOException {
		// File filename = new File(url);
		// BufferedImage image = ImageIO.read(filename);
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage grayImage = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_GRAY);
		int tr = 0, tg = 0, tb = 0;
		int ta = 0;
		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);
				ta = (rgb >> 24) & 0xff;
				tr = (rgb >> 16) & 0xff;
				tg = (rgb >> 8) & 0xff;
				tb = rgb & 0xff;
				int gray = (int) (0.299 * tr + 0.587 * tg + 0.114 * tb);
				rgb = (ta << 24) | (gray << 16) | (gray << 8) | gray;
				grayImage.setRGB(i, j, rgb);
			}
		}
		// ImageIO.write(grayImage,"jpg",new File("./test/test3.jpg"));
		return grayImage;
	}

	public Img getGrayImage(String url, BufferedImage image, int id) throws IOException {
		// File filename = new File(url);
		// BufferedImage image = ImageIO.read(filename);
		int width = image.getWidth();
		int height = image.getHeight();
		double[] piexl = new double[width * height];

		BufferedImage grayImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		int tr = 0, tg = 0, tb = 0;
		int ta = 0;
		int index = 0;
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < height; col++) {
				index = row * width + col;
				int rgb = image.getRGB(row, col);
				ta = (rgb >> 24) & 0xff;
				tr = (rgb >> 16) & 0xff;
				tg = (rgb >> 8) & 0xff;
				tb = rgb & 0xff;
				int gray = (int) (0.299 * tr + 0.587 * tg + 0.114 * tb);
				rgb = (ta << 24) | (gray << 16) | (gray << 8) | gray;
				piexl[index] = rgb;
				grayImage.setRGB(row, col, rgb);
			}
		}
		ImageIO.write(grayImage, "jpg", new File("./test/test4.jpg"));
		// 处理过的图片路径写入文件
		String urls = "./data/AfterPrepare.txt";
		String contain = id + " " + "dealedImage" + " " + "prepared" + id
				+ ".jpg";
		// FileOutputStream in = new FileOutputStream(urls);
		BufferedWriter in = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(urls, true)));
		in.write(contain + "\n");
		in.close();
		String string = "./dealedImage/" + "prepare" + id + ".jpg";
		File newFile = new File(string);
		ImageIO.write(grayImage, "jpg", newFile);
		Img img = new Img(id, width, height, url, piexl);
		return img;
	}

	// 二�?�化
	public Img getBinaryImage(BufferedImage image, int id)
			throws IOException {
		// File filename = new File(url);
		// BufferedImage image = ImageIO.read(filename);
		int width = image.getWidth();
		int height = image.getHeight();
		double[] piexl = new double[width * height];
		BufferedImage binaryImage = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_BINARY);
		int index = 0;
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				index = row * width + col;
				int rgb = image.getRGB(row, col);
				binaryImage.setRGB(row, col, rgb);
				piexl[index] = rgb;
				// System.out.println(piexl[i][j]);
			}
			// System.out.println("\n");
		}
		ImageIO.write(binaryImage, "jpg", new File("./test/test4.jpg"));
		// 处理过的图片路径写入文件
		String urls = "./data/AfterPrepare.txt";
		String contain = id + " " + "dealedImage" + " " + "prepared" + id
				+ ".jpg";
		// FileOutputStream in = new FileOutputStream(urls);
		BufferedWriter in = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(urls, true)));
		in.write(contain + "\n");
		in.close();
		String url = "./dealedImage/" + "prepare" + id + ".jpg";
		File newFile = new File(url);
		ImageIO.write(binaryImage, "jpg", newFile);
		Img img = new Img(id, width, height, url, piexl);
		return img;
	}

	public Img prepare(String url, int id) throws IOException {

		BufferedImage image = this.getColorImage(url);
		ImageIO.write(image, "jpg", new File("./test/color.jpg"));
	    //image = this.GausslanBlur(image);
		// ImageIO.write(image, "jpg", new File("./test/gauss.jpg"));
		//image = this.getGrayImage(image);
		// ImageIO.write(image, "jpg", new File("./test/gray.jpg"));
		Img img = this.getGrayImage(url, image, id);
		//Img img = this.getBinaryImage(image, id);
		return img;
	}

}
