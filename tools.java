import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

public class tools {
	
	static public int[][][] open_picture(String picture_name,boolean rgb) {	
		BufferedImage image = null;
		try{
			image = ImageIO.read(new File(picture_name));
		} catch (IOException e){System.out.println("picture not found : " +picture_name );return null;}
		int[][][] image_array = new int[image.getWidth()][image.getHeight()][3];
		if(rgb==true){image_array = new int[image.getWidth()][image.getHeight()][3];}
		else {image_array = new int[image.getWidth()][image.getHeight()][1];}
		int pixel;
		for(int i=0;i<image.getWidth();i++){
			for(int j=0;j<image.getHeight();j++){
				pixel = image.getRGB(i,j);
				if(rgb==true){
					image_array[i][j][0] = (pixel >> 16) & 0xff;
					image_array[i][j][1] = (pixel >> 8) & 0xff;
					image_array[i][j][2] =  (pixel) & 0xff;
				}else{image_array[i][j][0] =pixel; }
			}
	   }
		return image_array;
	}
	
	static public double[] rgb_hsv_converter(int[] rgb){
		double  r= (double) rgb[0]/256;
		double  g= (double) rgb[1]/256;
		double  b= (double) rgb[2]/256;
		double minrgb = Math.min(r,Math.min(g,b));
		double maxrgb = Math.max(r,Math.max(g,b));
		 if (minrgb==maxrgb) { 
			 double[] hsv = {0,0,minrgb};
			 return hsv; 
		}
		double d = (r==minrgb) ? g-b : ((b==minrgb) ? r-g : b-r);
		double h = (r==minrgb) ? 3 : ((b==minrgb) ? 1 : 5);
		double ch = 60*(h - d/(maxrgb - minrgb));
		double cs = (maxrgb - minrgb)/maxrgb;
		double cv = maxrgb;
		double[] hsv =  {ch,cs,cv};
	    return hsv;
	}
	
	static public double rgb_gray_converter(int[] rgb){
		return (double)rgb[0]* 0.30+  (double)rgb[1]* 0.59 +  (double)rgb[2] * 0.11;
	}
	
	static TreeMap<String,Double> SortByValue (HashMap<String, Double> map) {
		tools.ValueComparator vc =  new tools.ValueComparator(map);
		TreeMap<String,Double> sortedMap = new TreeMap<String,Double>(vc);
		sortedMap.putAll(map);
		return sortedMap;
	}
	
	static class ValueComparator implements Comparator<String> {
	    Map<String, Double> map;
	    public ValueComparator(Map<String, Double> base) {
	        this.map = base;
	    } 
	    public int compare(String a, String b) {
	        if (map.get(a) <= map.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        }
	    }
	}
	
	static public int[][][][][] split(int[][][] picture,int row ,int column){
		int[][][][][] split_picture = new int[row][column][picture.length/row][picture[0].length/column][];
		for(int i=0;i<picture.length;i++){
			for(int j=0;j<picture[0].length;j++){
				if( (i/(picture.length/row)) < row && (j/(picture[0].length/column))<column ){
					split_picture[i/(picture.length/row)][j/(picture[0].length/column)][i-(i/(picture.length/row))*(picture.length/row)][j-(j/(picture[0].length/column))*(picture[0].length/column)] = picture[i][j];	
				}
			}
		}
		return split_picture;
	}
	
	static public double caculate_l1_distance(ArrayList<Double> feature_list_1,ArrayList<Double> feature_list_2){
		double l1_distance = 0;
		for (int i=0;i<feature_list_1.size();i++){
			l1_distance += Math.abs(feature_list_1.get(i) - feature_list_2.get(i)); 	
		}
		return l1_distance;
	}
}
