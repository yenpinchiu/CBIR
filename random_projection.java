import java.util.ArrayList;
import java.util.Random;

public class random_projection {
	
	Random random;
	ArrayList<Double> random_projection_features;
	
	public random_projection(ArrayList<Double> input_features,int random_projection_features_dimension,int random_seed){
		
		random = new Random(random_seed);
		random_projection_features = new ArrayList<Double>();
		
		for(int i=0;i<random_projection_features_dimension;i++){
			double wx = 0;
			for(int j=0;j<input_features.size();j++){
				wx += random.nextGaussian()*input_features.get(j);
			}
			random_projection_features.add((double)(((int)Math.signum(wx)+1)/2));
		}
	}
}