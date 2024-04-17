package sprint4.finals.A_searchSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Solution {

    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            int n = Integer.parseInt(reader.readLine());
            String[] docs = new String[n];
            for (int i = 0; i < n; i++) {
                docs[i] = reader.readLine();
            }
            int m = Integer.parseInt(reader.readLine());
            String[] requests = new String[m];
            int[][] result = new int[m][];
            for (int i = 0; i < m; i++) {
                requests[i] = reader.readLine();
                result[i] = findRelevantDocs(requests[i], docs);
            }
            for (int i = 0; i < result.length; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < result[i].length; j++) {
                    sb.append(result[i][j]).append(" ");
                }
                System.out.println(sb.toString().trim());
            }
        }
    }

    public static int[] findRelevantDocs(String request, String[] docs) {
        LinkedHashMap<String, HashMap<Integer, Integer>> usagesToWord = new LinkedHashMap<>();
        String[] requestWords = request.split(" ");
        for (String requestWord : requestWords) {
            usagesToWord.put(requestWord, new HashMap<>());
            for (int i = 0; i < docs.length; i++) {
                if (docs[i].contains(requestWord)) {
                    String[] docWords = docs[i].split(" ");
                    int count = 0;
                    for (String docWord : docWords) {
                        if (docWord.equals(requestWord)) {
                            count++;
                        }
                    }
                    if (count > 0) {
                        usagesToWord.get(requestWord).put(i + 1, count);
                    }
                }
            }
        }
        return findFiveRelevantDocs(usagesToWord);
    }

    public static int[] findFiveRelevantDocs(HashMap<String, HashMap<Integer, Integer>> usagesToWord) {
        HashMap<Integer, Integer> usagesToDocument = new HashMap<>();
        for (HashMap<Integer, Integer> docMap: usagesToWord.values()){
            for (int docIdx : docMap.keySet()){
                if (!usagesToDocument.containsKey(docIdx)){
                    usagesToDocument.put(docIdx, docMap.get(docIdx));
                } else {
                    usagesToDocument.put(docIdx, usagesToDocument.get(docIdx) + docMap.get(docIdx));
                }
            }
        }
        int[] result = null;
        if (usagesToDocument.size() <= 5){
            result = new int[usagesToDocument.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = findMax(usagesToDocument);
                usagesToDocument.remove(result[i]);
            }
        }
        else {
            result = new int[5];
            for (int i = 0; i < 5; i++) {
                result[i] = findMax(usagesToDocument);
                usagesToDocument.remove(result[i]);
            }
        }
        return result;
    }

    public static int findMax(HashMap<Integer, Integer> map){
        int max = Integer.MIN_VALUE;
        int maxIdx = 0;
        for (int key : map.keySet()){
            int val = map.get(key);
            if (val > max){
                max = val;
                maxIdx = key;
            }
        }
        return maxIdx;
    }

//    public static int[] findRelevantDocs(String request, String[] docs) {
//        HashMap<String, List<int[]>> stats = new HashMap<>();
//        String[] words = request.split(" ");
//        for (String word : words) {
//            stats.put(word, new ArrayList<>());
//            for (int i = 0; i < docs.length; i++) {
//                if (docs[i].contains(word)) {
//                    int counter = 0;
//                    String[] docWords = docs[i].split(" ");
//                    for (String docWord : docWords) {
//                        if (docWord.equals(word)) {
//                            counter++;
//                        }
//                    }
//                    stats.get(word).add(new int[]{i + 1, counter});
//                }
//            }
//        }
//        return findFiveRelevantDocs(stats);
//    }

//    public static int[] findFiveRelevantDocs(HashMap<String, List<int[]>> statsOfRequest) {
//        HashMap<Integer, Integer> relevantDocs = new HashMap<>();
//        for (String key : statsOfRequest.keySet()) {
//            List<int[]> coincidences = statsOfRequest.get(key);
//            for (int[] pair : coincidences) {
//                if (!relevantDocs.containsKey(pair[0])) {
//                    relevantDocs.put(pair[0], pair[1]);
//                } else {
//                    relevantDocs.put(pair[0], relevantDocs.get(pair[0] + pair[1]));
//                }
//            }
//        }
//        int[] result;
//        int c = 0;
//        if (relevantDocs.size() <= 5) {
//            result = new int[relevantDocs.size()];
//            for (int i : relevantDocs.keySet()) {
//                result[c++] = i;
//            }
//        } else {
//            result = new int[5];
//            List<Integer> docRelevancy = new ArrayList<>(relevantDocs.values().stream().toList());
//            Collections.sort(docRelevancy);
//            for (int i = docRelevancy.size() - 1; i >= 0; i--) {
//                result[c++] = docRelevancy.get(i);
//                if (c >= 5) {
//                    break;
//                }
//            }
//        }
//        return result;
//    }
}
