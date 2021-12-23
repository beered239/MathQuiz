package quizGen;

public class Sort {

    //merge sort
    public static void mergeSortI(int[] a, int min, int max){
        if(min >= max)
            return;
        int mid = (min+max)/2;
        mergeSortI(a, min,mid);
        mergeSortI(a,mid+1,max);

        merge(a,min,mid,max);
    }
    
    /**@apiNote sorts in ascending order */
    public static void mergeSortD(Double[] a, int min, int max){
        if(min >= max)
            return;
        int mid = (min+max)/2;
        mergeSortD(a, min,mid);
        mergeSortD(a,mid+1,max);

        mergeD(a,min,mid,max);
    }
    
    
    private static void mergeD(Double[] a, int min, int mid, int max){

        Double[] temp = new Double[max-min+1];
        int i = min, j = mid+1, n =0;     //indexes for: start of lower half, start of upper half, start of new array

        //note: ascending order
            while(i <= mid || j <= max){
            if(i>mid){
                temp[n] = a[j];
                j++;
            }//if 1st half is inserted <^
            else if(j>max){
                temp[n] = a[i];
                i++;
            }//if 2nd half is inserted
            //if neither is done yet
            else if(a[i] < a[j]){
                temp[n] = a[i];
                i++;
            }//if lower half value is less than the upper half value <^
            else{
                temp[n] = a[j];
                j++;
            }// if upper half value is lower than the lower half value <^
            n++;
        }
        //copy temp array into og array
            if (max + 1 - min >= 0) System.arraycopy(temp, 0, a, min, max + 1 - min);
    }
    

    private static void merge(int[] a, int min, int mid, int max){

        int[] temp = new int[max-min+1];
        int i = min, j = mid+1, n =0;     //indexes for: start of lower half, start of upper half, start of new array

        //note: ascending order
            while(i <= mid || j <= max){
            if(i>mid){
                temp[n] = a[j];
                j++;
            }//if 1st half is inserted <^
            else if(j>max){
                temp[n] = a[i];
                i++;
            }//if 2nd half is inserted
            //if neither is done yet
            else if(a[i] < a[j]){
                temp[n] = a[i];
                i++;
            }//if lower half value is less than the upper half value <^
            else{
                temp[n] = a[j];
                j++;
            }// if upper half value is lower than the lower half value <^
            n++;
        }
        //copy temp array into og array
            if (max + 1 - min >= 0) System.arraycopy(temp, 0, a, min, max + 1 - min);
    }

}
