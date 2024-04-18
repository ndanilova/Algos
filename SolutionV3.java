package sprint4.finals.A_searchSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class SolutionV3 {
    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            PrintWriter out = new PrintWriter(System.out);
            int n = Integer.parseInt(reader.readLine());
            List<String>[] docs = new List[n];
            for (int i = 0; i < n; i++) {
                docs[i] = Arrays.stream(reader.readLine().split(" ")).collect(Collectors.toList());
            }
            int m = Integer.parseInt(reader.readLine());
            String[][] requests = new String[m][];
            for (int i = 0; i < m; i++) {
                requests[i] = reader.readLine().split(" ");
            }
            List<Integer> result;
            StringBuilder sb;
            for (String[] request : requests) {
                result = findRelevantFiles(request, docs);
                sb = new StringBuilder();
                for (int val : result) {
                    sb.append(val).append(" ");
                }
                out.println(sb.toString().trim());
            }
            out.flush();

        }
    }
    public static List<Integer> findRelevantFiles(String[] request, List<String>[] docs) {
        Map<String, Map<Integer, Integer>> usagesToRequest = new HashMap<>();
        for (String requestWord : request) {
            if (!usagesToRequest.containsKey(requestWord)) {
                usagesToRequest.put(requestWord, new HashMap<>());
                for (int i = 0; i < docs.length; i++) {
                    if (docs[i].contains(requestWord)) {
                        int count = 0;
                        for (String docWord : docs[i]) {
                            if (docWord.equals(requestWord)) {
                                count++;
                            }
                        }
                        usagesToRequest.get(requestWord).put(i + 1, count);
                    }
                }

            }
        }
        return findFiveRelevantFiles(usagesToRequest);
    }

    public static List<Integer> findFiveRelevantFiles(Map<String, Map<Integer, Integer>> usagesToRequests) {
        Map<Integer, Integer> relevanceToFiles = new HashMap<>();
        for (String word : usagesToRequests.keySet()) {
            Map<Integer, Integer> files = usagesToRequests.get(word);
            for (int usedFileIdx : files.keySet()) {
                int fileRelevancy = files.get(usedFileIdx);
                if (!relevanceToFiles.containsKey(usedFileIdx)){
                    relevanceToFiles.put(usedFileIdx, fileRelevancy);
                } else {
                    relevanceToFiles.put(usedFileIdx, relevanceToFiles.get(usedFileIdx) + fileRelevancy);
                }
            }
        }
        List<Integer> result = new ArrayList<>();
        while (result.size() < 5 && !relevanceToFiles.isEmpty()){
            result.add(findMax(relevanceToFiles));
        }
        return result;
    }
    public static int findMax(Map<Integer, Integer> map){
        int maxIdx = Collections.max(map.keySet(), (a, b) -> {
            int result = map.get(a) - map.get(b);
            if (result == 0){
                result = b - a;
            }
            return result;
        });
        map.remove(maxIdx);
        return maxIdx;
    }
}