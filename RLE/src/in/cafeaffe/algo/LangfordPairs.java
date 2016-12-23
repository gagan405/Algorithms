package in.cafeaffe.algo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Stack;

/**
 * Created by gmishra on 20/12/2016.
 */
public class LangfordPairs {

    public static void main(String[] args) {
        int[] arr = new int[56];
        for(int i = 0; i < arr.length; i++) {
            arr[i] = -1;
        }
        Long time1 = System.nanoTime();
        getFilledArray(arr, 28);
        Long time2 = System.nanoTime();
        for(int x : arr)
            System.out.print(x + " ");

        int[] arr1 = new int[56];
        for(int i = 0; i < arr1.length; i++) {
            arr1[i] = -1;
        }
        long time3 = System.nanoTime();
        hasSolutionIter(arr1, 28);
        long time4 = System.nanoTime();
        System.out.println();
        for(int x : arr1)
            System.out.print(x + " ");

        int[] arr2 = new int[56];
        for(int i = 0; i < arr2.length; i++) {
            arr2[i] = -1;
        }
        long time5 = System.nanoTime();
        hasSolutionIter1(arr2, 28);
        long time6 = System.nanoTime();
        System.out.println();
        for(int x : arr2)
            System.out.print(x + " ");

        System.out.println("\nTime taken for recursive method : " + String.valueOf(time2 - time1));
        System.out.println("Time taken for iter 1 method : " + String.valueOf(time4 - time3));
        System.out.println("Time taken for iter 2 method : " + String.valueOf(time6 - time5));
    }
    private static boolean getFilledArray(int[] arr, int n){
        int nextInt = n;
        return isSolutionFeasible(n) ? hasSolution(arr, nextInt) : false;
    }

    private static boolean isSolutionFeasible(int n) {
        return ((n % 4) == 3 || (n % 4) == 0) ? true : false;
    }

    private static boolean hasSolution(int[] arr, int nextInt) {
        int nextPos = findPos(arr, 0, nextInt);
        if (nextPos == -1) return false;

        while(nextPos < arr.length) {
            arr[nextPos] = nextInt;
            arr[nextPos + nextInt + 1] = nextInt;

            if (nextInt == 1) {
                return true;
            }

            if(!hasSolution(arr, nextInt - 1)) {
                arr[nextPos] = -1;
                arr[nextPos + nextInt + 1] = -1;
                nextPos = findPos(arr, nextPos + 1, nextInt);
                if(nextPos == -1) return false;
            } else {
                return true;
            }
        }
        return false;
    }


    private static boolean hasSolutionIter(int[] arr, int num){
        if(!isSolutionFeasible(num)){
            return false;
        }
        boolean done = false;
        int pos = 0;
        Deque<Integer> lastPositions = new ArrayDeque<Integer>();

        boolean[] nums = new boolean[num];

        while(!done){
            if(pos == arr.length) return false;

            if(arr[pos] != -1){
                pos++;
                continue;
            }
            //Find a number that can be placed at this position
            int number = getNextFitNumber(nums, arr, 0, pos);

            while(number == -1){
                //If no number can be placed at this position, backtrack
                if(lastPositions.isEmpty()){
                    return false;
                }
                pos = lastPositions.pop();
                int prevNum = arr[pos];
                //reset
                arr[pos] = -1;
                arr[pos + prevNum + 1] = -1;
                nums[nums.length - prevNum] = false;
                number = getNextFitNumber(nums, arr, nums.length - prevNum + 1, pos);
            }

            arr[pos] = number;
            arr[pos + number + 1] = number;
            lastPositions.push(pos);
            pos++;
            nums[nums.length - number] = true;
            if(isDone(nums)){
                done = true;
            }
        }
        return true;
    }

    private static boolean hasSolutionIter1(int[] arr, int num){
        if(!isSolutionFeasible(num)){
            return false;
        }

        Deque<Integer> lastPositions = new ArrayDeque<Integer>();

        int number = num;
        int pos = 0;

        while(number > 0){
            //Find a position where this number can be placed
            int position = getNextFitPosition(arr, 0, number);

            while(position == -1){
                //If this number can be placed at no position.. backtrack
                if(lastPositions.isEmpty()){
                    return false;
                }
                pos = lastPositions.pop();
                int prevNum = arr[pos];
                //reset
                resetPosition(arr, pos);
                position = getNextFitPosition(arr, pos + 1, prevNum);
                if(position != -1){
                    number = prevNum;
                }
            }
            placeNum(arr, number, position);
            lastPositions.push(position);
            number--;
        }
        return true;
    }

    private static void resetPosition(int[] arr, int pos) {
        int prevNum = arr[pos];
        arr[pos] = -1;
        arr[pos + prevNum + 1] = -1;
    }

    private static int getNextFitPosition(int[] arr, int i, int number) {
        for(int idx = i; idx < arr.length; idx++){
            if(number + idx + 1 >= arr.length){
                return -1;
            }
            if(arr[idx] == -1 && arr[number + idx + 1] == -1){
                return idx;
            }
        }
        return -1;
    }

    private static boolean isDone(boolean[] nums) {
        boolean done = true;
        for(boolean var : nums){
            done = done & var;
        }
        return done;
    }

    private static int getNextFitNumber(boolean[] nums,
                                        int[] arr,
                                        int i,
                                        int pos) {

        for(int idx = i; i < nums.length; idx++){
            if(idx == nums.length) return -1;

            if(nums[idx]) continue;

            if(pos + nums.length -idx + 1 >= arr.length) return -1;

            if(arr[pos] == -1 && arr[pos + nums.length -idx + 1] == -1){
                return nums.length-idx;
            }
        }
        return -1;
    }

    private static int findNextNum(boolean[] nums) {
        for(int idx = 0; idx < nums.length; idx++) {
            if(!nums[idx]){
                return idx;
            }
        }
        return -1;
    }

    private static int backtrack(int[] arr, Deque<IntegerPos> stack){
        if(stack.isEmpty()) return -1;

        IntegerPos prev = stack.pop();
        int nextNum = prev.getNumber();
        int nextPos = prev.getPosition();

        //reset
        arr[nextPos] = -1;
        arr[nextPos + nextNum + 1] = -1;

        return nextNum;
    }

    private static void placeNum(int[] arr, int num, int idx){
        arr[idx] = num;
        arr[idx + num + 1] = num;
    }

    private static int findPos(int[] arr, int begin, int num) {
        for (int x = begin; x + num + 1 < arr.length; x++){
            if(arr[x] == -1 && arr[x + num + 1] == -1){
                return x;
            }
        }
        return -1;
    }

    public static class IntegerPos {
        private int number;
        private int position;

        public IntegerPos(int number, int position){
            this.number = number;
            this.position = position;
        }

        public int getNumber() {
            return number;
        }

        public int getPosition() {
            return position;
        }
    }
}
