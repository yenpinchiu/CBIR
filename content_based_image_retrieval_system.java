import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public class content_based_image_retrieval_system {

	HashMap<String,HashMap<String,ArrayList<Double>>> picture_feature_maps_map = new HashMap<String,HashMap<String,ArrayList<Double>>>();
	
	public content_based_image_retrieval_system(String all_picture_path){

		File dir = new File(all_picture_path);
		File[] filelist= dir.listFiles();
		for(int i=0;i<filelist.length;i++){
			int[][][] current_picture = tools.open_picture(filelist[i].toString(),true);
			picture_feature_maps_map.put(filelist[i].toString(),new HashMap<String,ArrayList<Double>>());
			
			//////// Choose the features used. Note that the random projection features are depend on the uniform local binary patterns features so thet must be activate at the same time.////
			picture_feature_maps_map.get(filelist[i].toString()).put("regional_color_histogram_feature",regional_color_histogram_feature(current_picture,2 ,2));
			picture_feature_maps_map.get(filelist[i].toString()).put("uniform_local_binary_patterns_feature",uniform_local_binary_patterns_feature(current_picture,2 ,2));
			//picture_feature_maps_map.get(filelist[i].toString()).put("random_projection_on_uniform_local_binary_patterns_feature_1/4d",new random_projection(picture_feature_maps_map.get(filelist[i].toString()).get("uniform_local_binary_patterns_feature"),1*picture_feature_maps_map.get(filelist[i].toString()).get("uniform_local_binary_patterns_feature").size()/4,1).random_projection_features);
			//picture_feature_maps_map.get(filelist[i].toString()).put("random_projection_on_uniform_local_binary_patterns_feature_2/4d",new random_projection(picture_feature_maps_map.get(filelist[i].toString()).get("uniform_local_binary_patterns_feature"),2*picture_feature_maps_map.get(filelist[i].toString()).get("uniform_local_binary_patterns_feature").size()/4,1).random_projection_features);
			//picture_feature_maps_map.get(filelist[i].toString()).put("random_projection_on_uniform_local_binary_patterns_feature_3/4d",new random_projection(picture_feature_maps_map.get(filelist[i].toString()).get("uniform_local_binary_patterns_feature"),3*picture_feature_maps_map.get(filelist[i].toString()).get("uniform_local_binary_patterns_feature").size()/4,1).random_projection_features);
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			System.out.println(filelist[i].toString()+" indexed");
		}
	}
	
	public ArrayList<Double> regional_color_histogram_feature(int[][][] picture,int row ,int column){
		ArrayList<Double> picture_feature_list = new ArrayList<Double>();
		int[][][][][] split_picture = tools.split(picture,row,column);
		for(int x=0;x<split_picture.length;x++){
			for(int y=0;y<split_picture[x].length;y++){
				color_histogram current_split_picture_color_histogram = new color_histogram(split_picture[x][y]);
				for(int i=0;i<current_split_picture_color_histogram.rgb_bin.length;i++){
					for(int j=0;j<current_split_picture_color_histogram.rgb_bin[i].length;j++){
						for(int k=0;k<current_split_picture_color_histogram.rgb_bin[i][j].length;k++){	
							picture_feature_list.add(current_split_picture_color_histogram.rgb_bin[i][j][k]);
						}
					}
				}
			}
		}
		return picture_feature_list;
	}
	
	public ArrayList<Double> uniform_local_binary_patterns_feature(int[][][] picture,int row ,int column){
		
		ArrayList<Double> picture_feature_list = new ArrayList<Double>();
		int[][][][][] split_picture = tools.split(picture,row,column);
		for(int x=0;x<split_picture.length;x++){
			for(int y=0;y<split_picture[x].length;y++){
				uniform_local_binary_patterns current_split_picture_uniform_local_binary_patterns = new uniform_local_binary_patterns(split_picture[x][y]);
				for (Entry<String, Double> entry : current_split_picture_uniform_local_binary_patterns.uniform_local_binary_patterns_histogram_map_sorted.entrySet()) {
				     picture_feature_list.add(entry.getValue());
				}
			}
		}
		return picture_feature_list;
	}
	
	public TreeMap<String, Double> search_picture(String query_picture_path){
		
		System.out.println("query " + query_picture_path);
		
		HashMap<String,ArrayList<Double>> query_picture_feature_map = new HashMap<String,ArrayList<Double>>();
		
		////////Choose the features used. Note that the random projection features are depend on the uniform local binary patterns features so thet must be activate at the same time.////
		query_picture_feature_map.put("regional_color_histogram_feature",regional_color_histogram_feature(tools.open_picture(query_picture_path,true),2 ,2));
		query_picture_feature_map.put("uniform_local_binary_patterns_feature",uniform_local_binary_patterns_feature(tools.open_picture(query_picture_path,true),2 ,2));
		//query_picture_feature_map.put("random_projection_on_uniform_local_binary_patterns_feature_1/4d",new random_projection(query_picture_feature_map.get("uniform_local_binary_patterns_feature"),1*query_picture_feature_map.get("uniform_local_binary_patterns_feature").size()/4,1).random_projection_features);
		//query_picture_feature_map.put("random_projection_on_uniform_local_binary_patterns_feature_2/4d",new random_projection(query_picture_feature_map.get("uniform_local_binary_patterns_feature"),2*query_picture_feature_map.get("uniform_local_binary_patterns_feature").size()/4,1).random_projection_features);
		//query_picture_feature_map.put("random_projection_on_uniform_local_binary_patterns_feature_3/4d",new random_projection(query_picture_feature_map.get("uniform_local_binary_patterns_feature"),3*query_picture_feature_map.get("uniform_local_binary_patterns_feature").size()/4,1).random_projection_features);
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		////////If you activate some features that need to be normalized in order to do the fusion , activate the normalization for them here.////
		double[] max_distance = new double[2];
		double[] min_distance = new double[2];
		boolean[] set_min_distance = new boolean[2];
		double distance;
		
		for (Object key : picture_feature_maps_map .keySet()) {
			
			distance = tools.caculate_l1_distance(picture_feature_maps_map.get(key).get("regional_color_histogram_feature"),query_picture_feature_map.get("regional_color_histogram_feature"));
			if(distance>max_distance[0]){max_distance[0] =distance; }
			if(distance<min_distance[0]){min_distance[0] =distance; }
			if(set_min_distance[0] == false){min_distance[0] =distance; set_min_distance[0]=true;}
			
			distance = tools.caculate_l1_distance(picture_feature_maps_map.get(key).get("uniform_local_binary_patterns_feature"),query_picture_feature_map.get("uniform_local_binary_patterns_feature"));
			if(distance>max_distance[1]){max_distance[1] =distance; }
			if(distance<min_distance[1]){min_distance[1] =distance; }
			if(set_min_distance[1] == false){min_distance[1] =distance; set_min_distance[1]=true;}
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		///////Choose the features to caculate the distance here//////////////////////////////////////////////////////////////////////////////////
		HashMap<String,Double> distance_list = new HashMap<String,Double>();
		for (Object key : picture_feature_maps_map .keySet()) {
			distance = 0;
			distance += (tools.caculate_l1_distance(picture_feature_maps_map.get(key).get("regional_color_histogram_feature"),query_picture_feature_map.get("regional_color_histogram_feature"))-min_distance[0])*(100/max_distance[0]-min_distance[0]);
			distance += (tools.caculate_l1_distance(picture_feature_maps_map.get(key).get("uniform_local_binary_patterns_feature"),query_picture_feature_map.get("uniform_local_binary_patterns_feature"))-min_distance[1])*(100/max_distance[1]-min_distance[1]);
			//distance += (tools.caculate_l1_distance(picture_feature_maps_map.get(key).get("random_projection_on_uniform_local_binary_patterns_feature_1/4d"),query_picture_feature_map.get("random_projection_on_uniform_local_binary_patterns_feature_1/4d")));
			//distance += (tools.caculate_l1_distance(picture_feature_maps_map.get(key).get("random_projection_on_uniform_local_binary_patterns_feature_2/4d"),query_picture_feature_map.get("random_projection_on_uniform_local_binary_patterns_feature_2/4d")));
			//distance += (tools.caculate_l1_distance(picture_feature_maps_map.get(key).get("random_projection_on_uniform_local_binary_patterns_feature_3/4d"),query_picture_feature_map.get("random_projection_on_uniform_local_binary_patterns_feature_3/4d")));
			distance_list.put(key.toString(),distance);
		}
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		TreeMap<String, Double> sorted_distance_list = tools.SortByValue(distance_list); 
		
		for (Entry<String, Double> entry : sorted_distance_list.entrySet()) {
		     System.out.println(entry.getKey() +" : " +entry.getValue());
		}
		
		return sorted_distance_list;
	}
	
	public static void main(String[] args) {
		content_based_image_retrieval_system content_based_image_retrieval_system_1 = new content_based_image_retrieval_system("./dataset");
		content_based_image_retrieval_system_1.search_picture("./dataset/flower_01.jpg");		
	}
}
