package in.cafeaffe.algo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Created by gmishra on 20/12/2016.
 */

//Reference : http://dialectrix.com/langford/langford-algorithm.html
public class LangfordPairs {

    public static void main(String[] args) {
        int[] arr = new int[14];

        System.out.println("Finding a solution recursively...");
        initArray(arr);
        Long time1 = System.nanoTime();
        getLangfordPairsRecursive(arr, 7);
        Long time2 = System.nanoTime();
        printArray(arr);
        System.out.println();
        System.out.println();

        System.out.println("Finding a solution iterating on positions...");
        initArray(arr);
        long time3 = System.nanoTime();
        hasSolutionIterOnPosition(arr, 7);
        long time4 = System.nanoTime();
        printArray(arr);
        System.out.println();
        System.out.println();

        System.out.println("Finding a solution iterating on numbers...");
        initArray(arr);
        long time5 = System.nanoTime();
        hasSolutionIterOnNumbers(arr, 7);
        long time6 = System.nanoTime();
        printArray(arr);
        System.out.println();
        System.out.println();

        initArray(arr);
        System.out.println("Finding all solutions...");
        findAllSolutionsIterOnNumbers(arr, 7);

        System.out.println();
        System.out.println("\nTime taken for recursive method : " + String.valueOf(time2 - time1));
        System.out.println("Time taken for iter 1 method : " + String.valueOf(time4 - time3));
        System.out.println("Time taken for iter 2 method : " + String.valueOf(time6 - time5));
    }

    private static void initArray(int[] arr) {
        for(int i = 0; i < arr.length; i++) {
            arr[i] = -1;
        }
    }

    private static boolean getLangfordPairsRecursive(int[] arr, int n){
        int nextInt = n;
        return isSolutionFeasible(n) ? hasSolutionRecursive(arr, nextInt) : false;
    }

    private static boolean isSolutionFeasible(int n) {
        return ((n % 4) == 3 || (n % 4) == 0) ? true : false;
    }

    private static boolean hasSolutionRecursive(int[] arr, int nextInt) {
        int nextPos = findPos(arr, 0, nextInt);
        if (nextPos == -1) return false;

        while(nextPos < arr.length) {
            arr[nextPos] = nextInt;
            arr[nextPos + nextInt + 1] = nextInt;

            if (nextInt == 1) {
                return true;
            }

            if(!hasSolutionRecursive(arr, nextInt - 1)) {
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

    private static boolean hasSolutionIterOnNumbers(int[] arr, int num){
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

    private static boolean hasSolutionIterOnPosition(int[] arr, int num){
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

            placeNum(arr, number, pos);
            lastPositions.push(pos);
            pos++;
            nums[nums.length - number] = true;
            if(isDone(nums)){
                done = true;
            }
        }
        return true;
    }

    private static void findAllSolutionsIterOnNumbers(int[] arr, int num){
        List<Integer[]> solutions = new ArrayList<Integer[]>();

        if(!isSolutionFeasible(num)){
            return;
        }
        int[] positions = new int[3];
        int begin = 0;

        Deque<Integer> lastPositions = new ArrayDeque<Integer>();

        int number = num;
        int pos = 0;

        while(true){
            //Find a position where this number can be placed
            int position = getNextFitPosition(arr, begin, number);

            while(position == -1){
                //If this number can be placed at no position.. backtrack
                if(lastPositions.isEmpty()){
                    return;
                }
                pos = lastPositions.pop();
                int prevNum = arr[pos];
                //reset
                resetPosition(arr, pos);
                position = getNextFitPosition(arr, pos + 1, prevNum);
                if(position != -1){
                    number = prevNum;
                }
                if((prevNum == num) && position == -1){
                    printSolutions(solutions);
                    return;
                }
            }
            placeNum(arr, number, position);
            lastPositions.push(position);
            if(number <= 3){
                positions[number-1] = position;
            }
            number--;
            begin = 0;
            if(number == 0) {
                //Found a solution
                solutions.add(convertToObjectArray(arr));
                number = 3;
                lastPositions.pop();
                lastPositions.pop();
                lastPositions.pop();
                resetPosition(arr, positions[0]);
                resetPosition(arr, positions[1]);
                resetPosition(arr, positions[2]);
                begin = positions[2] + 1;
            }
        }
    }

    private static void printSolutions(List<Integer[]> solutions) {
        System.out.println("Total solutions found : " + solutions.size());
        for(Integer[] solution : solutions){
            System.out.println();
            printArray(solution);
        }
    }

    private static Integer[] convertToObjectArray(int[] arr) {
        Integer[] result = new Integer[arr.length];
        for(int x = 0; x < arr.length; x++){
            result[x] = arr[x];
        }
        return result;
    }

    private static void printArray(int[] arr) {
        for(int x : arr)
            System.out.print(x + " ");
    }

    private static void printArray(Integer[] arr) {
        for(int x : arr)
            System.out.print(x + " ");
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
}
