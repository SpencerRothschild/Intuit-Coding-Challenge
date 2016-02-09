package com.company;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Main{


    /* mergeSort(int[] a)
     * Takes in an array to be sorted */
    public static void mergeSort(int [ ] a)
    {
        int[] arr = new int[a.length];
        mergeSort(a, arr,  0,  a.length - 1);
    }

    /* mergeSort(int[] a, int[] arr, int left, int right)
     * Takes in an array to be sorted a temp array and left and right pointers
     * Splits the array into subarrays and sorts each sub array */
    private static void mergeSort(int [ ] a, int [ ] arr, int left, int right)
    {
        if( left < right )
        {
            int center = (left + right) / 2;
            mergeSort(a, arr, left, center);
            mergeSort(a, arr, center + 1, right);
            merge(a, arr, left, center + 1, right);
        }
    }

    /* merge(int[] a, int[]arr, int left, int right, int rightEnd)
     * sorts the subarrays that were created in mergeSort*/
    private static void merge(int[ ] a, int[ ] arr, int left, int right, int rightEnd )
    {
        int leftEnd = right - 1;
        int n = left;
        int num = rightEnd - left + 1;

        while(left <= leftEnd && right <= rightEnd)
            if(a[left] <= (a[right]))
                arr[n++] = a[left++];
            else
                arr[n++] = a[right++];

        while(left <= leftEnd)
            arr[n++] = a[left++];

        while(right <= rightEnd)
            arr[n++] = a[right++];

        for(int i = 0; i < num; i++, rightEnd--)
            a[rightEnd] = arr[rightEnd];
    }


    /* nthPrime(int n)
     * Finds the nth prime number */
    public static int nthPrime(int n){

        int bound = getUpperBound(n);

        /* Array to keep track of primes */
        boolean[] tmp = new boolean[bound + 1];

        int sqrt = (int)Math.sqrt(bound);

        /* prime number counter */
        int count = 0;

        /* initialize all values to true */
        for (int i = 2; i <= bound; i++)
            tmp[i] = true;

        /* Iterate until sqrt of the bound */
        for (int i = 2; i <= sqrt; i++) {

            if (tmp[i])

                /* set all multiples of number to not prime */
                for (int j = i; i*j <= bound; j++)
                    tmp[i*j] = false;
        }

        /* go through array and count number of primes */
        for (int i = 2; i <= bound; i++) {
            if (tmp[i])
                count++;
                if (count == n)
                    return i;
        }

        return -1;
    }

    /* get the upper bound of number */
    public static int getUpperBound(int n) {
        if (n > 5) {
            return (int) (n * (Math.log(n) + Math.log(Math.log(n))));
        } else {
            return 11;
        }
    }


    /* readJson(String value)
     * takes in a value to find in the json file
     * Iterates through the whole JSON file and finds the value then returns the path
     * Used the JSON.jar library JSONObject and JSONArray */
    public static String readJson(String value) throws FileNotFoundException, JSONException{
        String data = "";
        BufferedReader br = null;

        /* read from file */
        try{
            String line;
            br = new BufferedReader(new FileReader("/Users/Spencer/IdeaProjects/Intuit/src/com/company/items.json"));
            while((line = br.readLine()) != null){
                data += line + "\n";
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        String path = "/itemList/items";
        JSONObject obj = new JSONObject(data);
        JSONObject o = obj.getJSONObject("itemList");

        JSONArray a = o.getJSONArray("items");

        /* loop through whole JSON array */
        for (int i = 0; i < a.length(); i++){

            /* check if it has subItem array */
            if(a.getJSONObject(i).has("subItems")){

                JSONObject t = a.getJSONObject(i);

                /* iterate through subitem array if it has it */
                if(t.get("subItems") instanceof JSONArray){
                    JSONArray arr = t.getJSONArray("subItems");
                    for(int j = 0; j < arr.length(); j++){
                        if(arr.getJSONObject(j).get("id").equals(value)){
                            path = path + "[" + i + "]/subItems[" + j + "]/id";
                            System.out.println(path);
                        }
                        else if(arr.getJSONObject(j).has("label") && arr.getJSONObject(j).get("label").equals(value)){
                            path = path + "[" + i + "]/subItems[" + j + "]/label";
                            System.out.println(path);
                        }
                    }
                }

                /* incase subitems is not an array */
                else if(t.get("subItems") instanceof JSONObject){
                    JSONObject r = t.getJSONObject("subItems");
                    if(r.get("id").equals(value)){
                        path = path + "[" + i + "]/subItems[" + 0 + "]/id";
                        System.out.println(path);
                    }
                    else if(r.has("label") && r.get("label").equals(value)){
                        path = path + "[" + i + "]/subItems[" + 0 + "]/label";
                        System.out.println(path);
                    }
                }
            }

            /* get the id */
            else if(a.getJSONObject(i).get("id").equals(value)){
                path = path + "[" + i + "]/id";
                System.out.println(path);
            }
            /* get the label */
            else if(a.getJSONObject(i).has("label") && a.getJSONObject(i).get("label").equals(value)){
                path = path + "[" + i + "]/label";
                System.out.println(path);
            }

        }
        return path;
    }


    public static void main(String[] args) throws FileNotFoundException, JSONException {

        System.out.println("3: " + nthPrime(3));
        System.out.println("58: " + nthPrime(58));
        System.out.println("10,001: " + nthPrime(10001));

        int[]a = {5,3,4,20,7,10,8};
        mergeSort(a);

        for(int i = 0 ; i < a.length; i++){
            System.out.print(" " + a[i] + " :");
        }

        System.out.println();

        try {
            String retVal = readJson("item2");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
