    package sprint4.finals.A_searchSystem;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.io.PrintWriter;
    import java.util.*;
    import java.util.stream.Collectors;

    public class SolutionV2 {
        public static void main(String[] args) throws IOException {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                PrintWriter out = new PrintWriter(System.out);
                int n = Integer.parseInt(reader.readLine());
                List<Document> docs = readList(reader, n);
                int m = Integer.parseInt(reader.readLine());
                String[][] requests = readMatrix(reader, m);
                int[] result;
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
        //для каждого слова в запросе соответсвует список объектов класса Document, у каждого
        //из которых в numOfUsages хранится общее число использований от всех слов запрососв
        public static int[] findRelevantFiles(String[] requestWords, List<Document> docs) {
            LinkedHashMap<String, List<Document>> usagesToRequests = new LinkedHashMap<>();
            for (String requestWord : requestWords) {
                if (!usagesToRequests.containsKey(requestWord)) {
                    usagesToRequests.put(requestWord, new ArrayList<>());
                    for (Document doc : docs) {
                        if (doc.getDocValue().contains(requestWord)) {
                            for (String docWord : doc.getDocValue()) {
                                if (docWord.equals(requestWord)) {
                                    doc.increaseNumOfUsages(1);
                                }
                            }
                            usagesToRequests.get(requestWord).add(doc);
                        }
                    }
                }
            }
            return findFiveRelevantFiles(usagesToRequests);
        }


        public static int[] findFiveRelevantFiles(HashMap<String, List<Document>> usagesToRequests) {
            List<Document> relevantFiles = new ArrayList<>();
            for (List<Document> documents : usagesToRequests.values()) {
                for (Document document : documents) {
                    if (!relevantFiles.contains(document)) {
                        relevantFiles.add(document);
                    }
                }
            }
            int[] result;
            if (relevantFiles.size() <= 5) {
                result = new int[relevantFiles.size()];
                for (int i = 0; i < result.length; i++) {
                    Document maxDoc = findMax(relevantFiles);
                    maxDoc.setNumOfUsages(0);
                    relevantFiles.remove(maxDoc);
                    result[i] = maxDoc.getIndex();
                }
            } else {
                result = new int[5];
                for (int i = 0; i < result.length; i++) {
                    Document maxDoc = findMax(relevantFiles);
                    maxDoc.setNumOfUsages(0);
                    result[i] = maxDoc.getIndex();
                    relevantFiles.remove(maxDoc);
                }
                for (Document document : relevantFiles) { //так как в новый запрос пойдут те же объекты
                    document.setNumOfUsages(0);           // Document, то приходится занулить их numOfUsages
                }                                         // да, костыль но вообще не получается придумать что иначе сделать

            }
            return result;
        }

        private static Document findMax(List<Document> docs) {
            Document maxDocument = new Document(null, 0);
            for (Document doc : docs) {
                if (doc.getNumOfUsages() > maxDocument.getNumOfUsages()) {
                    maxDocument = doc;
                } else if (doc.getNumOfUsages() == maxDocument.getNumOfUsages() && doc.getIndex() < maxDocument.getIndex()) {
                    maxDocument = doc;
                }
            }
            return maxDocument;
        }

        private static String[][] readMatrix(BufferedReader reader, int n) throws IOException {
            String[][] matrix = new String[n][];
            for (int i = 0; i < n; i++) {
                matrix[i] = reader.readLine().split(" ");
            }
            return matrix;
        }

        private static List<Document> readList(BufferedReader reader, int n) throws IOException {
            List<Document> list = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                list.add(new Document(Arrays.stream(reader.readLine().split(" ")).collect(Collectors.toList()), i));
            }
            return list;
        }
    }

    class Document {
        private final int index;
        private int numOfUsages;

        private final List<String> docValue;

        public Document(List<String> docValue, int index) {
            this.docValue = docValue;
            this.index = index + 1;
            numOfUsages = 0;
        }

        public void increaseNumOfUsages(int increaseCount) {
            numOfUsages += increaseCount;
        }

        public void setNumOfUsages(int numOfUsages) {
            this.numOfUsages = numOfUsages;
        }

        public int getIndex() {
            return index;
        }

        public int getNumOfUsages() {
            return numOfUsages;
        }

        public List<String> getDocValue() {
            return docValue;
        }
    }
