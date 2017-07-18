package ArrayUtilities;

/**
 * Created by Kai on 2017/07/03.
 */

public class utility {
    public static int arrayMaxIndex(float[] array){
        int maxIndex=0;
        for(int i=1;i<array.length;i++){
            if(array[maxIndex]<array[i])
                maxIndex=i;
        }
        return maxIndex;
    }
    public static int arrayMaxIndex(double[] array){
        int maxIndex=0;
        for(int i=1;i<array.length;i++){
            if(array[maxIndex]<array[i])
                maxIndex=i;
        }
        return maxIndex;
    }
}
