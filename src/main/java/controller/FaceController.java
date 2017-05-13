package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modal.data;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import params.Img;
import service.BaseService;
import service.impl.BaseServiceImpl;
import Jama.Matrix;

import com.alibaba.fastjson.JSONObject;

import function.DetectFaceDemo;
import function.Pca;
import function.Pretreatment;

@Controller
public class FaceController {
	@Autowired
	private BaseService bs;
	Vector<double[]> modelOFace = new Vector<double[]>();

	@RequestMapping("/face")
	@ResponseBody
	public void Face(HttpServletRequest req, HttpServletResponse res) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// 接受数据
		String url = (String) req.getAttribute(url);  //拿到新图片的url
		List<Img> imageurls = new ArrayList<Img>();   //用来存储图片信息
		List<Object> answer = new ArrayList<Object>();    //存储返回的数据
		data imagedata = new data();                  //新加入图片的数据库信息
		data imageans = new data();                   //得到的结果图片的数据库信息
		imagedata.serUrl(url);
		bs.create(imagedata);
		int id = bs.getData(url).getId(); 
		
		Pretreatment pre = new Pretreatment();
		Mat image = Imgcodecs.imread(url);
		if(image.empty()) {
			System.out.println("未发现人脸");
			return;
		}
		DetectFaceDemo face = new DetectFaceDemo();
		face.detectface(url);
		Img img = pre.prepare(url, id);
		modelOFace.add(img.getPiexl());
		imageurls.add(img);
		
		
		
		

		double[][] faces = (double[][]) modelOFace.toArray(new double[0][0]);
		int n = faces.length;
		int p = faces[0].length;

		Pca pca = new Pca();
		double[][] stand = pca.Standardlizer(faces);
		double[][] assosiation = pca.CoefficientOfAssociation(stand);
		double[][] flagValue = pca.FlagValue(assosiation);

		double[][] flagVector = pca.FlagVector(assosiation);

		int[] xuan = pca.SelectPrincipalComponent(flagValue);

		System.out.println();
		System.out.println();
		double[][] result = pca.PrincipalComponent(flagVector, xuan);
		Matrix A = new Matrix(faces);
		Matrix B = new Matrix(result);
		Matrix C = A.times(B);
		C.print(4, 2);

		double[] ans = new double[faces.length];
		double[][] D = C.getArray();

		double[] E = new double[faces.length];
		int k = 0;
		double sumTop = 0;
		double sum1 = 0;
		double sum2 = 0;
		for (int i = 0; i < D.length; i++) {
			for (int j = 0; j < D[0].length; j++) {
				ans[i] += (D[k][j] - D[i][j]) * (D[k][j] - D[i][j]);
				sumTop += D[k][j] * D[i][j];
				sum1 += D[k][j] * D[k][j];
				sum2 += D[i][j] * D[i][j];
			}
			ans[i] = Math.sqrt(ans[i]);
			double cos = sumTop / (Math.sqrt(sum1) * Math.sqrt(sum2));
			cos = 0.5 + 0.5 * cos;
			if(ans[i] < 1.5) {
				imageans.serUrl(imageurls.get(i).getPath());
			}
			System.out.println("����" + ans[i] + "���ƶ�" + cos + "Ϊ��" + i
					+ "������" + "路由" + imageurls.get(i).getPath());
		}
		res.setContentType("application/json; charset=utf-8");
		JSONObject responseJSONObject = JSONObject.fromObject(answer);
		res.getWriter().write(responseJSONObject.toJSONString());
	}
}
