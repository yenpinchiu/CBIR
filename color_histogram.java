
public class color_histogram {
	
	double[][][] rgb_bin = null;
	double[] gray_bin = null;
	double[][][] hsv_bin = null;

	int[] rgb_bin_size= new int[3];
	int gray_bin_size;
	int[] hsv_bin_size = new int[3];

		public color_histogram(int[][][] picure){
			
			rgb_bin_size[0] = 8;
			rgb_bin_size[1] = 8;
			rgb_bin_size[2] = 8;
			gray_bin_size = 8;
			hsv_bin_size[0] = 3;
			hsv_bin_size[1] = 5;
			hsv_bin_size[2] = 5;
			
			rgb_bin = new double[(int)Math.ceil((double)256/rgb_bin_size[0])][(int)Math.ceil((double)256/rgb_bin_size[1])][(int)Math.ceil((double)256/rgb_bin_size[2])];
			gray_bin = new double[(int)Math.ceil((double)256/gray_bin_size)];
			hsv_bin = new double [(int)Math.ceil((double)361/hsv_bin_size[0])] [(int)Math.ceil((double)101/hsv_bin_size[1]) ][(int)Math.ceil((double)101/hsv_bin_size[2]) ];
			
			for(int i=0;i<picure.length;i++){
				for(int j=0;j<picure[i].length;j++){			
					rgb_bin[picure[i][j][0]/rgb_bin_size[0]][picure[i][j][1]/rgb_bin_size[1]][picure[i][j][2]/rgb_bin_size[2]] += (double)1/picure.length*picure[i].length;
					double gray = tools.rgb_gray_converter(picure[i][j]);
					gray_bin[(int)(gray/gray_bin_size)] += (double)1/picure.length*picure[i].length;
					double[] hsv = tools.rgb_hsv_converter(picure[i][j]);
					hsv_bin[(int)(hsv[0]/hsv_bin_size[0])][(int)(hsv[1]*100/hsv_bin_size[1])][(int)(hsv[2]*100/hsv_bin_size[2])] += (double)1/picure.length*picure[i].length;
				}
			}
		}
}
