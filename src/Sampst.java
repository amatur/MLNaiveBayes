import java.util.ArrayList;
import java.util.Collections;

public class Sampst {
    
    public  ArrayList<Double> numbers;
    
    public Sampst(){
       numbers = new ArrayList<Double>();
    }
    
    public void add(Double val){
        if(!val.isNaN()){
             numbers.add(val);
        }
    }
    
    public double avg(){
        return (Double) (sum()/numbers.size());
    }
    
    public double min(){
        return Collections.min(numbers, null);
    }
    
    public double max(){
        return Collections.max(numbers, null);
    }
    
    public double sum(){
        double sum = 0;
        for(Double i : numbers){
            sum += i;
        }
        return sum;
    }
    
    /***
     * 
     * @return Population Variance (sigma^2)
     */
    public  double variance()
    {
        double mean = avg();
        double temp = 0;
        for(double a : numbers)
            temp += (a-mean)*(a-mean);
        return temp/numbers.size();
    }

    /***
     * 
     * @return Population Standard Deviation (sigma)
     */
    public double stddev()
    {
        return Math.sqrt(variance());
    }
    
    /***
     * 
     * @return Sample Variance (s^2)
     */
    public double sampleVariance()
    {
        double mean = avg();
        double temp = 0;
        for(double a : numbers){
            temp += (a-mean)*(a-mean);           
        }
            
        return temp/(numbers.size()-1);
    }
    
    /***
     * 
     * @return Sample Standard Deviation (s)
     */
    public double sampleStddev()
    {
        return Math.sqrt(sampleVariance());
    }

    
    public static double tScore(Sampst s1, Sampst s2)
    {
        //level of significance (1-alpha/2) = 0.005, 0.01, 0.05
        //level of confidence, alpha = 0.95
        //t(alpha = 0.95, n-1 = 49) => 1.68
        //0.005 => 2.678 (2.679951974)
        //0.010 => 2.403 (2.40489176)
        //0.050 => 1.676 (1.676550893)
        
        System.out.println("NB Avg: "+ s1.avg() + " \t KNN Avg: " + s2.avg());
        System.out.println("NB Var: "+ s1.sampleVariance() + " \t KNN Var: " + s2.sampleVariance());

        return (   (s1.avg() - s2.avg()) /  Math.sqrt(s1.sampleVariance()/s1.numbers.size() + s2.sampleVariance()/s2.numbers.size()) );
        
    }

    //0.005 => 2.678 (2.679951974)
    
    //accept means mu01 - mu02 = 0, reject means mu01 < mu02  
    public static boolean accept005(double tscore){
        if(tscore > 2.679951974){
            return false;
        }else{
            return true;
        }
    }
    
    public static boolean accept01(double tscore){
        if(tscore > 2.40489176){
            return false;
        }else{
            return true;
        }
    }
    
    public static  boolean accept05(double tscore){
        if(tscore > 1.676550893){
            return false;
        }else{
            return true;
        }
    }
    
    
    /**
     * 
     * private int n = 0;
        private double sum = 0.0;
        private double sumSqr = 0.0;
        private double min = Double.POSITIVE_INFINITY;
        private double max = Double.NEGATIVE_INFINITY;

        public SampleStatistics() {}
        public void update(double x) {
                n++;
                sum += x;
                sumSqr += x*x;
                min = Math.min(x, min);
                max = Math.max(x, max);
        }
        public int getCount() {
                return n;
        }
        public double getMean() {
                return sum/n;
        }
        public double getVariance() {
                return (sumSqr - sum*sum/n)/(n-1);
        }
        public double getMinimum() {
                return min;
        }
        public double getMaximum() {
                return max;
        }
     */
}
