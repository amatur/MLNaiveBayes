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
    public  double variance()
    {
        double mean = avg();
        double temp = 0;
        for(double a : numbers)
            temp += (a-mean)*(a-mean);
        return temp/numbers.size();
    }

    //sqrt(sigma square n)
    public double stddev()
    {
        return Math.sqrt(variance());
    }
    
    //s^2(n)
    public double sampleVariance()
    {
        double mean = avg();
        double temp = 0;
        for(double a : numbers){
            temp += (a-mean)*(a-mean);           
        }
            
        return temp/(numbers.size()-1);
    }

    public static double tScore(Sampst s1, Sampst s2)
    {
        //level of significance (1-alpha/2) = 0.005, 0.01, 0.05
        //level of confidence, alpha = 0.95
        //t(alpha = 0.95, n-1 = 49) => 1.68
        //0.005 => 2.678 (2.679951974)
        //0.010 => 2.403 (2.40489176)
        //0.050 => 1.676 (1.676550893)
        //System.out.println("avg: "+s1.avg());
        //System.out.println("avg2: "+s2.avg());
        //System.err.println(s1.sampleVariance());
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
}
