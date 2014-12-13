import java.util.HashMap;
import java.util.TreeMap;

public class uniform_local_binary_patterns {
	
	public HashMap<String,Double> uniform_local_binary_patterns_histogram_map = new HashMap<String,Double>();
	public TreeMap<String, Double> uniform_local_binary_patterns_histogram_map_sorted;
	
	public uniform_local_binary_patterns(int[][][] picure){
		
		double[][] gray_picture = new double[picure.length][picure[0].length];
		for(int i=0;i<picure.length;i++){
			for(int j=0;j<picure[i].length;j++){			
				gray_picture[i][j] = tools.rgb_gray_converter(picure[i][j]);
			}
		}
		
		for (int i=0;i<256;i++){
			if(test_uniform_or_not(Integer.toBinaryString(i))==true){
				String local_binary_patterns = Integer.toBinaryString(i);
				while(local_binary_patterns.length()!=8){
					local_binary_patterns = 0 + local_binary_patterns;
				}
				uniform_local_binary_patterns_histogram_map.put(local_binary_patterns ,0.0);		
			}
		}
		//activate this if you want to make the 59th feature contain all the non-uniform patterm
		//uniform_local_binary_patterns_histogram_map.put("not_uniform" ,0.0);
		
		for(int i=1;i<gray_picture.length-1;i++){
			for(int j=1;j<gray_picture[i].length-1;j++){	
				String local_binary_patterns = "";
				if(gray_picture[i-1][j+1]>gray_picture[i][j]){
					local_binary_patterns += "1";
				}else{local_binary_patterns += "0";}
				if(gray_picture[i][j+1]>gray_picture[i][j]){
					local_binary_patterns += "1";
				}else{local_binary_patterns += "0";}
				if(gray_picture[i+1][j+1]>gray_picture[i][j]){
					local_binary_patterns += "1";
				}else{local_binary_patterns += "0";}
				if(gray_picture[i-1][j]>gray_picture[i][j]){
					local_binary_patterns += "1";
				}else{local_binary_patterns += "0";}
				if(gray_picture[i+1][j]>gray_picture[i][j]){
					local_binary_patterns += "1";
				}else{local_binary_patterns += "0";}
				if(gray_picture[i-1][j-1]>gray_picture[i][j]){
					local_binary_patterns += "1";
				}else{local_binary_patterns += "0";}
				if(gray_picture[i][j-1]>gray_picture[i][j]){
					local_binary_patterns += "1";
				}else{local_binary_patterns += "0";}
				if(gray_picture[i+1][j-1]>gray_picture[i][j]){
					local_binary_patterns += "1";
				}else{local_binary_patterns += "0";}
				
				if(test_uniform_or_not(local_binary_patterns)==true){
						uniform_local_binary_patterns_histogram_map.put(local_binary_patterns, uniform_local_binary_patterns_histogram_map.get(local_binary_patterns)+(double)1/(picure.length*picure[0].length));
				}//activate this if you want to make the 59th feature contain all the non-uniform patterm
				//else{uniform_local_binary_patterns_histogram_map.put("not_uniform", uniform_local_binary_patterns_histogram_map.get("not_uniform")+(double)1/(picure.length*picure[0].length));}
			}
		}
		 uniform_local_binary_patterns_histogram_map_sorted = new TreeMap<String, Double>(uniform_local_binary_patterns_histogram_map);
	}
	
	public boolean test_uniform_or_not(String pattern){
		int change_count = 0;
		char tmp_char = pattern.charAt(0);
		for (int i = 1, n = pattern.length(); i < n; i++) {
		    char c = pattern.charAt(i);
		    if (tmp_char!= c){change_count++;}
		    tmp_char = c;
		}
		if(change_count>2){return false;}
		return true;
	}
}
