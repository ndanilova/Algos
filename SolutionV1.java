package sprint4.finals.A_searchSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class SolutionV1 {
    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            PrintWriter out = new PrintWriter(System.out);
            int n = Integer.parseInt(reader.readLine());
            List<List<String>> docs = new ArrayList<>();
            docs.add(new ArrayList<>());
            for (int i = 0; i < n; i++) {
                docs.add(Arrays.stream(reader.readLine().split(" ")).collect(Collectors.toList()));
            }
            HashMap<String, HashMap<Integer, Integer>> searchIndex = fullIndex(docs);
            int m = Integer.parseInt(reader.readLine());
            Set<String>[] requests = new Set[m];
            for (int i = 0; i < m; i++) {
                requests[i] = Arrays.stream(reader.readLine().split(" ")).collect(Collectors.toSet());
                List<Integer> result = findFiveRelevantFiles(searchIndex, requests[i], n + 1);
                StringBuilder sb = new StringBuilder();
                for (int val : result){
                    sb.append(val).append(" ");
                }
                out.println(sb.toString().trim());
            }
            out.flush();

        }
    }

    public static HashMap<String, HashMap<Integer, Integer>> fullIndex(List<List<String>> docs){
        HashMap<String, HashMap<Integer, Integer>> index = new HashMap<>();
        for (int fileIdx = 1; fileIdx < docs.size(); fileIdx++){
            List<String> words = docs.get(fileIdx);
            for (String word : words){
                int count = Collections.frequency(words, word);
                if (!index.containsKey(word)){
                    index.put(word, new HashMap<>());
                }
                index.get(word).put(fileIdx, count);
            }
        }
        return index;
    }

    public static List<Integer> findFiveRelevantFiles(HashMap<String, HashMap<Integer, Integer>> index, Set<String> request, int docsSize) {
        List<int[]> result = new ArrayList<>();
        int idx = 0;
        int[][] indexesForResult = new int[docsSize + 1][2];
        for (String requestWord : request){
            if (index.containsKey(requestWord)){
                for (Map.Entry<Integer, Integer> entry : index.get(requestWord).entrySet()){
                    if (indexesForResult[entry.getKey()][0] == 0){
                        indexesForResult[entry.getKey()][0] = entry.getKey();
                        indexesForResult[entry.getKey()][1] = idx;
                        result.add(new int[]{entry.getKey(), -entry.getValue()});
                        idx++;
                    } else {
                        result.get(indexesForResult[entry.getKey()][1])[1] -= entry.getValue();
                    }

                }
            }
        }
        result.sort(Comparator.comparingInt((int[] a) -> a[1]).thenComparingInt(a -> a[0]));
        List<Integer> topFiveFiles = new ArrayList<>();
        for (int i = 0; i < Math.min(result.size(), 5); i++) {
            topFiveFiles.add(result.get(i)[0]);
        }
        return topFiveFiles;
    }
}
